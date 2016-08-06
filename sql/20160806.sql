ALTER TABLE `t_account_temp`
ADD COLUMN `handsel` INT(6) DEFAULT 0
COMMENT '彩金'
AFTER `mobile`;

ALTER TABLE `t_account`
ADD COLUMN `handsel` INT(6) DEFAULT 0
COMMENT '彩金'
AFTER `mobile`;