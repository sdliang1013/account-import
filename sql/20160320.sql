CREATE TABLE `t_user` (
  `id`        CHAR(32)    NOT NULL
  COMMENT '主键',
  `user_name` VARCHAR(30) NOT NULL
  COMMENT '账号',
  `real_name` VARCHAR(30) NULL
  COMMENT '姓名',
  `password`  VARCHAR(50) NOT NULL
  COMMENT '密码',
  `user_type` TINYINT(1)  NOT NULL DEFAULT 2
  COMMENT '用户类型 1-管理员,2-操作员',
  PRIMARY KEY (`id`)
)
  DEFAULT CHARACTER SET =utf8mb4
  COLLATE =utf8mb4_general_ci
  COMMENT ='管理账号';

CREATE TABLE `t_account` (
  `id`           CHAR(32)    NOT NULL
  COMMENT '主键',
  `account_name` VARCHAR(30) NOT NULL
  COMMENT '账号',
  `qq`           VARCHAR(20) NULL
  COMMENT 'qq',
  `mobile`       VARCHAR(15) NOT NULL
  COMMENT '手机号',
  `send_state`   TINYINT(1)  NOT NULL DEFAULT 1
  COMMENT '派送状态(未派送,已派送,已拒绝)',
  `arbitrage`    TINYINT(1)  NOT NULL DEFAULT 0
  COMMENT '是否套利',
  `create_time`  DATETIME    NOT NULL
  COMMENT '创建时间',
  PRIMARY KEY (`id`)
)
  DEFAULT CHARACTER SET =utf8mb4
  COLLATE =utf8mb4_general_ci
  COMMENT ='导入账号';

CREATE TABLE `t_account_temp` (
  `id`           CHAR(32)    NOT NULL
  COMMENT '主键',
  `account_name` VARCHAR(30) NOT NULL
  COMMENT '账号',
  `qq`           VARCHAR(20) NULL
  COMMENT 'qq',
  `mobile`       VARCHAR(15) NOT NULL
  COMMENT '手机号',
  `send_state`   TINYINT(1)  NOT NULL DEFAULT 1
  COMMENT '派送状态(未派送,已派送,已拒绝)',
  `arbitrage`    TINYINT(1)  NOT NULL DEFAULT 0
  COMMENT '是否套利',
  `create_time`  DATETIME    NOT NULL
  COMMENT '创建时间',
  PRIMARY KEY (`id`)
)
  DEFAULT CHARACTER SET =utf8mb4
  COLLATE =utf8mb4_general_ci
  COMMENT ='导入账号临时表';

ALTER TABLE `t_user`
ADD UNIQUE INDEX `UIDX_USRE_NAME` (`user_name`);

