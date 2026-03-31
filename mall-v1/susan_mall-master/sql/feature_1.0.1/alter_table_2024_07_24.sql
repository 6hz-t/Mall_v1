use susan_mall_order_0;

alter table `order_trade_0` add column order_type tinyint(1) DEFAULT 1 NOT NULL COMMENT '订单类型 1：普通商品订单 2：秒杀商品订单';
alter table `order_trade_1` add column order_type tinyint(1) DEFAULT 1 NOT NULL COMMENT '订单类型 1：普通商品订单 2：秒杀商品订单';

use susan_mall_order_1;

alter table `order_trade_0` add column order_type tinyint(1) DEFAULT 1 NOT NULL COMMENT '订单类型 1：普通商品订单 2：秒杀商品订单';
alter table `order_trade_1` add column order_type tinyint(1) DEFAULT 1 NOT NULL COMMENT '订单类型 1：普通商品订单 2：秒杀商品订单';
