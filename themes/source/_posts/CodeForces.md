---
title: CodeForces水一水
date: 2018-07-07 13:22:43
categories: 水题
tags: CodeForces
---
B. Binary String Constructing（Div3--构造）,CodeForces - 1004C（Div2--思维）,CodeForces - 962D(Div2--较为精辟的解法),1055B - Alice and Hairdresser(思维),1088A. Ehab and another construction problem(暴力与O(1)),

1088C. Ehab and a 2-operation task(通过操作使数组递增)

<!-- more -->

# B. Binary String Constructing（Div3--构造）

[B. Binary String Constructing](http://codeforces.com/contest/1003/problem/B)

题意：给出a，b，x，构造一个01串，有a个0，b个1。这个01串刚好有x个 S[i] 使得S[i] != S[i+1]。

思路：对于x，我们很容易就可以想到先输出x/2对0和1（n对0和1交错出现可以提供2*n-1个符合题意的S[i]），然后将剩余的0和剩余的1连续输出（提供1个符合条件的S[i]，这样就刚好是x个符合条件的S[i]了，需要注意的是，我们要优先将个数多的放在前面，例如，有10个1,5个0的话，我们先输出x/2个“10”，否则，输出x/2个“01”）。

还有一个情况就是x刚好整除2，这时候得额外处理一个。

比如4 3 2

就需要输出0111000，而不是0100011

之前没有判断x刚好整除2，wa了一个

Input  

```
50 47 18
```

Output  

```
0101010101010101010000000000000000000000000000000000000000011111111111111111111111111111111111111
```

Answer  

```
0101010101010101011111111111111111111111111111111111111100000000000000000000000000000000000000000
```

```c++
#include <iostream>
using namespace std;

int main()
{
    int zero, one, x;
    cin >> zero >> one >> x;
    zero -= x / 2;
    one -= x / 2;
    if(zero > one)
    {
        for(int i = 0; i < x / 2; i++)
            cout << "0" << "1";
        if(x % 2 == 0) 
        {
            for(int i = 0; i < one; i++)
                cout << "1";
            for(int i = 0; i < zero; i++)
                cout << "0";
        }
        else
        {
            for(int i = 0; i < zero; i++)
                cout << "0";
            for(int i = 0; i < one; i++)
                cout << "1";
        }
    }
    else
    {
        for(int i = 0; i < x / 2; i++)
            cout << "1" << "0";
        if(x % 2 == 0)
        {
            for(int i = 0; i < zero; i++)
                cout << "0";
            for(int i = 0; i < one; i++)
                cout << "1";
        }
        else
        {
            for(int i = 0; i < one; i++)
                cout << "1";
            for(int i = 0; i < zero; i++)
                cout << "0";
        }
    }
    return 0;
}
```

# CodeForces - 1004C（Div2--思维）

[CodeForces - 1004C](https://vjudge.net/problem/1655834/origin)

题目大意:有n个数,每个数只能与自己后面的数配对,相同的配对只算一个,求配对的数量.

最开始用set暴力求解，当然是TLE了。

我们只需从1号位置开始检查，判断1号位置后面有多少个不同的数字，然后再从2号位置开始检查，判断2号位置后面有多少个不同的数字，依此类推，最后将所有的结果加起来就可以了（注意，从i号位置检查后面的数时，需要先判读这个位置的数是否以及出现过）。 如果直接暴力检查的话，肯定会tle，所以，我们先预处理出每个位置后面不同数字的个数，然后累加。

```c++
/*
Status:Accepted			Time:62ms
Memory:117424kB			Length:863
2018-07-21 14:57:53
*/
#include <iostream>
#include <cstring>
using namespace std;

const int maxLen = 10000000;
int arr[maxLen];
int vis[maxLen]; //记录某个数字有没有被算过了
int cnt[maxLen]; //记录某个数字后面有几个不同的数字
int main()
{
    ios::sync_with_stdio(false);
    int n;
    cin >> n;
    for(int i = 1; i <= n; i++)
        cin >> arr[i];

    memset(vis, 0, sizeof(vis));
    memset(cnt, 0, sizeof(cnt));

    for(int i = n; i >= 1; i--) //求出每个数字后面有几个不同的数字
    {
        if(!vis[arr[i]])
        {
            cnt[i] = cnt[i + 1] + 1;
            vis[arr[i]] = 1;
        }
        else
        {
            cnt[i] = cnt[i + 1];
        }
    }

    memset(vis, 0, sizeof(vis));
    long long ans = 0; //注意用long long
    for(int i = 1; i < n; i++)
    {

        if(!vis[arr[i]])
        {
            ans += cnt[i + 1];
            vis[arr[i]] = 1;
        }
    }
    cout << ans;
    return 0;
}
```

# CodeForces - 962D(Div2--较为精辟的解法)

[CodeForces - 962D](https://vjudge.net/problem/1484062/origin)

题意：给出一个整数序列。选择其中最小且出现两次（或以上）的数，把最左边的两个从序列中移除， 然后把它们的和放到它们的后面第一位。不断重复上述过程，直到序列中的每个数都是唯一的。 输出最后的序列。

这道题可以用优先队列，set和map来做，挑了一种比较精辟的解法

```c++
/*
Status:Accepted			Time:171ms
Memory:84344kB			Length:593
2018-07-21 16:06:35
*/
#include <iostream>
#include <map>
using namespace std;
typedef long long ll; //注意要用long long
map<ll, ll>Map; //Map[i]代表i在数组中出现的位置

ll arr[10000000];
int main()
{
    ios::sync_with_stdio(false);
    int n;
    cin >> n;
    int cnt = n;
    for(int i = 1; i <= n; i++)
    {
        cin >> arr[i];
        while(Map[arr[i]])  //查看前面有没有出现过相同的数字，如果有，那么Map[arr[i]]非0
        {
            arr[Map[arr[i]]] = 0; //将之前那个位置的数字变为0，为了在后面输出时判断不是0就输出
            Map[arr[i]] = 0; //处理完后恢复Map状态
            arr[i] += arr[i]; 
            cnt--; //序列个数减一
        }
        Map[arr[i]] = i;
    }
    cout << cnt << endl;
    for(int i = 1; i <= n; i++)
    {
        if(arr[i])
            cout << arr[i] << " ";
    }
    return 0;
}


```

# [1055B - Alice and Hairdresser(div2--思维)](http://codeforces.com/problemset/problem/1055/B)

标签是写着并查集和模拟，刚开始想到用并查集，但是后来行不通，知道得分集合，还得合并集合，但是不知道怎么存储，然后看了别的代码才知道不用那么麻烦，直接判断左右数字就可以确定一个集合，还是太年轻。

题目大意：

       Alice去剪发，一共有n根头发，长度大于l的头发需要剪，如果一个区间中的头发长度全部大于l，那么可以一次给这个区间的所有头发都剪，
    输入给出0是询问需要剪几次。
    
    给出1是第p根头发长了d长度

题解：

      首先总计一下给出的数据需要剪的区间有几个，也就是需要剪的总次数是多少。
      
      当给第p根头发增长了d后，以前小于l，增长之后大于l的话
    
             如果左右两边都大于l，那么现在第p根也大于l了，就可以和左右两边连成片，一次减掉，所以要剪的总次数减一。
    
             如果左右两边都小于等于l，那么第p根现在需要剪了，就要剪的总次数加一。
    
     其他情况都不影响剪的总次数。
---------------------
参考：https://blog.csdn.net/hxxjxw/article/details/83990002 

```c++
/*
submit time:2018-11-20 12:39:55
time:109 ms
*/

#include <cstdio>
using namespace std;

typedef long long ll;
const int MAXN = 1E6;
ll arr[MAXN];
int main()
{
    int n, m, l, cnt = 0;
    scanf("%d %d %d", &n, &m, &l);
    for(int i = 1; i <= n; i++)
    {
        scanf("%lld", arr+i);
        if(arr[i-1] <= l && arr[i] > l)
            cnt++;
    }
    int q, p, d;
    while(m--)
    {
        scanf("%d", &q);
        if(q == 0)
            printf("%d\n", cnt);
        else
        {
            scanf("%d %d", &p, &d);
            if(arr[p] <= l && (arr[p]+d) > l)
            {
                if(arr[p-1] <= l && arr[p+1]<= l)
                    cnt++;
                else if(arr[p-1] > l && arr[p+1] > l)
                    cnt--;
            }
            arr[p] += d;
        }
    }
    return 0;
}
```

# [1088A. Ehab and another construction problem(暴力与O(1))](http://codeforces.com/contest/1088/problem/A)
给定一个n，找到两个数`a,b{1 <= a,b <= n}`使得a被b整除且`ab>n`且`a/b<n`
*符合条件的a,b非常多，任意输出一个即可*
## 解法1---暴力
暴力的话就是用两个for循环去暴力枚举a，b，然后找出符合条件的a，b输出就行了。
## 解法2---确定一个数
我们可以确定b为2，那么a的值就是`n-n%2`，可以得到如下证明：
(n-n%2)的意思：就是在小于等于n的范围内找出最大能被模2的数，例如11，`n%2=11%2=1`，那么`n-n%2=10`，又如12，`n%2=12%2=0`，那么`n-n%2=12`。
然后的话可以证明下，首先看范围，当`n>=2`时，b肯定是满足`[1， n]`，看上面的两个例子，当n是奇数时，因为a肯定是得到偶数，因为n是奇数，又是减法，故此时a就是`a-1`了，符合条件，当n是偶数时，那么是根据上式得出的a就是n。
当n是奇数时，a为`n-1`，b为2，那么就有`a*b=2*(n-1)`，当`n>2`时满足`a*b>n`，同时有`(n-1)/2<n`
当n是偶数时，a为`n`， b为2，那么就有`a*b=2*n`，当`n>=1`时满足`a*b>n`，同时有`n/2<n`
根据上面可以得出，当n>=2时，固定b为2，那么a的值就是`n-n%2`。
*可以看出固定b为2的情况容易证明，但是固定b为3（或者3以后）的情况就难以讨论。*
```c++
#include <iostream>
using namespace std;
int main()
{
    int x;
    cin >> x;
    if (x==1)
    cout << -1;
    else
    cout << x-x%2 << '   ' << 2;
}
```

# [1088C. Ehab and a 2-operation task(通过操作使数组递增)](http://codeforces.com/contest/1088/problem/C)

给定一个序列 两种操作：
1. 选定一个i，将[1,i]内的所有数加上x
2. 选定一个i，将[i,j]内所有数模x


让你在不大于n+1次操作后使序列严格递增，输出所进行的操作

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/_20181205_100439.JPG">

解法看上图，下面代码用的是Second solution

```c++
/*
submit time: 2018-12-05 10:02:07
time: 31ms
*/
#include <iostream>
using namespace std;

int main()
{
    ios::sync_with_stdio(false);
    int n;
    cin >> n;
    const int num = 1e5;
    cout << n + 1 << endl;
    cout << "1 " << n  << " " << num << endl;
    int input;
    for(int i = 1; i <= n; i++)
    {
        cin >> input;
        cout << "2 " << i << " " << input + num - i << endl;
    }
    return 0;
}
```

