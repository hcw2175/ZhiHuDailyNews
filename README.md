# 知乎新闻 - 高仿知乎日报的Andorid客户端
---

知乎新闻是本人仿知乎日报的练手项目，API来源其它Github项目。若有任何侵权行为，请及时通知我，我将立即删除本项目。

_练习项目，不接受合并请求，敬请原谅！_


## 特性
* UI完全仿知乎日报,尽量使用Android Material Design设计方案
* RecylcerView与CardView相结合，搭配出赏心悦目的列表卡片式布局，还可以上拉加载更多哦
* Google原生SwipeRefreshLayout下拉刷新，更显简洁
* Retrofit2 + OKHttp作为网络请求库，实现数据缓存，好用强大，网络请求不二之选
* 使用universal-image-loader实现图片懒加载、缓存，流量能省则省


## 预览图


## 更新日志
> * 首页列表卡片式布局，支持SwipeRefreshLayout下拉刷新，RecylcerView上拉加载历史消息
> * 首页图片轮播使用[RollViewPager](https://github.com/Jude95/RollViewPager)，支持标题显示、点击查看详情
> * 滚动并设置新闻日期到标题栏
> * 实现数据缓存，无网络时可访问数据
> * 优化图片缓存服务
> * 启动页图片获取、缓存显示