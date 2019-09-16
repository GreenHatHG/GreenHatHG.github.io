---
title: STL之二分法四个函数及两道对应习题
date: 2018-02-12 16:45:32
categories: STL
tags: 
- 二分
- STL
---
STL四个二分搜索操作函数：lower_bound, upper_bound, binary_search, equal_range
<!-- more -->
注意：
1. 四个函数都定义在头文件<algorithm>中，要使用它们必须包含这个头文件
2. 这四个函数都运用了有序区间，这也是二分查找前提，可以用sort函数(需要包含<algorithm>头文件)对数组从小到大排序

### 一、lower_bound()函数
*lower_bound()在first和last中的前闭后开区间进行二分查找，返回大于或等于val的第一个元素位置。如果所有元素都小于val，则返回last的位置。函数返回的是迭代器。*
函数功能简单实例:
```c++
#include <algorithm>
#include <iostream>
using namespace std;
#define len 12
int main()
{
    int val = 21;
    int arr[len]={12,15,17,19,20,22,23,26,29,35,40,51};
    int k = lower_bound(arr, arr, len)- arr;
    /*
    因为函数返回的是元素位置，默认是指针位置，所以要减去第一个位置，即arr(&arr[0]
    才能得出一个整数，这是指针的减法
    auto k = lower_bound(arr, arr + len,  21);  k=0x7ffc399f1ce4(在不同电脑上该值会不同)
    */
    cout << k; //输出5
    //把val换成11时，程序结果是0
    //把val换成12时，程序结果是0
    //把val换成52时，程序结果是12
    return 0;
}
```

注意：
lower_bound()在first和last中的前闭后开区间进行二分查找，返回大于或等于val的第一个元素位置。如果所有元素都小于val，则返回last的位置，且last的位置是越界的
**lower_bound()源码以及补充:**
[lower_bound - C++ Reference](http://zh.cppreference.com/w/cpp/algorithm/lower_bound)
### 二、upper_bound()函数
*返回指向范围 [first, last) 中首个大于 value 的元素的迭代器，或若找不到这种元素则返回 last 。函数返回的是迭代器。*
函数功能简单实例：
```c++
#include <algorithm>
#include <iostream>
using namespace std;
#define len 12
int main()
{
  int val = 21;
  int arr[len]={12,15,17,19,20,22,23,26,29,35,40,51};
  int k = lower_bound(arr, arr + len, 21)- arr;
    cout << k; //输出5
    //把val换成11时，程序结果是0
    //把val换成12时，程序结果是1
    //把val换成52时，程序结果是12
    return 0;
}
```
注意：
upper_bound()若找不到这种元素则返回 last，此时last的位置的越界的
**upper_bound()源码以及补充:**
[upper_bound - C++ Reference](http://zh.cppreference.com/w/cpp/algorithm/upper_bound)
### 三、binary_search()函数
*检查等价于 value 的元素是否出现于范围 [first, last) 中。*
函数模版:
```c++
template <typename T>
int  binary_search (T arr[ ],  int size,  T target) ;
T: 模版参数
arr:  数组首地址
size: 数组元素个数
T target: 需要查找的值
若找到等于 value 的元素则为 true ，否则为 false 。
```
函数功能简单实例：
```c++
#include <algorithm>
#include <iostream>
using namespace std;
#define len 12
int main()
{
  int val = 51;
  int arr[len]={12,15,17,19,20,22,23,26,29,35,40,51};
  if( binary_search(arr, arr + len, val) )
    cout << val << " exits in arr";
  else
    cout << val << " does not exist";
    return 0;
}
输出：51 exits in arr
```
注意：
binary_search试图在已排序的[first,last)中寻找元素value，若存在就返回true，若不存在则返回false。返回单纯的布尔值也许不能满足需求，而lower_bound、upper_bound能提供额外的信息。事实上由源码可知binary_search便是利用lower_bound求出元素应该出现的位置，然后再比较该位置的值与value的值。
**binary_search()源码以及补充:**
[binary_search() - C++ Reference](http://zh.cppreference.com/w/cpp/algorithm/binary_search)
### 四、equal_range()函数
*qual_range是C++ STL中的一种二分查找的算法，试图在已排序的[first,last)中寻找value，它返回一对迭代器i和j，其中i是在不破坏次序的前提下，value可插入的第一个位置（亦即lower_bound），j则是在不破坏次序的前提下，value可插入的最后一个位置（亦即upper_bound），因此，[i,j)内的每个元素都等同于value，而且[i,j)是[first,last)之中符合此一性质的最大子区间*
函数功能简单实例：
```c++
#include <iostream>
#include <vector>
#include <algorithm>
#include <utility>
using namespace std;
int main()
{
    vector<int> v = { 10, 10, 10, 30, 30, 30, 70, 70, 80, 100,
                      300, 300};

    pair<std::vector<int>::iterator,
              std::vector<int>::iterator> ip;

    ip = equal_range(v.begin(), v.begin() + 12, 30);

    cout << "30 is present in the sorted vector from index "
         << (ip.first - v.begin()) << " till "
         << (ip.second - v.begin());

    return 0;
}
程序输出：30 is present in the sorted vector from index 3 till 6
```
注意：
1. 就是30连续出现的区间是[3,6]（注意容器得是有序的）。
2. std::pair主要的作用是将两个数据组合成一个数据，两个数据可以是同一类型或者不同类型
```c++
std::pair定义于头文件<utility>
template<
    class T1,
    class T2
> struct pair;
```
那么，equal_range()有什么用呢？如果我们想同时使用std :: lower_bound和std :: upper_bound，可以使用这个函数。
```c++
#include <iostream>
#include <vector>
#include <algorithm>
#include <utility>
using namespace std;
int main()
{
    vector<int> v = { 1, 2, 3, 4, 5, 5, 6, 7 };

    pair<vector<int>::iterator,
              vector<int>::iterator> ip;

    ip = equal_range(v.begin(), v.end(), 5);

    cout << ip.first - v.begin() << endl;
    cout << ip.second - v.begin() << endl;

    vector<int>::iterator i1, i2;

    i1 = lower_bound(v.begin(), v.end(), 5);
    cout << i1 - v.begin() << endl;

    i2 = upper_bound(v.begin(), v.end(), 5);
    cout << i2 - v.begin() << endl;
    return 0;
}
程序输出：
4
6
4
6
```
尾言：
关联式容器(set, multiset, map, multimap)分别提供了等效的成员函数，性能更佳
### 五、两道习题：
1. ##### 猜数字游戏
**题目描述**

有一个由n个数字组成的数列a，且后一个数字严格大于前一个(a[i+1]>a[i])
小溜进行Q次猜数，每次他都猜一个x，若这个x在数列里面，小溜的得分加上这个分数的大小，被猜中的数不会从数列消失。
如：数列1 2 3 4 5 6，小溜猜5次，分别是1 1 3 5 7 那小溜的得分为1+1+3+5=10
现在请你计算出小溜的得分。

**输入**

第一行输入一个T（0 < T <= 100)
接下来T组测试数据
每组第一行两个数n，q(1 <= n, q <= 1000000)
接下来一行n个数a1，a2...an(1 <= a[i] <= 10^9)
再接下来一行q个数b1，b2...bq(1 <= b[i] <= 10^9)表示猜的数字

**输出**

输出总分

**例如：**

输入：
1
6 5
1 2 3 4 5 6
1 1 3 5 7
输出：
10

代码：
```c++
#include <cstdio>
#include <algorithm>
#include <cstring>
using namespace std;

typedef long long ll;
const int len = 1000005;
int p[len], q[len];

int main()
{
  int t, n, m;
  ll sum = 0;
  scanf("%d", &t);
  while(t--)
  {
    memset(p, 0, sizeof(p));
    memset(q, 0, sizeof(q));
    scanf("%d%d", &n, &m);
    for(ll i = 1; i <= n; i++)
      scanf("%d", &p[i]);
    for(ll j = 1; j <= m; j++)
    {
      scanf("%d", &q[j]);
      if(q[j] == p[upper_bound(p+1, p+n+1, q[j]) - (p + 1)])
      sum+=q[j];
    }
      printf("%lld\n", sum);
  }
  return 0;
}
```
2. ##### 询问
**题目描述**

每次给出一个整数x，共给出m个，需要找出每个整数在数列c中出现的次数。

**输入**

第一行两个整数n，m。n表示数列c长度，m表示共给出m个数(1 <= n, m <= 100000)
第二行为n个空格隔开的整数ci，c表示数列。(-10^9 <= ci <= 10^9)
第三行到m+2行，每行一个整数x，表示给出的整数x。（-10^9 <= x <= 10^9)

**输出**

共m行，每行一个整数y，表示整数x在数列c里面出现的次数

**例如:**

输入：
5 3
9 6 3 3 2
1
3
9
输出：
0
2
1

代码：
```c++
#include <cstdio>
#include <algorithm>
using namespace std;

int main()
{
    int n, m, a[100005], q;
    while(~scanf("%d%d", &n, &m))
    {
      for(int i = 1; i <= n; i++)
        scanf("%d",&a[i]);
      sort(a+1, a+n+1);
      for(int j = 1; j <= m; j++)
      {
        scanf("%d", &q);
        int k = upper_bound(a+1, a+n+1, q) - lower_bound(a+1, a+n+1, q);
        printf("%d\n",k);
      }
    }
    return 0;
}
```
