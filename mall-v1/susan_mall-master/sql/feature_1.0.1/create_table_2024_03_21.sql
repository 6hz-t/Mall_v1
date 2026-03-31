use susan_mall;

CREATE TABLE `sys_dict` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `type` varchar(20) NOT NULL COMMENT '字典类型',
  `label` varchar(30) NOT NULL COMMENT '文本',
  `value` varchar(30) NOT NULL COMMENT '值',
  `sort` int(3) NOT NULL COMMENT '排序',
  `create_user_id` bigint NOT NULL COMMENT '创建人ID',
  `create_user_name` varchar(30) NOT NULL COMMENT '创建人名称',
  `create_time` datetime(3) DEFAULT NULL COMMENT '创建日期',
  `update_user_id` bigint DEFAULT NULL COMMENT '修改人ID',
  `update_user_name` varchar(30)  DEFAULT NULL COMMENT '修改人名称',
  `update_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
  `is_del` tinyint(1) DEFAULT '0' COMMENT '是否删除 1：已删除 0：未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='数据字典';

insert into sys_dict(type,label,value,sort,create_user_id,create_user_name,create_time,is_del) value ('valid_status','启用','true','1','1','管理员',now(3),0);
insert into sys_dict(type,label,value,sort,create_user_id,create_user_name,create_time,is_del) value ('valid_status','禁用','false','2','1','管理员',now(3),0);


