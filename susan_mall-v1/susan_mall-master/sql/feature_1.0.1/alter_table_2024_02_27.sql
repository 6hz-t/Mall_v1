alter table `biz_log` add column `method_name` varchar(10) NULL COMMENT '所在城市';
alter table `sys_user` add column `last_login_city` varchar(10) NULL COMMENT '最后登录城市';
alter table `sys_user` add column  `last_login_time` datetime(3) DEFAULT NULL COMMENT '最后登录时间';
