use susan_mall_order_0;

CREATE TABLE `order_trade_delivery_address_0` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `trade_id` bigint NOT NULL COMMENT '订单ID',
  `code` varchar(30) NOT NULL COMMENT '订单编码',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `user_name` varchar(30) NOT NULL COMMENT '用户名称',
  `receiver_name` varchar(30) NOT NULL COMMENT '收货人姓名',
  `receiver_phone` char(11) NOT NULL COMMENT '收货人手机号',
  `province` varchar(10) NOT NULL COMMENT '省份',
  `city` varchar(10) NOT NULL COMMENT '城市',
  `district`  varchar(10) NOT NULL COMMENT '区县',
  `detail_address`  varchar(50) NOT NULL COMMENT '详细地址',
  `post_code`  varchar(10) NOT NULL COMMENT '邮编',
  `create_user_id` bigint NOT NULL COMMENT '创建人ID',
  `create_user_name` varchar(30) NOT NULL COMMENT '创建人名称',
  `create_time` datetime(3) DEFAULT NULL COMMENT '创建日期',
  `update_user_id` bigint DEFAULT NULL COMMENT '修改人ID',
  `update_user_name` varchar(30)  DEFAULT NULL COMMENT '修改人名称',
  `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
  `is_del` tinyint(1) DEFAULT '0' COMMENT '是否删除 1：已删除 0：未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单收货地址表';

CREATE TABLE `order_trade_delivery_address_1` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `trade_id` bigint NOT NULL COMMENT '订单ID',
  `code` varchar(30) NOT NULL COMMENT '订单编码',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `user_name` varchar(30) NOT NULL COMMENT '用户名称',
  `receiver_name` varchar(30) NOT NULL COMMENT '收货人姓名',
  `receiver_phone` char(11) NOT NULL COMMENT '收货人手机号',
  `province` varchar(10) NOT NULL COMMENT '省份',
  `city` varchar(10) NOT NULL COMMENT '城市',
  `district`  varchar(10) NOT NULL COMMENT '区县',
  `detail_address`  varchar(50) NOT NULL COMMENT '详细地址',
  `post_code`  varchar(10) NOT NULL COMMENT '邮编',
  `create_user_id` bigint NOT NULL COMMENT '创建人ID',
  `create_user_name` varchar(30) NOT NULL COMMENT '创建人名称',
  `create_time` datetime(3) DEFAULT NULL COMMENT '创建日期',
  `update_user_id` bigint DEFAULT NULL COMMENT '修改人ID',
  `update_user_name` varchar(30)  DEFAULT NULL COMMENT '修改人名称',
  `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
  `is_del` tinyint(1) DEFAULT '0' COMMENT '是否删除 1：已删除 0：未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单收货地址表';



use susan_mall_order_1;

CREATE TABLE `order_trade_delivery_address_0` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `trade_id` bigint NOT NULL COMMENT '订单ID',
  `code` varchar(30) NOT NULL COMMENT '订单编码',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `user_name` varchar(30) NOT NULL COMMENT '用户名称',
  `receiver_name` varchar(30) NOT NULL COMMENT '收货人姓名',
  `receiver_phone` char(11) NOT NULL COMMENT '收货人手机号',
  `province` varchar(10) NOT NULL COMMENT '省份',
  `city` varchar(10) NOT NULL COMMENT '城市',
  `district`  varchar(10) NOT NULL COMMENT '区县',
  `detail_address`  varchar(50) NOT NULL COMMENT '详细地址',
  `post_code`  varchar(10) NOT NULL COMMENT '邮编',
  `create_user_id` bigint NOT NULL COMMENT '创建人ID',
  `create_user_name` varchar(30) NOT NULL COMMENT '创建人名称',
  `create_time` datetime(3) DEFAULT NULL COMMENT '创建日期',
  `update_user_id` bigint DEFAULT NULL COMMENT '修改人ID',
  `update_user_name` varchar(30)  DEFAULT NULL COMMENT '修改人名称',
  `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
  `is_del` tinyint(1) DEFAULT '0' COMMENT '是否删除 1：已删除 0：未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单收货地址表';

CREATE TABLE `order_trade_delivery_address_1` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `trade_id` bigint NOT NULL COMMENT '订单ID',
  `code` varchar(30) NOT NULL COMMENT '订单编码',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `user_name` varchar(30) NOT NULL COMMENT '用户名称',
  `receiver_name` varchar(30) NOT NULL COMMENT '收货人姓名',
  `receiver_phone` char(11) NOT NULL COMMENT '收货人手机号',
  `province` varchar(10) NOT NULL COMMENT '省份',
  `city` varchar(10) NOT NULL COMMENT '城市',
  `district`  varchar(10) NOT NULL COMMENT '区县',
  `detail_address`  varchar(50) NOT NULL COMMENT '详细地址',
  `post_code`  varchar(10) NOT NULL COMMENT '邮编',
  `create_user_id` bigint NOT NULL COMMENT '创建人ID',
  `create_user_name` varchar(30) NOT NULL COMMENT '创建人名称',
  `create_time` datetime(3) DEFAULT NULL COMMENT '创建日期',
  `update_user_id` bigint DEFAULT NULL COMMENT '修改人ID',
  `update_user_name` varchar(30)  DEFAULT NULL COMMENT '修改人名称',
  `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
  `is_del` tinyint(1) DEFAULT '0' COMMENT '是否删除 1：已删除 0：未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单收货地址表';
