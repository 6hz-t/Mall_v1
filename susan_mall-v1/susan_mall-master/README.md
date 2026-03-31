⭐ 点右上角给一个 Star，鼓励技术人输出更多干货，爱了 ！

# susan_mall项目文档
<div align="left">
    <a href="https://www.zhihu.com/people/qing-jiao-wo-da-chu-2" target="_blank"> <img src="https://img.shields.io/badge/%E7%9F%A5%E4%B9%8E-%E6%BD%9C%E5%8A%9B%E7%AD%94%E4%B8%BB-ef8b00"></a>
    <a href="https://juejin.cn/user/465848661970824/posts" target="_blank"> <img src="https://img.shields.io/badge/%E6%8E%98%E9%87%91-%E4%BA%BA%E6%B0%94%E4%BD%9C%E8%80%85-ef8b00"></a>
    <a href="https://blog.csdn.net/lisu061714112" target="_blank"> <img src="https://img.shields.io/badge/CSDN-%E5%8D%9A%E5%AE%A2%E4%B8%93%E5%AE%B6-ef8b00"></a>
</div>

#### 项目介绍
susan_mall 是基于当前流行技术组合的前后端分离商城系统。

后端所使用的技术有：SpringBoot+Mybatis+SpringSecurity+jwt+Redis+ElasticSearch+RocketMQ+ShardingSphere+Nacos+geoip2+WebSocket+minio+hanlp+mahout+zxing+alipay+caffeine+guava+hutool等， 包含系统管理、通用管理、商品管理、购物管理、订单管理、营销管理、售后管理等功能。

#### 项目结构
susan-mall
- mall-common： 公共代码
- mall-business 业务模块
- mall-api -- 后端接口（后台管理系统接口 和 移动端接口）
- mall-job -- 定时任务 + MQ消费者 + WebSocket服务端

#### 系统截图
![系统截图](img%E5%95%86%E5%9F%8E%E7%B3%BB%E7%BB%9F%E5%8A%9F%E8%83%BD%E9%A1%B5%E9%9D%A21.png)

#### 系统架构图
![系统架构图](img%E5%95%86%E5%9F%8E%E6%9E%B6%E6%9E%84%E5%9B%BE.jpg)


#### 技术选型
![技术选型](img%E8%8B%8F%E4%B8%89%E5%95%86%E5%9F%8E%E4%BD%BF%E7%94%A8%E6%8A%80%E6%9C%AF.png)


| 技术       | 说明 |         官网 |
| :--------- | :--: | -----------: |
| SpringBoot     |  Web应用开发框架 |     https://spring.io/projects/spring-boot |
|SpringSecurity   |  认证和授权框架  |  https://spring.io/projects/spring-security |
|MyBatis | ORM框架 | http://www.mybatis.org/mybatis-3/zh/index.html |
| Elasticsearch | 搜索引擎 | https://github.com/elastic/elasticsearch |
| Redis | 缓存 |	https://redis.io/ |
| MongoDB |	文档数据库 |	https://www.mongodb.com |
| Druid	| 数据库连接池 |https://github.com/alibaba/druid |
| MinIO	| 对象存储 | https://github.com/minio/minio |
| JWT | JWT登录支持 | https://github.com/jwtk/jjwt |
| Lombok |	Java语言增强库 |	https://github.com/rzwitserloot/lombok |
| Hutool | Java工具类库	| https://github.com/looly/hutool |
| Hibernator-Validator | 验证框架 | http://hibernate.org/validator |


#### 开发工具
| 工具       | 说明 |         官网 |
| :--------- | :--: | -----------: |
| IDEA | 开发IDE | https://www.jetbrains.com/idea/download |
| RedisDesktop | redis客户端连接工具 | https://github.com/qishibo/AnotherRedisDesktopManager |
| Robomongo | mongoDB客户端连接工具 | https://robomongo.org/download |
| Navicat | 数据库连接工具	| http://www.formysql.com/xiazai.html |
| MindMaster | 思维导图工具 | http://www.edrawsoft.cn/mindmaster |
| Drawio |画流程图工具 |	https://draw.io/ |
| Postman | API接口调试工具 | https://www.postman.com/
| mdnice | Markdown编辑器 | https://editor.mdnice.com/

#### 开发环境

| 工具       | 说明 |         官网 |
| :--------- | :--: | -----------: |
| JDK |	8 | https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html |
| MySQL | 8	| https://www.mysql.com/ |
| Redis	| 6.0.16	| https://redis.io/download |
| MongoDB |	6.0.17	| https://www.mongodb.com/download-center |
| RocketMQ|	5.3.0 | http://www.rabbitmq.com/download.html
| Elasticsearch	| 7.17.23 | https://www.elastic.co/downloads/elasticsearch |
| Nacos	| 2021.0.4.0 | https://nacos.io/docs/latest/quickstart/quick-start/ |

#### 系统的亮点

![系统的亮点](img%E8%8B%8F%E4%B8%89%E5%95%86%E5%9F%8E%E7%9A%84%E9%A1%B9%E7%9B%AE%E4%BA%AE%E7%82%B9.png)


