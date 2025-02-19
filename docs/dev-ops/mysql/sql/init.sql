SET character_set_client = utf8;
SET character_set_results = utf8;
SET character_set_connection = utf8;
CREATE database if NOT EXISTS `utopia` default character set utf8mb4;
use utopia;

-- 头像表
DROP TABLE IF EXISTS `avatar`;
CREATE TABLE IF NOT EXISTS `avatar` (
    -- 主键
                                        `id` BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 对应用户id
                                        `user_id` VARCHAR(45) NOT NULL DEFAULT '' COMMENT '对应用户id',
    -- 头像唯一id
                                        `avatar_id` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '头像id',
    -- 头像url
                                        `avatar_url` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '头像url',
    -- 创建者
                                        `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    -- 软删除标识 0-未删除 1-已删除
                                        `is_deleted` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0-未删除;1-已删除'
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '头像表';

-- 评论表
DROP TABLE IF EXISTS `comment`;
CREATE TABLE IF NOT EXISTS `comment` (
    -- 主键
                                         `id` BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 根评论id
                                         `root_id` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '根/最顶评论id',
    -- 回复的评论id
                                         `reply_to` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '回复的评论id',
    -- 评论id
                                         `comment_id` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '评论id',
    -- 帖子id
                                         `post_id` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '帖子id',
    -- 用户id
                                         `user_id` VARCHAR(36) NOT NULL DEFAULT '' COMMENT '用户id',
    -- 内容
                                         `content` TEXT COMMENT '内容',
    -- 点赞数
                                         `like_count` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '点赞数',
    -- 踩数
                                         `unlike_count` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '踩数',
    -- 回复评论数量
                                         `reply_count` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '回复评论数量',
    -- 评论时间
                                         `comment_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
    -- 更新时间
                                         `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    -- 软删除标识 0-未删除 1-已删除
                                         `is_deleted` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0-未删除;1-已删除'
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '评论表';
CREATE UNIQUE INDEX uk_comment_id ON `comment` (comment_id);
CREATE INDEX idx_user_id ON `comment` (user_id);
CREATE INDEX idx_post_id ON `comment` (post_id);
CREATE INDEX idx_root_id ON `comment` (root_id);

-- 评论-图片中间表
DROP TABLE IF EXISTS `comment_image`;
CREATE TABLE IF NOT EXISTS `comment_image` (
    -- 主键
                                               `id` BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 评论id
                                               `comment_id` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '评论id',
    -- 图片id
                                               `image_id` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '图片id',
    -- 创建时间
                                               `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    -- 更新时间
                                               `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    -- 软删除标识 0-未删除 1-已删除
                                               `is_deleted` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0-未删除;1-已删除'
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '评论-图片中间表';
CREATE INDEX idx_comment_id ON `comment_image` (comment_id);

-- 帖子表
DROP TABLE IF EXISTS `discuss_post`;
CREATE TABLE IF NOT EXISTS `discuss_post` (
    -- 主键
                                              `id` BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 帖子id
                                              `discuss_post_id` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '帖子id',
    -- 用户id
                                              `user_id` VARCHAR(45) NOT NULL DEFAULT '' COMMENT '微信用户唯一标识',
    -- 所属院校代号
                                              `school_code` VARCHAR(5) NOT NULL DEFAULT '' COMMENT '所属院校代号',
    -- 标题
                                              `title` VARCHAR(36) NOT NULL DEFAULT '' COMMENT '帖子标题',
    -- 内容
                                              `content` text COMMENT '帖子内容',
    -- 标签/分类
                                              `tag` VARCHAR(36) NOT NULL DEFAULT '' COMMENT '标签/分类',
    -- 点赞数
                                              `like_count` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '点赞数',
    -- 收藏数
                                              `favorite_count` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '收藏数',
    -- 评论数
                                              `comment_count` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '评论数',
    -- 0-普通；1-置顶
                                              `type` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0-普通；1-置顶',
    -- 0-普通；1-封禁
                                              `status` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0-普通；1-封禁',
    -- 创建时间
                                              `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    -- 更新时间
                                              `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    -- 软删除标识 0-未删除 1-已删除
                                              `is_deleted` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0-未删除;1-已删除'
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '帖子表';
CREATE UNIQUE INDEX uk_discuss_post_id_school_code ON `discuss_post` (discuss_post_id, school_code);
CREATE INDEX idx_update_time ON `discuss_post` (update_time DESC);
CREATE INDEX idx_tag ON `discuss_post` (tag);

-- 共享文件下载记录表
DROP TABLE IF EXISTS `download_record`;
CREATE TABLE IF NOT EXISTS `download_record` (
    -- 主键
                                                 `id` BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 用户id
                                                 `user_id` VARCHAR(45) NOT NULL DEFAULT '' COMMENT '用户id',
    -- 文件id
                                                 `sfile_id` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '文件id',
    -- 创建时间
                                                 `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    -- 软删除标识 0-未删除 1-已删除
                                                 `is_deleted` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0-未删除;1-已删除'
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '共享文件下载记录表';
CREATE INDEX idx_user_id ON `download_record` (user_id);

-- 收藏表
DROP TABLE IF EXISTS `favorite`;
CREATE TABLE IF NOT EXISTS `favorite` (
    -- 主键
                                          `id` BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 用户id
                                          `user_id` VARCHAR(45) NOT NULL DEFAULT '' COMMENT '用户id',
    -- 帖子id
                                          `post_id` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '帖子id',
    -- 收藏状态
                                          `status` TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '收藏状态，0-未收藏;1-已收藏',
    -- 创建时间
                                          `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    -- 更新时间
                                          `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    -- 软删除标识 0-未删除 1-已删除
                                          `is_deleted` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0-未删除;1-已删除'
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '收藏表';
CREATE INDEX idx_user_id_post_id ON `favorite` (user_id, post_id);

-- 评论帖子图片表
DROP TABLE IF EXISTS `image`;
CREATE TABLE IF NOT EXISTS `image` (
    -- 主键
                                       `id` BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 对应用户id
                                       `user_id` VARCHAR(45) NOT NULL DEFAULT '' COMMENT '对应用户id',
    -- 图片唯一id
                                       `image_id` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '图片id',
    -- 图片url
                                       `image_url` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '图片url',
    -- 创建者
                                       `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    -- 软删除标识 0-未删除 1-已删除
                                       `is_deleted` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0-未删除;1-已删除'
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '评论帖子图片表';
CREATE UNIQUE INDEX uk_image_id ON `image` (image_id);

-- 点赞表
DROP TABLE IF EXISTS `like`;
CREATE TABLE IF NOT EXISTS `like` (
    -- 主键
                                      `id` BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 用户id
                                      `user_id` VARCHAR(45) NOT NULL DEFAULT '' COMMENT '用户id',
    -- 被点赞帖子/评论id
                                      `to_id` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '被点赞帖子/评论id',
    -- 点赞状态，0-未点赞;1-已点赞
                                      `status` TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '点赞状态，0-未点赞;1-已点赞',
    -- 创建时间
                                      `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    -- 更新时间
                                      `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    -- 软删除标识 0-未删除 1-已删除
                                      `is_deleted` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0-未删除;1-已删除'
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '点赞表';
CREATE UNIQUE INDEX uk_user_id_to_id ON `like` (user_id, to_id);

-- 帖子-图片中间表
DROP TABLE IF EXISTS `post_image`;
CREATE TABLE IF NOT EXISTS `post_image` (
    -- 主键
                                            `id` BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 帖子id，交易或讨论
                                            `post_id` VARCHAR(36) NOT NULL DEFAULT '' COMMENT '帖子id',
    -- 图片id
                                            `image_id` VARCHAR(36) NOT NULL DEFAULT '' COMMENT '图片id',
    -- 创建时间
                                            `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    -- 更新时间
                                            `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    -- 软删除标识 0-未删除 1-已删除
                                            `is_deleted` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0-未删除;1-已删除'
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '帖子-图片中间表';
CREATE INDEX idx_post_id ON `post_image` (post_id);

-- 学校表
DROP TABLE IF EXISTS `school`;
CREATE TABLE IF NOT EXISTS `school` (
    -- 主键
                                        `id` BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 院校代号
                                        `school_code` VARCHAR(5) NOT NULL DEFAULT '' COMMENT '院校代号',
    -- 学校名称
                                        `school_name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '学校名称',
    -- 讨论帖数目
                                        `discuss_post_count` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '讨论帖数目',
    -- 交易帖数目
                                        `trade_post_count` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '交易帖数目',
    -- 学生(用户)人数
                                        `student_count` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '学生(用户)人数',
    -- 软删除标识 0-未删除 1-已删除
                                        `is_deleted` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0-未删除;1-已删除'
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '用户表';
CREATE UNIQUE INDEX uk_school_code ON `school` (school_code);
CREATE UNIQUE INDEX uk_school_name ON `school` (school_name);

-- 文件表
DROP TABLE IF EXISTS `sfile`;
CREATE TABLE IF NOT EXISTS `sfile` (
    -- 主键
                                       `id` BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 文件唯一id
                                       `sfile_id` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '文件id',
    -- 文件名
                                       `sfile_name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '文件名',
    -- 文件url
                                       `sfile_url` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '文件url',
    -- 文件大小
                                       `sfile_size` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '文件大小',
    -- 下载次数
                                       `download_count` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '下载次数',
    -- 类型
                                       `sfile_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT '文件所属类型',
    -- 创建者
                                       `create_by` VARCHAR(36) NOT NULL DEFAULT '' COMMENT '创建者',
    -- 更新者
                                       `update_by` VARCHAR(36) NOT NULL DEFAULT '' COMMENT '更新者',
    -- 创建时间
                                       `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    -- 更新时间
                                       `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    -- 软删除标识 0-未删除 1-已删除
                                       `is_deleted` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0-未删除;1-已删除'
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '文件表';
CREATE UNIQUE INDEX uk_sfile_id ON `sfile` (sfile_id);

-- 交易帖子表
DROP TABLE IF EXISTS `trade_post`;
CREATE TABLE IF NOT EXISTS `trade_post` (
    -- 主键
                                            `id` BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 帖子id
                                            `trade_post_id` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '帖子id',
    -- 用户id
                                            `user_id` VARCHAR(45) NOT NULL DEFAULT '' COMMENT '用户id',
    -- 标题
                                            `title` VARCHAR(36) NOT NULL DEFAULT '' COMMENT '帖子标题',
    -- 内容
                                            `content` text COMMENT '帖子内容',
    -- 定价
                                            `price` FLOAT NOT NULL DEFAULT 0 COMMENT '定价',
    -- 0-出售;1-求购;2-赠送
                                            `type` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0-出售;1-求购:2-赠送',
    -- 0-未完成;1-已完成
                                            `status` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0-未完成;1-已完成',
    -- 创建时间
                                            `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    -- 更新时间
                                            `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    -- 软删除标识 0-未删除 1-已删除
                                            `is_deleted` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0-未删除;1-已删除'
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '交易帖子表';
CREATE UNIQUE INDEX uk_trade_post_id ON `trade_post` (trade_post_id);
CREATE INDEX idx_user_id ON `trade_post` (user_id);
CREATE INDEX idx_status_type ON `trade_post` (status, type);


-- 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
    -- 主键
                                      `id` BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    -- 微信用户唯一标识
                                      `user_id` VARCHAR(45) NOT NULL DEFAULT '' COMMENT '微信用户唯一标识',
    -- 用户名
                                      `user_name` VARCHAR(32) NOT NULL DEFAULT '' COMMENT '姓名',
    -- 院校代号
                                      `school_code` VARCHAR(5) NOT NULL DEFAULT '' COMMENT '院校代号',
    -- 真实姓名
                                      `real_name` VARCHAR(12) NOT NULL DEFAULT '' COMMENT '真实姓名',
    -- 手机号
                                      `phone` VARCHAR(11) NOT NULL DEFAULT '' COMMENT '手机号',
    -- 性别为0-隐藏；1-男；2-女
                                      `gender` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '性别，0-隐藏；1-男；2-女',
    -- 头像
                                      `avatar` VARCHAR(500) NOT NULL DEFAULT '' COMMENT '头像',
    -- 个性签名
                                      `signature` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '个性签名',
    -- 创建时间
                                      `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    -- 更新时间
                                      `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    -- 软删除标识 0-未删除 1-已删除
                                      `is_deleted` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '0-未删除;1-已删除'
) ENGINE = InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '用户表';
CREATE INDEX ind_user_id ON `user` (user_id);


INSERT INTO `avatar` (`id`, `user_id`, `avatar_id`, `avatar_url`, `create_time`, `is_deleted`) VALUES (1, '0001', '20250125T151132125_0001', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250125T151132125_0001.jpg', '2025-01-25 15:11:33', 0);
INSERT INTO `avatar` (`id`, `user_id`, `avatar_id`, `avatar_url`, `create_time`, `is_deleted`) VALUES (2, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '20250214T201124852_ozK7z69qmE2hIRR5txLNz9jBgR0g', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250214T201124852_ozK7z69qmE2hIRR5txLNz9jBgR0g.jpeg', '2025-02-14 20:15:45', 0);
INSERT INTO `avatar` (`id`, `user_id`, `avatar_id`, `avatar_url`, `create_time`, `is_deleted`) VALUES (3, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '20250214T201624325_ozK7z69qmE2hIRR5txLNz9jBgR0g', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250214T201624325_ozK7z69qmE2hIRR5txLNz9jBgR0g.jpeg', '2025-02-14 20:16:24', 0);
INSERT INTO `avatar` (`id`, `user_id`, `avatar_id`, `avatar_url`, `create_time`, `is_deleted`) VALUES (4, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '20250214T220806851_ozK7z69qmE2hIRR5txLNz9jBgR0g', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250214T220806851_ozK7z69qmE2hIRR5txLNz9jBgR0g.jpeg', '2025-02-14 22:08:07', 0);
INSERT INTO `avatar` (`id`, `user_id`, `avatar_id`, `avatar_url`, `create_time`, `is_deleted`) VALUES (5, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '20250214T220937937_ozK7z69qmE2hIRR5txLNz9jBgR0g', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250214T220937937_ozK7z69qmE2hIRR5txLNz9jBgR0g.jpeg', '2025-02-14 22:09:38', 0);
INSERT INTO `avatar` (`id`, `user_id`, `avatar_id`, `avatar_url`, `create_time`, `is_deleted`) VALUES (6, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '20250214T221021570_ozK7z69qmE2hIRR5txLNz9jBgR0g', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250214T221021570_ozK7z69qmE2hIRR5txLNz9jBgR0g.jpeg', '2025-02-14 22:10:21', 0);
INSERT INTO `avatar` (`id`, `user_id`, `avatar_id`, `avatar_url`, `create_time`, `is_deleted`) VALUES (7, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '20250214T221243498_ozK7z69qmE2hIRR5txLNz9jBgR0g', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250214T221243498_ozK7z69qmE2hIRR5txLNz9jBgR0g.jpeg', '2025-02-14 22:12:43', 0);
INSERT INTO `avatar` (`id`, `user_id`, `avatar_id`, `avatar_url`, `create_time`, `is_deleted`) VALUES (8, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '20250214T221808926_ozK7z69qmE2hIRR5txLNz9jBgR0g', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250214T221808926_ozK7z69qmE2hIRR5txLNz9jBgR0g.jpeg', '2025-02-14 22:18:09', 0);
INSERT INTO `avatar` (`id`, `user_id`, `avatar_id`, `avatar_url`, `create_time`, `is_deleted`) VALUES (9, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '20250214T221947411_ozK7z69qmE2hIRR5txLNz9jBgR0g', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250214T221947411_ozK7z69qmE2hIRR5txLNz9jBgR0g.jpeg', '2025-02-14 22:19:47', 0);
INSERT INTO `avatar` (`id`, `user_id`, `avatar_id`, `avatar_url`, `create_time`, `is_deleted`) VALUES (10, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '20250214T222144845_ozK7z69qmE2hIRR5txLNz9jBgR0g', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250214T222144845_ozK7z69qmE2hIRR5txLNz9jBgR0g.jpeg', '2025-02-14 22:21:45', 0);
INSERT INTO `avatar` (`id`, `user_id`, `avatar_id`, `avatar_url`, `create_time`, `is_deleted`) VALUES (11, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '20250214T222520757_ozK7z69qmE2hIRR5txLNz9jBgR0g', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250214T222520757_ozK7z69qmE2hIRR5txLNz9jBgR0g.jpeg', '2025-02-14 22:25:20', 0);
INSERT INTO `avatar` (`id`, `user_id`, `avatar_id`, `avatar_url`, `create_time`, `is_deleted`) VALUES (12, '0001', '20250214T223027055_0001', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250214T223027055_0001.jpg', '2025-02-14 22:30:28', 0);
INSERT INTO `avatar` (`id`, `user_id`, `avatar_id`, `avatar_url`, `create_time`, `is_deleted`) VALUES (13, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '20250214T223030122_ozK7z69qmE2hIRR5txLNz9jBgR0g', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250214T223030122_ozK7z69qmE2hIRR5txLNz9jBgR0g.jpeg', '2025-02-14 22:30:30', 0);
INSERT INTO `avatar` (`id`, `user_id`, `avatar_id`, `avatar_url`, `create_time`, `is_deleted`) VALUES (14, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '20250214T223146908_ozK7z69qmE2hIRR5txLNz9jBgR0g', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250214T223146908_ozK7z69qmE2hIRR5txLNz9jBgR0g.jpeg', '2025-02-14 22:31:47', 0);
INSERT INTO `avatar` (`id`, `user_id`, `avatar_id`, `avatar_url`, `create_time`, `is_deleted`) VALUES (15, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '20250214T223226530_ozK7z69qmE2hIRR5txLNz9jBgR0g', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250214T223226530_ozK7z69qmE2hIRR5txLNz9jBgR0g.jpeg', '2025-02-14 22:32:26', 0);
INSERT INTO `avatar` (`id`, `user_id`, `avatar_id`, `avatar_url`, `create_time`, `is_deleted`) VALUES (16, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '20250214T223650557_ozK7z69qmE2hIRR5txLNz9jBgR0g', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250214T223650557_ozK7z69qmE2hIRR5txLNz9jBgR0g.jpeg', '2025-02-14 22:36:50', 0);
INSERT INTO `avatar` (`id`, `user_id`, `avatar_id`, `avatar_url`, `create_time`, `is_deleted`) VALUES (17, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '20250214T223832317_ozK7z69qmE2hIRR5txLNz9jBgR0g', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250214T223832317_ozK7z69qmE2hIRR5txLNz9jBgR0g.jpeg', '2025-02-14 22:38:32', 0);
INSERT INTO `avatar` (`id`, `user_id`, `avatar_id`, `avatar_url`, `create_time`, `is_deleted`) VALUES (18, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '20250214T225028153_ozK7z69qmE2hIRR5txLNz9jBgR0g', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250214T225028153_ozK7z69qmE2hIRR5txLNz9jBgR0g.jpeg', '2025-02-14 22:50:28', 0);
INSERT INTO `avatar` (`id`, `user_id`, `avatar_id`, `avatar_url`, `create_time`, `is_deleted`) VALUES (19, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '20250214T231421091_ozK7z69qmE2hIRR5txLNz9jBgR0g', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250214T231421091_ozK7z69qmE2hIRR5txLNz9jBgR0g.jpeg', '2025-02-14 23:14:21', 0);
INSERT INTO `avatar` (`id`, `user_id`, `avatar_id`, `avatar_url`, `create_time`, `is_deleted`) VALUES (20, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '20250214T233150800_ozK7z69qmE2hIRR5txLNz9jBgR0g', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250214T233150800_ozK7z69qmE2hIRR5txLNz9jBgR0g.jpeg', '2025-02-14 23:31:51', 0);
INSERT INTO `avatar` (`id`, `user_id`, `avatar_id`, `avatar_url`, `create_time`, `is_deleted`) VALUES (21, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '20250214T234200402_ozK7z69qmE2hIRR5txLNz9jBgR0g', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250214T234200402_ozK7z69qmE2hIRR5txLNz9jBgR0g.jpeg', '2025-02-14 23:42:00', 0);
INSERT INTO `avatar` (`id`, `user_id`, `avatar_id`, `avatar_url`, `create_time`, `is_deleted`) VALUES (22, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '20250214T234658222_ozK7z69qmE2hIRR5txLNz9jBgR0g', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250214T234658222_ozK7z69qmE2hIRR5txLNz9jBgR0g.jpeg', '2025-02-14 23:46:58', 0);
INSERT INTO `avatar` (`id`, `user_id`, `avatar_id`, `avatar_url`, `create_time`, `is_deleted`) VALUES (23, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '20250214T235205682_ozK7z69qmE2hIRR5txLNz9jBgR0g', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250214T235205682_ozK7z69qmE2hIRR5txLNz9jBgR0g.jpeg', '2025-02-14 23:52:06', 0);
INSERT INTO `avatar` (`id`, `user_id`, `avatar_id`, `avatar_url`, `create_time`, `is_deleted`) VALUES (24, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '20250216T152552248_ozK7z69qmE2hIRR5txLNz9jBgR0g', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250216T152552248_ozK7z69qmE2hIRR5txLNz9jBgR0g.jpeg', '2025-02-16 15:25:53', 0);
INSERT INTO `avatar` (`id`, `user_id`, `avatar_id`, `avatar_url`, `create_time`, `is_deleted`) VALUES (25, 'ozK7z64u8OqUe0FtJyt0x4G9-y4M', '20250216T203333659_ozK7z64u8OqUe0FtJyt0x4G9-y4M', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250216T203333659_ozK7z64u8OqUe0FtJyt0x4G9-y4M.jpg', '2025-02-16 20:33:34', 0);
INSERT INTO `avatar` (`id`, `user_id`, `avatar_id`, `avatar_url`, `create_time`, `is_deleted`) VALUES (26, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '20250217T114106042_ozK7z69qmE2hIRR5txLNz9jBgR0g', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250217T114106042_ozK7z69qmE2hIRR5txLNz9jBgR0g.jpeg', '2025-02-17 11:41:06', 0);
INSERT INTO `avatar` (`id`, `user_id`, `avatar_id`, `avatar_url`, `create_time`, `is_deleted`) VALUES (27, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '20250217T122544565_ozK7z69qmE2hIRR5txLNz9jBgR0g', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250217T122544565_ozK7z69qmE2hIRR5txLNz9jBgR0g.jpg', '2025-02-17 12:25:44', 0);
INSERT INTO `avatar` (`id`, `user_id`, `avatar_id`, `avatar_url`, `create_time`, `is_deleted`) VALUES (28, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '20250217T122635907_ozK7z69qmE2hIRR5txLNz9jBgR0g', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250217T122635907_ozK7z69qmE2hIRR5txLNz9jBgR0g.jpg', '2025-02-17 12:26:36', 0);
INSERT INTO `avatar` (`id`, `user_id`, `avatar_id`, `avatar_url`, `create_time`, `is_deleted`) VALUES (29, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '20250217T123029617_ozK7z69qmE2hIRR5txLNz9jBgR0g', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250217T123029617_ozK7z69qmE2hIRR5txLNz9jBgR0g.jpeg', '2025-02-17 12:30:29', 0);
INSERT INTO `avatar` (`id`, `user_id`, `avatar_id`, `avatar_url`, `create_time`, `is_deleted`) VALUES (30, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '20250217T143425205_ozK7z69qmE2hIRR5txLNz9jBgR0g', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250217T143425205_ozK7z69qmE2hIRR5txLNz9jBgR0g.jpeg', '2025-02-17 14:34:25', 0);
INSERT INTO `avatar` (`id`, `user_id`, `avatar_id`, `avatar_url`, `create_time`, `is_deleted`) VALUES (31, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '20250217T143642416_ozK7z69qmE2hIRR5txLNz9jBgR0g', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250217T143642416_ozK7z69qmE2hIRR5txLNz9jBgR0g.jpeg', '2025-02-17 14:36:42', 0);
INSERT INTO `comment` (`id`, `root_id`, `reply_to`, `comment_id`, `post_id`, `user_id`, `content`, `like_count`, `unlike_count`, `reply_count`, `comment_time`, `update_time`, `is_deleted`) VALUES (1, 'pl-1', '', 'pl-1', 'dp_071993559c7a4537b54d3374fe5b5a93', '0001', '单机贴吧', 0, 0, 0, '2025-02-14 20:12:59', '2025-02-14 20:12:59', 0);
INSERT INTO `comment` (`id`, `root_id`, `reply_to`, `comment_id`, `post_id`, `user_id`, `content`, `like_count`, `unlike_count`, `reply_count`, `comment_time`, `update_time`, `is_deleted`) VALUES (2, 'pl-2', '', 'pl-2', 'dp_071993559c7a4537b54d3374fe5b5a93', 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '很难不支持', 0, 0, 0, '2025-02-14 20:12:59', '2025-02-14 20:12:59', 0);
INSERT INTO `comment` (`id`, `root_id`, `reply_to`, `comment_id`, `post_id`, `user_id`, `content`, `like_count`, `unlike_count`, `reply_count`, `comment_time`, `update_time`, `is_deleted`) VALUES (3, 'pl-3', '', 'pl-3', 'dp_071993559c7a4537b54d3374fe5b5a93', '0001', '3', 0, 0, 0, '2025-02-14 20:12:59', '2025-02-14 20:12:59', 0);
INSERT INTO `comment` (`id`, `root_id`, `reply_to`, `comment_id`, `post_id`, `user_id`, `content`, `like_count`, `unlike_count`, `reply_count`, `comment_time`, `update_time`, `is_deleted`) VALUES (4, 'pl-4', '', 'pl-4', 'dp_071993559c7a4537b54d3374fe5b5a93', '0001', '33', 0, 0, 0, '2025-02-14 20:12:59', '2025-02-14 20:12:59', 0);
INSERT INTO `comment` (`id`, `root_id`, `reply_to`, `comment_id`, `post_id`, `user_id`, `content`, `like_count`, `unlike_count`, `reply_count`, `comment_time`, `update_time`, `is_deleted`) VALUES (5, 'pl-5', '', 'pl-5', 'dp_071993559c7a4537b54d3374fe5b5a93', '0001', '333', 0, 0, 0, '2025-02-14 20:12:59', '2025-02-14 20:12:59', 0);
INSERT INTO `comment` (`id`, `root_id`, `reply_to`, `comment_id`, `post_id`, `user_id`, `content`, `like_count`, `unlike_count`, `reply_count`, `comment_time`, `update_time`, `is_deleted`) VALUES (6, 'cmt_78d2cc9c919c49a9af74a68151a452ce', '', 'cmt_78d2cc9c919c49a9af74a68151a452ce', 'dp_d2db2d3e7ee84f729bf7de3220c28510', '0001', 'Ciallo～(∠・ω< )⌒★', 0, 0, 0, '2025-02-16 14:35:22', '2025-02-16 14:35:22', 0);
INSERT INTO `comment` (`id`, `root_id`, `reply_to`, `comment_id`, `post_id`, `user_id`, `content`, `like_count`, `unlike_count`, `reply_count`, `comment_time`, `update_time`, `is_deleted`) VALUES (7, 'cmt_0a02a9e0110c47618cc41cb054dd84ac', '', 'cmt_0a02a9e0110c47618cc41cb054dd84ac', 'dp_d2db2d3e7ee84f729bf7de3220c28510', 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'Ciallo～(∠・ω< )⌒★', 0, 0, 0, '2025-02-16 15:00:28', '2025-02-16 15:00:28', 0);
INSERT INTO `comment` (`id`, `root_id`, `reply_to`, `comment_id`, `post_id`, `user_id`, `content`, `like_count`, `unlike_count`, `reply_count`, `comment_time`, `update_time`, `is_deleted`) VALUES (8, 'cmt_0a02a9e0110c47618cc41cb054dd84ac', 'cmt_0a02a9e0110c47618cc41cb054dd84ac', 'cmt_6c118e4cbaec42f3a1cd1736cce4fce2', 'dp_d2db2d3e7ee84f729bf7de3220c28510', '0001', 'Ciallo～(∠・ω< )⌒★', 0, 0, 0, '2025-02-16 15:11:13', '2025-02-16 15:11:13', 0);
INSERT INTO `comment` (`id`, `root_id`, `reply_to`, `comment_id`, `post_id`, `user_id`, `content`, `like_count`, `unlike_count`, `reply_count`, `comment_time`, `update_time`, `is_deleted`) VALUES (9, 'cmt_cf467bcb46d84cd7b2587066f70ede7a', '', 'cmt_cf467bcb46d84cd7b2587066f70ede7a', 'dp_d2db2d3e7ee84f729bf7de3220c28510', 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '333', 0, 0, 0, '2025-02-16 15:11:34', '2025-02-16 15:11:34', 0);
INSERT INTO `discuss_post` (`id`, `discuss_post_id`, `user_id`, `school_code`, `title`, `content`, `tag`, `like_count`, `favorite_count`, `comment_count`, `type`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (1, 'abcd1', '0001', '11819', '要改大了吗1.0？', 'rt，xdx今天看见公众号文章，有没有ldx说说情况', '其他', 0, 0, 0, 0, 0, '2025-01-25 16:53:10', '2025-01-25 16:53:10', 0);
INSERT INTO `discuss_post` (`id`, `discuss_post_id`, `user_id`, `school_code`, `title`, `content`, `tag`, `like_count`, `favorite_count`, `comment_count`, `type`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (2, 'abcd2', '0001', '11819', '要改大了吗2.0？', 'rt，xdx今天看见公众号文章，有没有ldx说说情况', '其他', 0, 0, 0, 0, 0, '2025-01-25 16:53:13', '2025-01-25 16:53:13', 0);
INSERT INTO `discuss_post` (`id`, `discuss_post_id`, `user_id`, `school_code`, `title`, `content`, `tag`, `like_count`, `favorite_count`, `comment_count`, `type`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (3, 'abcd3', '0001', '11819', '要改大了吗3.0？', 'rt，xdx今天看见公众号文章，有没有ldx说说情况', '其他', 0, 0, 0, 0, 0, '2025-01-25 16:53:18', '2025-01-25 16:53:18', 0);
INSERT INTO `discuss_post` (`id`, `discuss_post_id`, `user_id`, `school_code`, `title`, `content`, `tag`, `like_count`, `favorite_count`, `comment_count`, `type`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (4, 'abcd4', '0001', '11819', '要改大了吗4.0？', 'rt，xdx今天看见公众号文章，有没有ldx说说情况', '其他', 0, 0, 0, 0, 0, '2025-01-25 16:53:23', '2025-01-25 16:53:23', 0);
INSERT INTO `discuss_post` (`id`, `discuss_post_id`, `user_id`, `school_code`, `title`, `content`, `tag`, `like_count`, `favorite_count`, `comment_count`, `type`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (5, 'abcd5', '0001', '11819', '要改大了吗5.0？', 'rt，xdx今天看见公众号文章，有没有ldx说说情况', '其他', 0, 0, 0, 0, 0, '2025-01-25 16:53:27', '2025-01-25 16:53:27', 0);
INSERT INTO `discuss_post` (`id`, `discuss_post_id`, `user_id`, `school_code`, `title`, `content`, `tag`, `like_count`, `favorite_count`, `comment_count`, `type`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (6, 'abcd6', '0001', '11819', '要改大了吗6.0？', 'rt，xdx今天看见公众号文章，有没有ldx说说情况', '其他', 0, 0, 0, 0, 0, '2025-01-25 16:53:32', '2025-01-25 16:53:32', 0);
INSERT INTO `discuss_post` (`id`, `discuss_post_id`, `user_id`, `school_code`, `title`, `content`, `tag`, `like_count`, `favorite_count`, `comment_count`, `type`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (7, 'abcd7', '0001', '11819', '要改大了吗7.0？', 'rt，xdx今天看见公众号文章，有没有ldx说说情况', '其他', 0, 0, 0, 0, 0, '2025-01-25 16:53:36', '2025-01-25 16:53:36', 0);
INSERT INTO `discuss_post` (`id`, `discuss_post_id`, `user_id`, `school_code`, `title`, `content`, `tag`, `like_count`, `favorite_count`, `comment_count`, `type`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (8, 'abcd8', '0001', '11819', '要改大了吗8.0？', 'rt，xdx今天看见公众号文章，有没有ldx说说情况', '其他', 0, 0, 0, 0, 0, '2025-01-25 16:53:41', '2025-01-25 16:53:41', 0);
INSERT INTO `discuss_post` (`id`, `discuss_post_id`, `user_id`, `school_code`, `title`, `content`, `tag`, `like_count`, `favorite_count`, `comment_count`, `type`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (9, 'abcd9', '0001', '11819', '要改大了吗9.0？', 'rt，xdx今天看见公众号文章，有没有ldx说说情况', '其他', 0, 0, 0, 0, 0, '2025-01-25 16:53:44', '2025-01-25 16:53:44', 0);
INSERT INTO `discuss_post` (`id`, `discuss_post_id`, `user_id`, `school_code`, `title`, `content`, `tag`, `like_count`, `favorite_count`, `comment_count`, `type`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (10, 'abcd10', '0001', '11819', '要改大了吗10.0？', 'rt，xdx今天看见公众号文章，有没有ldx说说情况', '其他', 0, 0, 0, 0, 0, '2025-01-25 16:53:46', '2025-01-25 16:53:46', 0);
INSERT INTO `discuss_post` (`id`, `discuss_post_id`, `user_id`, `school_code`, `title`, `content`, `tag`, `like_count`, `favorite_count`, `comment_count`, `type`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (11, 'abcd11', '0001', '11819', '要改大了吗11.0？', 'rt，xdx今天看见公众号文章，有没有ldx说说情况', '其他', 0, 0, 0, 0, 0, '2025-01-25 16:53:49', '2025-01-25 16:53:49', 0);
INSERT INTO `discuss_post` (`id`, `discuss_post_id`, `user_id`, `school_code`, `title`, `content`, `tag`, `like_count`, `favorite_count`, `comment_count`, `type`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (12, 'abcd12', '0001', '11819', '要改大了吗12.0？', 'rt，xdx今天看见公众号文章，有没有ldx说说情况', '其他', 0, 0, 0, 0, 0, '2025-01-25 16:53:52', '2025-01-25 16:53:52', 0);
INSERT INTO `discuss_post` (`id`, `discuss_post_id`, `user_id`, `school_code`, `title`, `content`, `tag`, `like_count`, `favorite_count`, `comment_count`, `type`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (13, 'abcd13', '0001', '11819', '要改大了吗13.0？', 'rt，xdx今天看见公众号文章，有没有ldx说说情况', '其他', 0, 0, 0, 0, 0, '2025-01-25 16:53:55', '2025-01-25 16:53:55', 0);
INSERT INTO `discuss_post` (`id`, `discuss_post_id`, `user_id`, `school_code`, `title`, `content`, `tag`, `like_count`, `favorite_count`, `comment_count`, `type`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (14, 'abcd14', '0001', '11819', '要改大了吗14.0？', 'rt，xdx今天看见公众号文章，有没有ldx说说情况', '其他', 0, 0, 0, 0, 0, '2025-01-25 16:53:58', '2025-01-25 16:53:58', 0);
INSERT INTO `discuss_post` (`id`, `discuss_post_id`, `user_id`, `school_code`, `title`, `content`, `tag`, `like_count`, `favorite_count`, `comment_count`, `type`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15, 'abcd15', '0001', '11819', '要改大了吗15.0？', 'rt，xdx今天看见公众号文章，有没有ldx说说情况', '其他', 0, 0, 0, 0, 0, '2025-01-25 16:54:01', '2025-01-25 16:54:01', 0);
INSERT INTO `discuss_post` (`id`, `discuss_post_id`, `user_id`, `school_code`, `title`, `content`, `tag`, `like_count`, `favorite_count`, `comment_count`, `type`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (16, 'abcd16', '0001', '11819', '要改大了吗16.0？', 'rt，xdx今天看见公众号文章，有没有ldx说说情况', '其他', 0, 0, 0, 0, 0, '2025-01-25 16:54:03', '2025-01-25 16:54:03', 0);
INSERT INTO `discuss_post` (`id`, `discuss_post_id`, `user_id`, `school_code`, `title`, `content`, `tag`, `like_count`, `favorite_count`, `comment_count`, `type`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (17, 'dp_0781bf5a34d6484b87a7d8805a48b72a', '0001', '11819', '333', '3333', '其他', 0, 0, 0, 0, 0, '2025-01-27 19:16:21', '2025-02-16 14:38:07', 0);
INSERT INTO `discuss_post` (`id`, `discuss_post_id`, `user_id`, `school_code`, `title`, `content`, `tag`, `like_count`, `favorite_count`, `comment_count`, `type`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (18, 'dp_0bc7d404c74e4e7bbf6bc60be452849e', '0001', '11819', '333', '3333', '其他', 0, 0, 0, 0, 0, '2025-01-27 19:17:43', '2025-02-16 14:38:07', 0);
INSERT INTO `discuss_post` (`id`, `discuss_post_id`, `user_id`, `school_code`, `title`, `content`, `tag`, `like_count`, `favorite_count`, `comment_count`, `type`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (20, 'dp_071993559c7a4537b54d3374fe5b5a93', '0001', '11819', '我是炮姐的狗！', '你指尖跃动的电光，是我此生不变的信仰🐱‍🏍', '其他', 0, 1, 5, 0, 0, '2025-01-27 19:31:59', '2025-01-27 19:31:59', 0);
INSERT INTO `discuss_post` (`id`, `discuss_post_id`, `user_id`, `school_code`, `title`, `content`, `tag`, `like_count`, `favorite_count`, `comment_count`, `type`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (21, 'dp_d2db2d3e7ee84f729bf7de3220c28510', '0001', '11819', '我永远喜欢幼刀！', 'Ciallo～(∠・ω< )⌒★Ciallo～(∠・ω< )⌒★Ciallo～(∠・ω< )⌒★Ciallo～(∠・ω< )⌒★Ciallo～(∠・ω< )⌒★', '其他', 0, 1, 0, 0, 0, '2025-01-27 19:43:30', '2025-01-27 19:43:30', 0);
INSERT INTO `discuss_post` (`id`, `discuss_post_id`, `user_id`, `school_code`, `title`, `content`, `tag`, `like_count`, `favorite_count`, `comment_count`, `type`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (22, 'dp_5b22f1825775405d807b72d28f9785b7', '0001', '11819', '找学习搭子', '松山湖校区找英语四六级学习搭子', '学习', 0, 0, 0, 0, 0, '2025-02-16 19:12:12', '2025-02-16 19:12:12', 0);
INSERT INTO `discuss_post` (`id`, `discuss_post_id`, `user_id`, `school_code`, `title`, `content`, `tag`, `like_count`, `favorite_count`, `comment_count`, `type`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (23, 'dp_a9ca7c3004fe413a9ca213516a4f5e7d', 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '11819', '找乒乓球搭子', '松山湖校区找乒乓球搭子', '运动', 0, 0, 0, 0, 0, '2025-02-16 20:17:21', '2025-02-16 20:17:21', 0);
INSERT INTO `discuss_post` (`id`, `discuss_post_id`, `user_id`, `school_code`, `title`, `content`, `tag`, `like_count`, `favorite_count`, `comment_count`, `type`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (24, 'dp_300ea871b24248ff80f1cc0b5d9f851f', 'ozK7z64u8OqUe0FtJyt0x4G9-y4M', '11819', '怎么追到小美', '😭小美，我的小美', '休闲', 0, 0, 0, 0, 0, '2025-02-16 20:37:53', '2025-02-16 20:37:53', 0);
INSERT INTO `discuss_post` (`id`, `discuss_post_id`, `user_id`, `school_code`, `title`, `content`, `tag`, `like_count`, `favorite_count`, `comment_count`, `type`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (25, 'dp_8c83ea096bb5467cb6b6c7ed490ab19b', 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '11819', '测试话题1', '2333', '其他', 0, 0, 0, 0, 0, '2025-02-17 10:34:56', '2025-02-17 10:34:56', 0);
INSERT INTO `favorite` (`id`, `user_id`, `post_id`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (1, '0001', 'dp_071993559c7a4537b54d3374fe5b5a93', 1, '2025-02-07 20:27:39', '2025-02-14 20:46:08', 0);
INSERT INTO `favorite` (`id`, `user_id`, `post_id`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (2, '0001', 'dp_6ae2f9fecefb49279b7be391dce565be', 1, '2025-02-07 20:27:55', '2025-02-16 14:51:53', 1);
INSERT INTO `favorite` (`id`, `user_id`, `post_id`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (3, '0001', 'dp_d2db2d3e7ee84f729bf7de3220c28510', 1, '2025-02-07 20:28:05', '2025-02-16 14:55:38', 0);
INSERT INTO `favorite` (`id`, `user_id`, `post_id`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (4, '0001', 'dp_a41f410535c34dc3bf65c2d0718aa5d9', 1, '2025-02-07 20:28:13', '2025-02-16 14:55:38', 1);
INSERT INTO `favorite` (`id`, `user_id`, `post_id`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (5, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'dp_071993559c7a4537b54d3374fe5b5a93', 1, '2025-02-14 20:42:05', '2025-02-16 14:58:50', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (1, '0001', '20250127T192511757_0001', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/20250127T192511757_0001.jpg', '2025-01-27 19:25:13', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (2, '0001', '20250127T194155146_0001', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/20250127T194155146_0001.png', '2025-01-27 19:41:55', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (3, '0001', 'img_a107fe06add7488eb0213bd02ceb9d20', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/trade_post_image/img_a107fe06add7488eb0213bd02ceb9d20.jpg', '2025-01-31 01:21:36', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (4, '0001', 'img_9dfdf6e1d26a413eb3c1b3fcc46c58e1', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/trade_post_image/img_9dfdf6e1d26a413eb3c1b3fcc46c58e1.jpg', '2025-01-31 01:34:37', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (5, '0001', 'img_384f09b3e6aa4c0f87dd479b41959beb', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/trade_post_image/img_384f09b3e6aa4c0f87dd479b41959beb.jpg', '2025-01-31 01:35:14', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (6, '0001', 'img_dc1f7a7a1c2f44cdbec23d14bd46788c', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/trade_post_image/img_dc1f7a7a1c2f44cdbec23d14bd46788c.jpg', '2025-01-31 01:35:23', 1);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (7, '0001', 'img_a1ccdb62cacb4c4dbb5fa391562dacea', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/trade_post_image/img_a1ccdb62cacb4c4dbb5fa391562dacea.jpg', '2025-01-31 01:37:47', 1);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (8, '0001', 'img_27da7a227bc44094bb0c8d4937cceb2d', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/trade_post_image/img_27da7a227bc44094bb0c8d4937cceb2d.jpg', '2025-01-31 01:44:20', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (9, '0001', 'img_e7916dcaef16447d87300356d5e8eb5f', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/trade_post_image/img_e7916dcaef16447d87300356d5e8eb5f.jpg', '2025-01-31 01:44:28', 1);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (10, '0001', 'img_45976c908e964ccdb13c1733b94ba799', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/trade_post_image/img_45976c908e964ccdb13c1733b94ba799.jpg', '2025-01-31 01:51:43', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (11, '0001', 'img_32b00fdd0f634d2a96defdcbe89aa873', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/trade_post_image/img_32b00fdd0f634d2a96defdcbe89aa873.png', '2025-01-31 01:54:12', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (12, '0001', 'img_64145d82b19b4c6eb83ee4e18b38e431', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/trade_post_image/img_64145d82b19b4c6eb83ee4e18b38e431.png', '2025-01-31 01:57:47', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (13, '0001', 'img_5b0f5cab48d9432784683bf128984766', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/trade_post_image/img_5b0f5cab48d9432784683bf128984766.png', '2025-01-31 01:59:52', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (14, '0001', 'img_2068f9cc299a4cb68de4b24c8ded35fb', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/trade_post_image/img_2068f9cc299a4cb68de4b24c8ded35fb.png', '2025-01-31 02:00:20', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (15, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_e4be7ef575f1458baaffc31ff82f93e6', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_e4be7ef575f1458baaffc31ff82f93e6.png', '2025-02-16 15:29:29', 1);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (16, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_1f6c353f7fcc4adeaacd1c3a8b87ae38', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_1f6c353f7fcc4adeaacd1c3a8b87ae38.png', '2025-02-16 15:30:50', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (17, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_1314cf1a54aa4e4ca49a03afb7752c9b', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_1314cf1a54aa4e4ca49a03afb7752c9b.png', '2025-02-16 15:31:33', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (18, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_04adc46911614d738857db27ad53af0e', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_04adc46911614d738857db27ad53af0e.png', '2025-02-16 15:34:42', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (19, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_7ca1d37404ec4a8da5c677c57b5578ce', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_7ca1d37404ec4a8da5c677c57b5578ce.png', '2025-02-16 15:38:07', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (20, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_67476be714f745c5ba9e2838f9084604', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_67476be714f745c5ba9e2838f9084604.png', '2025-02-16 15:38:10', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (21, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_3c617724827d4348ac2b16972d492c08', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_3c617724827d4348ac2b16972d492c08.png', '2025-02-16 15:39:09', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (22, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_8b757585b6144ec2b8f38e2198d4d42e', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_8b757585b6144ec2b8f38e2198d4d42e.png', '2025-02-16 15:39:34', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (23, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_fbf90c40ba914acb81d22234ac66d4c1', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_fbf90c40ba914acb81d22234ac66d4c1.png', '2025-02-16 15:43:57', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (24, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_1c93beec34ff492c913bec31b0315e96', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_1c93beec34ff492c913bec31b0315e96.png', '2025-02-16 15:45:15', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (25, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_189522511f9147de930b94c98f6ddd15', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_189522511f9147de930b94c98f6ddd15.png', '2025-02-16 15:50:18', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (26, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_a01914e222234f02af45c4730c34c52f', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_a01914e222234f02af45c4730c34c52f.png', '2025-02-16 15:51:40', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (27, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_34ee9bca4ef14d9485da71c042f16120', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_34ee9bca4ef14d9485da71c042f16120.png', '2025-02-16 15:57:20', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (28, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_6efdaa6f7e5f4f548aaa0879a9a015bc', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_6efdaa6f7e5f4f548aaa0879a9a015bc.png', '2025-02-16 15:57:48', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (29, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_f10cbb16274340b79fb01c2c2067a5e0', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_f10cbb16274340b79fb01c2c2067a5e0.png', '2025-02-16 15:59:28', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (30, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_2755d5acdd634129a7c56ea3a1961138', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_2755d5acdd634129a7c56ea3a1961138.png', '2025-02-16 15:59:32', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (31, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_f7640dae2f0c43eeab330fb04a88a78f', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_f7640dae2f0c43eeab330fb04a88a78f.png', '2025-02-16 16:00:11', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (32, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_0a5e2f972bc74c0bb7c022d8d25d52e0', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_0a5e2f972bc74c0bb7c022d8d25d52e0.png', '2025-02-16 16:00:14', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (33, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_4825985e9831450390931ecbbbfc155e', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_4825985e9831450390931ecbbbfc155e.png', '2025-02-16 16:02:44', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (34, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_94ac3d1dbf954fb8b73dd868e79cce36', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_94ac3d1dbf954fb8b73dd868e79cce36.png', '2025-02-16 16:03:16', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (35, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_0b2d689bf774451c887fd96865ee228a', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_0b2d689bf774451c887fd96865ee228a.png', '2025-02-16 16:05:17', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (36, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_90ea67772eae47328a482334cd161756', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_90ea67772eae47328a482334cd161756.png', '2025-02-16 16:05:47', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (37, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_b7ee451cbb7443348ecbd938d2b3044e', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_b7ee451cbb7443348ecbd938d2b3044e.png', '2025-02-16 16:07:48', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (38, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_899242a3ab184a1dbc3ea13ed5c115c0', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_899242a3ab184a1dbc3ea13ed5c115c0.png', '2025-02-16 16:07:51', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (39, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_90aa14688b22461cbc9699a4dd9aec97', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_90aa14688b22461cbc9699a4dd9aec97.png', '2025-02-16 16:08:16', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (40, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_c03f33f19f044aaebe4a1e4def03e8ce', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_c03f33f19f044aaebe4a1e4def03e8ce.png', '2025-02-16 16:08:19', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (41, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_a92a06ab067540869a14557b10c4f3c8', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_a92a06ab067540869a14557b10c4f3c8.png', '2025-02-16 16:08:22', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (42, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_6f6177eb5ea14e7aa12fcdf60eb755d5', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_6f6177eb5ea14e7aa12fcdf60eb755d5.png', '2025-02-16 16:09:34', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (43, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_b75b50583c47424396971d35bc6b01bd', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_b75b50583c47424396971d35bc6b01bd.png', '2025-02-16 16:09:39', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (44, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_48846beb293b4c1dabefa250c7f0811d', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_48846beb293b4c1dabefa250c7f0811d.png', '2025-02-16 16:09:48', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (45, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_39b52f395cde4534baff490b4a1b76a2', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_39b52f395cde4534baff490b4a1b76a2.png', '2025-02-16 16:10:31', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (46, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_81d87296d6b64eb49a84e0778ad51630', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_81d87296d6b64eb49a84e0778ad51630.png', '2025-02-16 16:16:43', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (47, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_7d41973b2a874377bc6846e2aecebed4', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_7d41973b2a874377bc6846e2aecebed4.png', '2025-02-16 16:18:19', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (48, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_eef7a24b7aee4022a13fb29f52a29059', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_eef7a24b7aee4022a13fb29f52a29059.png', '2025-02-16 16:19:06', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (49, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_a40418e152a54a10aeedcfa348b0f245', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_a40418e152a54a10aeedcfa348b0f245.png', '2025-02-16 16:22:59', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (50, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_23e824dfc48f4498b3cbb13861b0dc50', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_23e824dfc48f4498b3cbb13861b0dc50.png', '2025-02-16 16:28:04', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (51, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_c07a688d26584c6686753f5d6a68a6c5', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_c07a688d26584c6686753f5d6a68a6c5.png', '2025-02-16 16:31:06', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (52, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_35aa5cf9a44e4d458f4ee53905896134', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_35aa5cf9a44e4d458f4ee53905896134.png', '2025-02-16 16:45:43', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (53, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_75cab687d9b14a08b8913c0d21ba5911', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_75cab687d9b14a08b8913c0d21ba5911.png', '2025-02-16 16:51:00', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (54, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_cfa1bcb9a3b44fd6b1809c56477b90ed', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_cfa1bcb9a3b44fd6b1809c56477b90ed.png', '2025-02-16 17:05:28', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (55, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_78a25c96511a4b838ecc1af07ff51933', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_78a25c96511a4b838ecc1af07ff51933.png', '2025-02-16 17:05:53', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (56, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_6273d6639a8f462db33821b3ea3fa83b', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_6273d6639a8f462db33821b3ea3fa83b.png', '2025-02-16 17:06:09', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (57, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_9aae171e14e04e568dc008a269d99bf6', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_9aae171e14e04e568dc008a269d99bf6.png', '2025-02-16 17:09:07', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (58, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_ae22df1d569a42f98918912cad8cc374', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_ae22df1d569a42f98918912cad8cc374.png', '2025-02-16 17:12:51', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (59, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_ae5820f0b4d249bb9ca7c6930b8e86e7', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_ae5820f0b4d249bb9ca7c6930b8e86e7.png', '2025-02-16 17:16:39', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (60, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_a39362cf26b540588fc75444e38035dd', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_a39362cf26b540588fc75444e38035dd.png', '2025-02-16 17:17:52', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (61, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_abb28650d8114514b384272740064440', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_abb28650d8114514b384272740064440.png', '2025-02-16 17:18:15', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (62, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_3e69053912774a0f970f2ac8c9ab9826', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_3e69053912774a0f970f2ac8c9ab9826.png', '2025-02-16 17:18:26', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (63, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_7e590cabb2ce43b4a98251b502afacc2', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_7e590cabb2ce43b4a98251b502afacc2.png', '2025-02-16 17:20:10', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (64, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_5c3cb977931a4784b3330fc5b1c18c3d', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_5c3cb977931a4784b3330fc5b1c18c3d.png', '2025-02-16 17:20:12', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (65, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_3135ac72459245478a1a2df70e7903ef', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_3135ac72459245478a1a2df70e7903ef.png', '2025-02-16 17:20:15', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (66, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_d08a0cf4c9544d64899b714e28b89850', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_d08a0cf4c9544d64899b714e28b89850.png', '2025-02-16 17:20:55', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (67, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_13fe743fe705421698199da2b63204ed', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_13fe743fe705421698199da2b63204ed.png', '2025-02-16 17:20:57', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (68, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_7c103725b6eb4a1094a8b94890261848', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_7c103725b6eb4a1094a8b94890261848.png', '2025-02-16 20:13:39', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (69, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'img_12882682b74a4b9cb9067595fc429fcf', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_12882682b74a4b9cb9067595fc429fcf.png', '2025-02-16 20:17:14', 0);
INSERT INTO `image` (`id`, `user_id`, `image_id`, `image_url`, `create_time`, `is_deleted`) VALUES (70, 'ozK7z64u8OqUe0FtJyt0x4G9-y4M', 'img_3d02de3800354c489d7482f99829215c', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/img_3d02de3800354c489d7482f99829215c.jpg', '2025-02-16 20:37:46', 0);
INSERT INTO `like` (`id`, `user_id`, `to_id`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (1, '0001', 'dp_071993559c7a4537b54d3374fe5b5a93', 1, '2025-02-14 21:29:54', '2025-02-15 13:14:20', 0);
INSERT INTO `post_image` (`id`, `post_id`, `image_id`, `create_time`, `update_time`, `is_deleted`) VALUES (1, 'dp_071993559c7a4537b54d3374fe5b5a93', '20250127T192511757_0001', '2025-01-27 19:31:59', '2025-01-27 19:31:59', 0);
INSERT INTO `post_image` (`id`, `post_id`, `image_id`, `create_time`, `update_time`, `is_deleted`) VALUES (2, 'dp_d2db2d3e7ee84f729bf7de3220c28510', '20250127T194155146_0001', '2025-01-27 19:43:30', '2025-01-27 19:43:30', 0);
INSERT INTO `post_image` (`id`, `post_id`, `image_id`, `create_time`, `update_time`, `is_deleted`) VALUES (3, 'tp_8c4cbcea62d0452eb25471522a47ade0', 'img_a107fe06add7488eb0213bd02ceb9d20', '2025-01-31 01:27:54', '2025-01-31 01:27:54', 0);
INSERT INTO `post_image` (`id`, `post_id`, `image_id`, `create_time`, `update_time`, `is_deleted`) VALUES (4, 'tp_3c63fe45ba0e4b3f9c8e20dd2627c713', 'img_dc1f7a7a1c2f44cdbec23d14bd46788c', '2025-01-31 01:51:16', '2025-01-31 01:51:16', 0);
INSERT INTO `post_image` (`id`, `post_id`, `image_id`, `create_time`, `update_time`, `is_deleted`) VALUES (5, 'tp_a56526c1563c49719360734f7f73d1b6', 'img_45976c908e964ccdb13c1733b94ba799', '2025-01-31 01:52:26', '2025-01-31 01:52:26', 0);
INSERT INTO `post_image` (`id`, `post_id`, `image_id`, `create_time`, `update_time`, `is_deleted`) VALUES (6, 'tp_1016e486f03a4c24a8425d9bceea7444', 'img_32b00fdd0f634d2a96defdcbe89aa873', '2025-01-31 01:55:33', '2025-01-31 01:55:33', 0);
INSERT INTO `post_image` (`id`, `post_id`, `image_id`, `create_time`, `update_time`, `is_deleted`) VALUES (7, 'tp_7dd4fac16e304b079d1aa2c8e92d5aad', 'img_2068f9cc299a4cb68de4b24c8ded35fb', '2025-01-31 02:00:39', '2025-01-31 02:00:39', 0);
INSERT INTO `post_image` (`id`, `post_id`, `image_id`, `create_time`, `update_time`, `is_deleted`) VALUES (8, 'tp_7dd4fac16e304b079d1aa2c8e92d5aad', 'img_5b0f5cab48d9432784683bf128984766', '2025-01-31 02:00:39', '2025-01-31 02:00:39', 0);
INSERT INTO `post_image` (`id`, `post_id`, `image_id`, `create_time`, `update_time`, `is_deleted`) VALUES (9, 'tp_7dd4fac16e304b079d1aa2c8e92d5aad', 'img_64145d82b19b4c6eb83ee4e18b38e431', '2025-01-31 02:00:39', '2025-01-31 02:00:39', 0);
INSERT INTO `post_image` (`id`, `post_id`, `image_id`, `create_time`, `update_time`, `is_deleted`) VALUES (10, 'dp_a41f410535c34dc3bf65c2d0718aa5d9', '20250127T194155146_0001', '2025-02-07 15:37:35', '2025-02-07 15:37:35', 0);
INSERT INTO `post_image` (`id`, `post_id`, `image_id`, `create_time`, `update_time`, `is_deleted`) VALUES (11, 'dp_6ae2f9fecefb49279b7be391dce565be', '20250127T194155146_0001', '2025-02-07 15:41:17', '2025-02-07 15:41:17', 0);
INSERT INTO `post_image` (`id`, `post_id`, `image_id`, `create_time`, `update_time`, `is_deleted`) VALUES (12, 'dp_5b22f1825775405d807b72d28f9785b7', '20250127T194155146_0001', '2025-02-16 19:12:12', '2025-02-16 19:12:12', 0);
INSERT INTO `post_image` (`id`, `post_id`, `image_id`, `create_time`, `update_time`, `is_deleted`) VALUES (13, 'dp_a9ca7c3004fe413a9ca213516a4f5e7d', 'img_12882682b74a4b9cb9067595fc429fcf', '2025-02-16 20:17:21', '2025-02-16 20:17:21', 0);
INSERT INTO `post_image` (`id`, `post_id`, `image_id`, `create_time`, `update_time`, `is_deleted`) VALUES (14, 'dp_300ea871b24248ff80f1cc0b5d9f851f', 'img_3d02de3800354c489d7482f99829215c', '2025-02-16 20:37:53', '2025-02-16 20:37:53', 0);
INSERT INTO `school` (`id`, `school_code`, `school_name`, `discuss_post_count`, `trade_post_count`, `student_count`, `is_deleted`) VALUES (1, '11819', '东莞理工学院', 0, 0, 2, 0);
INSERT INTO `sfile` (`id`, `sfile_id`, `sfile_name`, `sfile_url`, `sfile_size`, `download_count`, `sfile_type`, `create_by`, `update_by`, `create_time`, `update_time`, `is_deleted`) VALUES (1, 'cc137389-2801-4b7b-a41d-98409d81f3be', '3-2023年《数据库系统原理》期末考试卷(1).pdf', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/数据库/3-2023年《数据库系统原理》期末考试卷(1).pdf', 159460, 0, '数据库', '0001', '0001', '2025-01-26 11:20:10', '2025-01-26 18:19:01', 1);
INSERT INTO `trade_post` (`id`, `trade_post_id`, `user_id`, `title`, `content`, `price`, `type`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (1, 'tp_8c4cbcea62d0452eb25471522a47ade0', '0001', '菲比图', '湖出菲比等身立牌', 15.5, 0, 0, '2025-01-31 01:27:54', '2025-01-31 01:27:54', 0);
INSERT INTO `trade_post` (`id`, `trade_post_id`, `user_id`, `title`, `content`, `price`, `type`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (2, 'tp_3c63fe45ba0e4b3f9c8e20dd2627c713', '0001', '小鸟游六花', '湖出小鸟游六花镭射票', 12, 0, 0, '2025-01-31 01:51:16', '2025-01-31 01:51:16', 0);
INSERT INTO `trade_post` (`id`, `trade_post_id`, `user_id`, `title`, `content`, `price`, `type`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (3, 'tp_a56526c1563c49719360734f7f73d1b6', '0001', '孤独摇滚', '湖出孤独摇滚主角团抱枕', 56, 0, 0, '2025-01-31 01:52:26', '2025-01-31 01:52:26', 0);
INSERT INTO `trade_post` (`id`, `trade_post_id`, `user_id`, `title`, `content`, `price`, `type`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (4, 'tp_1016e486f03a4c24a8425d9bceea7444', '0001', '求芙莉莲图', 'rt，求多张类似的芙莉莲动画优美原图', 33, 1, 0, '2025-01-31 01:55:33', '2025-01-31 02:01:07', 0);
INSERT INTO `trade_post` (`id`, `trade_post_id`, `user_id`, `title`, `content`, `price`, `type`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (5, 'tp_7dd4fac16e304b079d1aa2c8e92d5aad', '0001', '原图发放', '百张自截夏目友人帐动漫好图', 0, 2, 0, '2025-01-31 02:00:39', '2025-01-31 02:01:11', 0);
INSERT INTO `trade_post` (`id`, `trade_post_id`, `user_id`, `title`, `content`, `price`, `type`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (6, 'tp_9c4d9312b3b7422c80cd9bd8263d80e5', '0001', 'C语言学生管理系统代', '找期末作业代做', 50, 1, 0, '2025-01-31 02:03:51', '2025-01-31 02:03:51', 0);
INSERT INTO `user` (`id`, `user_id`, `user_name`, `school_code`, `real_name`, `phone`, `gender`, `avatar`, `signature`, `create_time`, `update_time`, `is_deleted`) VALUES (1, '0001', 'Aseubel', '11819', '杨之耀', '13709670518', 0, 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250125T151132125_0001.jpg', '逸一时误一世', '2025-01-12 22:48:20', '2025-01-25 15:11:41', 0);
INSERT INTO `user` (`id`, `user_id`, `user_name`, `school_code`, `real_name`, `phone`, `gender`, `avatar`, `signature`, `create_time`, `update_time`, `is_deleted`) VALUES (2, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '星辰.', '11819', '', '', 0, 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250217T143642416_ozK7z69qmE2hIRR5txLNz9jBgR0g.jpeg', '', '2025-01-13 21:32:06', '2025-02-17 14:36:42', 0);
INSERT INTO `user` (`id`, `user_id`, `user_name`, `school_code`, `real_name`, `phone`, `gender`, `avatar`, `signature`, `create_time`, `update_time`, `is_deleted`) VALUES (3, 'ozK7z64u8OqUe0FtJyt0x4G9-y4M', '故渊', '', '', '', 0, 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250216T203333659_ozK7z64u8OqUe0FtJyt0x4G9-y4M.jpg', '', '2025-02-16 20:33:32', '2025-02-16 20:33:34', 0);
