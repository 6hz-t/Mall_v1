CREATE TABLE `mall_product_group` (
                                      `id` bigint NOT NULL COMMENT 'ID',
                                      `category_id` bigint NOT NULL COMMENT '分类ID',
                                      `unit_id` bigint NOT NULL COMMENT '单位ID',
                                      `name` varchar(60) NOT NULL COMMENT '商品组名称',
                                      `model` varchar(100) DEFAULT NULL COMMENT '规格',
                                      `hash` varchar(64) NOT NULL COMMENT 'hash值',
                                      `create_user_id` bigint NOT NULL COMMENT '创建人ID',
                                      `create_user_name` varchar(30) NOT NULL COMMENT '创建人名称',
                                      `create_time` datetime(3) NOT NULL COMMENT '创建日期',
                                      `update_user_id` bigint DEFAULT NULL COMMENT '修改人ID',
                                      `update_user_name` varchar(30) DEFAULT NULL COMMENT '修改人名称',
                                      `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
                                      `is_del` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 1：已删除 0：未删除',
                                      `del_id` bigint NOT NULL DEFAULT '0'  COMMENT '逻辑删除id',
                                      PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品组表';


CREATE TABLE `mall_product_group_attribute` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `product_group_id` bigint NOT NULL  COMMENT '商品组ID',
  `attribute_id` bigint NOT NULL  COMMENT '属性ID',
  `attribute_value_id` bigint NOT NULL  COMMENT '属性值ID',
  `create_user_id` bigint NOT NULL COMMENT '创建人ID',
  `create_user_name` varchar(30) NOT NULL COMMENT '创建人名称',
  `create_time` datetime(3) DEFAULT NULL COMMENT '创建日期',
  `update_user_id` bigint DEFAULT NULL COMMENT '修改人ID',
  `update_user_name` varchar(30)  DEFAULT NULL COMMENT '修改人名称',
  `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
  `is_del` tinyint(1) DEFAULT '0' COMMENT '是否删除 1：已删除 0：未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品组属性表';
