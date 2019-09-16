---
layout: 牛客--applese的回文串
title: 牛客--applese的回文串
date: 2019-01-30 12:09:12
tags: 
- 回文串 
- 牛客
categories: 水题
---

判断一个字符串在任意位置(包括最前面和最后面)插入一个字符后能不能构成一个回文串

 <!-- more -->

[Applese 的回文串](https://ac.nowcoder.com/acm/contest/330/I)

**解析**

TAG：思维，想法

可以认为插入和删除是等价的操作。想到这一点，这题就会好做很多。
如果这个串本身就是回文串，答案一定是Yes。
否则我们只需要考虑串中对称的位置不相等的两个字符，分别尝试把它们删掉后判断一下是不是回文的就行了。

参考自：https://ac.nowcoder.com/discuss/153012?type=101

```c++
/*
提交时间：2019-01-30 12:03:32 语言：C++ 代码长度：695 运行时间： 4 ms 占用内存：996K 
运行状态：答案正确
*/

#include <iostream>
#include <string>
using namespace std;

//判断对称位置的字符是否相等，相等返回-1，不是返回不相等的位置
int check(string s) 
{
    int len = s.size();
    for(int i  = 0; i < len ; i++)
    {
        if(s[i] != s[len - 1 - i])
            return i;
    }
    return -1;
}

int main()
{
    ios::sync_with_stdio(false);
    cin.tie(0);
    cout.tie(0);

    string s;
    cin >> s;
    int diff = check(s);
    if(diff == -1)
        cout << "Yes" << endl;
    else
    {
        string tmp = s;
        //删除掉不相等的那个字符，s1是删掉左边的，s2的删掉右边的
        string s1 = s.erase(diff, 1); 
        string s2 = tmp.erase(tmp.size() - 1 - diff, 1);
        if(check(s1) == -1 || check(s2) == -1)
            cout << "Yes" << endl;
        else
            cout << "No" << endl;
    }
    return 0;
}
```

