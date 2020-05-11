---
title: JAVA练习总结
date: 2018-04-24 22:13:33
categories: JAVA
tags: 练习
---

年少不知道JAVA好，老来望码空流泪

<!-- more -->

# 继承中的构造函数

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/QQ%E6%88%AA%E5%9B%BE20180424221235.png">

```
ans: A
```

# 包意义

package awt;的结果是()

A.编译结果出错

B.说明文件的类包含在JAVA的awt包中

C.说明文件的类在自定义的awt包中

D.导入自定义的awt包中的类

```
import与package:
Java 包(package)
为了更好地组织类，Java 提供了包机制，用于区别类名的命名空间。 

包的作用  
1、把功能相似或相关的类或接口组织在同一个包中，方便类的查找和使用。

2、如同文件夹一样，包也采用了树形目录的存储方式。同一个包中的类名字是不同的，不同的包中的类的名字是可以相同的，当同时调用两个不同包中相同类名的类时，应该加上包名加以区别。因此，包可以避免名字冲突。

3、包也限定了访问权限，拥有包访问权限的类才能访问某个包中的类。

Java 使用包（package）这种机制是为了防止命名冲突，访问控制，提供搜索和定位类（class）、接口、枚举（enumerations）和注释（annotation）等。

import 关键字
为了能够使用某一个包的成员，我们需要在 Java 程序中明确导入该包。使用 "import" 语句可完成此功能。

ans: C
```

参考链接：[JAVA包（package）](http://www.runoob.com/java/java-package.html)

# 继承String

你编译代码class MyString extends String()会出现那种情况（）   
A.编译成功   
B.不能编译，因为没有main方法   
C.不能编译，因为String是abstract的   
D.不能编译，因为String是final的  

```
解析：主要考察String这个类，该类是一个final关键字修饰的，final关键词修饰的类不能被继承，修饰的方法不能被重写，修饰的常量值不能被改变，否则就会报错。

ans: D
```

#  异常

当方法遇到异常又不知如何处理时，下列( )做法是正确的。  

A．捕获异常  
B．抛出异常  
C．声明异常  
D．嵌套异常  

```
ans： B
```

# 内部类与外部类

内部类可以访问外层类的任何变量,包括私有  

```
内部类就相当于一个外部类的成员变量，所以可以直接访问外部变量，外部类不能直接访问内部类变量，必须通过创建内部类实例的方法访问。
```

# 多个catch

java异常处理中可以使用多个catch子句，此时应将高级别的异常类的catch子句放在前面

```
JAVA没有相关规范
ans： wrong
```

