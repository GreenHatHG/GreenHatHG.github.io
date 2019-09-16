---
title: Java虚拟机笔记-自定义类加载器
date: 2019-05-10 07:53:05
categories: 读书笔记
tags:
- jvm
---

jvm自定义类加载器

<!-- more -->

# JDK加载类流程

参考：[https://juejin.im/post/5a1fad585188252ae93ab953](https://juejin.im/post/5a1fad585188252ae93ab953)

## loadClass

```java
protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException
{
    synchronized (getClassLoadingLock(name)) {
        /**
         * 1.检查类是否已经加载过
         * @param name 类的二进制名字     
         * @return 类对象（Class<?>），如果没有加载该类，则返回null
         */
        Class c = findLoadedClass(name);
        if (c == null) {
            long t0 = System.nanoTime();
            try {
                if (parent != null) {
                // 2.1.如果没有加载过，先调用父类加载器去加载
                    c = parent.loadClass(name, false);
                } else {
                // 2.2.如果没有加载过，且没有父类加载器，就用BootstrapClassLoader去加载
                c = findBootstrapClassOrNull(name);
                }
            } catch (ClassNotFoundException e) {
                // ClassNotFoundException thrown if class not found
                // from the non-null parent class loader
            }

            if (c == null) {
                //3. 如果父类加载器没有加载到，调用findClass去加载
                long t1 = System.nanoTime();
                c = findClass(name);

                // this is the defining class loader; record the stats
                sun.misc.PerfCounter.getParentDelegationTime().addTime(t1 - t0);
                sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
                sun.misc.PerfCounter.getFindClasses().increment();
            }
        }
        if (resolve) {
            resolveClass(c);
        }
        return c;
    }
}

```

从上面代码可以明显看出，`loadClass(String, boolean)`函数即实现了双亲委派模型！整个大致过程如下：

1. 检查一下指定名称的类是否已经加载过，如果加载过了，就不需要再加载，直接返回。
2. 如果此类没有加载过，那么，再判断一下是否有父加载器；如果有父加载器，则由父加载器加载（即调用`parent.loadClass(name, false);`）.或者是调用`bootstrap`类加载器来加载。
3. 如果父加载器及`bootstrap`类加载器都没有找到指定的类，那么调用当前类加载器的**`findClass`**方法来完成类加载。默认的`findclass`毛都不干，直接抛出`ClassNotFound`异常，所以我们自定义类加载器就要覆盖这个方法了。
4. **可以猜测:`ApplicationClassLoader`的`findClass`是去`classpath`下去加载，`ExtentionClassLoader`是去`java_home/lib/ext`目录下去加载。实际上就是`findClass`方法不一样罢了**。

---

`loadClass`在父加载器无法加载类的时候，就会调用我们自定义的类加载器中的`findClass`函数，因此我们必须要在`loadClass`这个函数里面实现将一个指定类名称转换为Class对象。

根据JavaDoc

> 一些类可能并不是来自一个文件，它们可能来自于其他来源，比如网络或者由应用本身构建出来。在这种情况下，defineClass方法会将一个字节数组转换成class类的一个实例，这个新定义的实例是可以通过Class.newInstance去创建。

于是我们可以运用`defineClass`去创建

![](Java虚拟机笔记-自定义类加载器/1.png)

## defineClass

```java
/**
 * 将字节数组转换为类Class的实例
 * @param b 组成类数据的字节数组
 *        off 数据起始位置
 *        len 数组长度
 * @ return 从指定的类数据创建的Class对象
 */
protected final Class<?> defineClass(byte[] b, int off, int len)
        throws ClassFormatError
    {
    	/**
         * @return 从数据创建的Class对象，以及可选的ProtectionDomain
    	 */
        return defineClass(null, b, off, len, null);
    }
```

```java
/**
  * 将字节数组转换为类别类的实例，其中可选的是ProtectionDomain
  * 如果域是null，则默认的域名将被作为文件规定分配给类defineClass(String, byte[], int, int)
  * 在类可以使用之前，必须解决域名问题
  * 包中定义的第一个类决定了该包中定义的所有后续类必须包含的精确的证书集
  * 一套类的证书是从类ProtectionDomain中的CodeSource获得的
  * 添加到该包中的任何类必须包含相同的证书集， 否则将抛出SecurityException
  * 请注意，如果name为null ，则不执行此检查
  * 你应该总是传递你所定义的类的binary name以及字节。 这确保你所定义的类确实是你认为的类。
  * 指定name不能以“java.”开始，因为在所有的类“java.*包只能由引导类装载程序来限定。
  * 如果name不是null，它必须等于binary name类的由字节数组指定” b “，否则将抛出一个NoClassDefFoundError
  * @param name 预期的类二进制名字，如果不知道则为null
  *       b    构成类数据的字节数组
  *       off  字节数组起始位置
  *       len  字节数组长度
  *       protectionDomain 该类的ProtectionDomain（保护域（保护域定义了授予一段特定代码的所有权限）） 
  * @return 从传入的数据中创建的类对象，可选ProtectionDomain 
  */
protected final Class<?> defineClass(String name, byte[] b, int off, int len,
                                         ProtectionDomain protectionDomain)
        throws ClassFormatError
    {
        protectionDomain = preDefineClass(name, protectionDomain);
        String source = defineClassSourceLocation(protectionDomain);
        Class<?> c = defineClass1(name, b, off, len, protectionDomain, source);
        postDefineClass(c, protectionDomain);
        return c;
    }
```



## findClass

```java
/**
 * 查找具有指定二进制名称的类。此方法应由遵循委托模型的类加载器实现覆盖以加载类，
 * 并且在检查所请求类的父类加载器  之后将由loadClass方法调用
 * @param name 类的二进制名
 * @retrun 生成的class对象
 * @throws ClassNotFoundException
 */
protected Class<?> findClass(String name) throws ClassNotFoundException {
        throw new ClassNotFoundException(name);
}
```

findClass默认是空的,所以得重写这个方法.

## 总结流程

其实很简单，继承ClassLoader类，覆盖findClass方法，这个方法的作用就是找到.class文件,转换成字节数组，调用defineClass对象转换成Class对象返回

# 简单实现自定义类加载器

```java
package indi.greenhat.jvm;

import java.io.*;

//用户自定义类加载器必须继承ClassLoader类
public class CustomClassLoader extends ClassLoader{

    private String classLoaderName;

    //类的扩展名
    private final String fileExtension = ".class";

    public CustomClassLoader(String classLoaderName){
        //使用方法getSystemClassLoader（）返回的ClassLoader作为父类加载器创建新的类加载器
        super();
        this.classLoaderName = classLoaderName;
    }

    public CustomClassLoader(String classLoaderName, ClassLoader parent){
        //使用指定的父类加载器创建新的类加载器以进行委派
        super(parent);
        this.classLoaderName = classLoaderName;
    }

    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException{
        byte[] data = this.loadClassDate(className);
        return this.defineClass(className, data, 0, data.length);
    }

    private byte[] loadClassDate(String name){
        InputStream is = null;
        byte[] data = null;
        ByteArrayOutputStream baos = null;

        try{
            //转换为磁盘对应的地址
            name = name.replace(".", "/");
            is = new FileInputStream(new File(name + this.fileExtension));
            baos = new ByteArrayOutputStream();

            int ch = 0;
            while((ch = is.read()) != -1){
                baos.write(ch);
            }
            data = baos.toByteArray();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                is.close();
                baos.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return data;
    }

    public static void test(ClassLoader classLoader) throws Exception {
        Class<?> clazz = classLoader.loadClass("indi.greenhat.jvm.Test");
        Object object = clazz.newInstance();

        System.out.println(object);
    }

    public static void main(String[] args) throws Exception {
        //创建一个自定义类加载器，其父加载器是系统类加载器
        CustomClassLoader loader1 = new CustomClassLoader("loader1");
        test(loader1);
    }
}
```

输出:

`indi.greenhat.jvm.Test@74a14482`

# 简单类加载器修改1

修改部分已经用标识起来

```java
/*************************************
 * update
 ************************************/
```

从标识部分我们可以看出，在`findClass`和`test`增加了几句输出语句，为了输出类的加载器

```java
package indi.greenhat.jvm;

import java.io.*;

//用户自定义类加载器必须继承ClassLoader类
public class CustomClassLoader extends ClassLoader{

    private String classLoaderName;

    //类的扩展名
    private final String fileExtension = ".class";

    public CustomClassLoader(String classLoaderName){
        //使用方法getSystemClassLoader（）返回的ClassLoader作为父类加载器创建新的类加载器
        super();
        this.classLoaderName = classLoaderName;
    }

    public CustomClassLoader(String classLoaderName, ClassLoader parent){
        //使用指定的父类加载器创建新的类加载器以进行委派
        super(parent);
        this.classLoaderName = classLoaderName;
    }


    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException{
        /*************************************
         * update
         ************************************/
        System.out.println("findClass invoked: " + className);
        System.out.println("class loader name: " + this.classLoaderName);
        /*************************************
         * update
         ************************************/
        byte[] data = this.loadClassDate(className);
        return this.defineClass(className, data, 0, data.length);
    }

    private byte[] loadClassDate(String name){
        InputStream is = null;
        byte[] data = null;
        ByteArrayOutputStream baos = null;

        try{
            //转换为磁盘对应的地址
            name = name.replace(".", "/");
            is = new FileInputStream(new File(name + this.fileExtension));
            baos = new ByteArrayOutputStream();

            int ch = 0;
            while((ch = is.read()) != -1){
                baos.write(ch);
            }
            data = baos.toByteArray();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                is.close();
                baos.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return data;
    }

    public static void test(ClassLoader classLoader) throws Exception {
        Class<?> clazz = classLoader.loadClass("indi.greenhat.jvm.Test");
        Object object = clazz.newInstance();

        System.out.println(object);
        /*************************************
         * update
         ************************************/
        System.out.println("----getClassLoader-----");
        System.out.println(clazz.getClassLoader());
        /*************************************
         * update
         ************************************/
    }

    public static void main(String[] args) throws Exception {
        CustomClassLoader loader1 = new CustomClassLoader("loader1");
        test(loader1);

    }
}

```

输出：

```java
indi.greenhat.jvm.Test@74a14482
----getClassLoader-----
sun.misc.Launcher$AppClassLoader@18b4aac2
```

可以看出该类是由系统类加载器加载的，而不是由我们自定义的类加载器加载的。

为什么呢？因为我们使用了`classLoader`的`super`办法，使用方法`getSystemClassLoader（）`返回的`ClassLoader`作为父类加载器创建新的类加载器，根据双亲委派机制，会先让双亲去尝试执行，因为要加载的类符合系统类加载器加载要求，所以是系统类加载器加载的。

---

所以系统类加载器是类`indi.greenhat.jvm.Test`的定义类加载器，而系统类加载器和`customClassLoader`是类`indi.greenhat.jvm.Test`的初始类加载器

> 若一个类加载器能够成功加载Test类，那么这个类加载器被称为定义类加载器，所有能够成功返回Class对象引用的类加载器（包括定义类加载器）都被称为初始类加载器

# 简单类加载器修改2

```java
package indi.greenhat.jvm;

import java.io.*;

//用户自定义类加载器必须继承ClassLoader类
public class CustomClassLoader extends ClassLoader{

    private String classLoaderName;

    //类的扩展名
    private final String fileExtension = ".class";

    private  String path;

    public void setPath(String path){
        this.path = path;
    }

    public CustomClassLoader(String classLoaderName){
        //使用方法getSystemClassLoader（）返回的ClassLoader作为父类加载器创建新的类加载器
        super();
        this.classLoaderName = classLoaderName;
    }

    public CustomClassLoader(String classLoaderName, ClassLoader parent){
        //使用指定的父类加载器创建新的类加载器以进行委派
        super(parent);
        this.classLoaderName = classLoaderName;
    }


    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException{
        System.out.println("findClass invoked: " + className);
        System.out.println("class loader name: " + this.classLoaderName);
        byte[] data = this.loadClassDate(className);
        return this.defineClass(className, data, 0, data.length);
    }

    private byte[] loadClassDate(String name){
        InputStream is = null;
        byte[] data = null;
        ByteArrayOutputStream baos = null;

        try{
            //转换为磁盘对应的地址
            name = name.replace(".", "/");
            is = new FileInputStream(new File(this.path+ name + this.fileExtension));
            baos = new ByteArrayOutputStream();

            int ch = 0;
            while((ch = is.read()) != -1){
                baos.write(ch);
            }
            data = baos.toByteArray();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                is.close();
                baos.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return data;
    }

    public static void main(String[] args) throws Exception {
        CustomClassLoader loader1 = new CustomClassLoader("loader1");
        loader1.setPath("/home/cc/IdeaProjects/test/out/production/test/");
        Class<?> clazz = loader1.loadClass("indi.greenhat.jvm.Test");
        Object object = clazz.newInstance();

        System.out.println(object);
        System.out.println(clazz.hashCode());
        System.out.println("----getClassLoader-----");
        System.out.println(clazz.getClassLoader());
        System.out.println();

        CustomClassLoader loader2 = new CustomClassLoader("loader2");
        loader2.setPath("/home/cc/IdeaProjects/test/out/production/test/");
        Class<?> clazz2 = loader2.loadClass("indi.greenhat.jvm.Test");
        Object object2 = clazz2.newInstance();

        System.out.println(object2);
        System.out.println(clazz2.hashCode());
        System.out.println("----getClassLoader-----");
        System.out.println(clazz2.getClassLoader());

    }
}
```

输出：

```java
indi.greenhat.jvm.Test@74a14482
356573597
----getClassLoader-----
sun.misc.Launcher$AppClassLoader@18b4aac2

indi.greenhat.jvm.Test@677327b6
356573597
----getClassLoader-----
sun.misc.Launcher$AppClassLoader@18b4aac2
```

可以看到，我们定义的两个加载器是一样并且是由系统类加载器加载的。前面所讲，如果该类被加载过了，那么就不会重新加载了。

-----

接着稍作修改

```java
	public static void main(String[] args) throws Exception {
        CustomClassLoader loader1 = new CustomClassLoader("loader1");
        loader1.setPath("/home/cc/test/");
        Class<?> clazz = loader1.loadClass("indi.greenhat.jvm.Test");
        Object object = clazz.newInstance();

        System.out.println(object);
        System.out.println(clazz.hashCode());
        System.out.println("----getClassLoader-----");
        System.out.println(clazz.getClassLoader());
        System.out.println();

        CustomClassLoader loader2 = new CustomClassLoader("loader1");
        loader2.setPath("/home/cc/test/");
        Class<?> clazz2 = loader2.loadClass("indi.greenhat.jvm.Test");
        Object object2 = clazz.newInstance();

        System.out.println(object2);
        System.out.println(clazz2.hashCode());
        System.out.println("----getClassLoader-----");
        System.out.println(clazz2.getClassLoader());

    }
}
```

输出：

```java
findClass invoked: indi.greenhat.jvm.Test
class loader name: loader1
indi.greenhat.jvm.Test@677327b6
21685669
----getClassLoader-----
indi.greenhat.jvm.CustomClassLoader@74a14482

findClass invoked: indi.greenhat.jvm.Test
class loader name: loader1
indi.greenhat.jvm.Test@135fbaa4
1173230247
----getClassLoader-----
indi.greenhat.jvm.CustomClassLoader@7f31245a
```

这一次我们把`Test.class`移动到`/home/cc/test/indi/greenhat/jvm`下面，然后尝试去该目录下加载`Test.class`，我们发现，此时类加载器是我们自定义的类加载器，因为该类不是项目目录下的，所以不能由系统类加载器加载，并且发现类加载器的`hashCode值不一样`，为什么呢？

这时候就涉及到了类的命名空间。

> - 每个类加载器都有自己的命名空间，命名空间由该加载器及所有父加载器所加载的类组成
> - 在同一个命名空间中，不会出现类的完整名字（包括类的包名）相同的两个类
> - 在不同的命名空间中，有可能会会出现类的完整名字（包括类的包名）相同的两个类

因为new的两个加载器属于不同的命名空间，所以`hashCode`的值不一样。

---

（项目没有Test.class前提）

```java
CustomClassLoader loader2 = new CustomClassLoader("loader1");
```

改为

```java
//将loader1作为loader2的父加载器
CustomClassLoader loader2 = new CustomClassLoader("loader2"， loader1);
```

结果会输出

```java
findClass invoked: indi.greenhat.jvm.Test
class loader name: loader1
indi.greenhat.jvm.Test@677327b6
21685669
----getClassLoader-----
indi.greenhat.jvm.CustomClassLoader@74a14482

indi.greenhat.jvm.Test@7f31245a
21685669
----getClassLoader-----
indi.greenhat.jvm.CustomClassLoader@74a14482
```

加载`Test`的是`loader1`，所以会执行`findClass`里面两个输出语句。`loader1`与`laoder2`在同一命名空间，又因为`loader1`已经加载过了，所以第二次输出的就是直接是`laoder1`已经加载过的结果

