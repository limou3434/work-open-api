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
CREATE TABLE IF NOT EXISTS `user`
(
    `id`            BIGINT AUTO_INCREMENT COMMENT 'id' PRIMARY KEY,
    `user_name`     VARCHAR(256)                           NULL COMMENT '用户昵称',
    `user_account`  VARCHAR(256)                           NOT NULL COMMENT '账号',
    `user_avatar`   VARCHAR(1024)                          NULL COMMENT '用户头像',
    `gender`        TINYINT                                NULL COMMENT '性别',
    `user_role`     VARCHAR(256) DEFAULT 'user'            NOT NULL COMMENT '用户角色: user / admin',
    `user_password` VARCHAR(512)                           NOT NULL COMMENT '密码',
    `access_key`    VARCHAR(512)                           NOT NULL COMMENT 'accessKey',
    `secret_key`    VARCHAR(512)                           NOT NULL COMMENT 'secretKey',
    `create_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete`     TINYINT      DEFAULT 0                 NOT NULL COMMENT '是否删除',
    CONSTRAINT `uni_user_account`
        UNIQUE (`user_account`)
) COMMENT '用户表';

CREATE TABLE IF NOT EXISTS `interface_info`
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

CREATE TABLE IF NOT EXISTS `user_interface_info`
(
    `id`                BIGINT                             NOT NULL AUTO_INCREMENT COMMENT '主键' PRIMARY KEY,
    `user_id`           BIGINT                             NOT NULL COMMENT '调用用户 id',
    `interface_info_id` BIGINT                             NOT NULL COMMENT '接口 id',
    `total_num`         INT      DEFAULT 0                 NOT NULL COMMENT '总调用次数',
    `left_num`          INT      DEFAULT 0                 NOT NULL COMMENT '剩余调用次数',
    `status`            INT      DEFAULT 0                 NOT NULL COMMENT '0-正常，1-禁用',
    `create_time`       DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_time`       DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete`         TINYINT  DEFAULT 0                 NOT NULL COMMENT '是否删除(0-未删, 1-已删)'
) COMMENT '用户调用接口关联表';

-- 项目测试
