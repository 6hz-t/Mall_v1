CREATE TABLE `common_sms_record` (
`id` bigint NOT NULL COMMENT 'ID',
`phone` char(11) NOT NULL COMMENT '手机号',
`sms_code` varchar(10) NOT NULL COMMENT '验证码',
`expire_second` int(3) NOT NULL COMMENT '有效期',
`send_time` datetime(3) NOT NULL COMMENT '发送时间',
`create_user_id` bigint NOT NULL COMMENT '创建人ID',
`create_user_name` varchar(30) NOT NULL COMMENT '创建人名称',
`create_time` datetime(3) NOT NULL COMMENT '创建日期',
`update_user_id` bigint DEFAULT NULL COMMENT '修改人ID',
`update_user_name` varchar(30) DEFAULT NULL COMMENT '修改人名称',
`update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
`is_del` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 1：已删除 0：未删除',
PRIMARY KEY (`id`)  USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COMMENT='短信发送记录表';
