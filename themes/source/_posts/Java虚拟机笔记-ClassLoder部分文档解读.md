---
title: Java虚拟机笔记-ClassLoder部分文档解读
date: 2019-05-04 20:13:07
categories: 读书笔记
tags:
- jvm
---

ClassLoder部分文档解读

<!-- more -->

# Documentation

```java
java.lang 
public abstract class ClassLoader
extends Object
```

---

```
A class loader is an object that is responsible for loading classes. The class ClassLoader is an abstract class. Given the binary name of a class, a class loader should attempt to locate or generate data that constitutes a definition for the class. A typical strategy is to transform the name into a file name and then read a "class file" of that name from a file system.

一个类加载器是一个对象，负责加载类。类ClassLoader是一个抽象类。给定了一个类的二进制名字，ClassLoader应该尝试去定位或者生成构成了类定义的数据。一种典型的策略是将给定的二进制名字转为文件名，然后从文件系统中读取这个文件名里面的class信息。
```

> Binary names
>
> Any class name provided as a String parameter to method in ClassLoader must be a binary name as definded by *The Java Language Specification*
>
> Examples of valid class name include:
>
> ```java
> "java.lang.String"
> "javax.swing.JSpinner$DefaultEditor"
> "java.security.KeyStore$Builder$FileBuilder$1"
> "java.net.URLClassLoader$3$1"
> ```

---

```
Every Class object contains a reference to the ClassLoader that defined it.
Class objects for array classes are not created by class loaders, but are created automatically as required by the Java runtime. The class loader for an array class, as returned by Class.getClassLoader() is the same as the class loader for its element type; if the element type is a primitive type, then the array class has no class loader.
Applications implement subclasses of ClassLoader in order to extend the manner in which the Java virtual machine dynamically loads classes.

每一个class对象都会包含着定义这个class对象的ClassLoader的引用。
针对于数组类的class对象，并不是由类加载器创建的，而是由java在运行时需要的时候自动创建的。对于一个数组类的类加载器来说，其Class.getClassLoader的返回值与这个数组中元素类型的etClassLoader的返回值是一样的。如果数组当中的元素类型是原生类型，那么这个数组类是没有加载器的。
应用实现了ClassLoader的子类是为了扩展java虚拟机动态加载类的方式
```

```java
public class Main{
    public static void main(String[] args){
        String[] strings = new String[10];
        //string类型由根加载器加载，所以返回null
        System.out.println(strings.getClass().getClassLoader());

        System.out.println("-----");
        Main[] mains = new Main[10];
        //Main类型由系统类加载器加载，所以返回sun.misc.Launcher$AppClassLoader@18b4aac2
        System.out.println(mains.getClass().getClassLoader());
        
        System.out.println("-----");
        int[] ints = new int[10];
        //原生类型没有类加载器
        System.out.println(ints.getClass().getClassLoader());
    }
}
/*输出：
null 
-----
sun.misc.Launcher$AppClassLoader@18b4aac2
-----
null
*/
```

---

```
Class loaders may typically be used by security managers to indicate security domains.
Class loaders may typically be used by security managers to indicate security domains.
The ClassLoader class uses a delegation model to search for classes and resources. Each instance of ClassLoader has an associated parent class loader. When requested to find a class or resource, a ClassLoader instance will delegate the search for the class or resource to its parent class loader before attempting to find the class or resource itself. The virtual machine's built-in class loader, called the "bootstrap class loader", does not itself have a parent but may serve as the parent of a ClassLoader instance.

类加载器典型情况下可以被安全管理器使用来去标识的安全域问题。
类加载器使用了一种委派模型来去寻找类和资源。ClassLoader的每一个实例都会有与之关联的双亲。当被请求去寻找一个类或资源的时候，在自己寻找前，ClassLoader的实例就会将对于类或资源的寻找委托给父ClassLoader。虚拟机内建的类加载器称之为启动类加载器，本身没有双亲，但是它可以作为类加载器的双亲。
```

---

```
Class loaders that support concurrent loading of classes are known as parallel capable class loaders and are required to register themselves at their class initialization time by invoking the ClassLoader.registerAsParallelCapable method. Note that the ClassLoader class is registered as parallel capable by default. However, its subclasses still need to register themselves if they are parallel capable.
In environments in which the delegation model is not strictly hierarchical, class loaders need to be parallel capable, otherwise class loading can lead to deadlocks because the loader lock is held for the duration of the class loading process (see loadClass methods).

我们将支持并行的类加载器称之为parallel capable class loaders，它被要求在它们的类初始化期间通过ClassLoader.registerAsParallelCapable方法将自身注册上。注意，ClassLoader默认注册为parallel capable。然而它们的子类如果也是并行的，仍然需要注册。
在委派模式不是严格的层次化环境下，类加载器需要并行，否则类加载会导致死锁，因为加载器的锁在加载过程中是一直持有的。
```

---

```
Normally, the Java virtual machine loads classes from the local file system in a platform-dependent manner. For example, on UNIX systems, the virtual machine loads classes from the directory defined by the CLASSPATH environment variable.
However, some classes may not originate from a file; they may originate from other sources, such as the network, or they could be constructed by an application. The method defineClass converts an array of bytes into an instance of class Class. Instances of this newly defined class can be created using Class.newInstance.

通常情况下，java虚拟机会从本地的文件系统以一种平台相关性加载类，比如在Unix，虚拟机会从由CLASSPATH变量所定义的目录下去加载类。
然而，一些类可能并不是来自一个文件，它们可能来自于其他来源，比如网络或者由应用本身构建出来。在这种情况下，defineClass方法会将一个字节数组转换成class类的一个实例，这个新定义的实例是可以通过Class.newInstance去创建。
```

---

```
The methods and constructors of objects created by a class loader may reference other classes. To determine the class(es) referred to, the Java virtual machine invokes the loadClass method of the class loader that originally created the class.
For example, an application could create a network class loader to download class files from a server. Sample code might look like:
     ClassLoader loader = new NetworkClassLoader(host, port);
     Object main = loader.loadClass("Main", true).newInstance();
          . . .
The network class loader subclass must define the methods findClass and loadClassData to load a class from the network. Once it has downloaded the bytes that make up the class, it should use the method defineClass to create a class instance. A sample implementation is:
       class NetworkClassLoader extends ClassLoader {
           String host;
           int port;
  
           public Class findClass(String name) {
               byte[] b = loadClassData(name);
               return defineClass(name, b, 0, b.length);
           }
  
           private byte[] loadClassData(String name) {
               // load the class data from the connection
                . . .
           }
       }
       
由类加载器所创建的对象的方法或构造方法可能会引用其他的类。为了确定所引用的类，java虚拟机会调用最初创建这个类的loadClass方法去加载它所引用的其他的类。
比如说，一个应用可以创建一个网络类加载器，从服务器上加载类：
     ClassLoader loader = new NetworkClassLoader(host, port);
     Object main = loader.loadClass("Main", true).newInstance();
          . . .
网络类加载器子类必须定义网络加载类的方法findClass和loadClassData 。 一旦下载构成类的字节，它应该使用方法defineClass创建一个类实例:
       class NetworkClassLoader extends ClassLoader {
           String host;
           int port;
  
           public Class findClass(String name) {
               byte[] b = loadClassData(name);
               return defineClass(name, b, 0, b.length);
           }
  
           private byte[] loadClassData(String name) {
               // load the class data from the connection
                . . .
           }
       }
```

