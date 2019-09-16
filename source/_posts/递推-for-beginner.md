---
title: 递推(for beginner)
date: 2018-04-27 08:33:41
categories: 算法
tags: 递推
---

斐波那契兔子问题、斐波那契数－从爬楼梯问题、HDU2046 骨牌铺方格、ＨDU2044 一只小蜜蜂、HDU2048 神、上帝以及老天爷、HDU2049 不容易系列之(4)——考新郎、递推中的平面问题、HDU 2097 Children’s Queue、HDU2045 不容易系列之(3)—— LELE的RPG难题、HDU2047 阿牛的EOF牛肉、for beginner,HDU2563统计问题

<!-- more -->

# 递推的基本思想

1. 首先确认，是否能很容易的得到简单情况的解
2. - 假设规模为N-1的情况已经得到解决
   - 重点分析：当规模扩大到N时，如何枚举出所有的情况，并且要确保对于每一种子情况都能用已经得到的数据解决
3. - 编程中的空间换时间的思想
   - 并不一定是从N-1到N的分析

# 递推中的经典----Fibonacci sequence(斐波那契数列)

先上递推中的fib数列的解题模板

```c++
#include <isotream>
#include <iostream>
using namespace std;
typedef long long ll;

int main()
{
    int num;
    while( cin >>num)
    {
        ll arr[100000] = {0};  
        arr[1] = 1;
        arr[2] = 2;
        int i;
        for(i = 3; i <= num; i++)
            arr[i] = arr[i - 1] + arr[i - 2];
        cout << arr[num]  << endl;
    }
    return 0;
}
```

应用：

1. 最经典当然是兔子繁殖问题 [斐波那契兔子问题](https://baike.baidu.com/item/%E6%96%90%E6%B3%A2%E9%82%A3%E5%A5%91%E5%85%94%E5%AD%90%E9%97%AE%E9%A2%98/1430035)

2. 还有一个经典的上楼梯问题[斐波那契数－从爬楼梯问题](https://blog.csdn.net/bairongdong1/article/details/52270726)

3. [HDU2046 骨牌铺方格](http://acm.hdu.edu.cn/showproblem.php?pid=2046)

   解题:[HDU 2046 骨牌铺方格(简单递推）](https://blog.csdn.net/hellohelloC/article/details/47699831)

   自己代码:

   ```c++
   #include <iostream>
   using namespace std;
   typedef long long ll;

   int main()
   {
       int num;
       while( cin >>num)
       {
           ll arr[100000] = {0};  
           arr[1] = 1;
           arr[2] = 2;
           int i;
           for(i = 3; i <= num; i++)
               arr[i] = arr[i - 1] + arr[i - 2];
           cout << arr[num]  << endl;
       }
       return 0;
   }
   ```

   ​

   主要还是推出这个状态可以由上次的状态推出。

4. 小变形:[ＨDU2044 一只小蜜蜂](http://acm.hdu.edu.cn/showproblem.php?pid=2044)

   解题:[一只小蜜蜂](https://blog.csdn.net/xu_fish/article/details/50128855)

   自己代码:

   ```c++
   #include <iostream>
   using namespace std;
   int main()
   {
       int n;
       cin >> n;
       while(n--)
       {
           long long arr[10000];
           arr[1] = arr[2] = 1;
           int a , b;
           cin >> a >> b;
           int temp;
           if(a>b)	//保证总是从数字较小的蜂房到数字数字较大的蜂房，直接相减可能会出现负数
           {
                   temp=a;
                   a=b;
                   b=temp;
          }
           b = b - a + 1;	//化为斐波那契数列模式，即从3走到4可以化为求fib里面的f(2)
           int i;
           for(i = 3 ;i <= b; i++)
               arr[i] = arr[i - 1] + arr[i - 2];
           cout << arr[b];
        //   if(n != 0)
               cout << endl;
       }
       return 0;
   }
   ```

# 错排公式
   1. [HDU2048 神、上帝以及老天爷](http://acm.hdu.edu.cn/showproblem.php?pid=2048)

      解题:[HDU2048](https://blog.csdn.net/a1647566717/article/details/6680001)

      公式:M(n)=(n-1)[M(n-2)+M(n-1)]，可以考虑参考上面理解

      自己代码:

      ```c++
      #include <iostream>
      #include <cstdio>
      using namespace std;
      typedef long long ll;

      int main()
      {
          int n;
          cin >> n;
          while(n--)
          {
              int num;
              ll arr[100000] = {0};
              arr[1] = 0;
              arr[2] = 1; 
              cin >> num;
              int i;
              for(i = 3; i <= num; i++) //所有人抽不到的结果
                  arr[i] = (arr[i - 1] + arr[i - 2]) * (i - 1);
              ll all = 1;
              for(i = 1; i <= num; i++) //所有可能出现的结果
                  all *= i;
              double ans = arr[num] / (double)all; 
              printf("%.02lf%%\n", ans * 100); //四舍五入
          }
          return 0;
      }
      ```

      2. [HDU2049 不容易系列之(4)——考新郎](http://acm.hdu.edu.cn/showproblem.php?pid=2049)

         解题:[HDU2049](https://blog.csdn.net/zhouhong1026/article/details/7855919)

         其实就是部分排错，利用组合公式实现成小部分群体中全部排错的效果，从而可以用公式

         自己代码:

         ```c++
         #include <iostream>
         using namespace std;
         typedef long long ll;

         ll fun(int n) //求n！
         {
             ll num = 1;
             for(int i = 1; i <= n; i++)
                 num *= i;
             return num;
         }

         int main()
         {
             int t;
             cin >> t;
             while(t--)
             {
                 int n, m;
                 cin >> n >> m;
                 ll arr[10000] = {0};
                 arr[1] = 0;
                 arr[2] = 1;
                 for(int i = 3; i <= m; i++)	//公式
                     arr[i] = (i - 1) * (arr[i - 1] + arr[i -2]);
                cout << fun(n) / fun(m) / fun(n - m) * arr[m] << endl;
                 //CMN=N!/M!/(N-M)!,然后再乘上公式得到的结果
             }
             return 0;
         }
         ```

         ​

   ​

   # 递推中的平面问题

   1. n条直线分平面问题

   2. 封闭曲线分平面问题

   3. 折线分割平面

      [HDU 2050](http://acm.hdu.edu.cn/showproblem.php?pid=2050)

      代码:

      ```c++
      #include <iostream>
      using namespace std;
      typedef long long ll;

      int main()
      {
          int n;
          cin >> n;
          while(n--)
          {
              int num;
              cin >>num;
          ll arr[100000] = {0};  
          arr[1] = 2;
          int i;
          for(i = 2; i <= num; i++)
              arr[i] = arr[i - 1] + 4*(i - 1) + 1;
          cout << arr[num]  << endl;
          
          }
          return 0;
      }
      ```

      4. 平面割空间
      5. 圆圈分割平面

      - 设有n条封闭曲线画在平面上，而任何两条封闭曲线恰好相交于两点，且任何三条封闭曲线不相交于同一点，问这些封闭曲线把平面分割成的区域个数。

      <img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/%E9%80%89%E5%8C%BA_011.png">

      ​

   可以看pdf,[github](https://github.com/GreenHatHG/blog_image/blob/master/%E5%90%84%E7%A7%8D%E5%88%86%E5%89%B2%E9%97%AE%E9%A2%98(hdu2050%261290).pdf)

   # 假装前面排错系列

   1. [HDU 2097 Children’s Queue](http://acm.hdu.edu.cn/showproblem.php?pid=1297)

      解析：

   按照最后一个人的性别分析，他要么是男，要么是女，所以可以分两大类讨论：

   - 如果n个人的合法队列的最后一个人是男，则对前面n-1个人的队列没有任何限制，他只要站在最后即可，所以，这种情况一共有F(n-1);
   - 如果n个人的合法队列的最后一个人是女，则要求队列的第n-1个人务必也是女生，这就是说，限定了最后两个人必须都是女生，这又可以分两种情况：
     - 如果队列的前n-2个人是合法的队列，则显然后面再加两个女生，也一定是合法的，这种情况有F(n-2);
     - 但是，难点在于，即使前面n-2个人不是合法的队列，加上两个女生也有可能是合法的，当然，这种长度为n-2的不合法队列，不合法的地方必须是尾巴，就是说，这里说的长度是n-2的不合法串的形式必须是“F(n-4)+男+女”，这种情况一共有F(n-4).

   所以，通过以上的分析，可以得到递推的通项公式：F(n)=F(n-1)+F(n-2)+F(n-4)   (n>3)。然后就是对n<=3 的一些特殊情况的处理了，显然：F(0)=1   (没有人也是合法的，这个可以特殊处理，就像0的阶乘定义为1一样)   F(1)=1，F(2)=2，F(3)=4

   根据这个公式计算f(n),其数列值的增长速度太快了，需要构筑一个大整数类来解决。此处选择用java解决

   ```java
   import java.io.*;
   import java.math.*;
   import java.util.*;
   public class Main
   {
       public static void main(String[] args)
       {
           int num;
           Scanner in = new Scanner(System.in);
           while(in.hasNextInt())
           {
               num = in.nextInt();
               BigInteger[] arr = new BigInteger[10000];
               arr[0] =BigInteger.valueOf(1);
               arr[1] = BigInteger.valueOf(1);
               arr[2] = BigInteger.valueOf(2);
               arr[3] =BigInteger.valueOf(4);
               for(int i = 4; i <= num; i++)
               {
                   arr[i] = arr[i - 2].add(arr[i  - 1]);
                   arr[i] = arr[i].add(arr[i - 4]);
               }
               System.out.println(arr[num]);
           }
       }
   }

   ```

   2. [HDU2045 不容易系列之(3)—— LELE的RPG难题](http://acm.hdu.edu.cn/showproblem.php?pid=2045)

   解题：[HDU2045](https://blog.csdn.net/mengxianglong123/article/details/79881899)

   ```c++
   #include <iostream>
   using namespace std;
   typedef long long ll;
   int main()
   {
       int n;
       while(cin >> n)
       {
           ll arr[100000] = {0, 3, 6, 6};
           for(int i = 4; i <= n; i++)
               arr[i] = 2 * arr[i - 2] + arr[i  - 1];
           cout << arr[n] << endl;
       }
       return 0;
   }
   ```

   ​


# 其他

1. [HDU2047 阿牛的EOF牛肉](https://blog.csdn.net/jarvenman/article/details/53132732)

解题:[HDU2047](https://blog.csdn.net/jarvenman/article/details/53132732)

```c++
#include <iostream>
using namespace std;
typedef long long ll;
int main()
{
    int n;
    while(cin >> n)
    {
    ll arr[10000] = {0};
    arr[1] = 3;
    arr[2] = 8;
    int i;
    for(i = 3; i <= n; i++)
    {
        arr[i] = 2 * (arr[i - 1] + arr[i - 2]);
    }
    cout << arr[n] << endl;
    }
    return 0;
}
```

***for beginner:***

- HDU1290 献给杭电五十周年校庆的礼物 
- HDU1297 Children’s Queue 
- HDU1438 钥匙计数之一 
- HDU1465 ~1466 1480 钥匙计数之二 
- HDU2013 蟠桃记 
- HDU2018 母牛的故事 
- HDU2041~2042 
<<<<<<< HEAD
- HDU2044~2050 (10/5专题练习)

# [HDU2563 统计问题](http://acm.hdu.edu.cn/showproblem.php?pid=2563)

**递推**

```
题目信息转化：当你走完第（n-2)步
           （1）.如果你向左（或向右）走出第(n-1)步，你的第n步只能选择向右（或向左）和向上。（因为你走的前一步路会塌陷而不能再走）
           （2）.如果你向上走出第（n-1）步，那你的第n步可以选择向左向右向上三个方向。
（如果你能理解上面的意思那么推出公式对你来说就是小菜一碟了）

递推公式思路：当你走完第（n-1）步，你一定有至少两个方向可以选择，故此时能走两个方向的情况为 2*f(n-1)。
           若你想走完第（n-1）步后一定有三个方向可以选择，则要求走完第(n-2)步后必须是向上走出（n-1）步，故此时的情况数为 f(n-2)。

综上所述： f(n) = 2*f(n-1) + f(n-2)
```

参考：[http://acm.hdu.edu.cn/discuss/problem/post/reply.php?postid=30032&messageid=1&deep=0](http://acm.hdu.edu.cn/discuss/problem/post/reply.php?postid=30032&messageid=1&deep=0)

```c++
/*
 Time:  2018-10-17 11:27:08
 Ext.time:  2ms
 */
#include <iostream>
#include <cstring>
using namespace std;

const int MAXN = 1E3;
int arr[MAXN] = {0};

int fun()
{
    arr[1] = 3;
    arr[2] = 7;
    for(int i = 3; i <= 20; i++)
        arr[i] = 2 * arr[i - 1] + arr[i - 2];
}

int main()
{
    ios::sync_with_stdio(false);
    fun();
    int test;
    cin >> test;
    while(test--)
    {
        int num;
        cin >> num;
        cout << arr[num] << endl;
    }
    return 0;
}
```

**DFS**

DFS的话是超时了

```c++
#include <iostream>
#include <cstring>
using namespace std;

const int MAXN = 1E3;
int arr[MAXN][MAXN] = {0};
int dir[3][2]={
        {0, 1},
        {1, 0},
        {-1, 0}
};
int has[25] = {0};
int ans = 0;

int dfs(int i, int j, int cnt)
{
    if(cnt == 0)
    {
        ans++;
        return 0;
    }
    for(int k = 0; k < 3; k++)
    {
        int x = i + dir[k][0];
        int y = j + dir[k][1];
        if(x >= 0 && y >= 0 && !arr[x][y])
        {
            arr[i][j] = 1;
            dfs(x, y, cnt - 1);
        }
        arr[x][y] = 0;
    }
    return ans;
}
int main()
{
    ios::sync_with_stdio(false);
    for(int i = 1; i <= 20; i++)
    {
        ans = 0;
        memset(arr, 0 ,sizeof(arr));
        has[i] = dfs(500, 0, i);
    }
    int test;
    cin >> test;
    while(test--)
    {
        int num;
        cin >> num;
        cout << has[num] << endl;
    }
    return 0;
}
```

之前写了一发错误的dfs，首先是dfs方向走错了，搞混了x，y轴，向下是x轴，向右是y轴。其次是状态回溯搞错了，应该是回溯最后一个点，而不是前一个点，因为最后那一个点可能通过别的方式还会走到。还有一个就是每次走过一个点应该标记，那个标记语句应该放在for里面，放在外面的话

```c++
#include <iostream>
#include <cstring>
using namespace std;
 
const int MAXN = 1E3;
int arr[MAXN][MAXN] = {0};
int dir[3][2]={
    {1, 0},
    {0, 1},
    {0, -1}
};
int has[25] = {0};
int ans = 0;
 
int dfs(int i, int j, int cnt, const int n)
{
    if(i < 0 || j < 0 || cnt > n || arr[i][j])
        return 0;
    arr[i][j] = 1;
    for(int k = 0; k < 3; k++)
    {
        int x = i + dir[k][0];
        int y = j + dir[k][1];
        if(x >= 0 && y >= 0 && !arr[x][y])
        {
            dfs(x, y, cnt + 1, n);
            if(cnt == n)
            {
                ans++;
                return 0;
            }
        }
        arr[i][j] = 0;
    }
    return ans;
}

//改正后的
/*
int dfs(int i, int j, int cnt, const int n)
{
    if(cnt == n)
    {
        ans++;
        return 0;
    }
    for(int k = 0; k < 3; k++)
    {
        int x = i + dir[k][0];
        int y = j + dir[k][1];
        if(x >= 0 && y >= 0 && !arr[x][y])
        {
            arr[i][j] = 1;
            dfs(x, y, cnt + 1, n);
        }
        arr[x][y] = 0;
    }
    return ans;
}*/

int main()
{
    ios::sync_with_stdio(false);
    for(int i = 1; i <= 20; i++)
    {
        ans = 0;
        memset(arr, 0 ,sizeof(arr));
        has[i] = dfs(500, 500, 0, i);
    }
    int test;
    cin >> test;
    while(test--)
    {
        int num;
        cin >> num;
        cout << has[num] << endl;
         
    }
    return 0;
}
```
- HDU2044~2050 (10/5专题练习)
