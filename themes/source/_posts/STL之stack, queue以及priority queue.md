---
title: STL之stack, queue以及priority_queue
date: 2018-02-13 10:31:11
categories: STL
tags: 
- priority_queue
- queue
- stack
- STL
---
栈、队列、优先队列基本总结
<!-- more -->
### 一、stack
1. 要使用stack，必须要包含头文件< stack >
2. 实际上stack只是很单纯地把各项操作转化为内部容器的对应调用，可以使用任何序列式容器来支持stack，只要它们支持back(), push_back(), pop_back()等动作就行。例如你可以使用vector或list来容纳元素：
```c++
std::stack< int, std::vector<int> >st;
```
</div>
<div align="center">
<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/stack%E5%86%85%E9%83%A8%E6%8E%A5%E5%8F%A3.png" width="70%" height="70%">
</div>

stack内部存放元素所用的实际容器，缺省采用deque。之所以采用deque，而非vector，是因为deque移除元素时会释放内存。

3. stack核心接口：
- push()：将一个元素置入stack内
- top():返回栈顶元素，但是并不移除它
- pop():从stack移除元素，但是并不将它返回
- empty():检查底层容器是否为空，当栈为空时返回true
- size():返回容纳的元素数

*如果stack内没有元素，则执行top()和pop()会导致未定义的行为，可以采用成员函数size()和empty()来检验容器是否为空*
4. stack简单实例:
```c++
#include <iostream>
#include <stack>
using namespace std;

int main()
{
    stack<int> st;
    st.push(1);
    st.push(2);
    st.push(3);

    cout << st.top() << ' ';
    st.pop();
    cout << st.top() << ' ';
    st.pop();

    st.top() = 77;
    st.push(4);
    st.push(5);
    st.pop();

    while(!st.empty())
    {
        cout << st.top() << ' ';
        st.pop();
    }
    return 0;
}
程序输出: 3 2 4 77
```
更多了解stack:
[stack- C++ Reference](http://zh.cppreference.com/w/cpp/container/stack)
### 二、queue
1. 要使用queue，必须包含头文件< queue >
2. 实际上queue只是很单纯地把各项操作转化为内部容器的对应调用，可以使用任何序列式容器来支持stack，只要它们支持front(), back(), push_back(), pop_front()等动作就行。例如你可以使用vector或list来容纳元素：
```c++
std::queue< std::string, std::list<std::string> > buffer;
```
</div>
<div align="center">
<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/queue%E5%86%85%E9%83%A8%E6%8E%A5%E5%8F%A3.png" width="80%" height="80%">
</div>

3. queue核心接口:
- push():入队，将一个元素置入队列末端中
- front():返回第一个被置入的元素，但并不移除
- back():返回最后一个元素，但并不移除
- pop():出队，移除队列中第一个元素，但并不返回
- empty():判断队列是否为空，队列为空时，返回true
- size():返回容纳的元素数

*如果queue之内没有元素，则front(), back(), pop()的执行行为会导致未定义行为。*
4. queue简单实例:
```c++
#include <iostream>
#include <queue>
#include <string>
using namespace std;

int main()
{
   queue<string> q;

   q.push("These ");
   q.push("are ");
   q.push("more than");

   cout << q.front();
   q.pop();
   cout << q.front();
   q.pop();

   q.push("four ");
   q.push("words!");
   q.pop();

   cout << q.front();
   q.pop();
   cout << q.front() << endl;
   q.pop();

   cout << "number pf elements in the queue: " << q.size() << endl;
   return 0;
}
程序输出：
These are four words!
number pf elements in the queue: 0
```
更多了解queue:
[queue- C++ Reference](http://zh.cppreference.com/w/cpp/container/queue)
### 三、priority_queue
1. 要使用priority_queue，必须包含头文件< queue >
2. 实际上priority_queue只是很单纯地把各项操作转化为内部容器的对应调用，可以使用任何序列式容器来支持priority_queue，只要它们支持随机存取迭代器和front(), push_back(), pop_front()等动作就行。由于priority_queue需要用到STL的heap算法，所以其内部容器必须支持随机存取迭代器。例如你可以使用deque来容纳元素：
```c++
std::priority_queue< float, std::deque<float> > pbuffer;
```

- 优先队列与队列的差别在于优先队列不是按照入队的顺序出队，而是按照队列中元素的优先权顺序出队（默认为大者优先，也可以通过指定算子来指定自己的优先顺序），如果同时存在若干个数值最大的元素，无法确知究竟哪个会入选。
- priority_queue模板类有三个模板参数，默认容器为vector，默认算子为less，即小的往前排，大的往后排（出队时序列尾的元素出队）
*一点要注意的是priority_queue中的三个参数，后两个可以省去，因为有默认参数，不过如果，有第三个参数的话，必定要写第二个参数。*
- 定义priority_queue的示例代码：
```c++
priority_queue<int> q1;
priority_queue< pair<int, int> >q2; //注意在两个尖括号之间一定要保留空格
priority_queue<int, vector<int>, greater<int> >q3; //定义小的先出队
```
- priority_queue与queue基本操作相同。接口有empty(), size(), top(), push(), pop()等等
3. priority_queue简单示例：
```c++
#include <iostream>
#include <queue>
using namespace std;

void showpq(priority_queue <int> gq)
{
    priority_queue <int> g = gq;
    while (!g.empty())
    {
        cout << '\t' << g.top();
        g.pop();
    }
    cout << '\n';
}

int main ()
{
    priority_queue <int> gquiz;
    gquiz.push(10);
    gquiz.push(30);
    gquiz.push(20);
    gquiz.push(5);
    gquiz.push(1);

    cout << "The priority queue gquiz is : ";
    showpq(gquiz);

    cout << "\ngquiz.size() : " << gquiz.size();
    cout << "\ngquiz.top() : " << gquiz.top();

    cout << "\ngquiz.pop() : ";
    gquiz.pop();
    showpq(gquiz);

    return 0;
}
程序输出：
The priority queue gquiz is :     30    20    10    5    1

gquiz.size() : 5
gquiz.top() : 30
gquiz.pop() :     20    10    5    1
```
4. 算子：
    默认为使用less算子。
    如果要定义自己的比较算子，则必须自己重载 operator< 或者自己写仿函数，
    优先队列试图将两个元素x 和y 代入比较运算符(对less 算子，调用x<y，对greater 算子，调用x>y)，若结果为真，则x 排在y 前面，y 将先于x 出队，反之，则将y 排在x 前面，x 将先出队。  
    下面程序是针对结构体的，对数据的比较是通过对结构体重载operator()。（转自[博客](http://blog.csdn.net/morewindows/article/details/6976468)）
    程序功能是模拟排队过程，每人有姓名和优先级，优先级相同则比较姓名，开始有5个人进入队列，然后队头2个人出队，再有3个人进入队列，最后所有人都依次出队，程序会输出离开队伍的顺序。
自定义算子可参考：

 [优先队列priority_queue的比较函数](https://www.cnblogs.com/flipped/p/5691430.html)

[优先级队列priority_queue之比较函数](https://blog.csdn.net/u014644714/article/details/68924863)

```c++
//by MoreWindows( http://blog.csdn.net/MoreWindows )
#include <queue>
#include <cstring>
#include <cstdio>
using namespace std;
//结构体
struct Node
{
	char szName[20];
	int  priority;
	Node(int nri, char *pszName)
	{
		strcpy(szName, pszName);
		priority = nri;
	}
};
//结构体的比较方法 改写operator()
struct NodeCmp
{
	bool operator()(const Node &na, const Node &nb)
	{
		if (na.priority != nb.priority)
			return na.priority <= nb.priority;
		else
			return strcmp(na.szName, nb.szName) > 0;
	}
};
void PrintfNode(Node &na)
{
	printf("%s %d\n", na.szName, na.priority);
}
int main()
{
	//优先级队列默认是使用vector作容器，底层数据结构为堆。
	priority_queue<Node, vector<Node>, NodeCmp> a;

	//有5个人进入队列
	a.push(Node(5, "小谭"));
	a.push(Node(3, "小刘"));
	a.push(Node(1, "小涛"));
	a.push(Node(5, "小王"));

	//队头的2个人出队
	PrintfNode(a.top());
	a.pop();
	PrintfNode(a.top());
	a.pop();
	printf("--------------------\n");

	//再进入3个人
	a.push(Node(2, "小白"));
	a.push(Node(2, "小强"));
	a.push(Node(3, "小新"));

	//所有人都依次出队
	while (!a.empty())
	{
		PrintfNode(a.top());
		a.pop();
	}

	return 0;
}
```
更多了解priority_queue:
[priority_queue- C++ Reference](http://zh.cppreference.com/w/cpp/container/priority_queue)
