---
theme: gaia
_class: lead
paginate: true
backgroundColor: #fff
backgroundImage: url('https://marp.app/assets/hero-background.jpg')
---
## ES入门


---

### 引入
请说出带“前”字的诗句，很多人都不会立马回答出来
这是因为在我们脑子中建立的索引是这样的：
![h:250](https://i.loli.net/2020/07/13/fr3uVtORbqx7yWY.png)
- 根据诗名背出诗句就很快
- 但是回答上面那个问题，由于没有索引，只能遍历所有已知的诗句

---
### 倒排索引
![h:350](https://i.loli.net/2020/07/13/LKXxQgU1MoZfrVb.png)
- 这样，说出带"前"字的诗句就很容易
---

### 多个索引
很明显，上面的索引不一定非得要以“前”字为索引

![h:350](https://i.loli.net/2020/07/13/NTkuMbslgBedjRw.png)
- 这样的话，kv数据量可能会太多，可以压缩一下

---

### 数据量压缩
既然根据诗名可以定位到诗句，那么可以不用索引到诗句
![h:400](https://i.loli.net/2020/07/13/yzujOXmUl8ebtxQ.png)

---
### 多首诗（索引矩阵）
![h:420](https://i.loli.net/2020/07/13/xvlHi6RytYzQf2g.png)
- 根据一个“前”字就可以方便地找出所有带“前”字的诗了

---
### 搜索引擎
爬取内容 -> 进行分词 -> 建立倒排索引

![h:450](https://i.loli.net/2020/07/13/Cy1LQdlfY2gm8Ju.png)

---

![h:200](https://miro.medium.com/max/700/1*BmvPfSSm2G8C-khX1rhCGg.png)
- Elasticsearch 是一个开源的搜索引擎，建立在一个全文搜索引擎库 **Apache Lucene™** 基础之上，用它就可以方便地建立**倒排索引**
  - 但是 Lucene 仅仅只是一个库，可能需要获得信息检索学位才能了解其工作原理
  - ES通过隐藏 Lucene 的复杂性，取而代之的提供一套简单一致的 **RESTful API**

---

### ES != Lucene
- ES 不仅仅是 Lucene，并且也不仅仅只是一个全文搜索引擎
  - 一个分布式的**实时文档存储**，每个字段 可以被索引与搜索
  - 一个分布式**实时分析搜索引擎**
  - 能胜任上百个服务节点的扩展，并支持 PB 级别的结构化或者非结构化数据

---
### ES基本概念：索引、类型、文档
![h:200](https://i.loli.net/2020/07/13/urcRMBfhev1Y7S9.png)
- 类型是用来定义数据结构的，文档就是最终的数据
- 文档使用**JSON**作为序列化格式

---
![h:456](https://i.loli.net/2020/07/14/j2WlTfQ7g3zqU5R.png)

---
比如一首诗，有诗题、作者、朝代、字数、诗内容等字段，可以建立一个名叫 **Poems 的索引**，然后创建一个名叫 **Poem 的类型**
![h:450](https://i.loli.net/2020/07/13/GBWNyus92MVI1Hq.png)
- Keyword 与Text：前者不会分词，直接根据字符串内容建立倒排索引
---
### ES分布式原理
ES 会对数据进行切分，同时每一个分片会保存多个副本，为了保证分布式环境下的高可用
![h:400](https://i.loli.net/2020/07/13/vBzGfEnyFuTKL8h.png)

---
### 建立索引
![h:230](https://i.loli.net/2020/07/13/uWxo813GFkZdODc.png)
- 建立索引的请求先发到master, master建立完索引后，将集群状态同步至slave
- 只有建立索引和类型需要经过 Master，数据的写入有一个简单的 Routing 规则，**可以 Route 到集群中的任意节点**，所以数据写入压力是分散在整个集群的

---
### ES另外用途：ELK
Elasticsearch + Logstash（日志收集系统） + Kibana（数据可视化）
![](https://i.loli.net/2020/07/13/EturOmIebJ5AY8F.png)
系统运行过程中，突然出现了异常，在日志中就能及时反馈，日志进入 ELK 系统中，我们直接在 Kibana 就能看到日志情况。如果再接入一些实时计算模块，还能做实时报警功能