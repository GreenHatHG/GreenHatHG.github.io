---
title: java基于URL网络编程--获取网页HTML
date: 2018-09-30 16:26:25
categories: JAVA
tags: 
- JAVA
- JDBC
---

JAVA URL编程

<!-- more -->

# 步骤

1. 创建一个URL。
2. 获取URLConnection对象。
3. 在URLConnection上设置输出功能。
4. 从连接获取输入或输出流。
5. 读取输入流或写入输出流。
6. 关闭输入流或输出流。

# 具体步骤

## 创建一个URL

### URL在程序中的表示

在Java中定义了一个URL类，该类可以封装URL信息，创建一个URL对象。

```java

```

### URL构造办法

常用的传一个网址

```java
URL(String spec)
//从 String表示形成一个 URL对象。
```

```java
URL myURL = new URL("http://example.com/");
```

还有根据协议，端口等等进行创建的。

### 使用URL对象

创建完成URL对象后，可以像使用普通的类对象一样去调用URL类的各种方法

```java

```

```java
String getHost( )
//返回URL的主机名
```

```java
int getPort( )
//返回URL的端口号, 如果没有设置端口号返回值为-1）。
```

```java
String getFile( )
//返回URL的文件名及路径。
```

```java
String getRef( )
//返回URL的标记    
```

等等

## 获取URLConnection对象

### URL与URLConnection

- 概念

​        URL类将URL地址封装成对象，提供了解析URL地址的方法，如获取uri部分、host部分、端口等。

​         URLConnection则是URL对象和Socket连接给结合起来了，使得可以更轻松地获取发起URL请求的连接套接字。

- 两者关系

​         通过URL的openConnection()方法可以获取URLConnection对象，这个对象是面对这个URL的连接。

​        也就是说，这个对象其实是一个已连接套接字，它不仅具有解析http响应报文的功能，还具有套接字的相关功能(例如获取输入流、输出流等)。

- 仅就解析对象来说，URL对象解析的是URL地址，可以看作是解析http请求报文(如getPort(),getFile()等)，而URLConnection则解析的是http响应报文(如getLastModified(),getHeaderFields()等)。

### URLConnection

```java
//java.net.URLConnection
public abstract class URLConnection extends Object
```

可以由URL获得URLConnection

```java
URLConnection(URL url)
//构造与指定URL的URL连接。
URLConnection conn = url.openConnection();//获取URLConnection对象
```

## 获取网络输入流

防止读取超时可以设置一波连接超时时间

```java
setConnectTimeout(int timeout)
//设置打开与此URLConnection引用的资源的通信链接时使用的指定超时值（以毫秒为单位）。
conn.setConnectTimeout(3 * 1000);
//conn：URLConnection对象
```

获取网络输入流

```java
InputStream inputStream = conn.getInputStream();
```



