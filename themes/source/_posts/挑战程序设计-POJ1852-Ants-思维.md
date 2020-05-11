---
title: 挑战程序设计--POJ1852(Ants)思维
date: 2018-09-22 09:04:53
categories: 看书
tags: 挑战程序设计
---

 《挑战程序设计竞赛入门》--POJ1852

<!-- more -->

[POJ1852 Ants（思维）](http://poj.org/problem?id=1852)

<img src = "https://raw.githubusercontent.com/GreenHatHG/blog_image/master/POJ1852-1.png">

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/POJ1852-2.png">

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/POJ1852-3.png">

```c++
/*
 Judge Status : Accepted	 Language : C++
 Submit Time：2018-09-22 09:20:29	
 Exe.Time:641MS	 Exe.Memory：572K
*/
#include <iostream>
#include <algorithm>
using namespace std;

const int MAXN = 1E6;
int arr[MAXN];

int main()
{
    ios::sync_with_stdio(false);
    int test;
    cin >> test;
    while(test--)
    {
        int l, n;
        cin >> l >> n;
        for(int i = 0; i < n; i++)
            cin >> arr[i];
        int minT = 0;
        for(int i = 0; i < n; i++)
        {
            minT = max(minT, min(arr[i], l - arr[i])); //求的是所有蚂蚁都下落的时间，所以求的是最大值，保证所有蚂蚁都下落了
        }

        int maxT = 0;
        for(int i = 0; i < n; i++)
        {
            maxT = max(maxT, max(arr[i], l - arr[i]));
        }
        cout << minT << " " << maxT << endl;
    }
    return 0;
}

```