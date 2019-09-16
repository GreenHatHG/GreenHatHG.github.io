---
title: 一步一步写出c++带指针类实例(标准库string部分)
date: 2018-02-14 00:40:11
categories: c++
tags: 写出string实例
---
共16步
<!-- more -->
*本篇文章注重与complex篇的区别，可能不那么详细*
### 流程：
1. 写出主体（行1,2,11)
2. 考虑private里面要放的数据，字符串，当然是放很多字符，一种想法是里面放一个数组，但是这种数组可能不是很好，因为数组你得指明多大，太大或太小都不好。所以我们选择另一种做法，在里面放一个指针，将来要分配多大内存，就动态去分配，用new的方式。
一个指针多大，在传统的32位电脑中是4个字节，所以无论以后字符串里面内容是什么，字符串这个对象本身就是4个字节。(行9,10)
3. 然后到了准备函数的环节，首先当然是构造函数，要使外界创建string，一定要调用构造函数，而且要放到public（当然也有放在private里面的，只是很少）。
4. 存进来以后当然不可能要改变里面的东西，我们只是拿来当初值而已，所以加const，并且给默认值为0。（行4）
5. class里面有指针，所以要去关注三个重要特殊的函数。
6. 第一个是拷贝构造函数。既然是拷贝构造，那么就是相当于它自己，则参数里面类型用String。
再考虑下引用和const的问题，引用必须的。（行5）
我们传进来只是复制个蓝本，没有考虑要去改变，所以加上const。
7. 第二是拷贝赋值函数。先写名称operator=，赋值的动作是把来源端拷贝到目的端去，来源端是String类型，所以参数也是String,这才叫拷贝。
这里和7一样同样考虑引用和const
返回值的话理所当然是String类型，因为本来就有目的端，所以可以加引用（行6）
8. 第三个是析构函数，与类的名称相同并加波浪号
9. 接着我们还需要加个辅助函数，能让字符串丢到cout，也就是你的屏幕。做法很简单，如果我能够取到private里面那个字符，cout是可以接受这种东西的，所以设置了一个get_str()函数，同时注意const。因为前面那些函数都是要改变目的端的数据，所以不能加const。（行7）
10. 设计构造函数，首先第一个要考虑的是得分配足够的空间去放初值，这里用new动态分配内存。接着把传进来初值的内容拷贝到已经分配的空间，即data，这就完成了创建新的字符串的内容（注意包含头文件）。加个inline。（行13-26）
11. 如果没有指定初值的话，我这里是也要分配一个空间去放结束符。
12. 下一步设计析构函数，这个把分配的内存释放掉就行了，用delete[]。（行28-31）
13. 然后是拷贝构造函数，同样是用new分配一块先，这里不同的是strlen里面用了str.data，因为传进来的是class，所以用.读取数据。（行33-38）
14. 看到这么多inline，那到底什么时候用呢？
emmm，反正全写行了，不管多长，要是编译器不支持的话自然不会去做，没有副作用。
15. 接着是拷贝赋值函数,赋值，把来源端赋值到目的端，现在这个目的端是本来已经存在的东西，所以目的端要先把自己用delete清掉，杀掉以后重新分配一块够大的空间，接着把来源端拷贝到目的端。考虑下返回值，如果把返回类型设置为void，则不能连续赋值，所以最好有返回。
16. 还得考虑一个容易忽略的点，就是判断是是不是自我赋值。（40-49）
```c++
class String
{
public:
  String(const char* str = 0); //构造函数
  String(const String& str); //拷贝构造函数
  String& operator = (const String & str); //拷贝赋值函数
  ~String(); //析构函数
  char* get_str() const { return data; } //把字符串丢到cout
private：
      char* data;
}

inline
String::String(const char* str = 0)
{
  if(str)
  {
    data = new char[strlen(str) + 1];
    strcpy(data, str);
  }
  else //未指定初值
  {
    data = new char[1];
    *data = '\0';
  }
}//构造函数

inline String::~String()
{
  delete[] data;
}//析构函数

inline
String::String(const String & str)
{
  data = new char[strlen(str.data) + 1];
  strcpy(data, str.data);
}//拷贝构造函数

inline
String& String::operator=(const String& str)
{
  if(this == &str) //判断是不是自我赋值
    return *this;
  delete[] data;
  data = new char[strlen(str.data + 1)];
  strcpy(data, str.data);
  return *this;
}//拷贝赋值函数
```
