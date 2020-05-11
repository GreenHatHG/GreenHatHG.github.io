---
title: MOOC学习python爬虫学习之Requests
date: 2018-09-23 19:54:28
categories: python
tags: 
- python
- 爬虫
- MOOC
---

Python-Requests库

<!-- more -->

# 安装

```python
pip3 install requests
# python3 deepin15.6
```

# Requests库的7个主要办法

| requests.request() | 构造一个请求，支撑以下各方法的基础方法         |
| ------------------ | ---------------------------------------------- |
| requests.get()     | 获取HTML网页的主要方法，对应于HTTP的GET        |
| requests.head()    | 获取HTML网页头信息的方法，对应于HTTP的HEAD     |
| requests.post()    | 向HTML网页提交POST请求的方法，对应于HTTP的POST |
| requests.put()     | 向HTML网页提交PUT请求的方法，对应于HTTP的PUT   |
| requests.patch()   | 向HTML网页提交局部修改请求，对应于HTTP的PATCH  |
| requests.delete()  | 向HTML页面提交删除请求，对应于HTTP的DELETE     |

# request.get()

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/python-requests.png">

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/python-requests2.png">

# requests.head()

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/python-requests-gead.png">

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/python-requests-head.png">

*HEAD获取头部信息，没有html主体，故r.text为空*‘

# requests.post()

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/python-post.png">

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/python-requests-post.png">

# requests.put()

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/python-put.png">

# requests.patch()

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/python-patch.png">

# requests.delete()

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/python-delete.png">

# Response对象

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/python-requests3.png">

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/python-Response.png">

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/python-response.png">

# Requests库的异常

 <img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/python-requests%E5%BC%82%E5%B8%B8.png">

```python
r.raise_for_status() #如果不是200，产生异常requests.HTTPError
```

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/python%E7%88%AC%E5%8F%96%E7%BD%91%E9%A1%B5%E9%80%9A%E7%94%A8%E6%A1%86%E6%9E%B6.png">

# Http协议对资源的操作

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/python-http%E5%AF%B9%E5%BF%97%E6%84%BF%E7%9A%84%E6%93%8D%E4%BD%9C.png">

# resquests.request()

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/python-request.png">

**kwargs参数讲解**

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/python-requests-request.png">

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/python-requests-request2.png">

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/python-requests-request3.png">

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/python-requests-request4.png">

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/python-requests-request5.png">

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/python-requests-request6.png">

 <img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/python-requests-request7.png">

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/python-requests-request8.png">

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/python-requests-request9.png">

