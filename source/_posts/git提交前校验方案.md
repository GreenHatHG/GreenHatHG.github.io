---
title: git提交前校验方案
date: 2020-08-07 20:53:28
categories: git
tags: 
- hooks
---

拦截commit事件，进行前置处理，校验通过则允许commit否则拒绝

<!-- more -->

# 介绍

Git有一种在发生某些重要操作时触发自定义脚本的方法，称为`Git Hooks`。 这些`hooks`有两种：客户端和服务器端。 客户端`hooks`由诸如提交和合并之类的操作触发，而服务器`hooks`由网络操作（如接收推送的提交）运行

https://git-scm.com/book/en/v2/Customizing-Git-Git-Hooks

本次是客户端方案

# 方案介绍

- 目的：在`git commit`之前，对变更的文件进行检查，如果通过，则允许提交，否则拒绝提交

- 流程：

1. `git diff`获取变更文件 
2. 执行检查脚本，将未通过文件列表保存到文本 
3. 从文本中获取所有出错文件 
4. 与变更文件列表比较 
5. 如果存在出错文件则拒绝`commit`，否则`commit`

# 遇到的问题

`git diff`获取变更文件时，如果git中对应的编码没有设置对，获取的文件名可能是一串数字

这里有两个解决方案：

- 手动设置git编码正确，`Windows`和`*nix`两个系统操作方式不一样

- 使用脚本自动对所有内容进行编码：https://github.com/Artalus/git-autounicode

这里采用第二种方案

# 实现

提供个稍微处理后的`pre-commit.pl`

```perl
#!/usr/bin/perl
# Should be put into <repository>/

my @diffFiles = GetFiles();
my $filecount = @diffFiles;
if ( $filecount == 0 )
{
	exit 0;
}

my @unicodedFiles = ();
my $unicodedCount = 0;
my $i = 0;
foreach $file (@diffFiles)
{
	$i += 1;
	ConvertFile($file);
}

if ( $i != $filecount )
{
	exit 1;
}

if ( $unicodedCount == 0 )
{
	exit 0;
}

exit 1;

###############################################################################

# get the files to encode
sub GetFiles
{
	# turn off escaping non-ascii characters so тест.файл won't turn to "\321\202\320\265\321\201\321\202.\321\204\320\260\320\271\320\273"
	my $QP = `git config --get core.quotepath`;
	`git config core.quotepath off`;

	# get files to process
	my @diffResult = `git diff --stat --cached --diff-filter=ACMR`;
	`git config core.quotepath $QP`;
	
	# skip last line "3 files changed, 1 insertion"
	pop @diffResult;
	
	my @diffFiles = ();
	
	for $d (@diffResult)
	{
		# split each string by " | " in center
		my @parts = split / \| /, $d;
		
		# skip binary files
		if (not BeginsWith(Trim($parts[1]), 'Bin'))
		{
			push @diffFiles, Trim($parts[0]);
		}
	}
	
	return @diffFiles;
}

# encode the file if needed
sub ConvertFile
{
	my ($f) = @_;
	
	# since git diff returns one file per line ending with \n
	$f =~ s/\n//;
	my $utf = '~'.$f.'.utf8';
	
	# if we can successfully convert from Utf8 to Utf8 - it's already encoded correctly, won't mess around
	`iconv -s -f utf-8 -t utf-8 $f > /dev/null`;
	if ( not $? )
	{
		return;
	}
	
	# otherwise, let's try to convert it
	`touch $utf`;
	`iconv -s -f cp1251 -t utf-8 $f > $utf`;
	if ( not $? )
	{
		`rm $f`;
		`mv $utf $f`;
		$unicodedCount += 1;
		push @unicodedFiles, $f;
		return;
	}
	
	`rm $utf`;
}


sub BeginsWith
{
    return substr($_[0], 0, length($_[1])) eq $_[1];
}

sub Trim
{
	my $s = shift;
	$s =~ s/^\s+|\s+$//g;
	return $s
};
```

将此文件放在仓库根目录

然后在`.git/hooks`文件夹下添加文件`pre-commit`

```shell
#!/bin/sh

perl "pre-commit.pl"

# 获取git变更文件
diff=`git diff --cached --name-only`
# 转换成数组
updated_file_arr=(${diff// /})

# 获取文件执行绝对路径
SHELL_FOLDER=$(dirname $(readlink -f "$0"))
# 获取项目src绝对路径
SRC_PATH=${SHELL_FOLDER/'.git/hooks'/'src/'}

cd $SRC_PATH
# 判断python执行命令
if type python3 >/dev/null 2>&1; then
  # 本地检查文件，错误列表保存在文本notification_list
  python3 local_conversion.py > /dev/null
else
  python local_conversion.py > /dev/null
fi

# 获取错误列表
content=`cat notification_list`
# 错误列表以~~~隔开，分割转换为数组
error_file_arr=(${content//~~~/ })

for err_file in ${error_file_arr[*]}
do
  for updated_file in ${updated_file_arr[*]}
  do
  	# 在错误列表中找到变更的文件则拒绝commit	
    [ "$err_file" == "$updated_file" ] && echo 'Check failed' && exit 1
  done
done

echo 'success!'
```

