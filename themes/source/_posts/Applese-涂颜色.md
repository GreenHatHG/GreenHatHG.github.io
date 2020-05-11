---
title: Applese-涂颜色
date: 2019-02-05 15:00:46
categories: 水题
tags:
- 数论 
- 牛客
- 欧拉降幂
- 组合数学
- 费马小定理
mathjax: true
---

[牛客 -330E Applese 涂颜色](https://ac.nowcoder.com/acm/contest/330/E)

<!-- more -->

# 题目

```
时间限制：C/C++ 1秒，其他语言2秒
空间限制：C/C++ 32768K，其他语言65536K
64bit IO Format: %lld
```

**题目描述**

精通程序设计的 Applese 叕写了一个游戏。
在这个游戏中，有一个 n 行 m 列的方阵。现在它要为这个方阵涂上黑白两种颜色。规定左右相邻两格的颜色不能相同。请你帮它统计一下有多少种涂色的方法。由于答案很大，你需要将答案对 $10^9+7$取模。

**输入描述**

```
仅一行两个正整数 n, m，表示方阵的大小。
```

**输出描述**

```
输出一个正整数，表示方案数对10^9+7取模。
```

**示例1**

输入

```
1 1
```

输出

```
2
```

**示例2**
输入

```
2 2
```

输出

```
4
```

**备注**

$1\leq n,m\leq10^{100000}$

# 题解

一个比较显然的结论是，对于每一列，有$2^n$种涂色方法。
我们可以发现，当确定了第一列之后，由于左右相邻不能同色，所以后面每一列的涂色方案也随之确定。因此答案就是 $2^n$



首先n的数字范围非常大，10的10万次方，所以数据一定是用字符串读进去的，其次，因为 n 很大，循环n次肯定超时。所以得降幂，费马小定理或者欧拉降幂都可以。

**费马小定理**

费马小定理：对于素数 $m​$ 任意不是 $m​$ 的倍数的 $b​$，都有：$b^{m-1}\equiv1\  mod \ m​$ 

1. 先决条件成立：$10^9+7​$是素数，$gcd(10^9+7, \ 2)=1​$

2. 处理：

   因$b^{m-1}=1$，故我们只需要算出除去1的部分有多少就行了，于是有

   $2^{n}=2^{n\%(m-1)}$ (m为$10^9+7$)

   这样n就减少了，但是$10^9+7$还是很大，要用到快速幂取模

```c++
/*
提交时间：2019-02-05 14:59:46 语言：C++ 代码长度：565 运行时间： 4 ms 占用内存：864K 
运行状态：答案正确
*/
#include <iostream>
#include <string>
using namespace std;
typedef long long ll;

const int MOD = 1e9 + 7;
ll mod_pow(ll x, ll n) //快速幂
{
    ll ans = 1;
    while(n)
    {
        if(n&1)
            ans = ans * x % MOD;
        x = x*x % MOD;
        n >>=1;
    }
    return ans;
}
int main()
{
    ios::sync_with_stdio(false);
    cin.tie(0);
    cout.tie(0);

    string s1, s2;
    cin >> s1 >> s2;
    ll p = 0;
    ll len = s1.size();
    for(int i = 0; i < len; i++)
      p = (p*10 + s1[i] - '0') % (MOD - 1); //将读入的字符变成整数，并且这个过程中取模
    cout << mod_pow(2, p) << endl;
    return 0;
}
```

**欧拉降幂**
$$
A^Bmod\ C=A^{ B\ mod\varphi(C)+\varphi(C) }mod\ C
$$
故有

$2^nmod\ m=2^{n\ mod\ \varphi(m)+\varphi(m)}\ mod\ m​$

所以

1. 先求出$ \varphi(m)$
2. 再用快速幂求出$2^n$

```c++
/*
提交时间：2019-02-05 16:38:55 语言：C++ 代码长度：855 运行时间： 8 ms 占用内存：916K 
运行状态：答案正确
*/
#include <iostream>
#include <string>
using namespace std;
typedef long long ll;

const int MOD = 1e9 + 7;
ll mod_pow(ll x, ll n) //快速幂
{
    ll ans = 1;
    while(n)
    {
        if(n&1)
            ans = ans * x % MOD;
        x = x*x % MOD;
        n >>=1;
    }
    return ans;
}

ll euler(ll n) //求phi
{
    ll res = n;
    for (ll i = 2; i*i <= n; i++)
        if (n%i == 0)
        {
            res =res/i*(i-1);
            while(n%i == 0)
                n /= i;
        }
    if (n > 1)
        res = res/n*(n-1);
    return res;
}

int main()
{
    ios::sync_with_stdio(false);
    cin.tie(0);
    cout.tie(0);

    string s1, s2;
    cin >> s1 >> s2;
    ll p = euler(MOD);
    ll len = s1.size();
    ll ans = 0;
    for(int i = 0; i < len; i++)
      ans = (ans*10 + s1[i] - '0') % p; //算出n mod phi(m),phi：欧拉函数
    ans += p; //算出n mod phi(m) + phi(m)
    cout << mod_pow(2, ans) << endl;
    return 0;
}
```

