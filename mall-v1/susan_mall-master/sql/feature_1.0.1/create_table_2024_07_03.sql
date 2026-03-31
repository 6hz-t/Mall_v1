CREATE TABLE `common_photo_group` (
  `id` bigint NOT NULL COMMENT 'ID',
  `name` varchar(60) NOT NULL COMMENT '分组名称',
  `create_user_id` bigint NOT NULL COMMENT '创建人ID',
  `create_user_name` varchar(30) NOT NULL COMMENT '创建人名称',
  `create_time` datetime(3) DEFAULT NULL COMMENT '创建日期',
  `update_user_id` bigint DEFAULT NULL COMMENT '修改人ID',
  `update_user_name` varchar(30) DEFAULT NULL COMMENT '修改人名称',
  `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
  `is_del` tinyint(1) DEFAULT '0' COMMENT '是否删除 1：已删除 0：未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='图片分组表';

CREATE TABLE `common_photo` (
  `id` bigint NOT NULL  COMMENT 'ID',
  `photo_group_id` bigint NOT NULL  COMMENT '图片分组ID',
  `name` varchar(60) NOT NULL COMMENT '图片名称',
  `url` varchar(200) NOT NULL COMMENT '图片url',
  `create_user_id` bigint NOT NULL COMMENT '创建人ID',
  `create_user_name` varchar(30) NOT NULL COMMENT '创建人名称',
  `create_time` datetime(3) DEFAULT NULL COMMENT '创建日期',
  `update_user_id` bigint DEFAULT NULL COMMENT '修改人ID',
  `update_user_name` varchar(30) DEFAULT NULL COMMENT '修改人名称',
  `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
  `is_del` tinyint(1) DEFAULT '0' COMMENT '是否删除 1：已删除 0：未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='图片表';
