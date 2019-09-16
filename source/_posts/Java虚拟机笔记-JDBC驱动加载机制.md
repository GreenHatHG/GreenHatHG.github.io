---
title: Java虚拟机笔记-JDBC驱动加载机制
date: 2019-06-21 15:39:08
categories: 读书笔记
tags:
- jvm
---
 通过JDBC驱动加载理解线程上下文加载器

<!-- more -->

# JDBC

`JDBC`提供了独立于数据库的统一`API`，`MySQL`，`Oracle`等数据库公司都可以基于这个标准接口来进行开发。包括`java .sql`包下的`Driver`，`Connection`，`Statement`，`ResultSet`是`JDBC`提供的接口。而`DriverManager`是用于管理`JDBC`驱动的服务类，主要用于获取`Connection`对象（此类中全是静态方法）

下面是一个简单的jdbc连接数据库过程。重点不在能不能连接，而是其加载过程。

```java
package Test;

import java.sql.Connection;
import java.sql.DriverManager;

public class Test {
    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/mytest";
        Connection connection = DriverManager.getConnection(
                url, "username", "password");
    }
}
```

# Class.forName

```java
Class.forName("com.mysql.cj.jdbc.Driver");
```

1. 查看`forName`

```java
/**
 * 返回与具有给定字符串名称的类或接口关联的Class对象。
 * 调用此方法等效于：Class.forName（className，true，currentLoader）
 * 其中currentLoader表示当前类的定义类加载器。
 * @ param className 所需类的完全限定名称
 * @ return 具有指定名称的类的Class对象
 */
public static Class<?> forName(String className)
                throws ClassNotFoundException {
        Class<?> caller = Reflection.getCallerClass();
        return forName0(className, true, ClassLoader.getClassLoader(caller), caller);
    }
```

2. 查看`forName0`，其是一个`native`方法

```java
private static native Class<?> forName0(String name, boolean initialize,
                                            ClassLoader loader,
                                            Class<?> caller)
        throws ClassNotFoundException;
```

# com.mysql.cj.jdbc.Driver

```java
public class Driver extends NonRegisteringDriver implements java.sql.Driver {
    public Driver() throws SQLException {
    }

    //使用DriverManager注册自己
    static {
        try {
            DriverManager.registerDriver(new Driver());
        } catch (SQLException var1) {
            throw new RuntimeException("Can't register driver!");
        }
    }
}
```

从上边可以看到，它是用静态代码块实现的。根据类加载机制，当执行`Class.forName(driverClass)`获取其`Class`对象时，`com.mysql.jdbc.Driver`就会被`JVM`加载连接，并进行初始化，初始化就会执行静态代码块，也就会执行下边这句代码：

`DriverManager.registerDriver(new Driver());`

这里会注册驱动，调用的是`DriverManager`的`registerDriver`方法，那么在调用之前，也会初始化`DriverManager`类。

## DriverManager

```java
public class DriverManager{
    ...
    //通过检查System属性jdbc.properties加载初始JDBC驱动程序，然后使用ServiceLoader机制
    static {
        loadInitialDrivers();
        println("JDBC DriverManager initialized");
    }
    ...
}
```

`DriverManager`类有一个静态代码块，会在初始化的时候执行

```java
private static void loadInitialDrivers() {
        String drivers;
        try {
            drivers = AccessController.doPrivileged(new PrivilegedAction<String>() {
                public String run() {
                    return System.getProperty("jdbc.drivers");
                }
            });
        } catch (Exception ex) {
            drivers = null;
        }
    
        // 如果驱动程序打包为服务提供程序，请加载它
        // 通过作为java.sql.Driver.class服务公开的类加载器获取所有驱动程序。
        // ServiceLoader.load（）替换了sun.misc.Providers（）
        AccessController.doPrivileged(new PrivilegedAction<Void>() {
            public Void run() {

                ServiceLoader<Driver> loadedDrivers = ServiceLoader.load(Driver.class);
                Iterator<Driver> driversIterator = loadedDrivers.iterator();

                /* 
                 * It may be the case that the driver class may not be there
                 * i.e. there may be a packaged driver with the service class
                 * as implementation of java.sql.Driver but the actual class
                 * may be missing. In that case a java.util.ServiceConfigurationError
                 * will be thrown at runtime by the VM trying to locate
                 * and load the service.
                 *
                 * Adding a try catch block to catch those runtime errors
                 * if driver not available in classpath but it's
                 * packaged as service and that service is there in classpath.
                 */
                try{
                    while(driversIterator.hasNext()) {
                        driversIterator.next();
                    }
                } catch(Throwable t) {
                // Do nothing
                }
                return null;
            }
        });

        println("DriverManager.initialize: jdbc.drivers = " + drivers);

        if (drivers == null || drivers.equals("")) {
            return;
        }
        String[] driversList = drivers.split(":");
        println("number of Drivers:" + driversList.length);
        for (String aDriver : driversList) {
            try {
                println("DriverManager.Initialize: loading " + aDriver);
                Class.forName(aDriver, true,
                        ClassLoader.getSystemClassLoader());
            } catch (Exception ex) {
                println("DriverManager.Initialize: load failed: " + ex);
            }
        }
    }
```

balabala大半天意思就是说如果`jdbc.drivers`这个系统属性为空，则会用ServerLoader机制去加载jdbc驱动。

`ServiceLoader<Driver> loadedDrivers = ServiceLoader.load(Driver.class);`会加载所有在`META-INF/services/java.sql.Driver`文件里边的类到JVM内存，完成驱动的自动加载，也就我们讲的`ServiceLoader`加载JDBC驱动机制

这个自动加载采用的技术叫做`SPI`，**在JDBC 4.0之后实际上我们不需要再调用`Class.forName`来加载驱动程序了，我们只需要把驱动的jar包放到工程的类加载路径里，那么驱动就会被自动加载**

这就是`SPI`的优势所在，能够自动的加载类到JVM内存。这个技术在阿里的`dubbo`框架里面也占到了很大的分量。

## registerDriver

```java
/**
 * 使用DriverManager注册给定的驱动程序。 
 * 新加载的驱动程序类应调用方法registerDriver使其自身为DriverManager所知。 
 * 如果驱动程序当前已注册，则不执行任何操作。
 * @param driver 要在DriverManager中注册的新JDBC驱动程序
 */
public static synchronized void registerDriver(java.sql.Driver driver)
        throws SQLException {

        registerDriver(driver, null);
    }
```

`registerDriver(driver, null);`

```java
public static synchronized void registerDriver(java.sql.Driver driver,
            DriverAction da)
        throws SQLException {

        // 如果它还没有被添加到驱动的列表,则注册驱动程序
        if(driver != null) {
            registeredDrivers.addIfAbsent(new DriverInfo(driver, da));
        } else {
            // This is for compatibility with the original DriverManager
            throw new NullPointerException();
        }

        println("registerDriver: " + driver);

    }
```

这个驱动列表定义：

```java
//已注册JDBC驱动程序列表
private final static CopyOnWriteArrayList<DriverInfo> registeredDrivers = new CopyOnWriteArrayList<>();
```

# DriverManager.getConnection

我们加载完驱动后就可以连接数据库，但是我们并没有看到mysql相关信息，接着我们看下`getConnection`方法，

```java
/**
 * 尝试建立到给定数据库URL的连接。
 * 驱动程序管理器尝试从已注册的JDBC驱动程序集中选择适当的驱动程序。
 * 注意:如果user或password属性也指定为url的一部分，则实现定义为哪个值优先。
 * 为了获得最大的可移植性，应用程序应该只指定一个属性一次。
 */
public static Connection getConnection(String url,
        String user, String password) throws SQLException {
        java.util.Properties info = new java.util.Properties();

        if (user != null) {
            info.put("user", user);
        }
        if (password != null) {
            info.put("password", password);
        }
		//Reflection.getCallerClass():调用者的类加载器实例，在本程序中即main的类加载器
        return (getConnection(url, info, Reflection.getCallerClass()));
    }
```

接着会发现会去调用getConnection三个参数的方法

```java
private static Connection getConnection(
        String url, java.util.Properties info, Class<?> caller) throws SQLException {
        
    	/**
    	 * 当callerCl为null时，我们应检查应用程序（间接调用此类）类加载器，
    	 * 以便可以从此处加载rt.jar外部的JDBC驱动程序类。
    	 * 从这个简单程序来看，这个caller其实是系统类加载器，因为main类的加载器就是系统类加载器
    	 */
        ClassLoader callerCL = caller != null ? caller.getClassLoader() : null;
        synchronized(DriverManager.class) {
            // 同步加载正确的类加载器
     		// 如果为根类加载器的话，则会返回当前线程的上下文类加载器
            if (callerCL == null) {
                callerCL = Thread.currentThread().getContextClassLoader();
            }
        }
    
		...
		//DriverInfo:已注册驱动程序的包装类
        for(DriverInfo aDriver : registeredDrivers) {
			//如果调用者没有加载驱动程序的权限，则跳过它。
            if(isDriverAllowed(aDriver.driver, callerCL)) {
                try {
                    println("    trying " + aDriver.driver.getClass().getName());
                    Connection con = aDriver.driver.connect(url, info);
                    if (con != null) {
                        // Success!
                        println("getConnection returning " + aDriver.driver.getClass().getName());
                        return (con);
                    }
                } catch (SQLException ex) {
                    if (reason == null) {
                        reason = ex;
                    }
                }

            } else {
                println("    skipping: " + aDriver.getClass().getName());
            }

        }
    
		...
    }

}
```

一个项目里边很可能会即连接MySQL，又连接Oracle，这样在一个工程里边就存在了多个驱动类，那么这些驱动类又是怎么区分的呢？

关键点就在于`getConnection`的步骤，`DriverManager.getConnection`中会遍历所有已经加载的驱动实例去创建连接，当一个驱动创建连接成功时就会返回这个连接，同时不再调用其他的驱动实例。如上面

```java
for(DriverInfo aDriver : registeredDrivers){
    ...
}
```

是不是每个驱动实例都真真实实的要尝试建立连接呢？不是的！

每个驱动实例在getConnetion的第一步就是按照url判断是不是符合自己的处理规则，是的话才会和db建立连接。比如，MySQL驱动类中的关键代码：

```java
public boolean acceptsURL(String url) throws SQLException {
        return (parseURL(url, null) != null);
    }

    public Properties parseURL(String url, Properties defaults)
            throws java.sql.SQLException {
        Properties urlProps = (defaults != null) ? new Properties(defaults)
                : new Properties();

        if (url == null) {
            return null;
        }

        if (!StringUtils.startsWithIgnoreCase(url, URL_PREFIX)
                && !StringUtils.startsWithIgnoreCase(url, MXJ_URL_PREFIX)
                && !StringUtils.startsWithIgnoreCase(url,
                        LOADBALANCE_URL_PREFIX)
                && !StringUtils.startsWithIgnoreCase(url,
                        REPLICATION_URL_PREFIX)) {

            return null;
        }
```

---

参考：

[Java动态加载数据库驱动 | Mingyu's Blog | 我荒废的今日，正是昨天殒身之人祈求的明日](https://www.mygu.club/java-dyna-load-db-drv.html)

[JDBC驱动加载机制 - 程序园](<http://www.voidcn.com/article/p-gqtpvojz-bro.html>)