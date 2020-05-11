---
title: 字符串常见算法--kmp，E-kmp，manacher，ac自动机
date: 2018-10-27 18:21:30
categories: 算法
tags: manacher
---

kmp，E-kmp，manacher，ac自动机

 <!-- more -->

# KMP

[http://t.cn/EZlzRyg](http://t.cn/EZlzRyg)

# Manacher--最长回文串O(N)

## 关于求最长回文串办法

[四种方法求最长回文串](https://www.jianshu.com/p/c82cada7e5b0)

但是上面几种都不是最快的，最快的是Manacher（马拉车）算法，算法复杂度是O(N)

## Manacher

[https://www.cnblogs.com/grandyang/p/4475985.html](https://www.cnblogs.com/grandyang/p/4475985.html)

[https://www.cnblogs.com/z360/p/6375514.html](https://www.cnblogs.com/z360/p/6375514.html)

[https://segmentfault.com/a/1190000008484167](https://segmentfault.com/a/1190000008484167)

[https://www.bilibili.com/video/av4829276?from=search&seid=9253656566579281943](https://www.bilibili.com/video/av4829276?from=search&seid=9253656566579281943)

[https://www.bilibili.com/video/av20495920?from=search&seid=9253656566579281943](https://www.bilibili.com/video/av20495920?from=search&seid=9253656566579281943)



### [模板题--HDU-3068(最长回文)](http://acm.hdu.edu.cn/showproblem.php?pid=3068)

注意，此题别的算法O(NlogN)都会超时，所以只能用马拉车的O(N)算法

```c++
/*
Exe.Time:1154MS
Exe.Memory:3328K
Submit Time:2018-10-27 19:10:19
*/

#include <iostream>
#include <algorithm>
#include <string>
 
using namespace std;
const int MAXN = 1E6;
int p[MAXN];
 
string Manacher(string s) //返回一个字符串，该字符串为形参s的最长回文串
{
    string t = "$#"; //字符串开头增加一个特殊字符'$'，防止越界
    for (int i = 0; i < s.size(); i++) //扩展原来的字符串，使之字符个数为奇数(不包括字符'$')
    {
        t += s[i];
        t += "#";
    }
    
    //mx即为当前计算回文串最右边字符的最大值
    //id为能延伸到最右端的位置的那个回文子串的中心点位置
    //maxLen：最长的长度
    //maxCenter：最长回文中心
    int mx = 0, id = 0, maxLen = 0, maxCenter = 0;
    for (int i = 1; i < t.size(); ++i)
    {
        p[i] = mx > i ? min(p[2 * id - i], mx - i) : 1; //取min是为了防止超过mx
        while (t[i + p[i]] == t[i - p[i]]) ++p[i]; 
        /*暴力从某一个点从左右扩展，注意此时p[i]的取值，
         *例如当mx<i时，p[i]=1， 那么说此时最长回文是自己，然后拓展匹配
         *不需边界判断，因为左有'$',右有'\0'
         */
        
        /*
         *我们每走一步 i，都要和 mx 比较
         *我们希望 mx 尽可能的远，这样才能更有机会执行 if (i < mx)这句代码，从而提高效率
         *即不断更新值
         */
        if (mx < i + p[i])
        {
            mx = i + p[i];
            id = i;
        }
        if (maxLen < p[i])
        {
            maxLen = p[i];
            maxCenter = i;
        }
    }
    return s.substr((maxCenter - maxLen) / 2, maxLen - 1);
    //最长子串的长度是半径减1，起始位置是中间位置减去半径再除以2。
}
 
int main()
{
    ios::sync_with_stdio(false);
    string s;
    while(cin >> s)
    {
        cout << Manacher(s).size() << endl; //输出最长回文长度
    }
    return 0;
}
```

