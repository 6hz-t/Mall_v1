CREATE TABLE `coupon` (
`id` bigint NOT NULL COMMENT 'ID',
`name` varchar(60) NOT NULL COMMENT '优惠券名称',
`type` int NOT NULL COMMENT '类型 1：现金券 2：阶梯满减 3：每满减 4：通用折扣 5：满N件折扣 6：满Y元折扣',
`photo_url` varchar(200) DEFAULT NULL COMMENT '图片地址',
`receive_start_time` datetime(3) NOT NULL COMMENT '领券开始时间',
`receive_end_time` datetime(3) NOT NULL COMMENT '领券结束时间',
`use_start_time` datetime(3) NOT NULL COMMENT '使用开始时间',
`use_end_time` datetime(3) NOT NULL COMMENT '使用结束时间',
`quantity` int DEFAULT NULL COMMENT '优惠券数量',
`off_money` int DEFAULT NULL COMMENT '优惠金额，比如：满100，减40， 这里就是40',
`discount` int DEFAULT NULL COMMENT '折扣，百分之多少，比如：8折，就填入80',
`min_money` int DEFAULT NULL COMMENT '最低使用金额，比如：满100，减40， 这里就是100',
`min_product_count` int DEFAULT NULL COMMENT '最少商品件数，比如：2件或者3件',
`limit_count_one_day` int DEFAULT 0 COMMENT '每日限额，每天领取多少张优惠券',
`valid_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '有效状态 1:有效 0:无效',
`create_user_id` bigint NOT NULL COMMENT '创建人ID',
`create_user_name` varchar(30) NOT NULL COMMENT '创建人名称',
`create_time` datetime(3) NOT NULL COMMENT '创建日期',
`update_user_id` bigint DEFAULT NULL COMMENT '修改人ID',
`update_user_name` varchar(30) DEFAULT NULL COMMENT '修改人名称',
`update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
`is_del` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 1：已删除 0：未删除',
PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='优惠券表';


CREATE TABLE `coupon_user_provide` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `coupon_id` bigint NOT NULL  COMMENT '优惠券ID',
  `product_id` bigint NOT NULL  COMMENT '商品ID，0表示所有商品',
  `user_id` bigint NOT NULL  COMMENT '用户ID，0表示所有用户',
  `valid_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '有效状态 1:有效 0:无效',
  `create_user_id` bigint NOT NULL COMMENT '创建人ID',
  `create_user_name` varchar(30) NOT NULL COMMENT '创建人名称',
  `create_time` datetime(3) DEFAULT NULL COMMENT '创建日期',
  `update_user_id` bigint DEFAULT NULL COMMENT '修改人ID',
  `update_user_name` varchar(30)  DEFAULT NULL COMMENT '修改人名称',
  `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
  `is_del` tinyint(1) DEFAULT '0' COMMENT '是否删除 1：已删除 0：未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='优惠券发放表';


CREATE TABLE `coupon_user_receive` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `coupon_id` bigint NOT NULL  COMMENT '优惠券ID',
  `user_id` bigint NOT NULL  COMMENT '用户ID，0表示所有用户',
  `use_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '使用状态 1:已使用 0:未使用',
  `use_time` datetime(3) DEFAULT NULL COMMENT '使用时间',
  `create_user_id` bigint NOT NULL COMMENT '创建人ID',
  `create_user_name` varchar(30) NOT NULL COMMENT '创建人名称',
  `create_time` datetime(3) DEFAULT NULL COMMENT '创建日期',
  `update_user_id` bigint DEFAULT NULL COMMENT '修改人ID',
  `update_user_name` varchar(30)  DEFAULT NULL COMMENT '修改人名称',
  `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
  `is_del` tinyint(1) DEFAULT '0' COMMENT '是否删除 1：已删除 0：未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='优惠券领取表';
