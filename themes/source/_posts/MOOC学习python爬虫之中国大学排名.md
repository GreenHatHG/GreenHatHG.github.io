---
title: MOOC学习python爬虫之中国大学排名
date: 2018-09-25 10:02:04
categories: python
tags: 
- python
- 爬虫
- MOOC
---

定向爬虫

<!-- more -->

# 功能描述

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/python-%E7%88%AC%E5%8F%96%E4%B8%AD%E5%9B%BD%E5%A4%A7%E5%AD%A6.png">

**还得确定排名信息是不是写在HTML里面**

# 程序设计

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/python-%E7%88%AC%E5%8F%96%E4%B8%AD%E5%9B%BD%E5%A4%A7%E5%AD%A62.png">

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/python-%E7%88%AC%E5%8F%96%E4%B8%AD%E5%9B%BD%E5%A4%A7%E5%AD%A63.png">

# 实例代码

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/python-%E7%88%AC%E5%8F%96%E4%B8%AD%E5%9B%BD%E5%A4%A7%E5%AD%A64.png">

```python
import requests
from bs4 import BeautifulSoup
import bs4
def getHTMLTest(url):
    try:
        r = requests.get(url)
        r.raise_for_status()
        r.encoding = r.apparent_encoding
        return r.text
    except:
        return ""

def fillUnivList(ulist, html):
    soup = BeautifulSoup(html, "html.parser")
    for tr in soup.find('tbody').children:
        if isinstance(tr, bs4.element.Tag):
            tds = tr('td')
            ulist.append([tds[0].string, tds[1].string, tds[2].string, tds[3].string])

def printUnivLIst(ulist, num):
    tplt = "{0:^10}\t{1:^10}\t{2:{4}^10}\t{3:^10}"
    print(tplt.format("排名", "学校名称", "地区", "分数", chr(12288)))
    for i in range(num):
        u = ulist[i]
        print(tplt.format(u[0], u[1], u[2], u[3], chr(12288)))

def main():
    uinfo = []
    url = 'http://www.zuihaodaxue.cn/zuihaodaxuepaiming2018.html'
    html = getHTMLTest(url)
    fillUnivList(uinfo, html)
    printUnivLIst(uinfo, 20)
main()
```

<img src="https://raw.githubusercontent.com/GreenHatHG/blog_image/master/python-%E7%88%AC%E5%8F%96%E4%B8%AD%E5%9B%BD%E5%A4%A7%E5%AD%A65.png">



