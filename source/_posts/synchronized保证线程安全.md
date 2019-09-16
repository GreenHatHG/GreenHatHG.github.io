---
title: synchronized保证线程安全
date: 2019-08-26 08:53:57
categories: 读书笔记
tags:
- 多线程
- Java
---

synchronized笔记

<!-- more -->

# 线程安全

## 主要诱因

- 存在共享数据(也称临界资源)
- 存在多条线程共同操作共享数据

## 解决问题根本

**同一时刻有且只有一个线程在操作共享数据，其他线程必须等到该线程处理完数据后再对共享数据进行操作**。所以可以引入互斥锁去解决问题

### 互斥锁

- 互斥性：即在同一时间只允许一个线程持有某个对象锁，通过这种特性来实现多线程的协调机制，**这样在同一时间只有一个线程对需要同步的代码块（复合操作）进行访问**。互斥性也称为操作的原子性。

- 可见性：必须确保在锁被释放之前，对共享变量所做的修改，对于随后获得该锁的另一个线程是可见的（即**在获得锁时应获得最新共享变量的值**），否则另一个线程可能是在本地缓存的某个副本上继续操作，从而引起不一致。

---

在 Java 中，关键字`synchronized`可以保证**在同一个时刻，只有一个线程可以执行某个方法或者某个代码块**(主要是对方法或者代码块中存在共享数据的操作)，同时我们还应该注意到`synchronized`另外一个重要的作用，`synchronized`可**保证一个线程的变化(主要是共享数据的变化)被其他线程所看到**（保证可见性，完全可以替代`Volatile`功能），这点确实也是很重要的

# 定义

Java语言的关键字，可用来给**对象**和**方法或者代码块**加锁（**注意，这里加锁是说可以用在对象或代码块上面，但是`synchronized`锁的是对象，不是代码，重要！！！**），当它锁定一个方法或者一个代码块的时候，同一时刻最多只有一个线程执行这段代码。

当两个并发线程访问同一个对象object中的这个加锁同步代码块时，一个时间内只能有一个线程得到执行。另一个线程必须等待当前线程执行完这个代码块以后才能执行该代码块。然而，**当一个线程访问object的一个加锁代码块时，另一个线程仍可以访问该object中的非加锁代码块**。

# 分类

根据获取的锁的分类：获取对象锁和类锁

## 对象锁

获取对象锁的两种用法：

1. 同步代码块（`synchronized(this)`，`synchronized(类实例对象)`），**锁是小括号()中的实例对象**。
2. 同步非静态方法（`synchronized method`），**锁是当前对象的实例对象**。

```java
package indi.greenhat.thread;

/**
 * 对某个对象加锁
 * @author GreenHatHG
 **/
public class Synchronized_01 {
    private int count = 10;
    //obj指向堆内存种new出来的对象
    //而synchronized锁的在堆内存中new出来的对象，而不是obj的引用
    //如果obj指向别的对象，那么锁的对象就变了
    //也就是说申请锁的时候，申请锁的信息是记录在堆内存对象里面的
    private Object obj = new Object();

    public void test(){
        //任何线程想要执行下面代码，必须先拿到obj的锁
        //也可以写成sychronized(this)
        synchronized (obj){
            count--;
            System.out.println(Thread.currentThread().getName() + " count = " + count);
        }
    }
}
```

## 类锁

获取类锁的两种用法：

1. 同步代码块（`synchronized(类.class)`），**锁是小括号()中的类对象**(`Class`对象)。
2. 同步静态方法（`synchronized static method`），**锁是当前对象的类对象**(Class对象)。

```java
package indi.greenhat.thread;

/**
 * @author GreenHatHG
 **/
public class Synchronized_02 {
    private static int count = 10;

    //这里等同于synchronized (Synchronized_02.class)
    public synchronized static void test1(){
        count--;
        System.out.println(Thread.currentThread().getName() + " count = " + count);
    }

    public static void test2(){
        //这里不能写(synchronized)
        //因为静态方法没有new对象
        synchronized (Synchronized_02.class){
            count--;
            System.out.println(Thread.currentThread().getName() + " count = " + count);
        }
    }
}
```

# 同步与非同步混用

```java
package indi.greenhat.thread;

/**
 * 同步与非同步混用，出现脏读
 * @author GreenHatHG
 **/
public class Synchronized_03 {

    String name;
    /**
     * 余额
     */
    double balance;

    public synchronized void set(String name, double balance){
        this.name = name;
        try{
            //放大2个线程的间隔，更加显示错误
            Thread.sleep(2000);
        }catch (Exception e){
            e.printStackTrace();
        }
        this.balance = balance;
    }

    public double getBalance(){
        return this.balance;
    }

    public static void main(String[] args) {
        Synchronized_03 account = new Synchronized_03();
        new Thread(()->account.set("zhangsan", 100.0)).start();

        try{
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println(account.getBalance());

        try{
            Thread.sleep(2000);
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println(account.getBalance());
    }
}
```

```
0.0
100.0
```

只对写加锁，而不对读加锁，可能产生脏读现象，虽然锁了`set()`，但是`getBalance()`并没有。所以正确的思路就是对读和写都加锁。

# 可重入

一个同步方法可以调用另外一个同步方法，一个线程已经拥有某个对象的锁，再次申请的时候仍然会得到该对象的锁，也就是说`synchronized`获得的锁是可重入的

```java
package indi.greenhat.thread;

/**
 * 可重入
 * @author GreenHatHG
 **/
public class Synchronized_04 {

    synchronized void m1(){
        System.out.println("m1 start");
        try{
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }
        m2();
        System.out.println("m1 end");
    }

    synchronized void m2(){
        System.out.println("m2 start");
        try{
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("m2 end");
    }

    public static void main(String[] args) {
        new Synchronized_04().m1();
    }
}
```

```
m1 start
m2 start
m2 end
m1 end
```



---

参考：

[Java并发编程—synchronized保证线程安全的原理分析 - 掘金](https://juejin.im/post/5c9f782de51d4534c15b711f)

---

