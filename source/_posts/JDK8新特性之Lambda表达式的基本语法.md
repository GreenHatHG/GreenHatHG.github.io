---
title: JDK8新特性之Lambda表达式的基本语法
date: 2019-08-28 15:18:46
categories: 读书笔记
tags:
- lambda
- Java
---

个人感觉Lambda用于内部类即可，其他地方可能还会造成效率低下，难于debug

<!-- more -->

# 简介

官方文档:[Lambda Expressions (The Java™ Tutorials > Learning the Java Language > Classes and Objects)](https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html)

---

`Lambda`表达式，也可称为闭包，它是推动 `Java8` 发布的最重要新特性。

`Lambda` 允许把函数作为一个方法的参数（函数作为参数传递进方法中）。

使用 `Lambda` 表达式可以使代码变的更加简洁紧凑

# 表达式格式

在`Java8`语言中引入的一种新的语言元素和操作符，这个操作符为`->`，该操作符被称为`Lambda`操作符或箭头操作符,它将`Lambda`分为以下两个部分

1. 左侧 : 指定`Lambda`表达式需要的参数列表
2. 右侧 : 制定了`Lambda`体,是抽象方法的实现逻辑，也既`Lambda`表达式要执行的功能

表达式的语法格式如下：

```java
(parameters) -> expression
或
(parameters) ->{ statements; }
```

本质：作为接口的实例

# 特征

- 可选类型声明 : 不需要声明参数类型,编译器可以统一识别参数值
- 可选的参数圆括号 : **一个参数无需定义圆括号,但多个参数需要定义圆括号**
- 可选的大括号 : 如果主体包含了一个语句,就不需要使用大括号
- 可选的返回关键字 : **如果主体只有一个表达式返回值则编译器会自动返回值,大括号需要指定明表达式返回了一个数值**

# 用于内部类的语法

## 无参,无返回值

```java
Runnable r1 = new Runnable() {
            @Override
            public void run() {
                System.out.println("Test1");
            }
        };
        
r1.run();
```

```java
Runnable r2 = () -> System.out.println("Test1");
r2.run();
```

## 需要一个参数,但是没有返回值

```java
Consumer<String> c1 = new Consumer<String>() {
    @Override
    public void accept(String s) {
        System.out.println(s);
    }
};

c1.accept("Test2");
```

```java
Consumer<String> c2 = (String s) -> {
    System.out.println(s);
};

c2.accept("Test2");
```

## 数据类型可以省略

数据类型可以省略，因为可由编译器推断得出,称为"类型推断"

```java
Consumer<String> c1 = new Consumer<String>() {
    @Override
    public void accept(String s) {
        System.out.println(s);
    }
};

c1.accept("Test2");
```

```java
Consumer<String> c2 = (s) -> {
    System.out.println(s);
};

c2.accept("Test2");
```

其他"类型推断"例子：

```java
//ArrayList<String> list = new ArrayList<String>();
ArrayList<String> list = new ArrayList<>();

//int[] arr = new int[]{1,2,3};
int [] arr = {1,2,3};
```

## 若需要一个参数时,参数的小括号可以省略

```java
Consumer<String> c1 = new Consumer<String>() {
    @Override
    public void accept(String s) {
        System.out.println(s);
    }
};

c1.accept("Test2");
```

```java
Consumer<String> c2 = s -> {
    System.out.println(s);
};

c2.accept("Test2");
```

## 需要两个或以上的参数,多条执行语句,并且可以有返回值

```java
Comparator<Integer> com1 = new Comparator<Integer>() {
    @Override
    public int compare(Integer o1, Integer o2) {
        System.out.println(o1);
        System.out.println(o2);
        return o1.compareTo(o2);
    }
};
```

```java
Comparator<Integer> com2 = (o1, o2) ->{
    System.out.println(o1);
    System.out.println(o2);
    return o1.compareTo(o2);
};
```

## Lambda体只有一条语句时,return与大括号若有,都可以省略

```java
Comparator<Integer> comparator = new Comparator<Integer>() {
        @Override
        public int compare(Integer x, Integer y) {
            return Integer.compare(x, y);
        }
    };
System.out.println(comparator.compare(1, 2));
```

```java
Comparator<Integer> comparator = (x, y) -> Integer.compare(x, y);
System.out.println(comparator.compare(1, 2));
```

或者

```java
Comparator<Integer> comparator = Integer::compare;
```

双冒号运算就是`Java`中的[方法引用],[方法引用]的格式是 **类名::方法名**。

```java
//调用实例person的getName方法
person -> person.getName();

Person::getName
```

```java
() -> new HashMap<>();

HashMap::new
```



---

参考:

[Java 8 Lambda 表达式 | 菜鸟教程](https://www.runoob.com/java/java8-lambda-expressions.html)

[JDK-8新特性之Lambda表达式的基本语法.md](https://github.com/YUbuntu0109/YUbuntu0109.github.io/blob/HexoBackup/source/_posts/JDK-8新特性之Lambda表达式的基本语法.md)

---

