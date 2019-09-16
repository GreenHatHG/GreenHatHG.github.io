---
title: luogu题目
date: 2018-05-14 09:59:43
categories: 水题 
tags: luogu
---

P1022 计算器的改良(解简单一元一次方程), P1583 魔法照片（多个信息多个处理系列（题意比较绕））, P1588 丢失的牛(倒逆思想), P1459 三值的排序, P2386 放苹果(递归、dp、dfs都可以做),  P2562 [AHOI2002]Kitty猫基因编码，P1106 删数问题 （贪心）,P2758 编辑距离(dp)，P2404 自然数的拆分问题（回溯）

<!-- more -->

# P1022 计算器的改良(解简单一元一次方程)

[P1022](https://www.luogu.org/problemnew/show/P1022)

这道题如果直接读入一个字符串再一个个处理的话可以会想复杂，我之前这样的处理的话代码长还wa（菜鸡），如果采用栈的思想，则会简单很多

```c++
#include <iostream>
#include <cstdio>
#include <cmath>
using namespace std;
typedef long long ll;

ll nowValue = 0; //当前输入的值
ll coefficient; //表示系数
ll position = 1;    //标示是在等式左边还是等式右边
ll sign = 1;    //表示符号
ll value; //表示某个操作符前面的值
bool is = false; //判断有没有读入数据了
char x;
int main()
{
    char c;
    c = getchar();
    while(1)
    {
   //     cout << nowValue << " " << value << " " << coefficient << endl;
        if(c >= 'a' && c <= 'z')
        {
            x = c;
            if(nowValue == 0 && !is)
            {
                coefficient += position * sign;
                is = false;
            }
            else
            {
                coefficient += position * sign * nowValue;
                sign = 1;
                nowValue = 0;
                is = false;
            }
        }
        //有个坑点（虽然数据没有涉及），如果nowValue为0又读入了变量名称有两种情况：一时+0x或-0x，另一种是默认系数为1，is专门判断这两种情况 
        else if(c >= '0' && c <= '9')
        {
            nowValue = nowValue * 10 + c - '0'; //将输入的字符串数字转化为整型数字
            is = true;
        }
        else if(c == '-')
        {
            value += -position * sign * nowValue;    //将前面的值累加
            sign = -1;     //因为是负号，所以后面的值是负数
            nowValue = 0;
            is = false;
        }
        else if(c == '+')
        {
            value += -position * sign * nowValue;
            sign = 1;
            nowValue = 0;
            is = false;
        }
        else if(c == '=')
        {
            value += -position * sign * nowValue;   //算出等号前面那个数字值
            nowValue = 0;
            sign = 1;
            position = -1; //将一些值恢复为默认状态
            is = false;
        }
        else //最后的值累加上去
        {
            value += -position * sign * nowValue;
            break;
        }
        c = getchar();
    }
 //   cout << nowValue << " " << value << " " << coefficient << endl;
    double ans = (double)(value) / coefficient;
    printf("%c=%.3lf",x,ans==0?abs(ans):ans);
    //这涉及一个很坑的地方：C++里0除以一个负数值为-0，专门避免这种情况
    return 0;
}
```

# P1583 魔法照片（多个信息多个处理系列（题意比较绕））

[p1583](https://www.luogu.org/problemnew/show/P1583)

这道题如果直接用结构体或者用map和数组模拟的话，原则上是可以，但是比较复杂（因为题目比较绕），后来在评论区看见一个比较方便的解法

摘自评论区  作者: [白井黑子1](https://www.luogu.org/space/show?uid=52913)

```
题目实际上指的是先给定你1~n号对应的权值，从大到小排序后根据当前次序再编第二次号，分类别加上对应的e[i]，再次从大到小进行排序后输出前k大权值分别的初始编号。
注意！第二次编号与最终的编号输出无关，仅用于分类。
举个例子：
输入：（测试点#1）

14 3 9 2 5 4 0 0 0 0 0 0

1 1 3 4 9 2 8 2 8 8 8 7 1 9

编号为：1 2 3 4 5 6 7 8 9 10 11 12 13 14

第一次排序后：（序号小优先）

9 9 8 8 8 8 7 4 3 2 2 1 1 1

编号为：5 14 7 9 10 11 12 4 3 6 8 1 2 13

类别为：1 2 3 4 5 6 7 8 9 10 1 2 3 4

加上e[i]后：18 11 13 12 8 8 7 4 3 2 11 3 6 5

第二次排序后：（序号小优先）

18 13 12 11 11 8 8 7 6 5 4 3 3 2

编号为：5 7 9 8 14 10 11 12 2 13 4 1 3

输出：5 7 9

这就是这道题大概的思路：输入——排号——排序——分类加e[i]——排序——输出
```

```c++
#include<cstdio>
#include<algorithm>
using namespace std;
int e[12],n,k;
struct person{
    int w;//权值 
    int num;//编号 
    int d;//类别 
}p[20010];//储存每个人的信息 
int w_comp(const person &a,const person &b){
    if(a.w!=b.w)return a.w>b.w;//从大到小排序 
    return a.num<b.num;//序号小优先 
}//结构体排序 
int main(){
    scanf("%d%d",&n,&k);
    for(int i=0;i<10;i++)scanf("%d",&e[i]);
    for(int i=0;i<n;i++){
        scanf("%d",&p[i].w);
        p[i].num=i+1;
    }//读入+编号 
    sort(p,p+n,w_comp);//第一次排序 
    for(int i=0;i<n;i++){
        p[i].d=i%10;//分类 
        p[i].w+=e[p[i].d];//加上e[i] 
    }
    sort(p,p+n,w_comp);//第二次排序 
    for(int i=0;i<k;i++)printf("%d ",p[i].num);
}
```

#  P1588 丢失的牛(倒逆思想)

[p1588](https://www.luogu.org/problemnew/show/P1588)

这道题可以用bfs，但是可能比较复杂，后来在题解发现了比较容易的解法，就倒过来想加上一点贪心。

作者: [我真的要吃酱](https://www.luogu.org/space/show?uid=19174)

看到题解一溜溜全是搜索，这里发个带贪心的解题方法，而且是所有题解中最快的（0ms）  

原题是FJ追牛，我们可以反过来想，变成牛追FJ，这样子，牛的移动方式有y+1，y-1，和直接y/2，因此我们在移动牛的时候可以**优先**考虑能否y/2，然后再考虑y+1,y-1的移动方式  

之所以要反过来想是因为 在FJ追牛的时候是不能优先考虑x*2再考虑x+1，x-1的，也不能优先考虑x+1,x-1再考虑x*2！！  

举个例子，FJ在25，牛在102，先*2再+1再*2共三步，FJ追牛的时候没办法优先考虑怎么走。但是如果是牛追FJ，牛可以直接y/2，这个时候牛的位置变成51，可以发现是奇数，没办法优先y/2，所以y-1变成50（y+1也要考虑），然后优先考虑y/2变成25，共三步。  

然后这个思路我刚开始是用数组写，发现在-1，+1那里要考虑很多东西，然后看下了用队列，瞬间简单了很多。。。

```c++
#include <iostream>
#include <queue>
using namespace std;

int main()
{
    typedef pair<int, int> P; //p.first 表示牛的位置，p.second表示牛走了多少步 
    P p;
    queue<P>q;
    int t;
    cin >> t;
    while(t--)
    {
        int Min = 100000000;
        int n;
        cin >> n >> p.first;
        q.push(P(p.first, 0));
        while(q.size())
        {
            p = q.front();
            q.pop();
            if(p.first < n) ////如果牛在FJ的后面，那就直接走到FJ
            {
                p.second += n - p.first;
                if(Min > p.second) Min = p.second;
            }
            else if(p.first % 2 == 0) //牛可以直接除以2，优先考虑除以2 
            {
                if(p.first / 2 > n)
                    q.push(P(p.first / 2, p.second + 1)); 
                //这里考虑的是除以2后会超过FJ的情况，然后前进，但是还是比直接走到FJ快
                else if(n - p.first / 2 < p.first - n)
                    q.push(P(p.first / 2, p.second + 1));
                else //否则直接走到FJ
                {
                    p.second += p.first - n;
                    if( Min > p.second ) Min = p.second;
                }
            }
            else//如果不能直接y/2，选择y+1，y-1的方式 ，这里是队列方便之处，直接加到队列，然后一一判断
            {
                q.push( P(p.first+1,p.second+1) );
                q.push( P(p.first-1,p.second+1) );
            }
        }
        cout << Min << endl;
    }
    return 0;
}
```



#  P1459 三值的排序

题目：[P1459](https://www.luogu.org/problemnew/show/P1459)

作者: [敌敌畏](https://www.luogu.org/space/show?uid=65602) 

这道题可以用线段树什么的，但是也可以用模拟，先模拟1、2、3各有多少个，发现1区间的2与2区间的1交换，发现1区间的3与3区间的1交换，发现2区间的3与3区间的2交换。  

剩下的3个区间内的无法一次交换的数也只用两次交换就可以把无法一次交换的三个数换了。

主要是遍历过程。怎么样才能做到不漏呢。

下面代码主要思路：  

区间一：应该存的是1，区间二：存的是应该是2，区间三同理

从末尾开始遍历区间三，如果是1的话，那么就在区间一找看看有没有3，有的话就交换，没有的话在区间一二搜索不等于1的值，然后交换，接着判断如果是2的话（如果上面找不到3将2与1交换的话，那么同样也可以判断到这个已经交换了的2），然后在区间2找3，如果找不到的话，在区间一找。其实主要是还是上面那个找不到1的话，就将其他值交换了，这样就可以将1换到上面了。

然后再处理区间二就行了。

```c++
#include <algorithm>
#include <iostream>

using namespace std;
const int len = 1010;
int arr[len];
int num[4]; //num[1]代表大小为1的数量，以后的类推

int main()
{
    int n;
    cin >> n;
    for(int i = 1;i <= n;i ++)
    {
        cin >> arr[i];
        num[arr[i]]++;
    }
    num[2] = num[1] + num[2];
    num[3] = num[1] + num[2] + num[3];
    int ans = 0;
    for(int i = n; i > num[2]; i--) //搜索区间三
    {
        if(arr[i] == 1) //区间三本来应该是3，如果是1的话，那个得进行交换
        {
            bool l = 1; //判断是否找到了3，如果没有就搜索2
            for(int k = 1; k <= num[1]; k++) //1在区间一，在区间1搜索
                if(arr[k] == 3)
                {
                    swap(arr[k], arr[i]);
                    l = 0;
                    ans++;
                    break;
                }
            if(l)   //在区间一找不到3
                for(int k = 1;k <= num[2]; k++) //然后在区间一，二搜索不等于1的数字
                {
                    if(arr[k] != 1)
                    {
                        swap(arr[k], arr[i]); //把1换到区间一，把2或3换到区间三
                        ans++;
                        break;
                    }
                }
        }
        if(arr[i] == 2) //如果上一步是把2换到区间三，那么就在区间二里面找3，然后两者交换
        {               //或者本来就是2
            bool l = 1;
            for(int k = num[1] + 1; k <= num[2]; k++)
                if(arr[k] == 3) //找到3，然后交换
                {
                    swap(arr[k],arr[i]);
                    ans++;
                    l = 0;
                    break;
                }
            if(l) //如果找不到，那就在区间一找
                for(int k = 1; k <= num[1]; k++)
                    if(arr[k] == 3){
                        ans ++;
                        swap(arr[k], arr[i]);
                        break;
                    }
        }
    }
    for(int i = num[2]; i >= num[1]+1; i--) //处理区间三后再处理区间二。这里只要交换1,2，因为3已经全部在区间三了
    {
        if(arr[i] == 1)
            for(int k = 1; k <= num[1]; k++)
                if(arr[k] == 2)
                {
                    swap(arr[k], arr[i]);
                    ans++;
                    break;
                }
    }
    cout << ans;
    return 0;
}

```

# P2386 放苹果

题目：[P2386 放苹果](https://www.luogu.org/problemnew/show/P2386)

题解：作者: [ybb756032937](https://www.luogu.org/space/show?uid=45384) 递归

详细解释思路（当苹果数大于等于盘子数时的分法）：一种是**目前**所有的盘子都放一个苹果，盘子数不变，即：apple(m-n,n);  

另一种是将一个盘子不放，再来进行思考，即：apple(m,n-1);  

两种情况的总和就是答案；  

接下来就用一个例子来详细解释：  

现在有5个苹果和3个盘子；  

因为苹果数大于盘子数，所以分成两种：  

第一种：目前所有盘子放一个苹果（目前盘子数3个）apple(2,3);  

第二种：拿一个盘子不放苹果，剩下的盘子继续考虑，apple(5,2);  

apple(2,3):因为苹果数小于盘子数，所以apple(2,2);  

apple(2,2):因为苹果数等于盘子数，所以又分为两种：  

第一种：目前所有盘子放一个苹果（目前盘子数2个）apple(0,2); 没有苹果，达到边界，**返回值1**；  

第二种：拿一个盘子不放苹果，剩下的盘子继续考虑，apple(2,1); 盘子数只有1个，达到边界，**返回值1**；  

apple(5,2):因为苹果数大于盘子数，所以又分为两种：  

第一种：目前所有盘子放一个苹果（目前盘子数2个)apple(3,2);  

第二种：拿一个盘子不放苹果，剩下的盘子继续考虑，apple(5,1); 因为盘子数只有1个，达到边界，**返回值1**；  

apple(3,2):因为苹果数大于盘子数，所以又分为两种：  

第一种：目前所有盘子放一个苹果（目前盘子数2个)apple(1,2); 苹果数只有1个，达到边界，**返回值1**；  

第二种：拿一个盘子不放苹果，剩下的盘子继续考虑，apple(3,1); 盘子数只有1个，达到边界，**返回值1**；  

所有的值相加得到最后的结果5，记录在数组sum中，最后输出；  

```c++
#include <iostream>
using namespace std;

int put(int m,int n) //m是苹果数，n是盘子数
{
    if(m == 0 || m == 1 || n == 1) //边界：当苹果只有一个或者没有苹果时或者盘子只有一个时，只有一种放法，所以达到边界，返回值；
        return 1;
    if(m < n) //当苹果数少于盘子数，就只有m个盘子作用，所以接下来就计算m个苹果和m个盘子；
        return put(m, m);
    if(m >= n) //如果苹果数大于等于盘子数，分成两个部分，一种是目前所有的盘子都放一个苹果，另一种是拿一个盘子不放；
        return put(m - n, n) + put(m, n - 1);
}
int main()
{
    int t;
    cin >> t;
    while(t--)
    {
        int m ,n, cnt = 0;
        cin >> m >> n;
        cnt = put(m, n);
        cout << cnt << endl;
    }
    return 0;
}

```



#  P2562 [AHOI2002]Kitty猫基因编码(递归)

题目：[p2562](https://www.luogu.org/problemnew/show/P2562)

看到这道题一想就是递归题，类似于分段函数的题目都可以想想递归，但是有了别的思路，用队列来做，就是不断出队入队，但是如果提前出队的话这样的结果不对，如果都不出队的话，直接处理完了的话又不知道怎么结束循环，所以还是递归好

```c++
#include <iostream>
#include <string>
using namespace std;

string check(string s)
{
    int mid = s.size() / 2;
    if(s.find('0') != string::npos) //表示找到了0
    {
        if(s.find('1') != string::npos) //同时找到了1
            return 'C' + check(s.substr(0, mid)) +
                    check(s.substr(mid, mid));
        else //没有的找到1的话那么就代表全是0
            return "A";
    }
    else //没有找到0，代表全是1
            return "B";
}

int main()
{
    ios::sync_with_stdio(false);
    string in;
    cin >> in;
    cout << check(in);
    return 0;
}

```

# P1209 [USACO1.3]修理牛棚 Barn Repair（贪心）

题目：[p1209](https://www.luogu.org/problemnew/show/P1209)

题解作者: [Starlight_Glimmer](https://www.luogu.org/space/show?uid=41165) 

典型的贪心题目

我们可以先假设只有一块木板从编号最小的牛棚一直铺到编号最大的牛棚，然后断开m-1处。自然要按相邻牛棚的编号差从大到小断开才能使我们断开的地方可以有效节省木板长度（因为中间省去的要更多）

另外，要将输入的数据排序，数据可能不是按编号从小到大给的

```c++
#include <iostream>
#include <functional>
#include <algorithm>

const int len = 1000000;
using namespace std;

int main()
{
    int m, s, c;
    int C[len], dis[len]; //dis代表两个牛棚之间的距离
    cin >> m >> s >> c;
    for(int i = 0; i < c; i++)
        cin >> C[i];
    if(m > c) //特判，如果木板数大于牛数，那么每只牛可以有一块木板
    {
        cout << c;
        return 0;
    }
    int ans = 0;
    sort(C, C + c);
    ans = C[c - 1] - C[0] + 1; //假设只有一块木板连续地铺着
    for(int i = 0; i < c - 1; i++)
        dis[i] = C[i + 1] - C[i];
    sort(dis, dis + c - 1, greater<int>());
    for(int i = 0; i < m - 1; i++) //原来已经假设有一块了，则需要减去m-1个
        ans = ans - dis[i] + 1;
    cout << ans;
    return 0;
}

```

# P1106 删数问题 （贪心）

[P1106 删数问题](https://www.luogu.org/problemnew/show/P1106)

题解参考： [书海扬帆](https://www.luogu.org/space/show?uid=39912) 

这道题目是一道非常经典的贪心问题。由于n的位数非常多，而且删除操作改变的是每个数位，所以我们非常自然地能想到用字符串来存储n。  

本题目的贪心策略：从高位到低位搜索，如果各位数字均递增，则删去最后一个数字，否则删除第一个递减区间的首字符。举个栗子，如果1234567要删除2位的话，我们必定是删除6和7，而3654321如果要删除2位的话，我们则要选择删去6和5.

重复以上过程s次，剩下的数字串便是问题的解了。

这里再谈一下0的问题，由于前导0不能输出~~（你见过有人把123写成0123或是000123嘛）~~所以我们再来一个布尔变量flag来记录每一位是否为0.但是这个时候还出现了一个问题，就是这个字符串被删除完了，只剩下一个0怎么办？比如说样例7，10删除1位之后，我们还是必须要输出0的。因此我们再建立一个cnt变量来存储输出的数字的个数。如果个数为0，那么说明整个字符串均为0，此时我们还需要输出一个0.

```c++
#include <iostream>
#include <string>
using namespace std;

int main()
{
    ios::sync_with_stdio(false);
    string s;
    cin >> s;
    int n;
    cin >> n;
    int len = s.size(); //长度，不能都用s.size()；因为要缩减，数组向前移，但是s.size()不变
    for(int i = 0; i < n; i++)
    {
        for(int j = 0; j < len; j++) //从字符串的第一个字符开始查找，len是字符串的长度
        {
            if(s[j] > s[j + 1]) //如果找到了递减区间
            {
                for(int k = j; k < len - 1; k++) //那么就删除字符串n的第j个字符，后面字符往前调整。
                    s[k] = s[k + 1];
                break;
            }
        }
        len--;  //由于已经删除了一个元素，所以长度-1
    }
    int cnt = 0; //记录被输出字符的个数
    int flag = 0;
    for(int i = 0; i < len; i++)
    {
        if(s[i] != '0') //如果不是0，则说明可以输出，flag=1
            flag = 1;
        if(flag)
        {
            cout << s[i];
            cnt++;
        }
    }
    if(cnt == 0) //如果啥都没输出，就说明整个字符串都是0，此事我们还要输出一个0
        cout << 0;
    return 0;
}
```

# P2758 编辑距离(dp)

[p2758](https://www.luogu.org/problemnew/show/P2758)

题解参考：

[4396瞎](https://www.luogu.org/space/show?uid=36788)

 [qwaszx](https://www.luogu.org/space/show?uid=22136)

做动态规划的题一般分为四个步骤：确定子问题—>定义状态—>转移方程—>避免重复求解  

**1.确定子问题：  **  

由于对于字符串的操作只有4种情况（删除，添加、更改、不变），所以该题的子问题就是进行了这4种操作后的A字符串变为B字符串需要多少步。  

**2.定义状态： ** 

也就是说递归的dp函数需要哪些参数，参数越少越好因为需要建memo。后来想到dp（i，j）代表字符串A的前i个字符（包括第i个）变为字符串B的前j个（包括第j个）需要多少步。也就是说解出来dp（lenA，lenB）就可以了。

 **3.转移方程：  **

假设用f[i][j]表示将串a[1…i]转换为串b[1…j]所需的最少操作次数（最短距离）

首先是边界：  

①i==0时，即a为空，那么对应的f[0][j]的值就为j：增加j个字符，使a转化为b  

②j==0时，即b为空，那么对应的f[i][0]的值就为i：减少i个字符，使a转化为b  

```  
转移有三种:

①f[i - 1][j] -> f[i][j],插入即可,f[i][j] = f[i - 1][j] + 1;

②f[i][j - 1] ->f [i][j],删除即可f[i][j] = f[i][j - 1] + 1;

③f[i - 1][j - 1] -> f[i][j],要把a[i - 1]换成b[j - 1],如果相同就不用换了

```

那么，

```c++
if(a[i-1] == b[j-1])
	f[i][j] = f[i - 1][j - 1];
else 
	f[i][j] = min(f[i - 1][j] + 1,f[i][j - 1] + 1,f[i - 1][j - 1] + 1);
```

代码:

```c++
#include <iostream>
#include <algorithm>
#include <string>
using namespace std;

string s1, s2;
const int maxLen = 3000;
int arr[maxLen][maxLen];

void dp(int lenA, int lenB)
{
    //初始化
    for(int i = 1; i <= lenA; i++) //a不为空，长度为i，b为空，则a需要删去i个字母
        arr[i][0] = i;
    for(int i = 0; i <= lenB; i++) //同理
        arr[0][i] = i;
    for(int i = 1; i <= lenA; i++)
        for(int j = 1; j <= lenB; j++)
        {
            if(s1[i - 1] == s2[j - 1])
                arr[i][j] = arr[i - 1][j - 1];
            else
                arr[i][j] = min(arr[i - 1][j] + 1, min(arr[i][j - 1] + 1, arr[i - 1][j - 1] + 1));
        }
}

int main()
{
    ios::sync_with_stdio(false);
    cin >> s1 >> s2;
    dp(s1.size(), s2.size());
    cout << arr[s1.size()][s2.size()];
    return 0;
}

```

# P2404 自然数的拆分问题（回溯）

[p2404](https://www.luogu.org/problemnew/show/P2404)

```c++
#include <iostream>
using namespace std;

int arr[1000] = {1}; //从拆1开始
int n, m;

void print(int num)
{
    for(int i = 1; i < num; i++)
        cout << arr[i] << "+";
    cout << arr[num] << endl;
}

void dfs(int num)
{
    for(int i = arr[num - 1]; i <= m; i++)
    {
        if(i == n) continue; //防止n进入到数组arr，避免最后输出n
        arr[num] = i; //结果放到arr里面
        m -= i; //已经拆分出了一个i，对应的m要减i
        if(m == 0)  //说明m已经拆分完成了，直接输出
            print(num);
        else    //还没有拆分完成，继续拆分
            dfs(num + 1);
        m += i; //回溯，即进入到下个阶段的拆分
    }
}

int main()
{
    ios::sync_with_stdio(false);
    cin >> n;
    m = n;
    dfs(1);
    return 0;
}
```



