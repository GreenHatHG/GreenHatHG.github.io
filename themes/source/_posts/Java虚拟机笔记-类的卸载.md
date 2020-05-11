---
title: Java虚拟机笔记-类的卸载
date: 2019-05-15 08:07:56
categories: 读书笔记
tags:
- jvm
---

jvm类的卸载

<!-- more -->

当`MySample`类被加载，连接和初始化后，它的生命周期就开始了。当代表`MySample`类的`Class`对象不再被引用，即不可触及时，`Class`对象就会结束生命周期，`MySample`类在方法区内的数据也会被卸载，从而结束`MySample`类的生命周期。

一个类何时结束生命周期，取决于代表它的Class对象何时结束生命周期。

----

由Java虚拟机自带的加载器所加载的类，在虚拟机的生命周期中，始终不会被卸载。Java虚拟机自带的类加器包括根类加载器，扩展类加载器和系统类加载器。Java虚拟机本身会始终引用这些类加载器，而这些类加载器则会始引用它们所加载的类的`Class`对象，因此这`Class`对象终是可及的。

由用户自定义的类加载器所加载的类是可以被卸载的。

----

Sample类中由`loader1`加载。在类加载器的内部实现中，用一个Java集合来存放所加载类的引用。另一方面，一个`Class`对象总是会引用它的类加载器，调用`Class`对象的`getClasslodar()`方法，就能获得它的类加载器。此可见，代表`Sample`类的`Class`实例与`loader1`之间为双向关取关系。
一个类的实例总是引用代表这个类的`C1ass`对象。在`Object`类中定义了`getClass()`
方法，这个方法返回代表对象所属类的`Class`对象的引用。此外，所有的Java类都有
一个静态属性class.它引用代表这个类的Class对象。

# 验证类的卸载

添加`-XX:+TraceClassUnloading`参数后可以看到类的卸载信息

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
        loader1.setPath("/home/cc/test/");
        Class<?> clazz = loader1.loadClass("indi.greenhat.jvm.Test");
        Object object = clazz.newInstance();
        System.out.println(object);

        System.out.println("-----------------");
        loader1 = null;
        clazz = null;
        object = null;
        /*
         *运行垃圾回收器。
		 *调用gc方法表明，Java虚拟机花费了回收未使用对象的努力，以使其当前占用的内存可以快速重用。
         *当控件从方法调用返回时，Java虚拟机已经尽力从所有丢弃的对象中回收空间。
         *等于Runtime.getRuntime().gc();
         */
        System.gc();
        System.out.println("-----------------");
    }
}
```

输出

```java
findClass invoked: indi.greenhat.jvm.Test
class loader name: loader1
indi.greenhat.jvm.Test@677327b6
-----------------
[Unloading class indi.greenhat.jvm.Test 0x0000000100061028]
-----------------
```

