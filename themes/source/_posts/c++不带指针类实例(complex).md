---
title: 一步一步写出c++不带指针类实例(标准库complex部分)
date:  2018-02-13 20:54:21
categories: c++
tags: 写c++实例
---
总共有19步
<!-- more -->

### 流程：
1. 首先先养成防卫式习惯，在头文件中加入下面三行，防止重复包含(行2,3,19)
2. 然后写出主体（行4,5,18）
3. 考虑下这个复数需要什么数据，当然这个数据应该放在私有部分(private)。
    复数有实部虚部，可得出private变量的定义（行15,16）
4. 接下来考虑需要什么函数来实现，这些函数当然需要操作在复数身上，然后
    函数是对外发表的，所以放在public里面（行7，行8）
5. 首先任何一个class都有构造函数，然后构造函数的语法是什么？
    构造函数名称与class名称相同，没有返回类型。接着，一个构造函数应该接收什么参数？
    在这个例子中，有实部虚部，我们就应该有实部虚部的参数，然后我们就考量要不要默认值。
6. 我们还要考虑这个函数的传递是按值传递还是按引用传递？
    这个函数中传值或者传引用区别不大，所以随便选择一个
7. 还得想到构造函数有个很特别的语法--初始列，一般最好运用初始列
8. 然后考虑构造函数还需要干什么，这个例子已经不需要什么操作，在别的地方可能要在构造函数中分配一些内存，开个窗口，或者开个文件等等（行9,10）
9. 接下来考虑这个class要实现什么功能，在这个例子中，我们实现复数的加法。
    对于该函数有两种选择，一种是成为成员函数，另一种则是非成员函数，这两种选择都可以，这里我们选择第一种。（行12）
10. 还需要准备什么函数呢？需要两个函数去取得private里面实部和虚部的值。对于一个函数，我们考虑是否要加const，在这里只是要取实虚部的值，所以要加const保证安全。（行13,14）
11. 接下来在class本体外实现复数的加法，先把函数首部除参数之外的东西写出来(complex::operator += )，然后再思考参数是什么？
+=，一定有个左边有个右边，由于该函数是一个成员函数，是作用在左边身上，所以左边就有一个隐藏的参数放进来，不知道在参数列表第几个位置，反正就是一定有，所以在参数里面就写右边就好。（行12）
12. 首先我们要考虑传引用，所以加&，那要不要加const？+=是左边动，右边不动，所以要加const。
然后考虑返回值类型，显而易见+=得到左边的是一个复数，所以是complex类型，那么要不要传引用？由于左边本来就存在，不是在函数里面才定义的，所以可以传引用。
由于这个函数是在class本体之外，为了提高效率，所以加个inline，至于最终会不会变成内联函数，这个就要看编译器
13. 然后补充完整函数主体（行22-33）
14. 然后我们多了三个加法，复数+复数，复数+实数，实数+复数，是在本体外，为什么？（行42-54）
如果放在class里面，就只能实现一种加法
15. 接着考虑输出功能，举个例子，要输出complex c1(9,8)。
这里可能需要<<操作符重载，操作符<<只能作用在左边，那么就意味着得是c1 << cout;为了避免这种情况，我们需要重载运算符，实现cout << c1;（行56-61）
16. 由于该重载函数不是成员函数，所以不需要complex::，由于该函数要实现cout << c1，所以左边是cout，左边参数就得是“cout类型”，查手册得cout属于class ostream（得包含头文件< iostream >，同理右边是complex。
然后考虑传引用，class complex和class ostream都是很大东西，传引用速度快。然后考虑要不要加const，右边作用于左边，右边肯定不变，加const，那么左边呢？
左边会改变，状态会改变，所以不能加const
17. 接着考虑返回值对于cout << c1这种情况，可以设置为void，但是对于cout << c1 << endl这种情况呢？
18. 当cout << c1执行完毕之后，应该传回一种想cout这种东西让<< endl继续执行，那cout是什么东西，ostream，所以返回值最好设置为ostream。ostream是本来就有的，所以可以传引用
19. 最后补全函数
```c++
本体
#ifndef _COMPLEX_
#define _COMPLEX_
class complex
{

public:
  complex (double r = 0, double i = 0)
  : re (r), im (i)
  { }

  complex& operator += (const complex&);
  double real const () { return re;} //函数完整，是inline
  double imag const () { return im;} //inline
private:
  double re, im;
  friend complex& _doapl (complex*, const complex);
}
#endif

本体外
inline complex&
_doapl(complex* ths, const complex & r) //左边变，右边不变，所以左边不要const
{
  ths->re += r.re;
  ths->im += r.im;
  return *ths;
}
inline complex&
complex::operator += (const complex & r)
{
  return _doapl (this, r); //让_doapl实现加法
}

inline complex //不能传引用
operator + (const complex& x, const complex& y)
{
  return complex(real(x) + real(y),
                imag(x) + imag(y)); //类名称+小括号创建临时对象
}//复数+复数

inline complex
operator + (const complex& x, const complex& y)
{
  return complex(real(x) + y,
                 imag(y));
}//复数+实数

inline complex
operator + (const complex& x, const complex& y)
{
  return complex(x + real(y),
                imag(y));
}//实数+复数

#include <iostream>
ostream&
operator << (ostream& os, const complex & x)
{
  return os << '(' << real(x) << imag(x) << ')';
}
```
