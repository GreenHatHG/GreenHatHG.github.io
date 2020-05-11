---
title: c++小总结
date: 2018-02-17 21:28:51
categories: c++
---
构造函数不能共存例子、构造函数放在private、const在函数前与后、相同class的各个objects互为友元、设计一个类注意事项、操作符重载、new与delete、static、继承，复合，委托
<!-- more -->


# 1. 构造函数不能共存例子

```c++
（1）
complex (double r = 0, double i = 0)
  : re(r), im(i)
{ }

（2）
complex() : re(0), im(0) { }
```

当使用complex c1;或complex c2();时，两种创建办法都没有参数，编译器可以调用（1），对于（2），虽然函数有参数，但是有默认值，也可以调用，所以会冲突，不能共存。

# 2. 构造函数放在private

一般我们不把构造函数放在private里面，因为这样不能被外界调用。其实也有特殊情况，下面列举一种，Meyers Singleton设计模式

```c++
class A
{
public:
  static A& getInstance();
  setup() {...}
private:
  A();	//构造函数
  A(const A& rhs);	//构造函数
  ...
};

A& A::getInstance()
{
  static A a;
  return a;
}

调用：A::getInstance.setup(); //通过函数来得到唯一的一份
```

# 3. const在函数前与后

- 概念：当const在函数名前面的时候修饰的是函数返回值，在函数名后面表示是常成员函数，该函数不能修改对象内的任何成员，只能发生读操作，不能发生写操作。
- 原理：我们都知道在调用成员函数的时候编译器会将对象自身的地址作为隐藏参数传递给函数，在const成员函数中，既不能改变this所指向的对象，也不能改变this所保存的地址，this的类型是一个指向const类型对象的const指针。
- 使用const关键字进行说明的成员函数，称为常成员函数。只有常成员函数才有资格操作常量或常对象，没有使用const关键字说明的成员函数不能用来操作常对象。
- const对象只能调用const成员函数。在const函数中调用非const成员函数是语法错误 。

# 4. 相同class的各个objects互为友元

```c++
class complex
{
public:
   ....
   int fun(const complex& param)
   {
    return param.re + param.im; //直接可以拿封装里面数据
   }
private:
  double re, double im;
}
```

# 5. 设计一个类注意事项

- 数据尽量放在private
- 参数和返回值尽量通过引用传递
- 考虑const
- 构造函数特殊初始化语法

# 6. 操作符重载 

- 成员函数（有this）：

</div>

<div align="center">

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/%E6%93%8D%E4%BD%9C%E7%AC%A6%E9%87%8D%E8%BD%BD.png" width="80%" height="80%">

</div>

- 非成员函数（无this）：

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/%E6%93%8D%E4%BD%9C%E7%AC%A6%E9%87%8D%E8%BD%BD2.png" width="90%" height="90%">

*注：typename()，创建临时对象，如complex( real (x) + y, imag (x) );*

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/%E6%93%8D%E4%BD%9C%E7%AC%A6%E9%87%8D%E8%BD%BD3.png" width="90%" height="90%">



- 取反：

```c++
inline complex
operator + (const complex x)
{
  return x;
}

inline complex
operator - (const complex& x)
{
  return complex(-real (x), -imag(y));	//临时对象
}

//调用
{
  complex c1(2, 1);
  complex c2;
  cout << -c1;
  cout << +c2;
}
```



# 7. new与delete

- new：先分配memory，再调用ctor。

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/new%E4%B8%8Edelete.jpg" width="90%" height="90%">



- delete：先调用dtor，再调用memory

  <img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/new%E4%B8%8Edelete2.png" width="100%" height="100%">

  ​


# 8. static

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/Static.png" width="100%" height="100%">



- 在没有使用static的情况下，要使用complex创建n个对象，就是在内存里面创建n个非静态数据。函数只有一个，但是要处理n个数据，靠的是this pointer指向数据来引导函数要去处理哪个数据。
- 使用static函数和对象后，函数同样有一份，但是静态数据只有一份。静态函数没有this pointer，可见静态函数只能去处理静态数据。

例如：一个银行中，账户可能有很多个，不必用static，但是利率只有一份，可以用static。

```c++
class account
{
  public:
  	static double rate; //利率
  	static void set_rate(const double& x) { rete = x;} //设置利率
};
int main()
{
  /*
  调用static函数的方式有二种：
  1. 通过函数调用；
  2. 通过类名调用；
  */
  account::set_rate(5.0); //(1)

  account a;
  a.set_rate(7.0); //(2)
}
```

# 9. 继承，复合，委托

## 1. 复合(has-a)

```c++
template <class T, class Sequence = deque<T> >
  class queue
  {
    ...
    protected:
    	Sequence c;	//底层容器，复合
    public:
    	bool empty() const { return c.empty(); }
    	size_type size() cosnt const { return c.size(); }
    	....
  };
```

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/%E5%A4%8D%E5%90%88.png" width="100%" height="100%">

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/%E5%A4%8D%E5%90%882.png">

*deque和queue同时出现同时消失*

## 2. 委托

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/%E5%A7%94%E6%89%98.png">

*StringRep与String没有共存关系*

## 3. 继承(is-a)

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/%E7%BB%A7%E6%89%BF.png">

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/%E7%BB%A7%E6%89%BF2.png">
