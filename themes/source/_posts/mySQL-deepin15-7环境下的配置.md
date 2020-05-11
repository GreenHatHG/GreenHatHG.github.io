---
title: MySQL--deepin15.7环境下的配置
date: 2018-09-16 14:58:36
categories: 教程
tags:
- MySQL
- deepin
---

deepin15.7基于debian9

<!-- more -->

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/deepin%E4%BF%A1%E6%81%AF.png">



为了还原最好的配置环境，先把MySQL的遗留配置以及部分依赖卸载干净

# 使用APT删除MySQL

参考：

[Removing MySQL with APT](https://dev.mysql.com/doc/mysql-apt-repo-quick-guide/en/index.html#apt-repo-remove)

要卸载MySQL服务器以及使用MySQL APT存储库安装的相关组件，首先，使用以下命令删除MySQL服务器

```shell
sudo apt-get remove mysql-server
```

强力卸载可以使用以下这条命令（卸载并清除软件包的配置）

```shell
sudo apt-get purge mysql-server
```

然后，删除使用MySQL服务器自动安装的任何其他软件

```shell
sudo apt-get autoremove
```

如果要卸载MySQL其他组件，可以先看看软件包列表

```shell
dpkg -l | grep mysql | grep ii
```

然后再对应删除

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/mysql%E5%8D%B8%E8%BD%BD.png">

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/mysql%E5%8D%B8%E8%BD%BD2.png">

# 全新安装MySQL步骤之之配置软件源(APT)

参考：

[Steps for a Fresh Installation of MySQL](https://dev.mysql.com/doc/mysql-apt-repo-quick-guide/en/index.html#apt-repo-fresh-install)

这里导入相关源的信息是用deb包，也可以自己手动编辑对应的源文件

```shell
wget https://repo.mysql.com//mysql-apt-config_0.8.10-1_all.deb
```

然后安装对应的deb包

```shell
sudo dpkg -i mysql-apt-config_0.8.10-1_all.deb
```

接着会进入相关的设置

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/mysql-apt-config1.png">

因为本次配置的环境是debian stretch，所以选择debian stretch

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/mysql-apt-config2.png">

然后默认ok，然后就完成了apt源的设置

接着就是常规更新源

```shell
sudo apt-get update
```

重新配置mysql-apt-config

```shell
sudo dpkg-reconfigure mysql-apt-config
```

# 全新安装MySQL步骤之之安装MySQL(APT)

通过以下命令安装MySQL,这将安装MySQL服务器的包，以及客户端和数据库公共文件的包。

```shell
sudo apt-get install mysql-server
```

*然后据说安装新版MySQL(8.0)没有出现设置root的提示框，确实，这次安装我也没有出现，那么就安装后再通过命令行设置*

可选安装图形化管理工具workbench

```shell
 sudo apt-get install mysql-workbench 
```

# 完成上一步未完成的工作---设置root密码

参考：

[deepin15.7安装mysql无输入密码提示，导致安装后无法使用mysql](https://bbs.deepin.org/forum.php?mod=viewthread&tid=168685&highlight=mysql)

登录Mysql

```shell
sudo mysql -uroot -p
```

输入系统密码回车

然后输入，注意还有`;`号

```shell
use sql;
```

回车，接着输入下面的命令,123456是将要设置的密码，然后可以自己更改

```shell
 update user set authentication_string=password('123456'),plugin='mysql_native_password' where user='root';
```

回车，接着输入

```shell
 flush privileges;
```

回车，已经设置好了密码，输入`exit`回车退出命令行

# 不知道密码登录MySQL

```shell
sudo service mysql stop回车
```

```shell
sudo mysqld_safe --skip-grant-tables &回车
```

```shell
 mysql回车
```





