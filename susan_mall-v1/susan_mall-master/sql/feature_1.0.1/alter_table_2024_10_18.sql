alter table mall_product_view_record add column `view_count` int NOT NULL DEFAULT '1' COMMENT '浏览次数' after product_id;

