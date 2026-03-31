CREATE TABLE `mall_index_product` (
  `id` bigint NOT NULL COMMENT 'ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `sort` int(3) NOT NULL DEFAULT '999' COMMENT '排序',
  `type` int(3) NOT NULL COMMENT '商品类型 1: 热门商品 2: 最新商品 3：秒杀商品',
  `create_user_id` bigint NOT NULL COMMENT '创建人ID',
  `create_user_name` varchar(30) NOT NULL COMMENT '创建人名称',
  `create_time` datetime(3) NOT NULL COMMENT '创建日期',
  `update_user_id` bigint DEFAULT NULL COMMENT '修改人ID',
  `update_user_name` varchar(30) DEFAULT NULL COMMENT '修改人名称',
  `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
  `is_del` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 1：已删除 0：未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='首页商品'
