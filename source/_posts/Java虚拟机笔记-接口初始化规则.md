---
title: Java虚拟机笔记-接口初始化规则
date: 2019-04-26 07:45:34
categories: 读书笔记
tags:
- jvm
---

接口初始化规则

<!-- more -->

# 接口初始化规则

当java虚拟机初始化一个类时，要求它的所有父类都已经初始化，但是这条规则不适用于接口

- 在初始化一个类时，并不会先初始化它所实现的接口
- 在初始化一个接口时，并不会先初始化它的父接口

因此，一个父接口并不会因为它的子接口或者实现类的初始化而初始化，只有当程序首次使用特定接口的静态变量时，才会导致该接口的初始化



# 样例1

```java
public class Main{
    public static void main(String[] args) {
        new C();
        new C();
    }
}

class C{
    {
        System.out.println("CC"); //每次创建对象时都会输出
    }
    public C(){
        System.out.println("c()");
    }
}
/*ouput:
CC
c()
CC
c()
*/
```

```java
public class Main{
    public static void main(String[] args) {
        new C();
        new C();
    }
}

class C{
    static{
        System.out.println("CC"); //创建对象时只会输出一次
    }
    public C(){
        System.out.println("c()");
    }
}
/*ouput:
CC
c()
*/
```

```java
public class Main{
    public static void main(String[] args) {
        System.out.println(Mychild5.b);
    }
}

interface MyParent5{
    public static Thread thread = new Thread(){
        {
            System.out.println("MyParent5 invoked");
        }
    };
}

interface  Mychild5 extends MyParent5{
    int b = 5;
}
```

程序输出5，并没有`MyParent5 invoked`

# 样例2

把接口改成类

```java
public class Main{
    public static void main(String[] args) {
        System.out.println(Mychild5.b);
    }
}

class MyParent5{
    public static Thread thread = new Thread(){
        {
            System.out.println("MyParent5 invoked");
        }
    };
}

class  Mychild5 extends MyParent5{
    public static int b = 5;
}
```

则两个都会输出

如果变b变为final的话

```java
public class Main{
    public static void main(String[] args) {
        System.out.println(Mychild5.b);
    }
}

class MyParent5{
    public static Thread thread = new Thread(){
        {
            System.out.println("MyParent5 invoked");
        }
    };
}

class  Mychild5 extends MyParent5{
    public static final int b = 5;
}

```

正好验证了之前学的结论，b已经被放到常量池中，这时候与`Mychild5`和`MyParent5`都没有关系，即使删掉这两者`.class`文件都可以运行

# 样例3

```java
public class Main{
    public static void main(String[] args) {
        System.out.println(Mychild5.b);
    }
}
interface MyGrandpa{
    public static Thread thread = new Thread(){
        {
            System.out.println("MyGrandpa invoked");
        }
    };
class MyParent5 extends MyGrandpa{
    public static Thread thread = new Thread(){
        {
            System.out.println("MyParent5 invoked");
        }
    };
}

class Mychild5 extends MyParent5{
    public static final int b = 5;
}
```

只输出5

因为输出b并不会导致MyParent5的初始化，更不会导致接口的初始化。

改成

```java
public class Main{
    public static void main(String[] args) {
        System.out.println(Mychild5.b);
    }
}
class MyGrandpa{
    public static Thread thread = new Thread(){
        {
            System.out.println("MyGrandpa invoked");
        }
    };
}
class MyParent5 extends MyGrandpa{
    public static Thread thread = new Thread(){
        {
            System.out.println("MyParent5 invoked");
        }
    };
}

class Mychild5 extends MyParent5{
    public static int b = 5;
}
```

则三者都会输出

其加载顺序

```java
[Loaded MyGrandpa from file:/home/cc/IdeaProjects/test/out/production/test/]
[Loaded MyParent5 from file:/home/cc/IdeaProjects/test/out/production/test/]
[Loaded Mychild5 from file:/home/cc/IdeaProjects/test/out/production/test/]
```

