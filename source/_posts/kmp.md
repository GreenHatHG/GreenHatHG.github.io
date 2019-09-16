---
title: kmp
date: 2018-08-15 23:03:11
tags: 
- KMP
- 字符串
categories: 算法
---

**KMP是一个非常实用的字符串匹配算法**

<!-- more -->

推荐网站：

<https://www.cnblogs.com/yjiyjige/p/3263858.html> <https://blog.csdn.net/starstar1992/article/details/54913261/>

视频：

 [KMP字符串匹配算法1---kmp如何运作](https://is.gd/KMP01) 

[KMP字符串匹配算法2----代码实现](https://is.gd/KMP02)

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/KMP1.png">

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/kmp2.png">





# [HDU - 1711 Number Sequence](http://acm.hdu.edu.cn/showproblem.php?pid=1711) 

```c++
/*
 Judge Status : Accepted	 Language : C++
 Submit Time：2018-08-16 16:11:34		
 Exe.Time:748MS	 Exe.Memory：5.4MB
*/

#include <iostream>
#include <cstring>
using namespace std;

const int MAXN = 1000000 + 10;
typedef int type;
type text[MAXN]; //较长的
type pattern[MAXN]; //较短的
int prefix[MAXN];

void get_prefix(int len_pattern)
{
    int i = 0, j = -1;
    prefix[0] = -1;
    while(i < len_pattern)
    {
        if(j == -1 || pattern[i] == pattern[j])
        {
            i++;
            j++;
            if(pattern[i] != pattern[j]) //正常情况
                prefix[i] = j;
            else //特殊情况，这里即为优化之处。考虑下AAAB, 防止4个A形成012在匹配时多次迭代
                prefix[i] = prefix[j];
        }
        else
            j = prefix[j];
    }
}

void kmp(int len_text, int len_pattern)
{
    get_prefix(len_pattern);
    int i = 0, j = 0;
    while(i < len_text)
    {
        if (j == -1 || text[i] == pattern[j])
        {
            i++;
            j++;
        }
        else
            j = prefix[j];

        if (j == len_pattern)
        {
            cout << i - j + 1 << endl;
            return;
        }
    }
    cout << "-1" << endl;
}

int main()
{
    ios::sync_with_stdio(false);
    int t;
    cin >> t;
    while(t--)
    {
        int len_text, len_pattern;
        cin >> len_text >> len_pattern;
        for(int i = 0; i < len_text; i++)
            cin >> text[i];
        for(int i = 0; i < len_pattern; i++)
            cin >> pattern[i];
        kmp(len_text, len_pattern);
    }
    return 0;
}
```

# [HDU 1358 Period---求循环节还有次数](http://acm.hdu.edu.cn/showproblem.php?pid=1358)

```c++
字符编号从1开始，那么if(i%(i-next[i])==0)，则i前面的串为一个轮回串，其中轮回子串出现i/(i-next[i])次。
```

```c++
/*
 Judge Status : Accepted	 Language : C++
 Submit Time：2018-08-17 11:42:23			
 Exe.Time:171MS	 Exe.Memory：6308K
*/

#include <iostream>
#include <cstring>
using namespace std;

const int MAXN = 1000000 + 10;
typedef char type;
type pattern[MAXN];
int prefix[MAXN];

void get_prefix(int len_pattern)
{
    int i = 0, j = -1;
    prefix[0] = -1;
    while(i < len_pattern)
    {
        if(j == -1 || pattern[i] == pattern[j])
        {
            i++;
            j++;
            prefix[i] = j;
        }
        else
            j = prefix[j];
    }
}

void solve(int len_pattern)
{
    for(int i = 1; i <= len_pattern; i++)
    {
        int tmp = i - prefix[i];
        if(i % tmp == 0 && i / tmp > 1)
            cout << i << " " << i / tmp << endl;
    }
    cout << endl;
}
int main()
{
    ios::sync_with_stdio(false);
    int n, cas = 1;
    while(cin >> n && n)
    {
        cin >> pattern;
        cout << "Test case #" << cas++ << endl;
        get_prefix(n);
        /*
        for(int i = 1; i <= n; i++)
            cout << prefix[i] << " ";
        cout << endl;
         */
        solve(n);
    }
    return 0;
}

/*
Test:
3
aaa
12
aabaabaabaab
0

Output array of prefix(1 <= i <= n):
Test case #1
0 1 2 
Test case #2
0 1 0 1 2 3 4 5 6 7 8 9 
*/
```

