use susan_mall_order_0;
alter table order_trade_item_0 add column `code` varchar(30) NOT NULL DEFAULT '1' COMMENT '订单编码';
alter table order_trade_item_1 add column `code` varchar(30) NOT NULL DEFAULT '1' COMMENT '订单编码';

use susan_mall_order_1;
alter table order_trade_item_0 add column `code` varchar(30) NOT NULL DEFAULT '1' COMMENT '订单编码';
alter table order_trade_item_1 add column `code` varchar(30) NOT NULL DEFAULT '1' COMMENT '订单编码';
