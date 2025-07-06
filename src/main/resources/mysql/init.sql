-- 项目数库
DROP DATABASE IF EXISTS `work_open_api`;
CREATE DATABASE `work_open_api` CHARACTER SET `utf8mb4` COLLATE = `utf8mb4_unicode_ci`;
USE `work_open_api`;

-- 项目用户
DROP USER IF EXISTS 'woa'@'%';
CREATE USER 'woa'@'%' IDENTIFIED BY 'Qwe54188_';
GRANT ALL PRIVILEGES ON `work_open_api`.* TO 'woa'@'%';
FLUSH PRIVILEGES;

-- 项目数表
CREATE TABLE IF NOT EXISTS `work_open_api`.`interface_info`
(
    `id`              BIGINT                             NOT NULL AUTO_INCREMENT COMMENT '唯一标识' PRIMARY KEY,
    `name`            VARCHAR(256)                       NOT NULL COMMENT '接口名称',
    `description`     VARCHAR(256)                       NOT NULL COMMENT '接口描述',
    `url`             VARCHAR(256)                       NOT NULL COMMENT '接口地址',
    `request_header`  TEXT                               NOT NULL COMMENT '接口请求头',
    `response_header` TEXT                               NOT NULL COMMENT '接口响应头',
    `status`          TINYINT                            NOT NULL COMMENT '接口状态(0-关闭, 1-开启)',
    `method`          VARCHAR(256)                       NOT NULL COMMENT '请求类型',
    `user_id`         BIGINT                             NOT NULL COMMENT '用户唯一标识',
    `create_time`     DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_time`     DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`      TINYINT  DEFAULT 0                 NOT NULL COMMENT '是否删除(0-未删, 1-已删)'
) COMMENT '接口信息表';

-- 项目测试
