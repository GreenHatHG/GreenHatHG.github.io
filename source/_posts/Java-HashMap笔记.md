---
title: Java-HashMap笔记
date: 2019-10-10 19:57:14
categories: 读书笔记
tags:
- Java
- HashMap
---
HashMap
<!-- more -->

# Hashtable and HashMap

## Hashtable and HashMap in Java

Hashtable and HashMap are quite similar – both are collections that implement the Map interface.*

Hashtable和HashMap是非常相似的，因为它们都是实现了Map接口的集合



*Also, the **put()**, **get()**, **remove()**, and **containsKey()** methods provide constant-time performance O(1). Internally, these methods work based on a general concept of hashing using buckets for storing data.*

并且，它们的`put()`, `get()`, `remove()`, 和`containsKey()`的时间复杂度都固定为O(1)。 这些方法内部是基于一般概念的哈希去用桶(`bucket`)存储数据的。

>  a bucket is each element in the array you are referring to
>
> [java - What exactly is bucket in hashmap? - Stack Overflow](https://stackoverflow.com/questions/37959941/what-exactly-is-bucket-in-hashmap)
>
> bucket是您要引用的数组中的每个元素



*Neither class maintains the insertion order of the elements. In other words, the first item added may not be the first item when we iterate over the values.*

这两个类都不保证元素的插入顺序。换句话说，当我们遍历值时，第一项可能不是第一次插入的。



*But they also have some differences that make one better than another in some situations. Let's look closer at these difference**Hashtable and HashMap in Java**s.*

它们也有一些差异，在某种情况下，可能使用前者会比后者好。



## Differences Between Hashtable and HashMap

### Synchronization

*Firstly, **Hashtable is thread-safe** and can be shared between multiple threads in the application.*

首先，`Hashtable`是线程安全的，并且可以在应用程序中的多个线程之间共享。



*On the other hand, HashMap is not synchronized and can't be accessed by multiple threads without additional synchronization code. We can use Collections.synchronizedMap() to make a thread-safe version of a HashMap. We can also just create custom lock code or make the code thread-safe by using the synchronized keyword.*

从另外一个角度来讲，`HashMap`不是线程同步的，并且在不添加额外的同步的代码情况下是不能被别的线程访问的。我们可以使用`Collections.synchronizedMap()`方法去生成线程安全的`HashMap`





---

参考：

[Differences Between HashMap and Hashtable | Baeldung](https://www.baeldung.com/hashmap-hashtable-differences)

[A Guide to Java HashMap | Baeldung](https://www.baeldung.com/java-hashmap)

----

