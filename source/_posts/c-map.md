---
title: c++ map
date: 2018-04-03 14:26:50
categories: STL
tags: 
- STL
- map
---

 map和multimap

<!-- more -->

# 开始

1. c++有map和multimap两种容器，区别在于multimap允许重复元素，map则不允许，在头文件中< map >中定义
2. Map是STL的一个关联容器，它提供一对一（其中第一个可以称为关键字(key)，每个关键字只能在map中出现一次，第二个可能称为该关键字的值(value)）的数据处理能力，由于这个特性，它完成有可能在我们处理一对一数据的时候，在编程上提供快速通道。这里说下map内部数据的组织，map内部自建一颗红黑树(一种非严格意义上的平衡二叉树)，这颗树具有对数据自动排序的功能，所以在map内部所有的数据都是有序的

</div>
<div align="center">
<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/map-example.png">

3. 它的特点是增加和删除节点对迭代器的影响很小，除了那个操作节点，对其他的节点都没有什么影响。对于迭代器来说，可以修改实值，而不能修改key。

4. 自动建立Key － value的对应。key 和 value可以是任意你需要的类型。  **根据key值快速查找记录，查找的复杂度基本是Log**(N)，如果有1000个记录，最多查找10次，1,000,000个记录，最多查找20次。

5. 什么是一对一的数据映射。比如一个班级中，每个学生的学号跟他的姓名就存在着一一映射的关系，这个模型用map可能轻易描述，很明显学号用int描述，姓名用字符串描述,下面给出map描述代码：

   ```c++
   map<int, string> mapStudent;
   ```

# 声明初始化

```c++
//头文件
#include<map>

map<int, string> ID_Name;
map<int, string, op>; //以op为排序准则。
// 使用{}赋值是从c++11开始的，因此编译器版本过低时会报错，如visual studio 2012
map<int, string> ID_Name = {
                { 2015, "Jim" },
                { 2016, "Tom" },
                { 2017, "Bob" } };
```

# 插入操作

## 使用数组方式插入

```c++
#include <iostream>
#include <map>
using namespace std;
int main()
{
    map<int, string> mymap;
    mymap[10] = "hello";	
    //如果已经存在键值10，则会作赋值修改操作，如果没有则插入
    cout << mymap.begin()->first << mymap.begin()->second;

    return 0;
}
//程序输出：10hello
```

## 使用insert进行单个和多个插入

### 插入单个键值对

```c++
// 插入单个键值对，并返回插入位置和成功标志，插入位置已经存在值时，插入失败
pair<iterator,bool> insert (const value_type& val);
```

一个简单的栗子，使用插入功能:

```c++
#include <iostream>
#include <map>
using namespace std;

int main()
{
    ios::sync_with_stdio(false); //关闭同步，加快速度
    
    map<string, int>mymap;
    mymap.insert(pair<string, int>("hello world", 2010));
    //mymap.insert(make_pair("hello world", 2010));
    //插入单个键值对
    auto iter = mymap.begin();
    while(iter != mymap.end())
    {
        cout << iter->first << " ";
        cout << iter->second;
        iter++;
    }
    return 0;
}
//程序输出：hello world 2010
```

使用返回值的判断

```c++
#include <iostream>
#include <map>
using namespace std;

int main()
{
    ios::sync_with_stdio(false);

    map<string, int>mymap;
    mymap.insert(pair<string, int>("hello world",  2010));

    pair<map<string, int>::iterator, bool> ret;
    ret = mymap.insert(pair<string, int>("hello", 500));
    //auto ret = mymap.insert(pair<string, int>("hello", 500));
    if (ret.second == false)
         cout << "already existed";
    else
    {
         cout << ret.first->first << " ";
         cout << ret.first->second ;
    }

    return 0;
}
//程序输出：hello 500
```

下面是插入相同的内容

```c++
#include <iostream>
#include <map>
using namespace std;

int main()
{
    ios::sync_with_stdio(false);

    map<string, int>mymap;
    mymap.insert(pair<string, int>("hello world",  2010));

    pair<map<string, int>::iterator, bool> ret;
    ret = mymap.insert(pair<string, int>("hello world", 500));
    if (ret.second == false)
         cout << "already existed";
    else
    {
         cout << ret.first->first << " ";
         cout << ret.first->second ;
    }

    return 0;
}
//程序输出：already existed
```

可见，key或value与map里面已经存在的内容相同的话，就会不能插入

### 在指定位置插入

```c++
//在指定位置插入，在不同位置插入效率是不一样的，因为涉及到重排
iterator insert (const_iterator position, const value_type& val);
```

```c++
#include <iostream>
#include <map>
using namespace std;

int main()
{
    ios::sync_with_stdio(false);

    map<string, int>mymap;
    mymap.insert(pair<string, int>("hello world", 2010));

    map<string, int>::iterator it = mymap.begin() ;
     //auto it = mymap.begin();
    mymap.insert(it, pair<string, int>("good morning, ", 300));
	//在开始位置插入
    auto iter = mymap.begin();
    while(iter != mymap.end())
    {
        cout << iter->first << " ";
        //cout << iter->second;
        iter++;
    }
    return 0;
}
//程序输出：good morning, hello world
```

### 插入多个

```c++
void insert (InputIterator first, InputIterator last);
```

 ```c++
#include <iostream>
#include <map>
using namespace std;
int main()
{
    std::map<char, int> mymap;

    mymap.insert(pair<char, int>('a', 100));
    mymap.insert(pair<char, int>('z', 200));
    mymap.insert(pair<char, int>('b', 300));
    mymap.insert(pair<char, int>('c', 400));

    //范围多值插入
    std::map<char, int> anothermap;
    anothermap.insert(mymap.begin(), mymap.find('z'));

   auto iter = anothermap.begin();
    while(iter != anothermap.end())
    {
        cout << iter->first;
        iter++;
    }
    return 0;
}
//程序输出：abc
//自己的值插给自己是没有意义的，因为map不能存在相同元素
 ```

### 列表插入

```c++
#include <iostream>
#include <map>
using namespace std;
int main()
{
    std::map<char, int> mymap;
    mymap.insert({{'a', 100}, {'b', 200}});
    auto iter = mymap.begin();
    while(iter != mymap.end())
    {
        cout << iter->first << iter->second << endl;
        iter++;
    }
    return 0;
}
/*程序输出：
a100
b200
*/
```

关于更多insert信息以及c++17定义的insert函数可以参考:

[std::map::insert](http://zh.cppreference.com/w/cpp/container/map/insert)

# 取值

map中元素取值主要有at和[ ]两种操作，at会作下标检查，而[]不会。

```c++
#include <iostream>
#include <map>
using namespace std;
int main()
{
    map<int, string> mymap;
    mymap[10] = "hello";
    //使用at会进行关键字检查，因此下面语句会报错
    mymap.at(2016) = "world";
    
    //mymap中没有关键字2017，使用[]取值会导致插入
	//因此，下面语句不会报错，但打印结果为空
    cout << mymap[2017];
    return 0;
}
```

取key和value

```c++
....
map<string, int>mymap;
mymap.insert(pair<string, int>("hello world", 2010));
auto pos = mymap,begin();
cout << pos->first; //取出key
cout << pos->second; //取出value
//pos->first是(*pos).first的简写形式

pos->first = "hello"; //如果尝试改变元素的key，会引发错误
pos->second = 13; //改变value则没有问题
```

注意：map提供了一种非常方便的方法让你改变元素的key

```c++
//insert new element with value of old element
coll["new_key"] = coll["old_key"];
// remove old element
coll.erase("old_key");
```



# 容量查询

```c++
// 查询map是否为空
bool empty();

// 查询map中键值对的数量
size_t size();

// 查询map所能包含的最大键值对数量，和系统和应用库有关。
// 此外，这并不意味着用户一定可以存这么多，很可能还没达到就已经开辟内存失败了
size_t max_size();

// 查询关键字为key的元素的个数，在map里结果非0即1
size_t count( const Key& key ) const; 
```

# 迭代器

共有八个获取迭代器的函数：** begin, end, rbegin,rend\** 以及对应的 ** cbegin, cend, crbegin,crend\**。

二者的区别在于，后者一定返回 const_iterator，而前者则根据map的类型返回iterator 或者 const_iterator。const情况下，不允许对值进行修改。如下面代码所示：

```c++
map<int,int>::iterator it;
map<int,int> mmap;
const map<int,int> const_mmap;

it = mmap.begin(); //iterator
mmap.cbegin(); //const_iterator

const_mmap.begin(); //const_iterator
const_mmap.cbegin(); //const_iterator
```

返回的迭代器可以进行加减操作，此外，如果map为空，则 begin = end。

# 删除交换

## 删除

#### 删除一定范围内的元素

```c++
// 删除一定范围内的元素，并返回一个指向下一元素的迭代器
iterator erase( const_iterator first, const_iterator last );
```

```c++
#include <iostream>
#include <map>
using namespace std;
int main()
{
    map<int, string> mymap;
    mymap.insert(pair<int, string>(1,"asdf"));
    mymap.insert(pair<int, string>(2,"qwer"));
    mymap.insert(pair<int, string>(3,"zxcv"));
    mymap.erase(mymap.begin(), mymap.find(2));
    auto iter = mymap.begin();
    while(iter != mymap.end())
    {
        cout << iter->first << " " << iter->second << endl;
        iter++;
    }
    return 0;

//程序输出：
2 qwer
3 zxcv
```

#### 删除迭代器指向位置的键值对

```c++
// 删除迭代器指向位置的键值对，并返回一个指向下一元素的迭代器
iterator erase( iterator pos )
```

```c++
#include <iostream>
#include <map>
using namespace std;
int main()
{
    map<int, string> mymap;
    mymap.insert(pair<int, string>(1,"asdf"));
    mymap.insert(pair<int, string>(2,"qwer"));
    mymap.insert(pair<int, string>(3,"zxcv"));
    mymap.erase(mymap.find(2));
    auto iter = mymap.begin();
    while(iter != mymap.end())
    {
        cout << iter->first << " " << iter->second << endl;
        iter++;
    }
    return 0;
}
//程序输出：
1 asdf
3 zxcv
```

### 根据Key来进行删除 

```c++
// 根据Key来进行删除， 返回删除的元素数量，在map里结果非0即1
size_t erase( const key_type& key );
```

```c++
#include <map>
#include <string>
#include <iostream>
using namespace std;

int main()
{
    map<int, string>mymap;
    mymap.insert(pair<int, string>(1,"hello"));
    mymap.insert(pair<int, string>(2,"wolrd"));
    mymap.erase(1);
    auto iter = mymap.begin();
    while(iter != mymap.end())
    {
        cout << iter->first << iter->second << endl;
        iter++;
    }
    return 0;
}
//程序输出：2world
```

### 清空map

```c++
// 清空map，清空后的size为0
void clear();
```

## 交换

```c++
// 就是两个map的内容互换
void swap( map& other );
```

# 查找

```c++
// 关键字(key)查询，找到则返回指向该关键字的迭代器，否则返回指向end的迭代器
// 根据map的类型，返回的迭代器为 iterator 或者 const_iterator
iterator find (const key_type& k);
const_iterator find (const key_type& k) const;
```

```c++
#include <map>
#include <string>
#include <iostream>
using namespace std;

int main()
{
    map<char,int> mymap;
    mymap['a']=50;
    mymap['b']=100;
    mymap['c']=150;
    mymap['d']=200;

    auto it = mymap.find('b');
    if(it != mymap.end())
        mymap.erase (it); //b被成功删除
    auto iter = mymap.begin();
    while(iter != mymap.end())
    {
        cout << iter->first << iter->second << endl;
        iter++;
    }
    return 0;
}
//程序输出：
a50
c150
d200
```

参考：

[c++常见用法说明](https://blog.csdn.net/shuzfan/article/details/53115922)

# multimap的一些注意事项

## 用erase删除重复元素的第一个

```c++
#include <iostream>
#include <map>
using namespace std;

int main()
{
    multimap<int, char>mymap;
    mymap.insert(make_pair(1,'a'));
    mymap.insert(make_pair(2,'b'));
    mymap.insert(make_pair(3,'c'));
    mymap.insert(make_pair(2,'a'));
    mymap.insert(make_pair(1,'a'));
    /*mymap.erase(2);	error
    程序输出：
    1a
    1a
    2c
    */
    mymap.erase(mymap.find(2));
    auto it = mymap.begin();
    while(it != mymap.end())
    {
        cout << it->first << it->second << endl;
        it++;
    }
    return 0;
}

/*程序输出：
1a
1a
2a
3c
*/
```

## 移除所有与x相等的元素

假如是是移除所有与x相等的key值，可以像上面用erase，如果是value值，则看下面：

错误做法(c++11之前)：

*在C++11中，erase返回下一个迭代器，但之前的版本都返回void*

```c++
multimap<int, char>mymap;
for(auto iter = mymap.begin(); iter != mymap.end(); iter++)
{
  if(iter->second == value)
    mymap.erase(iter);
}
```

对iter所指元素施加erase()，会使pos不再成为一个有效的mymap迭代器。

正确做法(c++11之前)：

```c++
multimap<int, char>mymap;
for(auto iter = mymap.begin(); iter != mymap.end(); )
{
  if(iter->second == value)
    mymap.erase(iter++);
  else
    ++iter;
}
```

iter++会将iter移向下一个元素，但返回其原始值（指向原位置）的一个副本。因此，当erase()被调用时，iter已经不再指向那个即将被移除的元素了。

