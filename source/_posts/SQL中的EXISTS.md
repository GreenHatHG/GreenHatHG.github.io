---
title: SQL中的EXISTS
date: 2019-07-02 09:52:23
categories: 读书笔记
tags:
- 数据库
- Mysql
---
SQL中的EXISTS与SQL中的NOT EXISTS

<!-- more -->

# 从`SQL`中基础的`WHERE`字句开始

有一学生信息表

```plsql
SELECT Sno, Sname FROM Student WHERE Sdept = 'IS';
```

很显然，在执行这条 `SQL `语句的时候，`DBMS `会扫描 `Student `表中的每一条记录，然后把符合 `Sdept = 'IS' `这个条件的所有记录筛选出来，并放到结果集里面去。也就是说 **`WHERE `关键字的作用就是判断后面的逻辑表达式的值是否为 `True`。如果为 `True`，则将当前这条记录（经过` SELECT `关键字处理后）放到结果集里面去，如果逻辑表达式的值为 `False `则不放**。

# 使用`EXISTS`关键字

```plsql
SELECT Sname FROM Student WHERE EXISTS
	(SELECT * FROM SC WHERE
     	Sno = Student.Sno AND Cno = '1');
```

这条` SQL`语句的作用，就是**查找所有选修了 1 号课程的课程的学生，并显示他们的姓名**。

我们先不管` EXISTS `关键字在其中起了什么作用，而是先来看子查询中的` WHERE` 关键字后的表达式 :

```plsql
Sno = Student.Sno AND Cno = '1'
```

其中的 `Sno = Student.Sno `是怎么一回事？这就涉及到 SQL 中的**不相关子查询与相关子查询**了

# 不相关子查询与相关子查询

## 不相关子查询

我们常见的带子查询的` SQL `语句是这样的：

```plsql
SELECT Sno FROM SC WHERE Cno IN
	(SELECT Cno FROM Course WHERE 
    	Cname = '数据结构');
```

查询过程如下：

1. 首先通过子查询得到课名为 “数据结构” 的课程的课号
2. 然后遍历 `SC` （选课）表中的每一条选课记录

   - 若当前这条记录的课号为 “数据结构” 这门课的课号，则将这条记录的 Sno 列的值放到结果集里面去。
3. 最终我们可以得到所有选修了 ”数据结构“ 这门课的学生的学号。

---

这种类型的查询，叫做"不相关子查询"：

**这种类型的查询是先执行子查询，得到一个集合（或值），然后将这个集合（或值）作为一个常量带入到父查询的 WHERE 子句中去。如果单纯地执行子查询，也是可以成功的。**


## 相关子查询

大多数情况下，不相关子查询已经够用了，但是如果有这样的一个查询要求：

```
查询每个学生超过自身选修的所有课程的平均成绩的课程的课程号
```

子查询我们可以先查询出**每个学生自身选修的所有课程的平均成绩**：

```plsql
SELECT AVG(Grade) FROM SC WHERE Sno = ?
```

那么问题来了，`WHERE`后面应该怎么写？也就`Sno=？`这里面怎么填

关键问题就是，？ 处这个常量，并不是一个确定的值，而**应该是不断地将 `Student `表中的每一条记录中的 `Sno `列的值代入此处，然后求出该` Sno `对应的平均成绩。我们需要的是输入一系列的值，然后得到一系列对应的输出。**

---

这个时候，我们就要用到另一种嵌套查询，叫做 “相关子查询”。**“相关子查询” 的意思就是，子查询中需要用到父查询中的值。**

对于这个查询要求，我们可以使用以下` SQL` 语句：

```plsql
SELECT Cno FROM SC X WHERE Grade >=
	(SELECT AVG(Grade) FROM SC Y WHERE
    	Y.Sno = X.Sno);
```

其工作原理就是：

1. 先扫描父查询中数据来源（如 `SC` 表）中的每一条记录
2. 然后将当前这条记录中的，在子查询中会用到的值代入到子查询中去
3. 接着执行子查询并得到结果（可以看成是返回值）
4. 然后再将这个结果代入到父查询的条件中，判断父查询的条件表达式的值是否为 `True`
   - 若为 `True`，则将当前 `SC` 表中的这条记录（经过 `SELECT` 处理）后放到结果集中去
   - 若为 `False` 则不放

在这个例子中：

1. 父查询先从 SC 表中取出第一条记录（如 95001）

2. 然后将当前这条记录的 Sno 列的值（95001）代入到子查询中，求出学号为 95001 的学生选修的所有课程的平均分（如 80 分）

3. 然后将这个 80 作为 `Grade >=` 后面的值代入

   - 若 `SC` 表中的第一条记录的`Grade `列的值为 90，那么 `Grade >= 80 `这个条件表达式的值为 True，则将当前这条记录中的 `Cno `列的值（如1）放入结果集中去。

4. 以此类推，遍历 `SC `表中的所有记录，即可得到每个超过学生超过他/她所有课程平均分的课程的课号了。

**判断是否是 “相关子查询” 也很简单，只要子查询不能脱离父查询单独执行，那么就是 “相关子查询”。**

# `EXISTS `关键字的作用

它的作用，就是判断子查询得到的结果集是否是一个空集，如果不是，则返回 `True`，如果是，则返回 `False`。EXISTS 本身就是 “存在” 的意思，用我们可以理解的话来说，**就是如果在当前的表中存在符合条件的这样一条记录，那么返回` True`，否则返回 `False`。**

有一条语句：

```plsql
SELECT Sname FROM Student WHERE EXISTS(
	SELECT * FROM SC WHERE
    	SC.Sno=Student.Sno AND Cno='1');
```

在这个查询中：

1. 首先会取出 `Student `表中的第一条记录，得到其 Sno 列（因为在子查询中用到了）的值（如 95001）
2. 然后将该值代入到子查询中。若能找到这样的一条记录，那么说明学号为 95001 的学生选修了 1 号课程。
   - 因为能找到这样的一条记录，所以子查询的结果不为空集，那么 `EXISTS `会返回 `True`，从而使 `Student `表中的第一条记录中的 `Sname `列的值被放入结果集中去。
   - 以此类推，遍历 `Student `表中的所有记录后，就能得到所有选修了 1 号课程的学生的姓名。

# `NOT EXISTS`关键字

与` EXISTS `关键字相对的是` NOT EXISTS`，作用与 `EXISTS `正相反，**当子查询的结果为空集时，返回 `True`，反之返回 `False`。也就是所谓的 ”若不存在“。**

对于下面的查询要求，只能通过` NOT EXISTS `关键字来实现，因为 `SQL` 中并未直接提供关系代数中的除法功能。

- 查询选修了全部课程的学生的姓名

可以通过以下步骤的思路来实现：

1. 先取 `Student` 表中的第一个元组，得到其 `Sno` 列的值。也就是获取到学生对应的学号
2. 再取` Course `表中的第一个元组，得到其` Cno` 列的值。也就是获取到课程对应的课程号
3. 根据 `Sno` 与 `Cno` 的值，遍历 `SC` 表中的所有记录（也就是选课记录）。若对于某个 `Sno `和 `Cno `的值来说，**在 `SC` 表中找不到相应的记录，则说明该 `Sno` 对应的学生没有选修该 `Cno` 对应的课程。**

4. 对于某个学生来说，若在遍历 `Course `表中所有记录（也就是所有课程）后，**仍找不到任何一门他/她没有选修的课程，就说明此学生选修了全部的课程。**
5. 将此学生放入结果元组集合中
6. 回到第一步，取`Student`中的下一个元组
7. 将所有结果元组集合显示

```plsql
SELECT Sname FROM Student WHERE NOT EXISTS
	(SELECT * FROM Course WHERE NOT EXISTS
    	(SELECT * FROM SC WHERE
    		Sno=Student.Sno AND Cno=Course.Cno));
```

其中第一个 `NOT EXISTS `对第四步，第二个 `NOT EXISTS `对应第三步。

---

同理，对于类似的查询要求

- 查询被所有学生选修的课程的课名

```plsql
SELECT Cname FROM Course WHERE NOT EXISTS
	(SELECT * FROM Stuent WHERE NOT EXISTS
    	(SELECT * FROM SC WHERE
        	Sno=Student.Sno AND Cno=Course.Cno));
```

- 查询选修了95001号学生选修的全部课程的学生的学号

```plsql
SELECT DISTINCT Sno FROM SC X WHERE NOT EXISTS
	(SELECT * FROM SC Y WHERE Y.Sno='95001' AND NOT EXISTS
    	(SELECT * FROM SC Z WHERE 
        	Z.Sno=X.Sno AND Z.Cno=Y.Cno));
```



参考：

[SQL 中的 EXISTS 到底做了什么？ - 知乎](https://zhuanlan.zhihu.com/p/20005249)