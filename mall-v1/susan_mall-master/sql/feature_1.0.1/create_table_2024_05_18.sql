CREATE TABLE `common_sensitive_word` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `type` int(3) NOT NULL COMMENT '类型 1:广告 2:政治 3：违法 4：色情 5：网址',
  `word` varchar(30) NOT NULL COMMENT '名称',
  `create_user_id` bigint NOT NULL COMMENT '创建人ID',
  `create_user_name` varchar(30) NOT NULL COMMENT '创建人名称',
  `create_time` datetime(3) DEFAULT NULL COMMENT '创建日期',
  `update_user_id` bigint DEFAULT NULL COMMENT '修改人ID',
  `update_user_name` varchar(30)  DEFAULT NULL COMMENT '修改人名称',
  `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
  `is_del` tinyint(1) DEFAULT '0' COMMENT '是否删除 1：已删除 0：未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='敏感词表';
