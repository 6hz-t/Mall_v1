# 商城（Mall-v1）项目文档



---

## 一、项目介绍

**商城（Mall-v1）** 是基于当前流行技术组合的前后端分离商城系统，包含 **Web 管理后台**、**移动端（UniApp）** 和 **后端服务** 三大部分。

- **后端**：SpringBoot + MyBatis + SpringSecurity + JWT + Redis + Elasticsearch + RocketMQ + ShardingSphere + Nacos + MinIO 等
- **Web 管理后台**：Vue 2 + Element UI
- **移动端**：UniApp（支持微信小程序、H5、App 等多端发布）

系统包含系统管理、通用管理、商品管理、购物管理、订单管理、营销管理、售后管理等功能模块。

---

## 二、项目结构

```
mall-v1/
├── mall-v1/                        # 后端项目
│   └── susan_mall-master/
│       ├── mall-common/            # 公共模块（工具类、常量、基础配置等）
│       ├── mall-business/          # 业务逻辑层（核心业务服务）
│       ├── mall-mgt/               # 后台管理系统（Controller 层）
│       ├── mall-job/               # 定时任务 + MQ 消费者 + WebSocket 服务端
│       └── sql/                    # 数据库脚本
│
├── mall_v1_web/                    # Web 管理后台前端
│   └── susan_mall_web-master/
│       ├── src/
│       │   ├── api/                # API 接口
│       │   ├── components/         # 公共组件
│       │   ├── layout/             # 布局
│       │   ├── router/             # 路由配置
│       │   ├── store/              # 状态管理
│       │   ├── utils/              # 工具类
│       │   └── views/              # 页面
│       └── package.json
│
└── mall_v1_uni/                    # 移动端（UniApp）
    └── susan_mall_uni-master/
        ├── pages/                  # 页面
        ├── components/             # 组件
        ├── common/                 # 公共代码
        ├── static/                 # 静态资源
        ├── utils/                  # 工具类
        └── uni_modules/            # UniApp 模块
```

---

## 三、技术选型

### 3.1 后端技术栈

| 技术 | 说明 | 官网 |
| :--- | :--- | :--- |
| SpringBoot 2.7.18 | Web 应用开发框架 | https://spring.io/projects/spring-boot |
| SpringSecurity | 认证和授权框架 | https://spring.io/projects/spring-security |
| MyBatis | ORM 框架 | http://www.mybatis.org/mybatis-3/zh/index.html |
| Elasticsearch 7.17.23 | 搜索引擎 | https://github.com/elastic/elasticsearch |
| Redis 6.0.16 | 缓存 | https://redis.io/ |
| MongoDB 6.0.17 | 文档数据库 | https://www.mongodb.com |
| RocketMQ 5.3.0 | 消息队列 | https://rocketmq.apache.org/ |
| Nacos 2021.0.4.0 | 服务注册与配置中心 | https://nacos.io/ |
| ShardingSphere 4.1.1 | 数据库分库分表 | https://shardingsphere.apache.org/ |
| MinIO 7.1.0 | 对象存储 | https://github.com/minio/minio |
| JWT 0.10.6 | JWT 登录支持 | https://github.com/jwtk/jjwt |
| Druid 1.2.16 | 数据库连接池 | https://github.com/alibaba/druid |
| Caffeine | 本地缓存 | https://github.com/ben-manes/caffeine |
| Guava 20.0 | Google 工具库 | https://github.com/google/guava |
| Hutool 5.8.4 | Java 工具类库 | https://github.com/looly/hutool |
| HanLP 1.6.8 | 中文分词 | https://hanlp.hankcs.com/ |
| Mahout 0.12.2 | 推荐算法 | https://mahout.apache.org/ |
| ZXing 3.4.1 | 二维码生成 | https://github.com/zxing/zxing |
| 支付宝 SDK 2.2.0 | 支付宝支付 | https://opendocs.alipay.com/ |
| MinIO 7.1.0 | 文件存储 | https://min.io/ |
| WebSocket | 实时通信 | https://developer.mozilla.org/zh-CN/docs/Web/API/WebSocket |
| Quartz | 定时任务 | http://www.quartz-scheduler.org/ |
| Caffeine | 本地缓存 | https://github.com/ben-manes/caffeine |
| Lombok | Java 语言增强库 | https://github.com/rzwitserloot/lombok |
| Hibernate-Validator 6.1.7.Final | 验证框架 | http://hibernate.org/validator |
| Knife4j 3.0.2 | Swagger 增强 | https://github.com/xiaoymin/knife4j |

### 3.2 前端技术栈（Web 管理后台）

| 技术 | 说明 | 版本 |
| :--- | :--- | :--- |
| Vue | 渐进式 JavaScript 框架 | 2.6.14 |
| Vue Router | 路由管理 | 3.0.2 |
| Vuex | 状态管理 | 3.1.0 |
| Element UI | UI 组件库 | 2.15.8 |
| Axios | HTTP 请求库 | 0.21.1 |
| ECharts | 图表库 | 4.2.1 |
| WangEditor | 富文本编辑器 | 4.7.11 |
| MavonEditor | Markdown 编辑器 | 2.9.1 |
| Vue-Cropper | 图片裁剪 | 0.4.9 |
| VueDraggable | 拖拽排序 | 2.20.0 |

### 3.3 移动端技术栈（UniApp）

| 技术 | 说明 |
| :--- | :--- |
| UniApp | 跨端开发框架 |
| Vue 3 | 渐进式 JavaScript 框架 |
| uView UI | 移动端 UI 组件库 |

---

## 四、开发环境

| 工具 | 说明 | 版本 |
| :--- | :--- | :--- |
| JDK | Java 开发环境 | 1.8 |
| MySQL | 关系型数据库 | 8.0 |
| Redis | 缓存数据库 | 6.0.16 |
| MongoDB | 文档数据库 | 6.0.17 |
| RocketMQ | 消息队列 | 5.3.0 |
| Elasticsearch | 搜索引擎 | 7.17.23 |
| Nacos | 服务注册与配置中心 | 2021.0.4.0 |
| Node.js | 前端运行环境 | >=8.9 |
| Maven | 项目构建工具 | 3.6+ |

---

## 五、开发工具

| 工具 | 说明 | 官网 |
| :--- | :--- | :--- |
| IDEA | 开发 IDE | https://www.jetbrains.com/idea/download |
| RedisDesktop | Redis 客户端连接工具 | https://github.com/qishibo/AnotherRedisDesktopManager |
| Robomongo | MongoDB 客户端连接工具 | https://robomongo.org/download |
| Navicat | 数据库连接工具 | http://www.formysql.com/xiazai.html |
| MindMaster | 思维导图工具 | http://www.edrawsoft.cn/mindmaster |
| Draw.io | 流程图工具 | https://draw.io/ |
| Postman | API 接口调试工具 | https://www.postman.com/ |
| MDNice | Markdown 编辑器 | https://editor.mdnice.com/ |

---

## 六、功能模块

### 6.1 后端模块（mall-mgt）

#### 系统管理
- 用户管理（UserController）
- 角色管理（RoleController）
- 菜单管理（MenuController）
- 部门管理（DeptController）
- 岗位管理（JobController）
- 字典管理（DictController、DictDetailController）
- 用户角色关联（UserRoleController）
- 角色菜单关联（RoleMenuController）
- 角色部门关联（RoleDeptController）

#### 商品管理
- 商品管理（ProductController）
- 商品分类（CategoryController）
- 商品品牌（BrandController）
- 商品属性（ProductAttributeController）
- 属性管理（AttributeController）
- 属性值管理（AttributeValueController）
- 商品图片（ProductPhotoController）
- 商品分组（ProductGroupController）
- 计量单位（UnitController）
- 首页商品（IndexProductController）

#### 订单管理
- 订单管理（TradeController）
- 订单项管理（TradeItemController）
- 订单配送地址（TradeDeliveryAddressController）

#### 购物车管理
- 购物车（ShoppingCartController）
- 商品浏览记录（ProductViewRecordController）
- 商品收藏（ProductFavoritesController）
- 商品评价（ProductCommentController）
- 收货地址（DeliveryAddressController）

#### 营销管理
- 优惠券管理（CouponController）
- 优惠券发放（CouponUserProvideController）
- 优惠券领取（CouponUserReceiveController）
- 秒杀商品（SeckillProductController）

#### 售后管理
- 退款/退货（RefundController）

#### 首页管理
- 轮播图（IndexCarouselImageController）
- 公告（IndexNoticeController）

#### 通用管理
- 地区管理（CommonAreaController）
- 敏感词（CommonSensitiveWordController）
- 图片管理（CommonPhotoController、CommonPhotoGroupController）
- 通知管理（CommonNotifyController）
- 定时任务（CommonJobController、CommonJobLogController）
- 短信记录（CommonSmsRecordController）

#### 文件上传
- 文件上传（UploadController）

#### 移动端接口（web 包）
- 首页接口（WebIndexController）
- 商品接口（WebProductController）
- 分类接口（WebCategoryController）
- 购物车接口（WebTradeController）
- 订单接口（WebTradeController）
- 支付接口（WebPayController）
- 用户接口（WebUserController）
- 优惠券接口（WebCouponController）
- 收货地址接口（WebDeliveryAddressController）
- 退款接口（WebRefundController）
- 推荐商品（WebProductRecommendController）
- 短信接口（SmsController）
- IP 地址解析（GeoIpController）

### 6.2 定时任务模块（mall-job）

#### 定时任务
- 同步商品到 ES（SyncProductToEsJob）
- 刷新首页数据（RefreshIndexDataJob）
- 推荐商品（RecommendProductJob）
- 字典缓存（DictJob）
- 通用任务（CommonTaskJob）

#### MQ 消费者
- 超时取消订单消费者（OverTimeCancelTradeConsumer）
- Excel 导出消费者（ExcelExportConsumer）
- 动态任务消费者（DynamicJobConsumer）

#### WebSocket
- WebSocket 服务端（WebSocketServer）

### 6.3 移动端页面（UniApp）

| 页面路径 | 说明 |
| :--- | :--- |
| pages/index/index | 首页 |
| pages/login/login | 用户登录 |
| pages/index-product/index-product | 商品列表 |
| pages/product-detail/product-detail | 商品详情 |
| pages/shopping-cart/shopping-cart | 购物车 |
| pages/coupon/coupon-center | 优惠券中心 |
| pages/create-order/create-order | 下单页面 |
| pages/delivery-address/delivery-address | 收货地址管理 |
| pages/pay-result/pay-result | 支付结果页面 |
| pages/order-list/order-list | 订单列表 |
| pages/order-detail/order-detail | 订单详情 |
| pages/user/user | 个人中心 |
| pages/search-product/search-product | 商品搜索 |
| pages/notice-list/notice-list | 通知列表 |
| pages/notice-detail/notice-detail | 通知详情 |
| pages/edit-address/edit-address | 编辑收货地址 |
| pages/recommend-product/recommend-product | 猜你喜欢 |
| pages/comment/comment | 商品评价 |
| pages/refund/refund | 申请退款 |
| pages/category/category | 商品分类 |
| pages/sub-category/sub-category | 子分类 |

---



## 七、项目亮点

1. **前后端分离架构**：前端 Vue/UniApp，后端 SpringBoot，接口清晰，易于维护
2. **微服务架构设计**：模块化设计，mall-common、mall-business、mall-mgt、mall-job 职责分明
3. **完整的电商功能**：商品、订单、购物车、优惠券、秒杀、支付、售后等全流程覆盖
4. **多种缓存策略**：Redis 分布式缓存 + Caffeine 本地缓存，提升系统性能
5. **搜索引擎集成**：Elasticsearch 商品搜索，支持全文检索、高亮显示
6. **消息队列解耦**：RocketMQ 处理订单超时取消、异步导出等场景
7. **数据库分库分表**：ShardingSphere 支持订单等大数据量场景
8. **推荐算法集成**：Mahout 实现商品推荐（猜你喜欢）
9. **多种支付方式**：集成支付宝支付，支持扫码支付
10. **对象存储**：MinIO 文件存储，支持图片、视频等上传
11. **实时通知**：WebSocket 实现系统通知、订单状态变更等实时推送
12. **定时任务**：Quartz 定时任务，处理数据同步、缓存刷新等
13. **多端支持**：UniApp 一套代码发布微信小程序、H5、App 等多端

---

## 八、快速开始

### 9.1 后端启动

```bash
# 1. 导入 SQL 脚本到 MySQL
# 执行 mall-v1/susan_mall-master/sql/ 目录下的 SQL 脚本

# 2. 修改配置文件
# 修改 application.yml 中的数据库、Redis、MQ 等配置

# 3. 启动 Nacos
# 下载并启动 Nacos 服务

# 4. 启动 Redis
redis-server

# 5. 启动 RocketMQ
# 按照 RocketMQ 官方文档启动

# 6. 启动 Elasticsearch
# 按照 Elasticsearch 官方文档启动

# 7. 启动 MongoDB
mongod

# 8. 启动后端项目
# 使用 IDEA 打开 mall-v1/susan_mall-master 目录
# 分别启动 mall-mgt 和 mall-job 模块的 Application 主类
```

### 9.2 Web 管理后台启动

```bash
# 1. 进入项目目录
cd mall_v1_web/susan_mall_web-master

# 2. 安装依赖
npm install

# 3. 启动开发服务器
npm run dev

# 4. 构建生产版本
npm run build:prod
```

### 9.3 移动端启动

```bash
# 1. 使用 HBuilderX 打开项目
# 打开 mall_v1_uni/susan_mall_uni-master 目录

# 2. 运行到浏览器或微信小程序
# 选择运行目标，进行调试
```

---

## 十、API 接口文档

启动 mall-mgt 模块后，访问以下地址查看 API 文档：

- **Swagger UI**: http://localhost:8080/swagger-ui/
- **Knife4j**: http://localhost:8080/doc.html

---

## 十一、常见问题

### 11.1 依赖安装失败

```bash
# 清理 Maven 缓存
mvn clean

# 使用阿里云镜像
# 在 settings.xml 中配置阿里云 Maven 镜像
```

### 11.2 前端依赖安装失败

```bash
# 使用淘宝镜像
npm config set registry https://registry.npmmirror.com

# 清理缓存后重新安装
npm cache clean --force
npm install
```

### 11.3 端口被占用

```bash
# 查看端口占用
netstat -ano | findstr :8080

# 杀死占用端口的进程
taskkill /F /PID <进程 ID>
```



---

## 十二、许可证

本项目采用 Apache-2.0 许可证，详见 [LICENSE](mall-v1/susan_mall-master/LICENSE) 文件。

---

<div align="center">
    <p>如果项目对你有帮助，请点右上角给一个 Star ⭐</p>
</div>
