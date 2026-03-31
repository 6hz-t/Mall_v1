alter table delivery_address add column `province_id` bigint(20) DEFAULT 0 COMMENT '省份ID' after receiver_phone;
alter table delivery_address add column `city_id` bigint(20) DEFAULT 0 COMMENT '城市ID' after province;
alter table delivery_address add column `district_id` bigint(20) DEFAULT 0 COMMENT '区县ID' after city;
