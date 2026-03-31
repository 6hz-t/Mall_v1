alter table mall_product add column `remain_quantity` int NOT NULL COMMENT '剩余库存' after quantity;
update mall_product set remain_quantity=quantity where 1=1;
