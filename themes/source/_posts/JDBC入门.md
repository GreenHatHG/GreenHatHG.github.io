---
title: JDBC入门
date: 2018-09-18 12:25:28
categories: JAVA
tags: JDBC
---

java访问数据库技术

<!-- more -->

# JDBC概述以及功能

**概述**

- JDBC（Java DataBase Connectivity）是数据库公司开发的一种提供给Java编程者使用的Java API（类库），能够帮助Java程序访问表格数据，主要应用在对关系数据库的访问中。
- 在Java程序中，需要JDK中java.sql和javax.sql包中的类和JDBC包中的类配合完成对数据库的访问。

**功能**

1. 连接数据源（数据库）。
2. 将SQL语句（查询、更新）发送到数据库。
3. 获取并处理从数据库中返回的结果

# 基本代码

*省去项目加载驱动包过程，直接代码*

## 加载驱动类

```java
Class.forName(driverClass);
```

- Class.forName(xxx.xx.xx)返回的是一个类。
  Class.forName(xxx.xx.xx)的作用是要求JVM查找并加载指定的类，也就是说JVM会执行该类的静态代码段

  ```java
  static Class<?> forName(String className)
  ```

  *<？>是1.5的新特性，泛型  如果是?表示可以放Object类型以及他的子*类

- driverClass：不同数据库的驱动类不一样

  - MySQL的jdbc驱动类是： com.mysql.jdbc.Driver
  - Oracle的jdbc驱动类是：oracle.jdbc.driver.OracleDriver

## 连接数据库，获取Connection对象

```java
Connection con = DriverManager.getConnection(URL, databaseUserName, databasePassword);
```

- Connection：

```java
public interface Connection extends Wrapper, AutoCloseable;
```

- DriverManager类的静态方法通过数据库URL，可以连接数据库并获得一个Connection对象，方法的后两个参数是连接数据库的用户名和密码。

## 获取Statement对象，用于执行SQL语句

```java
Statement stmt  =  con.creatStatement();
```

- Statement:

```java
public class Statement extends Object;
```

- Statement对象:

  由Connection对象获得Statement对象，Statement对象可以执行SQL语句：

  - executeQuery方法执行查询语句
  - executeUpdate方法执行更新语句（增，删，改）

## 执行SQL语句，获取查询结果

```java
ResultSet rs = stmt.executeQuery(querySQL);
```

```
ResultSet executeQuery(String sql) throws SQLException //执行给定的SQL语句，返回单个ResultSet对象
```

*querySQL代表是SQL的查询语句*

- ResultSet:

```java
public interface ResultSet extends Wrapper, AutoCloseable;
```

## 关闭数据库连接，释放资源

```java
rs.close();	rs = null;
stmt.close(); stmt = null;
con.close(); con = null;
```

依次关闭ResultSet、Statement、Connection对象，通过赋值null释放资源。

# ResultSet

1. ResultSet对象表示数据库查询结果集，可以看成一个数据表，通常通过执行查询数据库来生成。 

2. 实现Statement接口的任何对象（包括PreparedStatement，CallableStatement和RowSet）都可以创建一个ResultSet对象。

3. 访问ResultSet中的对象：

   可以通过游标访问ResultSet对象中的数据，游标可以看做是指向ResultSet对象中的一行数据的指针。 

   最初，游标位于数据表第一行之前。ResultSet.next()方法将游标移动到下一行。 如果游标位于最后一行之后，此方法返回false，否则返回true。 该方法可以作为while循环的条件，来遍历ResultSet中的所有数据。

   **常用默认类型是游标只能前移，只读**

```java
while(rs.next())
{
    System.out.println(rs.getString("Name"));
}
```

```java
String getString(int columnIndex) throws SQLException
String getString(int columnIndex) throws SQLException
```

rs:数据集，即ResultSet对象。
​	rs.getInt(int index);
​	rs.getInt(String columName);
​	可以通过索引或者列名来获得查询结果集中的某一列的值。

# 元数据(MetaData)

## 概念

元数据：描述数据库或数据表本身属性的数据，例如数据库中的表名，每个表中的列名称，主键，外键，存储过程等。

所谓的MetaData在英文中的解释为“Data about Data”，直译成中文则为“有关数据的数据”或者“描述数据的数据”，实际上就是描述及解释含义的数据。以Result的MetaData为例，ResultSet是以表格的形式存在，所以getMetaData就包括了数据的字段名称、类型以及数目等表格所必须具备的信息。 

## 获取

JDBC提供了ResultSetMetaData接口，该接口可以获得数据表的元数据

```java
ResultSetMetaData getMetaData() //ResultSet接口中定义了一个方法
//该方法返回的对象可以获取表的元数据，例如有多少列，列名是什么等
```

# 总结代码

```java
import java.sql.*;

public class DB
{
    static Statement stm = null;
    static ResultSet rs = null;

    //连接数据库并且获取Statement对象
    public DB(String driverClass, String dataBase, String usrName, String passWord)
    {
        try {
            Class.forName(driverClass);
            Connection con = DriverManager.getConnection(dataBase, usrName, passWord);
            stm = con.createStatement();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    //查询数据库
    public void queryDatabase(String querySQL)
    {
        try{
            rs = stm.executeQuery(querySQL);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount(); //获取表的列数

            for(int i = 1; i <= columnCount; i++)
                System.out.print(rsmd.getColumnName(i) + " "); //输出每一列的列名
            System.out.println();

            while(rs.next())
            {
                for(int i = 1; i <= columnCount; i++)
                    System.out.print(rs.getObject(i) + "  "); //输出每一列内容
                System.out.println();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    //修改数据库中的信息
    public void updateDatabase(String updateSQL)
    {
        try{
            int numUpate = stm.executeUpdate(updateSQL); //返回已更新个数
            System.out.println("已更新了" + numUpate + "条记录");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    //删除数据库中的信息
    public void deleteDatabase(String deleteSQL)
    {
        try{
            stm.executeUpdate(deleteSQL);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    //往数据库中添加信息
    public void insertDatabase(String insertSQL)
    {
        try{
            stm.executeUpdate(insertSQL);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    
    public void close()
    {
        try{
            rs.close();	
            rs = null;
			stmt.close(); 
            stmt = null;
			con.close(); 
            con = null;
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

     public static void main(String[] args)
    {
        String schema = "data";
        String table = "new_table";
        String driverClass = "com.mysql.cj.jdbc.Driver";
        String database = "jdbc:mysql://localhost:3306/" + schema + "?useSSL=false&serverTimezone=GMT%2B8&characterEncoding=UTF-8";
        String username = "root";
        String password = "123456";

        String querySQL = "select* from " + table;
        String updateSQL = "update " + table + " set grade=70 where name='小陈'";
        String deleteSQL = "delete from " + table + " where name='小丽'";
        String insertSQL = "insert into " + table + " values('小白', '男', '98')";

        DB db = new DB(driverClass, database, username, password);
        System.out.println("原先的数据库数据为");
        db.queryDatabase(querySQL);
        System.out.println();
        db.updateDatabase(updateSQL);
        System.out.println("执行update将小陈成绩变成70");
        db.queryDatabase(querySQL);
        System.out.println();
        System.out.println("将名为小丽那一行删除");
        db.deleteDatabase(deleteSQL);
        db.queryDatabase(querySQL);
        System.out.println();
        System.out.println("添加名为小白一行");
        db.insertDatabase(insertSQL);
        db.queryDatabase(querySQL);
        db.close();
    }
}
```

