---
title: LinuxShell
date: 2018-06-16 14:52:18
categories: linux
tags: LinuxShell
---

LinuxShell操作小总结

<!-- more -->

# sed，获取当前路径，强制复制，du的用法

##  命令格式

sed [选项] ‘命令’ 输入文本 

```shell
sed [-nefri] ‘command’ 输入文本   
```

## 选项与参数

| 选项 | 说明                                                         |
| :--: | :----------------------------------------------------------- |
|  -n  | 使用安静(silent)模式。在一般 sed 的用法中，所有来自 STDIN 的数据一般都会被列出到终端上。但如果加上参数后，则只有经过sed 特殊处理的那一行(或者动作)才会被列出来 |
|  -e  | 直接在命令列模式上进行 sed 的动作编辑                        |
|  -f  | 直接将 sed 的动作写在一个文件内， -f filename 则可以运行 filename 内的 sed 动作 |
|  -r  | sed 的动作支持的是延伸型正规表示法的语法。(默认是基础正规表示法语法) |
|  -i  | 直接修改读取的文件内容，而不是输出到终端                     |

## 常用命令

| 命令 | 说明                                                         |
| :--: | ------------------------------------------------------------ |
|  a   | 新增， a 的后面可以接字串，而这些字串会在新的一行出现(目前的下一行) |
|  c   | 取代， c 的后面可以接字串，这些字串可以取代n1,n2 之间的行！  |
|  d   | 删除，因为是删除啊，所以 d后面通常不接任何咚咚               |
|  i   | 插入， i的后面可以接字串，而这些字串会在新的一行出现(目前的上一行) |
|  p   | 输出，亦即将某个选择的资料输出。通常 p 会与参数 sed-n 一起运作 |
|  s   | 取代，可以直接进行取代的工作哩！通常这个 s的动作可以搭配正则表达式！例如 1,20s/old/new/g 就是啦！ |

## 对行操作

现有一文件

```shell
# cat ~/test.sh 
#!/bin/bash
str1="[archlinuxcn]"
str2="Server = https://mirrors.ustc.edu.cn/archlinuxcn/\$arch"
echo ${str2}
echo "写入完成"
```

### 删除一行

```shell
sed '1d' ~/test.sh
```

举个例子：

(以后同理)

```shell
# sed '1d' ~/test.sh
str1="[archlinuxcn]"
str2="Server = https://mirrors.ustc.edu.cn/archlinuxcn/\$arch"
echo ${str2}
echo "写入完成"
# cat ~/test.sh  
#!/bin/bash
str1="[archlinuxcn]"
str2="Server = https://mirrors.ustc.edu.cn/archlinuxcn/\$arch"
echo ${str2}
echo "写入完成"
```

对源文件进行操作

```shell
# sed -i '1d' ~/test.sh
# cat ~/test.sh           
str1="[archlinuxcn]"
str2="Server = https://mirrors.ustc.edu.cn/archlinuxcn/\$arch"
echo ${str2}
echo "写入完成"
```

### 删除最后一行 

```shell
sed '$d' ~/test.sh
```

### 删除第一行到第二行

```shell
sed '1,2d' ~/test.sh
```

### 删除第二行到最后一行

```shell
sed '2,$d' ～/test.sh
```

### 显示某行

第一行

```shell
sed -n '1p' ～/test.sh
```

最后一行

```shell
sed -n '$p' ~/test.sh
```

第一行到第二行

```shell
sed -n '1,2p' ～/test.sh
```

第二行到最后一行

```shell
sed -n '2,$p' ~/test.sh
```

*不加-n*

```shell
# sed '$p' ~/test.sh  
#!/bin/bash
str1="[archlinuxcn]"
str2="Server = https://mirrors.ustc.edu.cn/archlinuxcn/\$arch"
echo ${str2}
echo "写入完成"
echo "写入完成"

# sed '1p' ~/test.sh
#!/bin/bash
#!/bin/bash
str1="[archlinuxcn]"
str2="Server = https://mirrors.ustc.edu.cn/archlinuxcn/\$arch"
echo ${str2}
echo "写入完成"

```

### 显示含有某个字符串的所有行

```shell
sed -n '/echo/p' ~/test.sh 
```

```shell
# sed -n '/echo/p' ~/test.sh 
echo ${str2}
echo "写入完成"
```

### 在第一行后增加一行

```shell
sed '1a new str1' ~/test.sh
```

```shell
# sed '1a new str1' ~/test.sh
#!/bin/bash
new str1
str1="[archlinuxcn]"
str2="Server = https://mirrors.ustc.edu.cn/archlinuxcn/\$arch"
echo ${str2}
echo "写入完成"
```

### 在第一行到第三行后增加一行

```shell
sed '1,3a new str' ~/test.sh
```

```shell
# sed '1,3a new str' ~/test.sh
#!/bin/bash
new str
str1="[archlinuxcn]"
new str
str2="Server = https://mirrors.ustc.edu.cn/archlinuxcn/\$arch"
new str
echo ${str2}
echo "写入完成"

```

### 在第一行后增加多行

使用\n将要增加的每行内容分割开

```shell
sed '1a newstr1\nnewstr2' ~/test.sh
```

```shell
sed '1a newstr1\nnewstr2' ~/test.sh
#!/bin/bash
newstr1
newstr2
str1="[archlinuxcn]"
str2="Server = https://mirrors.ustc.edu.cn/archlinuxcn/\$arch"
echo ${str2}
echo "写入完成"
```

# 获取当前路径

```Shell
filepath=$(cd "$(dirname "$0")"; pwd)
```

dirname $0，取得当前执行的脚本文件的父目录

cd `dirname $0`，进入这个目录(切换当前工作目录)

pwd，显示当前工作目录(cd执行后的)

# cp -f强制复制


-f 参数是强制复制,比如你在A文件夹里面有个文件名叫B，然后你把C文件夹里面的另一个文件名叫B的复制到A里面,这个时候会照成冲突,然后会提示你要不要继续复制.加上-f 就不会提示你了

# du的用法

du命令用来查看目录或文件所占用磁盘空间的大小。常用选项组合为：du -sh

| du常用的选项    | 解释                                                         |
| --------------- | ------------------------------------------------------------ |
| -h              | 以人类可读的方式显示                                         |
| -a              | 显示目录占用的磁盘空间大小，还要显示其下目录和文件占用磁盘空间的大小 |
| -s              | 显示目录占用的磁盘空间大小，不要显示其下子目录和文件占用的磁盘空间大小 |
| --apparent-size | 显示目录或文件自身的大小                                     |
| -l              | 统计硬链接占用磁盘空间的大小                                 |
| -L              | 统计符号链接所指向的文件占用的磁盘空间大小                   |
| -c              | 显示几个目录或文件占用的磁盘空间大小，还要统计它们的总和     |

| du常用命令           | 解释                                                   |
| -------------------- | ------------------------------------------------------ |
| du -sh               | 查看当前目录总共占的容量。而不单独列出各子项占用的容量 |
| du -lh --max-depth=1 | 查看当前目录下一级子文件和子目录占用的磁盘容量。       |
| du -sh * \| sort -n  | 统计当前文件夹(目录)大小，并按文件大小排序             |
| du -sk filename      | 查看指定文件大小                                       |

