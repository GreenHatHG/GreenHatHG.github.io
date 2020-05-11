---
title: c++string
date: 2018-02-21 14:08:11
categories: STL
tags: 
- STL
- string
---

string小结

<!-- more -->

# 开始

1. string类由头文件string支持

*（头文件string.h和cstring支持对c-风格字符串进行操纵的c库字符串函数，但不支持string类）*

2. string类将string::nops定义为字符串最大长度，通常为unsigned int最大值
3. string与wstring：

- string是针对char而预先定义的特化版本，wstring是针对wchat_t而预先定义的特化版本
- 使用wstring可以让你使用宽字符集，例如Unicode或某些亚洲字符集‘
- 所有字符串类型都采用相同接口，所以两者用法都一样。

4. C++字符串并不以'/0'结尾
5. 注意对象名不会被看做是对象地址，比如string s(20, 'x'); s不是指针，s+6没有意义，而&s[6]却是指针。

# 字符串各种操作函数

| Name                             | Feature                                                      |
| -------------------------------- | ------------------------------------------------------------ |
| swap()                           | 交换两个字符串的内容                                         |
| =, assign()                      | 赋以新值                                                     |
| +=,  append(),  push_back()      | 在尾部添加字符                                               |
| insert()                         | 插入字符                                                     |
| erase()                          | 删除字符                                                     |
| clear()                          | 删除全部字符                                                 |
| replace()                        | 替换字符                                                     |
| +                                | 串联字符串                                                   |
| ==, != , <, <=, >, >=, compare() | 比较字符串                                                   |
| size(), length()                 | 返回字符数量                                                 |
| max_size()                       | 返回字符的可能最大个数                                       |
| empty()                          | 判断字符串是否为空                                           |
| capacity()                       | 返回重新分配之前的字符容量                                   |
| reserve()                        | 保留一定量内存以容纳一定数量的字符                           |
| [ ],  at()                       | 存取单一字符                                                 |
| >> ,  getline()                  | 从stream读取某值                                             |
| <<                               | 将谋值写入stream                                             |
| copy()                           | 将某值赋值为一个C_string，不添加'\0'字符                     |
| c_str()                          | 将内容以C_string返回，在尾端添加'\0'字符                     |
| data()                           | 将内容以字符数组形式返回，并未追加'\0'字符，所以返回类型并非有效的c-string |
| substr()                         | 返回某个子字符串                                             |
| begin() end()                    | 提供类似STL的迭代器支持                                      |
| rbegin()  rend()                 | 逆向迭代器                                                   |
| get_allocator()                  | 返回配置器                                                   |

## 转换成c-string或字符数组

操作函数data(), c_str(), copy()

注意：

1. data()和c_str()返回的字符数组由该字符串拥有，也就是说调用者千万不能修改它或释放其内存
2. 一般而言，整个程序中应该坚持使用string，直到你必须将其内容转化为char*时才把他们转换成c-string。

##  C++字符串存在三种大小

1.  现有的字符数，函数是size()和length()，他们等效。Empty()用来检查字符串是否为空。

2.  size()是获取容器元素个数的通用成员函数，length()则对应于一般c-string strlen()函数，传回字符串长度。

2.  max_size() ：这个大小是指当前C++字符串最多能包含的字符数，很可能和机器本身的限制或者字符串所在位置连续内存的大小有关系。

   *我们一般情况下不用关心他，应该大小足够我们用的。但是不够用的话，会抛出length_error异常*

3.  capacity()：重新分配内存之前，string所能包含的最大字符数

## 元素存取

使用下标操作符[]和函数at()对元素包含的字符进行访问

注意：

*操作符[]并不检查索引是否有效（有效索引0~str.length()），如果索引失效，会引起未定义的行为。而at()会检查，如果使用 at()的时候索引无效，会抛出out_of_range异常。*

*str.length()：const string a;的操作符[]对索引值是a.length()仍然有效，其返回值是'/0'。其他的各种情况，str.length()索引都是无效的*

举个例子：

```c++
const string Cstr(“const string”);
string Str(“string”);
Str[3];      //ok
Str.at(3);    //ok
Str[100]; //未定义的行为
Str.at(100);    //throw out_of_range
Str[Str.length()]    //未定义行为
Cstr[Cstr.length()] //返回 ‘/0'
Str.at(Str.length());//throw out_of_range
Cstr.at(Cstr.length()) ////throw out_of_range
```

## 比较函数

1. c++字符串支持常见的比较操作符（>,>=,<,<=,==,!=），甚至支持string与C-string的比较(如 str<”hello”)。在使用>,>=,<,<=这些操作符的时候是根据“当前字符特性”将字符按字典顺序进行逐一得 比较。字典排序靠前的字符小，比较的顺序是从前向后比较，遇到不相等的字符就按这个位置上的两个字符的比较结果确定两个字符串的大小。同时，string (“aaaa”) <string(aaaaa)
2. 另一个功能强大的比较函数是成员函数compare()。他支持多参数处理，支持用索引值和长度定位子串来进行比较。他返回一个整数来表示比较结果，返回值意义如下：0-相等 〉0-大于 <0-小于。

举个栗子：

```c++
string s(“abcd”);
s.compare(“abcd”); //返回0
s.compare(“dcba”); //返回一个小于0的值
s.compare(“ab”); //返回大于0的值
s.compare(s); //相等
s.compare(0,2,s,2,2); //用”ab”和”cd”进行比较 小于零
s.compare(1,2,”bcx”,2); //用”bc”和”bc”比较。
```

## 更改内容

### 赋值

第一个赋值方法当然是使用操作符=，新值可以是string(如：s=ns) 、c_string(如：s=”gaint”)甚至单一字符（如：s='j'）。还可以使用成员函数assign()，这个成员函数可以使你更灵活的对字符串赋值。

举个栗子：

```c++
s.assign(str); //你懂得
s.assign(str,1,3);//如果str是”iamangel” 就是把”ama”赋给字符串
s.assign(str,2,string::npos);//把字符串str从索引值2开始到结尾赋给s
s.assign(“gaint”); //不说
s.assign(“nico”,5);//把'n' ‘I' ‘c' ‘o' ‘/0'赋给字符串
s.assign(5,'x');//把五个x赋给字符串
```

```c++
string &operator=(const string &s);//把字符串s赋给当前字符串
string &assign(const char *s);//用c类型字符串s赋值
string &assign(const char *s,int n);//用c字符串s开始的n个字符赋值
string &assign(const string &s);//把字符串s赋给当前字符串
string &assign(int n,char c);//用n个字符c赋值给当前字符串
string &assign(const string &s,int start,int n);//把字符串s中从start开始的n个字符赋给当前字符串
string &assign(const_iterator first,const_itertor last);//把first和last迭代器之间的部分赋给字符串
```

int copy(char *s, int n, int pos = 0) const;

把当前串中以pos开始的n个字符拷贝到以s为起始位置的字符数组中，返回实际拷贝的数目

### 清空

把字符串清空的方法有三个：

s=””;	s.clear();		s.erase();

### 增加

函数有 +=、append()、push_back()。

举个栗子：

```c++
s+=str;//加个字符串
s+=”my name is jiayp”;//加个C字符串
s+='a';//加个字符
s.append(str);
s.append(str,1,3);//不解释了 同前面的函数参数assign的解释
s.append(str,2,string::npos)//不解释了
s.append(“my name is jiayp”);
s.append(“nico”,5);
s.append(5,'x');
s.push_back(‘a'); //这个函数只能增加单个字符对STL熟悉的理解起来很简单
            
//在string中间的某个位置插入字符串
//需要指定一个安插位置的索引，被插入的字符串将放在这个索引的后面。
s.insert(0,”my name”);
s.insert(1,str);
```

```c++
string &operator+=(const string &s);//把字符串s连接到当前字符串的结尾 
string &append(const char *s);//把c类型字符串s连接到当前字符串结尾
string &append(const char *s,int n);
//把c类型字符串s的前n个字符连接到当前字符串结尾
string &append(const string &s);//同operator+=()
string &append(const string &s,int pos,int n);
//把字符串s中从pos开始的n个字符连接到当前字符串的结尾
string &append(int n,char c);//在当前字符串结尾添加n个字符c
string &append(const_iterator first,const_iterator last);
//把迭代器first和last之间的部分连接到当前字符串的结尾
```



注意：

*1. 这种形式的insert()函数不支持传入单个字符，这时的单个字符必须写成字符串形式。*

*2. 为了插 入单个字符，insert()函数提供了两个对插入单个字符操作的重载函数：insert(size_type index,size_type num,chart c)和insert(iterator pos,size_type num,chart c)。*

*3. 其中size_type是无符号整数，iterator是char*,所以，你这么调用insert函数是不行的：insert(0,1, 'j');这时候第一个参数将转换成哪一个呢？所以你必须这么写：insert((string::size_type)0,1,'j')！第二种形式指 出了使用迭代器安插字符的形式。

*4. 顺便提一下，string有很多操作是使用STL的迭代器的，他也尽量做得和STL靠近。*

### 删除

直接举个栗子：

```c++
string s="il8n";
s.replace(1,2,"nternationalizatio");
//从索引1开始的2个替换成后面的C_string
//即输出： nternationalization
s.replace(1,2,”nternationalizatio”);
s.erase(13);//从索引13开始往后全删除
s.erase(7,5);//从索引7开始往后删5个
```

### 提取子串和字符串连接

```c++
s.substr();//返回s的全部内容
s.substr(11);//从索引11往后的子串
s.substr(5,6);//从索引5开始6个字符
//把两个字符串结合起来的函数是+。
```

## 搜索和查找

| Name                | Feature                                     |
| ------------------- | ------------------------------------------- |
| find()              | 搜索第一个与value相等的字符                 |
| rfind()             | 搜索最后一个与value相等的字符               |
| find_first_of()     | 搜索第一个“与value中某值相等”的字符         |
| find_last_of()      | 搜索最后一个个“与value中某值相等”的字符     |
| find_first_not_of() | 搜索第一个“与value中任何值都不相等”的字符   |
| find_last_not_of()  | 搜索最后一个“与value中任何值都不相等”的字符 |

1. 所有搜索函数都返回符合条件的字符位置，如果搜索不成功，则返回npos。

2. 这些函数都采用下面的参数方案：

   - 第一参数总是搜索对象
   - 第二参数（可有可无）指出string搜索起点（字符位置）
   - 第三参数（可有可无）指出搜索的字符个数

3. 遍历：

   ```c++
   #include <string>  
   #include <iostream>  
   using namespace std;  
   int main(void)  
   {  
       char* cArray="hello, world!";  
       string s(cArray);  
       //数组方式  
       for(unsigned int j=0; j< s.size(); j++)  
           cout << "第" << j << "个字符: " << s[j] << endl;  
       //迭代器方式  
       string::reverse_iterator ri;  
       cout << "反向打印字符" << endl;  
       for(ri=s.rbegin(); ri!=s.rend(); ri++)  
           cout << *ri << ' ' ;  
       cout << endl;  
       return 0;  
   } 
   ```

   ​

   ​

## 数值npos

string::npos的类型是string::size_type,所以，一旦需要把一个索引与npos相比，这个索引值必须是string::size)type类型的，更多的情况下，我们可以直接把函数和npos进行比较（如：if(s.find(“jia”)== string::npos)）。

```c++
string s;
string::size_type idx;
...
idx = s.find("sub");
if(id == string::nops)
    ....
```



# string的构造和析构

| Name                      | Feature                              |
| ----------------------------- | ---------------------------------------- |
| string s                  | 生成一个空字符串s                            |
| string s(str)             | 拷贝构造函数生成str的复制品                      |
| string s(str, stridx)     | 将字符串str内"始于位置stridx"的部分当作字符串的初值      |
| string s(str, stridx, strlen) | 将字符串str内"始于stridx且长度顶多strlen"的部分作为字符串的初值 |
| string s(cstr)            | 将C字符串（以NULL结束）作为s的初值                 |
|string s(chars, chars_len)   |将C字符串前chars_len个字符作为字符串s的初值|
|string s(num, ‘c’) | 生成一个字符串，包含num个c字符 |
|string s(“value”)| 将s初始化为一个字符串string s=“value”字面值副本 |
| string s=“value” |将s初始化为一个字符串string s=“value”字面值副本|
|string s(begin, end)|以区间begin/end(不包含end)内的字符作为字符串s的初值|
|s.~string()|销毁所有字符，释放内存2 |

注意：

*不能以单一字符；爱初始化某个字符串*

但是可以这么做：

```c++
std::string s('x'); //error
std::string s(1, 'x') //ok,creates a string that has one character 'x'
```

这表示了编译器提供了一个从const char*到string的自动类型转换功能，但是反过来却不行

参考自：

[c++中的string常用函数用法总结]: http://www.jb51.net/article/41725.htm



# string类的输入

## 对于c-风格字符串

```c++
char info[100];
cin >> info;	//read a word
cin.getline(info, 100);	//read a line,discard \n
cin.get(info, 100);	read a line,leave \n in queue
```

## 对于string对象

```c++
string stuff;
cin >> stuff;
getline(cin, stuff);
```

### getline

1. 两个版本的getline()都有一个可选参数，用于指定使用哪个字符来确定输入的边界

```c++
cin.getline(info, 100, ':');
getline(cin, stuff, ':');
```

2. string版本的getline()函数从输入中读取字符，并将其储存到目标string中，直到发生下种情况之一：
   - 到达文件结尾，输入流的eofbit将被设置，这意味着方法fail()和eof()将被返回true
   - 遇到分界字符(默认未\n)，将把分界字符从输出流中删除，但是不储存
   - 读取的字符数达到最大允许值(string::nops和可供分配的内存字节数较小的一个)，在这种情况下，将设置输入流的failbit，这意味着方法fail()将放回true