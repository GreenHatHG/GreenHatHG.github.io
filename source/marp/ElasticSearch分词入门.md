---
theme: gaia
_class: lead
paginate: true
backgroundColor: #fff
backgroundImage: url('https://marp.app/assets/hero-background.jpg')
---
## ES分词入门


---

### 概述
- **分词**和**倒排索引**为ES的强大搜索能力提供了重要的支撑
- ES有很多分析器，用于**将一块文本分成适合于倒排索引的独立的词条**
  - 之后，将这些词条统一化为标准格式以提高它们的“可搜索性”
- 每一种分析器都有一种不同的文本分析方法

---
### 分析器
分析器内部是一个小的处理管道，包括以下几个阶段：
字符过滤（可选）、分词、词条过滤

![](https://api.contentstack.io/v2/assets/575e4c8c3dc542cb38c08267/download?uid=blt51e787daed39eae9?uid=blt51e787daed39eae9)

1. 字符串按顺序通过每个字符过滤器 。他们的任务是在**分词前整理字符串**。一个字符过滤器可以用来去掉HTML，或者将 & 转化成 and
2. 其次，字符串被**分词器**分为单个的词条
3. 最后，词条按顺序通过每个**token 过滤器**。这个过程可能会改变词条（例如，小写化 Quick ），删除词条（例如， 像 a， and等无用词）

---
### 示例
![h:550](https://qbox.io/img/blog/Tokenization.png)

---
### 配置分析器
ES自带了一些默认分析器，可以配置已存在的分析器或针对索引创建新的自定义分析器
![](https://i.loli.net/2020/07/14/bD3HoCrOu1hJxwY.png)

---
### 常见的中文分析器（仅随机挑两款）
- 开源分词工具
  - jieba
  - snownlp
- 各大院校NLP项目
  - 北大pkuseg
  - 清华THULAC
- 大厂NLP项目
  - 腾讯文智
  - 阿里云NLP