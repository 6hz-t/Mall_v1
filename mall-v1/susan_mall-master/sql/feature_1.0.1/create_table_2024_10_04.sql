CREATE TABLE `common_area` (
                               `id` bigint NOT NULL COMMENT 'ID' AUTO_INCREMENT,
                               `parent_id` bigint NOT NULL COMMENT '上一级ID',
                               `name` varchar(20) NOT NULL COMMENT '名称',
                               `pinyin` varchar(100) NOT NULL COMMENT '拼音',
                               `full_name` varchar(60) NOT NULL COMMENT '全称',
                               `code` varchar(20) NOT NULL COMMENT '行政编码',
                               `level` int NOT NULL COMMENT '级别',
                               `create_user_id` bigint NOT NULL COMMENT '创建人ID',
                               `create_user_name` varchar(30) NOT NULL COMMENT '创建人名称',
                               `create_time` datetime(3) NOT NULL COMMENT '创建日期',
                               `update_user_id` bigint DEFAULT NULL COMMENT '修改人ID',
                               `update_user_name` varchar(30) DEFAULT NULL COMMENT '修改人名称',
                               `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
                               `is_del` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 1：已删除 0：未删除',
                               PRIMARY KEY (`id`)  USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='地区表'
