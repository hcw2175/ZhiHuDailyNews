# 知乎新闻 - 高仿知乎日报的Andorid客户端
---

## 温馨提示：本项目因历史久远，可能很多东西已不适合最新的Android SDK。同时因是当初学习 Android 使用，所以不会提供维护！感谢您的到来。

---

知乎新闻是本人仿知乎日报的练手项目，API来源其它Github项目。若有任何侵权行为，请及时通知我，我将立即删除本项目。

_练习项目，不接受合并请求，敬请原谅！_


## 特性
* UI完全仿知乎日报,尽量使用Android Material Design设计方案
* RecylcerView与CardView相结合，搭配出赏心悦目的列表卡片式布局，还可以上拉加载更多哦
* Google原生SwipeRefreshLayout下拉刷新，更显简洁
* Retrofit2 + OKHttp作为网络请求库，实现数据缓存，好用强大，网络请求不二之选
* 加入Rxjava异步请求库，远离回调地狱
* 使用Google推荐的Glide图片库进行图片加载,速度是杠杆的

## 预览图

![首页大图](http://oa5k1q7cb.bkt.clouddn.com/ZhihuDailyNews_Home.png?imageView2/0/w/500)


![按日期分组](http://oa5k1q7cb.bkt.clouddn.com/ZhihuDailyNews_DateGroup.png?imageView2/0/w/500)


![详情](http://oa5k1q7cb.bkt.clouddn.com/ZhihuDailyNews_Detail.png?imageView2/0/w/500)

## 更新日志
2016.07.14
> * 尝试加入MVP模式，解耦业务逻辑与UI操作，详见NewsContract
> * RecyclerView二次封装，增加下拉刷新、加载更多事件监听
> * RecyclerView Adapter二次封装，尽量精简代码，可添加Header、Footer

2016.07.13
> * 使用Glide替换universal-image-loader

2016.07.10
> * 首页列表卡片式布局，支持SwipeRefreshLayout下拉刷新，RecylcerView上拉加载历史消息
> * 首页图片轮播使用[RollViewPager](https://github.com/Jude95/RollViewPager)，支持标题显示、点击查看详情
> * 滚动并设置新闻日期到标题栏
> * 实现数据缓存，无网络时可访问数据
> * 优化图片缓存服务
> * 启动页图片获取、缓存显示
