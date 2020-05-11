---
title: Java虚拟机笔记-线程上下文加载器
date: 2019-06-10 20:57:22
categories: 读书笔记
tags:
- jvm
---
 线程上下文加载器

<!-- more -->

# 线程上下文类加载器重要性

首先我们看两行输出语句，第一个是输出Thread对象的上下文类加载器，而第二个是输出Thread类的加载器

```java
package indi.greenhat.jvm;

public class Test {
    public static void main(String[] args) throws Exception{

        /**
         * currentThread():
         * 返回对当前正在执行的线程对象的引用
         */
        /**
         * getContextClassLoader():
         * 返回此Thread的上下文ClassLoader
         * 上下文ClassLoader由线程的创建者提供，供加载类和资源时在此线程中运行的代码使用
         * 如果未设置，则默认为父线程的ClassLoader上下文
         * 原始线程的上下文ClassLoader通常设置为用于加载应用程序的类加载器
         */
        System.out.println(Thread.currentThread().getContextClassLoader());

        System.out.println(Thread.class.getClassLoader());
    }
}
```

输出：

```java
sun.misc.Launcher$AppClassLoader@18b4aac2
null
```

我们先看null，表示的是启动类加载器，因为Thread类是位于Java核心包里面，所以当然是由启动类加载器加载的。

解释第一条例子前先看一点概念：

- **当前类加载器（Current ClassLoader）**

每个类都会使用自己的类加载器（即加载自身的类加载器）来去加载其他类（指的是所依赖的类），如果ClasssX引用了ClassY，那么ClassX的类加载器就会去加载ClassY（前提是ClassY尚未被加载）

- **线程上文类加载器（Context ClassLoader）**

1. 线程上下文加载器是从JDK1.2开始引入的，类Thread中的`getContextClassLoader`与`setContextClassLoader(ClassLoader cl)`分别用来获取和设置上下文类加载器

2. 如果没有通过`setContextClassLoader(ClassLoader cl)`进行设置的话，线程将继承其父线程的上下文类加载器。

3. Java应用运行时的初始线程的上下文类加载器是系统类加载器。在线程中运行的代码可以通过该类加载器来加载类与资源。

4. 线程上文类加载器重要性：

其实很简单，**就是为了解决类加载器命名空间的限制**。在双亲委托机制下，类加载是由下至上的，即下层的类加载器会委托上层进行加载。在SPI（Service Provider Interface）场景下，**有些接口是Java核心库提供的，并且是由启动类加载器来加载的，而这些接口的实现却来自不同的jar包（实现厂商提供），Java的启动类加载器是不会加载其它来源的Jar包**，这样传统的双亲委托机制无法满足SPI要求。通过给当前线程设置上下文类加载器，就可以由其负责加载接口实现的类。

就好像JDBC：

```java
Class.forName(driverClass);
Connection con = DriverManager.getConnection(dataBase, usrName, passWord);
stm = con.createStatement();
```

一些接口是本来已经存在于JDK中，而驱动包（接口的实现）则是厂商提供的。

**所以，父ClassLoader可以使用当前线程`Thread.currentThread().getContextLoader()`所指定的ClassLoader加载的类。这样就改变了父ClassLoader不能使用子ClassLoader或是其他没有直接父子关系的ClassLoader加载的类的情况，即改变了双亲委派模型。**

当高层提供了统一的接口让底层去实现，同时又要在高层加载（或实例化）底层的类时，就必须要通过线程上下文类加载器来帮助高层的ClassLoader找到并加载该类。

---

**线程上下文加载器就是当前线程的Current Loader**

因为Java应用运行时的初始线程的上下文类加载器是系统类加载器，所以第一行输出的是系统类加载器

为什么呢？

从Launcher类的构造函数就可以看出，默认是设置为系统类加载器

```java
 public Launcher() {
    	...	
        //用于启动应用程序的类加载器
        loader = AppClassLoader.getAppClassLoader(extcl);
		...
        // 为原始线程设置上下文类加载器。
        Thread.currentThread().setContextClassLoader(loader);
     	...
    }
```

---

# 线程上下文类加载器使用

线程上下文类加载器的一般使用模式：（获取--使用--还原）

```java
ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

try{
    Thread.currentThread().setContextClassLoader(customerLoader);
    /**
     * 调用了Thread.currentThread().getContextClassLoader()
     * 获取当前线程上下文类加载器做某些事情
     */
    myMethod(); 
} finally{
    Thread,currentThread().setContextClassLoader(classLoader); //还原，以供下次使用
}
```

