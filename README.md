# 什么是OAuth2？

OAuth（开放授权）是一个开放标准，允许用户授权第三方移动应用访问他们存储在另外的服务提供者上的信息，而不需要将用户名和密码提供给第三方移动应用或分享他们数据的所有内容，OAuth2.0是OAuth协议的延续版本，但不向后兼容OAuth 1.0即完全废止了OAuth1.0。

**OAuth 2.0是目前最流行的授权机制，用来授权第三方应用，获取用户数据。**

**简单说，OAuth 就是一种授权机制。数据的所有者告诉系统，同意授权第三方应用进入系统，获取这些数据。系统从而产生一个短期的进入令牌（token），用来代替密码，供第三方应用使用。**

**也可以做微服务之间的认证授权。**

# 角色

1. 客户端

本身不存储资源，需要通过资源拥有者的授权去请求资源服务器的资源，比如：web浏览器、微信客户端等。

2. 资源拥有者

通常为用户，也可以是应用程序，即该资源的拥有者。

3. 授权服务器（也称认证服务器）

用来对资源拥有的身份进行认证、对访问资源进行授权。客户端要想访问资源需要通过认证服务器由资源拥有者授

权后方可访问。

4. 资源服务器

存储资源的服务器，比如，微博资源服务器存储微博用户的信息，存储微信的资源服务存储了微信的用户信息等。客户端最终访问资源服务器获取资源信息。

# OAuth2认证流程

OAuth2.0认证流程如下：

引自Oauth2.0协议rfc6749 https://tools.ietf.org/html/rfc6749

![](http://ww1.sinaimg.cn/large/006w8oYnly1g608s3u3u4j313o0p6djy.jpg)



第三方平台（QQ、新浪微博、微信…）登录就是OAuth2的例子





# 标准的授权模式

**所有的客户端必须在授权服务器先备案**

Oauth2有以下授权模式：

- 授权码模式（Authorization Code） 
- 隐式授权模式（Implicit）
- 密码模式（Resource Owner Password Credentials）
- 客户端模式（Client Credentials）

## 授权码授权流程

**授权码（authorization code）方式，指的是第三方应用先申请一个授权码，然后再用该码获取令牌。**

**这种方式是最常用的流程，安全性也最高，它适用于那些有后端的 Web 应用。授权码通过前端传送，令牌则是储存在后端，而且所有与资源服务器的通信都在后端完成。这样的前后端分离，可以避免令牌泄漏。**



授权码模式认证过程：

![](http://ww1.sinaimg.cn/large/006w8oYnly1g608t3w55ej317q0qytha.jpg)



1. 客户端请求第三方授权

用户进入第三方客户端的登录页面，点击微信的图标以微信账号登录系统，用户是自己在微信里信息的资源拥有者。

2. 资源拥有者同意给客户端授权

资源拥同意给客户端授权，微信会对资源拥有者的身份进行验证， 验证通过后，微信会询问用户是否给授权第三方客户端访问自己的微信数据，用户点击“确认”表示同意授权，微信认证服务器会颁发一个授权码，并重定向指定的网站。

3. 客户端获取到授权码，请求认证服务器申请令牌

此过程用户看不到，客户端应用程序请求认证服务器，请求携带授权码。

4. 认证服务器向客户端响应令牌

认证服务器验证了客户端请求的授权码，如果合法则给客户端颁发令牌，令牌是客户端访问资源的通行证。

此交互过程用户看不到。

5. 客户端服务器请求资源服务器的资源

客户端携带令牌访问资源服务器的资源，携带令牌请求访问微信服务器获取用户的基本信息。

6. 资源服务器返回受保护资源

资源服务器校验令牌的合法性，如果合法则向用户响应资源信息内容。

注意：资源服务器和认证服务器可以是一个服务也可以分开的服务，如果是分开的服务资源服务器通常要请求认证

服务器来校验令牌的合法性。



### 获取授权码：

```text
http://localhost:8080/oauth/authorize?client_id=dev&response_type=code&scop=app&redirect_uri=http://www.baidu.com
```

参数说明：

| 参数          | 是否必须 | 说明                                                         |
| :------------ | :------: | :----------------------------------------------------------- |
| client_id     |    是    | 客户端id                                                     |
| redirect_uri  |    是    | 授权后重定向的回调链接地址， 请使用 urlEncode 对链接进行处理 |
| response_type |    是    | 返回类型，请填写code                                         |
| scope         |    是    | 表示要求的授权范围                                           |
| state         |    否    | 重定向后会带上state参数                                      |

当授权码申请成功后会跳转到此地址，并在`redirect_uri`后边带上code参数（授权码）

### 申请令牌

拿到授权码后，申请令牌。

Post ：`http://localhost:8080/oauth/token`

参数说明：

| 参数          | 是否必须 | 说明                                                         |
| :------------ | :------: | :----------------------------------------------------------- |
| grant_type    |    是    | 授权类型，填写authorization_code，表示授权码模式             |
| code          |    是    | 刚刚获取的授权码，注意：授权码只使用一次就无效了，需要重新申请 |
| redirect_uri  |    是    | 申请授权码时的跳转uri                                        |
| client_id     |    是    | 客户端id                                                     |
| client_secret |    是    | 客户端secret                                                 |

返回值：

```json
{
    "access_token": "4a056148-b4ae-48ed-acbc-65c55f657c5a",
    "token_type": "bearer",
    "refresh_token": "34a5e800-466a-4e6c-911f-adf6f9de4ffa",
    "expires_in": 3599,
    "scope": "app"
}
```

- access_token：访问令牌，携带此令牌访问资源
- token_type：有MAC Token与Bearer Token两种类型，两种的校验算法不同，RFC 6750建议OAuth2采用 Bearer Token（http://www.rfcreader.com/#rfc6750)
- refresh_token：刷新令牌，使用此令牌可以延长访问令牌的过期时间
- expires_in：过期时间，单位为秒
- scope：范围



### 资源服务授权

1、客户端请求认证服务申请令牌

2、认证服务生成令牌

3、客户端携带令牌访问资源服务

客户端在Http header 中添加： Authorization：Bearer 令牌。

4、资源服务请求认证服务校验令牌的有效性

资源服务接收到令牌，使用公钥校验令牌的合法性。

5、令牌有效，资源服务向客户端响应资源信息



## 密码授权流程

**如果你高度信任某个应用，也允许用户把用户名和密码，直接告诉该应用。该应用就使用你的密码，申请令牌，这种方式称为"密码式"（password）。**

**用户完全信任客户端，直接把密码给客户端**

1. 客户端要求用户资源服务器的用户名和密码。拿到以后，客户端就直接向授权服务器请求令牌。
2. 授权服务器验证身份通过后，直接给出令牌。注意，这时不需要跳转，而是把令牌放在 `JSON` 数据里面，作为 HTTP 回应，客户端直接拿到令牌。

这种方式需要用户给出自己的用户名/密码，显然风险很大，因此只适用于其他授权方式都无法采用的情况，而且必须是用户高度信任的应用。

![](http://ww1.sinaimg.cn/large/006w8oYnly1g608u3mar3j31740r6n58.jpg)



### 申请令牌

post请求：`http://localhost:8080/oauth/token`

参数说明：

| 参数          | 是否必须 | 说明                                 |
| :------------ | :------: | :----------------------------------- |
| grant_type    |    是    | 授权类型，填写password，表示密码模式 |
| username      |    是    | 用户名                               |
| password      |    是    | 密码                                 |
| client_id     |    是    | 客户端id                             |
| client_secret |    是    | 客户端secret                         |

返回值：

```json
{
    "access_token": "d5a5aad6-473f-42f9-b8e8-f55efe5de927",
    "token_type": "bearer",
    "refresh_token": "d0c98715-e279-46f4-a7e4-72d6e2367798",
    "expires_in": 7146,
    "scope": "app"
}
```



## 隐式授权模式 

有些 Web 应用是纯前端应用，没有后端。这时就不能用上面的方式了，必须将令牌储存在前端。**允许直接向前端颁发令牌。这种方式没有授权码这个中间步骤，所以称为（授权码）"隐藏式"（implicit）。**

### 申请令牌

get请求：`http://localhost:8080/oauth/authorize?client_id=dev&response_type=token&scop=app&redirect_uri=http://www.baidu.com`

参数说明：

| 参数          | 是否必须 | 说明                                                         |
| :------------ | :------: | :----------------------------------------------------------- |
| response_type |    是    | token                                                        |
| redirect_uri  |    是    | 授权后重定向的回调链接地址， 请使用 urlEncode 对链接进行处理 |
| scope         |    是    | 表示要求的授权范围                                           |
| client_id     |    是    | 客户端id                                                     |

登录后同意给予 A 网站授权。这时，B 网站就会跳回`redirect_uri`参数指定的跳转网址，并且把令牌作为 URL 参数，传给客户端。

```
https://www.baidu.com/#access_token=d5a5aad6-473f-42f9-b8e8-f55efe5de927&token_type=bearer&expires_in=6880&scope=app
```

注意，令牌的位置是 URL 锚点（fragment），而不是查询字符串（querystring），这是因为 OAuth 2.0 允许跳转网址是 HTTP 协议，因此存在"中间人攻击"的风险，而浏览器跳转时，锚点不会发到服务器，就减少了泄漏令牌的风险。

## 客户端凭证授权

**适用于没有前端的命令行应用，即在命令行下请求令牌。**

**服务器和服务器之间的交互**

![](http://ww1.sinaimg.cn/large/006w8oYnly1g608v1di9wj318e0ngagr.jpg)

1. 向认证服务器发送请求
2. 认证服务器通过以后，直接返回令牌

参数说明：

| 参数          | 是否必须 | 说明               |
| :------------ | :------: | :----------------- |
| grant_type    |    是    | client_credentials |
| client_secret |    是    | 客户端秘钥         |
| client_id     |    是    | 客户端id           |



#  更新令牌

令牌的有效期到了，如果让用户重新走一遍上面的流程，再申请一个新的令牌，很可能体验不好，而且也没有必要。OAuth 2.0 允许用户自动更新令牌。

```
post http://localhost:8080/oauth/token
```

参数说明：

| 参数          | 是否必须 | 说明               |
| :------------ | :------: | :----------------- |
| grant_type    |    是    | client_credentials |
| client_secret |    是    | 客户端秘钥         |
| client_id     |    是    | 客户端id           |
| refresh_token |    是    | 刷新token          |



# 四种授权模式的特点

## 密码模式（为遗留系统设计）

- 这种模式是最不推荐的，因为client可能存了用户密码
- 这种模式主要用来做遗留项目升级为oauth2的适配方案
- 当然如果client是自家的应用，也是可以
- 支持refresh token

## 授权码模式（正宗方式）

- 这种模式算是正宗的oauth2的授权模式
- 设计了auth code，通过这个code再获取token
- 支持refresh token

## 简化模式（为web浏览器应用设计）

- 这种模式比授权码模式少了code环节，回调url直接携带token
- 这种模式的使用场景是基于浏览器的应用
- 这种模式基于安全性考虑，建议把token时效设置短一些
- 不支持refresh token

## 客户端模式（为后台api服务消费者设计）

- 这种模式直接根据client的id和密钥即可获取token，无需用户参与
- 这种模式比较合适消费api的后端服务，比如拉取一组用户信息等
- 不支持refresh token，主要是没有必要



# 授权模式的选择

![](http://ww1.sinaimg.cn/large/006w8oYnly1g608vpt9njj30qw0gjdpd.jpg)



# Wechat OAuth2



先备案客户端详情：

![](http://ww1.sinaimg.cn/large/006w8oYnly1g608x2lp2nj316m0pcjvi.jpg)

一共两种授权模式：

## 服务器端

文档地址：https://developers.weixin.qq.com/doc/offiaccount/Basic_Information/Get_access_token.html

![](http://ww1.sinaimg.cn/large/006w8oYnly1g608xrhv6mj315810ogs2.jpg)



**微信服务端认证模式为<font color=red>客户端凭证授权</font>**

获取用户信息：

```
接口调用请求说明
http请求方式: GET
https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN
```



## 网页授权

文档地址：https://developers.weixin.qq.com/doc/offiaccount/OA_Web_Apps/Wechat_webpage_authorization.html



![](http://ww1.sinaimg.cn/large/006w8oYnly1g608yg8oafj318g122n5c.jpg)

**微信网页授权为<font color=red>授权码授权</font>**



## 发起授权申请：

```
https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxe89488e21d961fe0&redirect_uri=https%3A%2F%2Flive.photoplus.cn%2Fpage3%2Fhome%2Fme%3FuniqCode%3D74sHxlYGqE%26state%3DSTATE&response_type=code&scope=snsapi_userinfo&state=STATE&uin=MTk5MjAyMzAyMA%3D%3D&key=62678e6ed297ddf47f16ef23db631a621d0958173c5d9171df11a3c31a036463727f42fc691d71832a1eba0d420d2c6fd1cc4e1569a79fa783f387bf1b18ab5db83db00d4fec3a2b19deaf56a1c85d7d&version=62060841&pass_ticket=%2BSVWYpsBBrmu3lDdI2xpjb17hkoRSM2%2BAn4wTzs5bFc%2B%2FMq8MW0AufS48BDAoyjc
```



![](http://ww1.sinaimg.cn/large/006w8oYnly1g60bfid29mj31fe0qyae6.jpg)



![](http://ww1.sinaimg.cn/large/006w8oYnly1g60bj4mykuj31f80so0x8.jpg)





<img height="500" src="http://ww1.sinaimg.cn/large/006w8oYnly1g60bkmwoszj30l40oimy6.jpg"/>



## 同意授权

```
https://open.weixin.qq.com/connect/oauth2/authorize_reply
```

## redirect_uri携带code

```
https://live.photoplus.cn/page3/home/me?uniqCode=74sHxlYGqE&state=STATE&code=001oS0Ci2fHmfB0AbKCi2N73Ci2oS0Cs&state=STATE
```





# OAuth体系

## 应用体系结构

- 应用注册

应用注册后，OAuth2会下发应用app_id和app_secret，用以标记该应用的唯一性，并且这两个参数将贯穿整个OAuth协议，用以对应用合法性进行校验。同时，应用需要提供redirect_uri，用以和平台进行异步交互，获取用户令牌及错误信息。

- 授权/鉴权中心

1. 对用户的应户名、密码进行鉴权
2. 对第三方应用的app_id,app_secret进行鉴权
3. 展示授权界面，并对用户对第三方应用的授权操作进行响应
4. 对用户授权码及用户token的真实性进行鉴权

- access_token管理

1. 创建token 刷新token
2. 查询token详细数据
3. 校验token时效性

## 核心功能

- 平台门户

门户应用入住界面，需要集成OAuth2应用创建接口，录入第三方回调地址，并回显app_id和app_secret参数，平台门户负责向第三方展示用于进行业务及技术集成的管理界面，至少包含以下几个功能

1. 服务商入住(第三方合作伙伴入住
2. 应用配置(第三方应用管理
3. 权限申请(一般包括接口权限和字段权限)
4. 运维中心(开放平台当前服务器、接口状态，服务商接口告警等)
5. 帮助中心(入住流程说明，快速接入说明，API文档等

- 鉴权服务

鉴权服务集成OAuth2的authorize及token接口，提供用户授权及code/token鉴权功能，负责整个平台的安全性

1. 接口调用鉴权(第三方合作伙伴是否有权限调用某接口
2. 用户授权管理(用户对某个第三方应用获取改用户信息的权限管理)
3. 用户鉴权(平台用户的鉴权)
4. 应用鉴权(第三方合作伙伴的应用是否有权调用该平台)

- 开放接口

开放接口需集成OAuth2的resource server角色，对用户数据进行安全管理，对第三方应用发起的请求做出响应，并对token进行真实性校验

1. 平台用户接口(用于获取公司APP生态链中的用户信息
2. 平台数据接口(平台中的一些开放数据)
3. 其它业务接口(平台开放的一些业务数据)

- 运营系统

运营系统是整个平台的后台业务管理系统
对当前OAuth2应用的管理功能
对用户授权列表管理，用户token管理等OAuth2协议相关管理功能。
对第三方合作伙伴提出的各种申请进行审核操作
对当前应用的操作进行审计工作
对当前业务健康度进行监控等

1. 服务商管理(对第三方合作伙伴的资质进行审核、操作)
2. 应用管理(对第三方应用进行审核、上下线管理)
3. 权限管理（对合作伙伴申请的资源进行审核、操作）
4. 统计分析(监控平台当前运行状态，统计平台业务数据)

## 搭建开放平台的意义

- 搭建基于API的生态体系
- 利用开放平台，搭建基于计费的API数据平台
- 为APP端提供统一接口管控平台，类似于网关的概念
- 为第三方合作伙伴的业务对接提供授信可控的技术对接平台



# OAuth2在供应链平台中的应用

- 服务器之间使用`客户端凭证`授权模式
- todo H5 





# Spring Security OAuth2认证解决方案

Spring Security + OAuth2 + JWT

略