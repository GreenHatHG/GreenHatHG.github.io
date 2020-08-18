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

- 添加编码设置到`~/.gitconfig`

这里采用第二种方案

```python
    def add_git_config():
        git_config = """
#tapd
[core]
   quotepath = false
[gui]
   encoding = utf-8
[i18n]
   commitencoding = utf-8
[svn]
   pathnameencoding = utf-8
"""
        git_config_path = os.path.join(os.path.expanduser("~"), '.gitconfig')
        with open(git_config_path, 'r', encoding="utf-8") as f:
            content = f.read()
        if content.find('#tapd') == -1:
            with open(git_config_path, 'a', encoding="utf-8") as f:
                f.write(git_config)
```

# pre-commit实现

在`.git/hooks`文件夹下添加文件`pre-commit`

```shell
#!/bin/sh

# 获取git变更文件
diff=`git diff --cached --name-only`
echo $diff
# 转换成数组
updated_file_arr=(${diff// /})

# 获取文件执行绝对路径
SHELL_FOLDER=$(cd "$(dirname "$0")"; pwd -P)
echo $SHELL_FOLDER
cd $SHELL_FOLDER
cd ../../src

PYTHON_PATH=`head -n +1 python_info.txt`
${PYTHON_PATH} local_conversion.py > /dev/null

# 获取错误列表
content=`cat notification_list`
error_file_arr=(${content//~~~/ })

for err_file in ${error_file_arr[*]}
do
  for updated_file in ${updated_file_arr[*]}
  do
    echo $err_file
    echo $updated_file
    [ "$err_file" == "$updated_file" ] && echo 'Check failed' && exit 1
  done
done

echo 'success!'
```

这里需要注意二点，有些Shell命令在Windows和Mac下不兼容的，这里需要验证一下，(上面的命令已经经过验证)；非windows系统需要执行`chmod`对该文件授予可执行权限

主要有两条不兼容：

- 获取文件执行绝对路径

  ```shell
  SHELL_FOLDER=$(cd "$(dirname "$0")"; pwd -P)
  ```

- 文件替换

  ```shell
  PROJECT_PATH=${SHELL_FOLDER/'.git/hooks'/ }
  ```

# 自动配置git hooks脚本

这里主要有几个功能点：

1. 文件夹和文件合法性检验
2. 复制`pre-commit`到`.git/hooks`，如果是非windows系统再执行`chmod`授予权限
3. 获得python执行路径保持到`src/python_info.txt`，用于`pre-commit`脚本执行python命令
4. 添加gitconfig配置，避免使用git命令得到文件列表是乱码的

```python
import os
import platform
import subprocess
import sys
from shutil import copy
from sys import platform as _platform

base_dir = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
pre_commit = os.path.join(base_dir, 'pre-commit')
git_path = os.path.join(base_dir, '.git')
hooks_path = os.path.join(git_path, 'hooks')


def check_file():
    if not (os.path.isfile(pre_commit) and os.path.getsize(pre_commit) > 0):
        raise Exception(pre_commit + '文件为空或者不存在')


def copy_pre_commit():
    if not os.path.exists(git_path):
        raise Exception('.git文件夹不存在')
    if not os.path.exists(hooks_path):
        raise Exception('hooks文件夹不存在')
    check_file()
    copy(pre_commit, hooks_path)
    if not _platform == "win32" and not _platform == "win64":
        subprocess.Popen('chmod +x {}'.format(os.path.join(hooks_path, 'pre-commit')), shell=True).wait()


def get_info():
    if '3.6' > platform.python_version():
        raise Exception('请更新到python3.6版本以上')
    python_path = sys.executable
    with open(os.path.join(base_dir, 'src', 'python_info.txt'), 'w', encoding="utf-8") as f:
        f.write(python_path)


if __name__ == '__main__':
    print('开始执行.......')
    copy_pre_commit()
    get_info()
    Util.add_git_config()
    print('执行完成.......')

```

