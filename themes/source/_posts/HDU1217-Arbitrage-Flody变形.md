---
title: HDU1217-Arbitrage-Flody变形
date: 2019-04-14 00:40:46
categories: 水题
tags:
- 图论
- 最短路
mathjax: true
---

HDU1217-Arbitrage

<!-- more -->

# [HDU1217](acm.hdu.edu.cn/showproblem.php?pid=1217)

```
Time Limit: 2000/1000 MS (Java/Others)    Memory Limit: 65536/32768 K (Java/Others)
```

**Problem Description**

Arbitrage is the use of discrepancies in currency exchange rates to transform one unit of a currency into more than one unit of the same currency. For example, suppose that 1 US Dollar buys 0.5 British pound, 1 British pound buys 10.0 French francs, and 1 French franc buys 0.21 US dollar. Then, by converting currencies, a clever trader can start with 1 US dollar and buy 0.5 * 10.0 * 0.21 = 1.05 US dollars, making a profit of 5 percent. 

Your job is to write a program that takes a list of currency exchange rates as input and then determines whether arbitrage is possible or not.

---------------------
**Input**

The input file will contain one or more test cases. Om the first line of each test case there is an integer n (1<=n<=30), representing the number of different currencies. The next n lines each contain the name of one currency. Within a name no spaces will appear. The next line contains one integer m, representing the length of the table to follow. The last m lines each contain the name ci of a source currency, a real number rij which represents the exchange rate from ci to cj and a name cj of the destination currency. Exchanges which do not appear in the table are impossible.

Test cases are separated from each other by a blank line. Input is terminated by a value of zero (0) for n. 

----

**Output**

For each test case, print one line telling whether arbitrage is possible or not in the format "Case case: Yes" respectively "Case case: No".

----

**Sample Input**

```
3
USDollar
BritishPound
FrenchFranc
3
USDollar 0.5 BritishPound
BritishPound 10.0 FrenchFranc
FrenchFranc 0.21 USDollar

3
USDollar
BritishPound
FrenchFranc
6
USDollar 0.5 BritishPound
USDollar 4.9 FrenchFranc
BritishPound 10.0 FrenchFranc
BritishPound 1.99 USDollar
FrenchFranc 0.09 BritishPound
FrenchFranc 0.19 USDollar

0
```

**Sample Output**

```
Case 1: Yes
Case 2: No
```

# 解析

参考：[https://blog.csdn.net/wyxeainn/article/details/69157966](https://blog.csdn.net/wyxeainn/article/details/69157966)

**题目意思：给出n中货币，和m个货币间的汇率，让求解是否存在一种货币，先将这种货币兑换为其他货币，再从其他货币兑换回自己。所获得的钱更多。这样就可以通过货币兑换赚取利润。如果存在这样的货币就输出Yes,如果怎样兑换都会赔本，就输出No.**

先开辟一个二维数组，arr,`arr[i][j]`存放i可以兑换成多少j。因此如果存在一种货币k，货币i兑换成货币k,再从货币k兑换成货币j,所得到j货币的数量比从货币i直接兑换成货币j的大。则要更新`arr[i][j]`要的是i兑换成j的最大j数。其思想与求最短路Flody算法的思想是相同的。所以可以借助Flody算法（这种思想）的模板去解决这个问题。

要解决的问题，如何将字符串对应成整型的节点下标？ 可以采用stl的map实现字符串与下标的一一映射。

```c++
#include <cstdio>
#include <map>
#include <string>
#include <iostream>
#include <cstring>
using namespace std;
double arr[1000][1000];

void Flody(int n)
{
    int i, j, k;
    for(k = 1; k <= n; k++)
        for(i = 1; i <= n; i++)
            for(j = 1; j <= n; j++) //如果货币i兑换成k再兑换成j，比i直接兑换成j所得j货币更多，更新
            {
                if(arr[i][k]*arr[k][j] > arr[i][j])
                    arr[i][j] = arr[i][k]*arr[k][j];
            }
}

int main()
{
    int t, n;
    char name[100], str1[100], str2[100];
    double num;
    int index = 0;
    while(scanf("%d", &t) != EOF && t)
    {
        map<string, int>Map;
        for(int i = 1; i <= t; i++)
        {
            scanf("%s", name);
            Map[name] = i; //用map来处理货币对应的下标
        }
        for(int i = 1; i <= t; i++)
        {
            arr[i][i] = 1.0; //自身兑换自身的汇率为1
            for(int j = i+1; j <= t; j++)
                arr[i][j] = arr[j][i] = 0.0; //0代表两种货币不能兑换
        }
        scanf("%d", &n);
        while(n--)
        {
            scanf("%s%lf%s", str1, &num, str2);
            arr[Map[str1]][Map[str2]] = num;
        }
        Flody(t);
        int k;
        for(k = 1; k <= t; k++)
        {
            //如果第i种货币经过来回兑换后，比原来钱多了，则就可以通过这样的方式赚到钱
            if(arr[k][k] > 1.0) 
            {
                printf("Case %d: Yes\n",++index);
                break;
            }
        }
        if(k > t)
            printf("Case %d: No\n",++index);
    }
    return 0;
}
```

