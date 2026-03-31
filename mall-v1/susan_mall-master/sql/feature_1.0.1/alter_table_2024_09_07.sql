alter table mall_product add `product_group_id` bigint NOT NULL  COMMENT '商品组ID' after id;

#重建联合索引
alter table mall_product add `del_id` bigint NOT NULL DEFAULT '0'  COMMENT '逻辑删除id' after is_del;

update mall_product set del_id=id where is_del=1;

alter table mall_product drop index uk_category_unit_brand_hash;
ALTER TABLE mall_product  ADD UNIQUE uk_group_id_brand_hash_del_id (product_group_id, brand_id, hash,del_id);

ALTER TABLE mall_product_group  ADD UNIQUE uk_category_id_unit_id_hash_del_id (category_id, unit_id, hash,del_id);
