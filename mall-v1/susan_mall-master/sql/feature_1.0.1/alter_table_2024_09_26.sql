use susan_mall;
alter table mall_product add column `cover_url` varchar(200) DEFAULT NULL COMMENT '封面图片url';

#更新历史数据
update mall_product p
inner join mall_product_photo  m on  p.id = m.product_id
set p.cover_url = m.url
where m.type=1 and m.is_del=0;


use susan_mall_order_0;

alter table order_trade_item_0 add column `cover_url` varchar(200) DEFAULT NULL COMMENT '封面图片url' after `amount` ;
alter table order_trade_item_1 add column `cover_url` varchar(200) DEFAULT NULL COMMENT '封面图片url' after `amount` ;

#更新历史数据
update order_trade_item_0 p
inner join susan_mall.mall_product_photo  m on  p.id = m.product_id
set p.cover_url = m.url
where m.type=1 and m.is_del=0;

update order_trade_item_1 p
inner join susan_mall.mall_product_photo  m on  p.id = m.product_id
set p.cover_url = m.url
where m.type=1 and m.is_del=0;


use susan_mall_order_1;

alter table order_trade_item_0 add column `cover_url` varchar(200) DEFAULT NULL COMMENT '封面图片url' after `amount` ;
alter table order_trade_item_1 add column `cover_url` varchar(200) DEFAULT NULL COMMENT '封面图片url' after `amount` ;

#更新历史数据
update order_trade_item_0 p
inner join susan_mall.mall_product_photo  m on  p.id = m.product_id
set p.cover_url = m.url
where m.type=1 and m.is_del=0;

update order_trade_item_1 p
inner join susan_mall.mall_product_photo  m on  p.id = m.product_id
set p.cover_url = m.url
where m.type=1 and m.is_del=0;
