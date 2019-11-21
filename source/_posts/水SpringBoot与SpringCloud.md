---
title: 水SpringBoot与SpringCloud
date: 2019-11-21 21:33:54
categories: 悠闲谈
tags:
- Java
- SpringBoot
- SpringCloud
---

近几个月来水项目有感

<!-- more -->

# SpringCloud自动部署问题

当我们使用`SpringCloud`的时候，意味着我们一个项目会有很多个服务，每个项目都是打包部署的，而我们可能会有几百个服务，所以要是像我们之前使用`SpringBoot`手动打包项目成`Jar`，然后再手动上传到服务器那样的话，那么肯定会累si。所以当我们这个很简单版的`SpringCloud`项目完成后，我就考虑如何去部署它。

既然这么多项目需要打包了，那么直觉就可以告诉我们要上自动部署了，所以这几天一直都在写那个自动部署的脚本。回归正题，其实这东西现在看来是一个`CI/CD`的过程，`CI/CD`又关系到`devops`，之前对这个词有点模糊，现在可以趁机补习一下。

## CI/CD与devops

其实，`CI`的全称是`continuous integration`，`CD`的全称是`continuous delivery`

翻译过来分别是**持续集成**和**持续交付**

那么`devops`呢

> DevOps is a set of practices that combines software development (Dev) and information-technology operations (Ops) which aims to shorten the systems development life cycle and provide continuous delivery with high software quality.
>
> [DevOps - Wikipedia](https://en.wikipedia.org/wiki/DevOps#Definition)

> DevOps（Development和Operations的组合词）是一组过程、方法与系统的统称，用于促进开发（应用程序/软件工程）、技术运营和质量保障（QA）部门之间的沟通、协作与整合。
>
> [devops_百度百科](https://baike.baidu.com/item/devops/2613029)

baba什么的，其实我感觉它的作用应该就是让软件更快更好地交付给客户使用，所以它们的关系应该是

**`CI`与`CD`是`devops`的最佳实践之一，可以更频繁更可靠得交付**

更多可点下面链接了解

[What is CI/CD? Continuous integration and continuous delivery explained | InfoWorld](https://www.infoworld.com/article/3271126/what-is-cicd-continuous-integration-and-continuous-delivery-explained.html)

[如何从零开始搭建 CI/CD 流水线-InfoQ](https://www.infoq.cn/article/WHt0wFMDRrBU-dtkh1Xp)

**总结**

> DevOps 是一种软件开发方法。它将持续开发、持续测试、持续集成、持续部署和持续监控贯穿于软件开发的整个生命周期
>
> CI/CD可以将它们看作是类似于软件开发生命周期的过程

