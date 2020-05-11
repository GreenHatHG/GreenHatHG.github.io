---
title: JDBC之PreparedStatement对象执行SQL语句
date: 2018-09-18 18:26:22
tags: JDBC
categories: JAVA
---

实现Statement接口

<!-- more -->

# 概述

PreparedStatement实现了Statement接口，使用该接口类型的对象同样可以执行SQL语句。

**特点**

- 预编译：创建时就包含了一个SQL语句，并送到数据库系统进行编译，下次再执行同样的对象就不用在编译了，节省时间。
- SQL语句可以包含参数：使用采用参数的SQL语句的优点是可以使用相同的语句，并在每次执行它时提供不同的值

# 使用代码

*假设之前的工作已经做好，con是已经获取到的connection对象*

## 查询

```java
Preparestatement pstmt = con.prepareStatement("select * from student");
ResultSet rs = pstmt.execuQuery();
try{
    while(rs.next())
    	System.out.println(rs.getString("Name"));	
}catch (SQLException e){
    e.printStackTrace();
}
```

## 插入

```java
Preparestatement pstmt = con.prepareStatement("insert into student1 values (?,?,?,?)");
pstmt.setString(1, "Name");
pstmt.setString(2, "Grade");
pstmt.setString(3, "Sex");
pstmt.setString(4, "Sno");  
int numInsert=pstmt.executeUpdate(); //返回插入的数量                                     
```

setString是定义了字符串中第n个”?“字符的替换

```java
void setString(int parameterIndex, String x) throws SQLException
```

## 修改

```java
Preparestatement pstmt = con.prepareStatement("update student1 set NAME=?,SEX=?,DEPT=? where NO=?");
pstmt.setString(1, "Name");
pstmt.setString(2, "Grade");
pstmt.setString(3, "Sex");
pstmt.setString(4, "Sno");  
int numDelete=pstmt.executeUpdate();
```

## 删除

```java
Preparestatement pstmt = con.prepareStatement("delete from student1 where NO=?");
pstmt.setString(1, "Sno");				
int numDelete=pstmt.executeUpdate();
```

