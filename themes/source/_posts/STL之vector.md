---
title: STL之vector
date: 2018-02-14 13:03
categories: STL
tags: 
- STL
- vector
---
vector的声明、向量操作、访问方式、赋值操作、安插，移除相关操作、应用实例。一道习题（第k个约数）
<!-- more -->
### 一、 开始：
- 要使用vector必须包含头文件< vector >
- vector可以看成是动态一维数组
- vector可以存在重复元素(set不能存在)
- vector迭代器是随机存取迭代器，所以对任何一个STL算法都奏效
- 在末端附加或删除元素时，vector的性能相当好。但是如果在前端或在中部安插或删除元素，性能就不怎么样了。
*因为操作点之后的每一个元素都必须移到另一个位置，而每一次移动都得调用赋值操作符*
### 二、 vector的声明:
```c++
vector<type> a;
/*type可以是int，double等等.
此时a.size()为0，但是因为是动态的，所以大小会可能改变*/
int n; cin >> n;
vector<type> a(n); //动态分配
vector<int> a(100, 0);
//声明一个存放了100个0的整数vector，其大小没有被固定为100，还可以加数据
vector<type> a(b); //产生另一个同型vector的副本（所有元素都被拷贝）
vector<type> a(beg,end); //产生一个vector，以区间[beg,end]做为元素初值
a.~vector<type>(); //销毁所有元素并且释放内存
```
### 三、 向量操作:

| Name | Feature |
| :-: | :-: |
| push_back()| 在数组的最后添加一个数据
| pop_back() |        去掉数组的最后一个数据
| at()|               得到编号位置的数据
| begin()|            得到数组头的指针
| end() |             得到数组的最后一个单元+1的指针
| front() |            得到数组头的引用（返回第一个元素）
| back()|             得到数组的最后一个单元的引用（返回最后一个元素）
| max_size()|         得到vector最大可以是多大
| capacity() |        当前vector分配的大小
| size()      |       当前使用数据的大小
| resize()     |      改变当前使用数据的大小，如果它比当前使用的大，则填充默认值
| reserve()     |     改变当前vecotr所分配空间的大小(如果容量不足，扩大之，但是references，pointers，iterators会失效)
| erase()     |       删除指针指向的数据项
| clear()     |       清空当前的vector
| rbegin()    |       将vector反转后的开始指针返回(其实就是原来的end-1)
| rend()      |       将vector反转构的结束指针返回(其实就是原来的begin-1)
| empty()     |       判断vector是否为空
| swap()      |       与另一个vector交换数据


*对着一个空vector调用operator[], front(), back(),都会引起未定义行为，所以执行这些函数时必须确定容器不空*
### 四、 访问方式：
```c++
cout << a[5] << endl;
cout << a.at(5) << endl;
```
*以上区别在于后者在访问越界时会抛出异常，而前者不会。*
###五、 vector的赋值操作
```c++
c1 = c2; //将c2全部元素赋值给c1
c.assign(n, elem); //复制n个elem，赋值给c
c.assign(beg, end); //将区间[beg, end]内的元素赋值给c
c1.swap(c2); //将c1与c2元素互换
```
### 六、vector的安插、移除相关操作

| Name | Feature |
| :-: | :-: |
|c.insert(pos, elem)|	在pos位置上插入一个elem 副本，并返回新元素的位置
|c.insert(pos, n, elem)|	在pos位置上插入n个elem 副本。无返回值
|c.insert(pos, beg, end)|	在pos位置上插入区间[beg ; end]内的所有元素的副本，无回传值
|c.push_back(elem)|	在尾部添加一个elem副本
|c.pop_back()|	移除最后一个元素（但不回传）
|c.erase(pos)|	移除pos位置上的元素，返回下一元素的位置
|c.erase(beg , end)|	移除[beg , end]区间内的所有元素，返回下一元素的位置
|c.resize(num)	|将元素数量改为num（如果size()变大了，多出来的新元素都需要以default构造函数构造完成）
|c.resize(num , elem)|	将元素数量改为num（如果size()变大了，多出来的新元素都需要以elem的副本）
|c.clear()|	移除所有元素，将容器清空
### 七、vector应用实例
1. 删除所有的值为val的元素
```c++
vector<string> sentence;
sentence.erase(remove(sentence.begin() ,
              sentence.end() ,val) ,sentence.end());
```
简单解释下：remove()从指定位置开始遍历容器，将后面的元素依次前移，跳过和value相同值的元素，也就是说，所有和value相同值的元素都会被覆盖，而其他的元素都会依次前移，并返回范围新结尾的迭代器，整个过程并没有修改原容器的size,以及end()。如下：
我们要从{1,1,9,5,2,1,3}除去1。
```c++
#include <iostream>
#include <vector>
#include <algorithm> //使用remove()
using namespace std;
int main()
{
    vector<int> c = {1,1,9,5,2,1,3};
    auto op =remove(c.begin(), c.end(), 1);
    //op里面存储的是返回值，可以看成是一个元素指针
    cout << *op << endl;
    //输出返回值
    for(int x : c)
        cout << x << ' '; //输出remove()后的vector
    return 0;
}
程序输出:
2
9 5 2 3 2 1 3
```
接着这里erase()作用是去除范围内的所有元素，这个范围里面的值就是{2,1,3},所以就可以得到想要的{9,5,2,3}。

2. 只是要删除“与某值相等”的第一个元素：
```c++
vector<string> sentence;
auto pos;
//或std::vector<string>::iterator sentence;
pos=find(sentence.begin() , sentence.end() ,val);
if( pos != sentence.end() ){
    sentence.erase(pos);
}
```
### 八、一道习题：
**题目描述**
给出一个数n和一个数k，按从小到大算出n的第k个约数。
**输入**
每一个测试数据包括一个n和一个k (1 <= n <= 10^15, 1 <= 10^9)
**输出**
如果n没有第k个约数，则输出-1，如果有则输出第k个约数
**示例**
输入：
4 2
输出：
2
代码：
```c++
#include <iostream>
#include <vector>
#include <cmath>
#include <algorithm>
using namespace std;
typedef long long ll;

int main()
{
    ll n, k;
    vector<ll> v;
    cin >> n >> k;
    for(ll i = 1; i <= sqrt(n); i++)
    {
        if(n % i == 0)
            v.push_back(i);
        if(n % i == 0 && i * i != n)
            v.push_back(n / i);
    }
    sort(v.begin(), v.end());
    if(k > v.size())
        cout << "-1";
    else
        cout << v[k-1] << endl;
    return 0;
}
```
