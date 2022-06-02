# MyShow
android综合设计项目
### 图片分享APP

#### 登录界面

##### 注册

>检验账号是否存在，
>
>检测两次密码是否一致

```
{
	"id":用户昵称,
    "username": email/phonenumber,
    "password":8-16位密码
}
```

#####  登录

>检测账号密码是否合法

**数据格式:json** 

```json
{
    "username": email/phonenumber,
    "password":8-16位密码
}
```



#### 首页

> 推送图片显示（基础）
>
> 上传图片（基础）
>
> 转发图片和保存图片（基础） 
>
> 点赞
>
> 评论
>
> 关注

**图片信息数据格式**

```json
{
    "content":"内容",
    "id":图片唯一标识,
    "pUserld": "发布者名字",
    "time":"上传时间",
    "imageCode":"图片",
    "title":"标题"
}
```

#### 用户主页

 >用户信息

**数据格式**

```json
{
    "username":账号,
    "id":用户昵称,
    "avatar":用户头像,
    "sex":性别,
    "introduce":介绍
    
}
```

>图片收藏列表
>
>关注列表



### 项目开发第一阶段

>1.设计实现基本功能
>
>2.设计实现数据库
>
>2.测试功能完整性

### 项目开发第二阶段

>1.完善功能
>
>2.测试功能

### 项目开发第三阶段

>优化布局

### 更新

类：

![image-20220602230836437](H:\AndroidStudioProject\MyShow\image-20220602230836437.png)

Contants：静态类

用于定义常用变量以及函数如请求头

MainActivity：

主页面渲染界面，在该页面实现各种功能

user类：

用于定义用户的各种属性

mImage类：

用于定义图片的各种属性

Imagelist：

用于接收图片列表

ViewPagerFragmentAdapter：

在viewPage中嵌套Fragment所定义的适配器，用于切换不同的fragment

Fragment：

fragment是一种更灵活的页面，项目的所有的页面渲染均由fragment完成。

