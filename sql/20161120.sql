ALTER TABLE `t_account`
MODIFY COLUMN `qq` VARCHAR(30) NOT NULL
COMMENT 'qq',
MODIFY COLUMN `mobile` VARCHAR(20) NOT NULL
COMMENT '手机号';