---
title: Rinne Loves Math
date: 2019-02-12 10:01:32
categories: 水题
tags:
- 牛客
mathjax: true
---

[牛客70j-Rinne Loves Math](https://ac.nowcoder.com/acm/contest/370/J)

<!-- more -->

# 题目

```
时间限制：C/C++ 1秒，其他语言2秒
空间限制：C/C++ 262144K，其他语言524288K
64bit IO Format: %lld
```

**题目描述**

给了你形如$\sqrt{n}$的式子中的 n，让你输出化简后的结果 $a\sqrt{b}$ 中的 a,b，如果这样的式子在**实数**范围内没有意义，输出 -1。

**输入描述**

```
第一行一个整数 T,表示数据组数。
接下来 T 行，每行一个整数 x 表示根号下的数。
```

**输出描述**

```
输出一共 T 行，每行两个数表示化简后的答案 a,b
```

**示例1**

输入

```
4
20
25
-2005
11
```

输出

```
2 5
5 1
-1
1 11
```

*说明*

20=4×5
25=5×5
实数范围内$ \sqrt{n} ​$中 n 小于 0 没有意义。
11 是个质数。

**备注**

$T≤100, 0<|x|≤10^7$

# 题解

比较水的模拟题，但是比赛时想错了，想得有点麻烦了，以为只除4和9就行了，emmm，不过还好最后做出来了。既然是化简，那么肯定是平方，所以逆序遍历$x\in[0,sqrt(n)]$，如果有n%(x*x)==0就代表可以化简出x，为什么是逆序，因为要求最简，所以可能得除以最大（之前就是没想到这个emmm）

```c++
/*
提交时间：2019-02-12 10:21:33 语言：C++14 代码长度：520 运行时间： 20 ms 占用内存：632K 
运行状态：答案正确
*/

#include <iostream>
#include <cmath>
using namespace std;

int main()
{
    ios::sync_with_stdio(false);
    cin.tie(0);
    cout.tie(0);
    
    int t, n;
    cin >> t;
    while(t--)
    {
        cin >> n;
        if(n < 0)
        {
            cout << "-1" << endl;
            continue;
        }
        for(int i = sqrt(n); i >= 0; i--)
        {
            if(n % (i*i) == 0)
            {
                cout << i << " " << n/i/i << endl;
                break;
            }
        }
    }
    return 0;
}
```

