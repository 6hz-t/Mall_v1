# Mall-v1 Web

商城前端代码

#### 项目源码

|     |   后端源码  |   前端源码  |
|---  |--- | --- |
|  码云   |  https://gitee.com/dvsusan/mall_v1   |  https://gitee.com/dvsusan/mall_v1_web   |

#### 开发文档


#### Build Setup
**推荐 node 版本：12-16**
``` bash
# 配置镜像加速
https://www.ydyno.com/archives/1219.html

# 安装依赖
npm install

# 启动服务 localhost:8013
npm run dev

# 构建生产环境
npm run build:prod
```

如果启动前端项目时报了下面这个异常：
error:0308010C:digital envelope routines::unsupported OpenSSL3.0对允许算法和密钥大小增加了严格的限制。

可以执行下面命令解决问题：
```
export NODE_OPTIONS=--openssl-legacy-provider
```

#### 常见问题

1、linux 系统在安装依赖的时候会出现 node-sass 无法安装的问题

解决方案：
```
1. 单独安装：npm install --unsafe-perm node-sass 
2. 直接使用：npm install --unsafe-perm
```

2、加速node-sass安装

https://www.ydyno.com/archives/1219.html

### 特别鸣谢
eladmin:https://github.com/elunez/eladmin
hutool:https://github.com/looly/hutool
vue:https://github.com/vuejs/vue
element:https://github.com/ElemeFE/element