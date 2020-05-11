---
title: map几道水题
date: 2018-04-06 19:25:42
categories: 水题
tags: map
---

uva156 codeforces 959B

<!-- more -->

# uva156

## 题目描述

输入一些单词，找出所有满足如下条件的单词：该单词不能通过字母重排，得到输入文本中的另外一个单词。在判断是否满足条件时，字母不分大小写，但在输出时应保留输入中大小写，按照字典序进行排序。

## 输入

```
ladder came tape soon leader acme RIDE lone Dreis peat
 ScAlE orb  eye  Rides dealer  NotE derail LaCeS  drIed
noel dire Disk mace Rob dries
#
```

## 输出

```Disk
Disk
NotE
derail
drIed
eye
ladder
soon
```

## 解析

其实这道题挺简单的，把所有单词变成小写，然后排序，然后放到map里面，没有重复的就输出，但是自己写的过程中还是出了点问题。

```c++
#include <map>
#include <cctype>
#include <string>
#include <vector>
#include <iostream>
#include <algorithm>
using namespace std;

//将输进来的字符串转换成小写，并且按照字典序从小到大排序
string repr(const string&s)
{
    string ans = s;
    for(int i = 0; i < ans.length(); i++)
        ans[i] = tolower(ans[i]);
    sort(ans.begin(), ans.end());
    return ans;
}
int main()
{
    string s;   //接受输入的单词
    vector<string> words;   //储存输入的所有单词
    map<string, int> cnt;
    while(cin >> s)
    {
        if(s[0] == '#')
            break;
        words.push_back(s);
        string r = repr(s);
        if(!cnt.count(r))
            cnt[r] = 0;   //如果r之前没有被插入到中，则将cnt的第二个值，也就是value变成0
        cnt[r]++;   //这里代表着字符串r出现的次数
    }
    vector<string> ans; //保存满足条件的单词
    for(int i = 0; i < words.size(); i++)
    {
        if(cnt[repr(words[i])] == 1) //如果处理后的单词在cnt只出现一次的话，那么就输出,注意这里不能改words里面的内容。
            ans.push_back(words[i]);
    }
    sort(ans.begin(), ans.end());   //按照字典序输出
    for(int i = 0; i < ans.size(); i++)
        cout << ans[i] << endl;
    return 0;
}
```

总的来说，差不多就是先把要输入的单词经过处理后放到map里面，然后记录出现的次数，一点注意就是函数不能直接处理存放所有单词的数组，因为要按照原样输出，所以在处理函数里面赋值给了一个新的字符串，然后返回，最后再遍历一次map，把满足条件的输出。其实这里处理了两次，一次在输入后处理后放到map，一次是把words数组里面的元素处理后当做map的key去判断改key有没有满足这个条件。



# codeforces 959B

## 题目原文：[959B](http://codeforces.com/problemset/problem/959/B)

一个人给另一个人发消息，其中发的消息里面的信息，每一个信息都有对应的价值，也就是发送的成本。 这个想发送的成本最低。输出最低的成本。代表输入n个已知的信息，下面一行对应每个信息的价值。然后是k行，每一行第一个就代表有x个可以互相替换的信息的下标。

参考：[csdn](https://blog.csdn.net/memory_qianxiao/article/details/79828217)

## 题解

 模拟  这道题两秒  暴力大法好 用map用string longlong 对应值。耐心模拟就好。

```c++
#include <iostream>
#include <map>
#include <string>
#include <algorithm>
#include <climits>
using namespace std;

map<string,int> gr;
string arr[100005];
int cost[100005];
int gcost[100005];
int n,k,m;
 
int main()
{
    cin >> n >> k >> m;
    int i;
    for(i=0;i<n;i++)
        cin >> arr[i];
    for(i=0;i<n;i++)
        cin >> cost[i];
    for(i=0;i<k;i++)
    {
        gcost[i]=INT_MIN;   
        int x;
        cin >> x;
        while(x--)
        {
            int z;
            cin >> z; z--;
            gcost[i]=min(gcost[i],cost[z]);
            gr[arr[z]]=i;
        }
    }
    long long ans=0;
    for(i=0;i<m;i++)
    {
        string x;
        cin >> x;
        ans+=gcost[gr[x]];
    }
    cout << ans << endl;
}
```



