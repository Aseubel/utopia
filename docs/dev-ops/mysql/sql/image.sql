create table image
(
    id          bigint unsigned auto_increment comment '主键ID'
        primary key,
    user_id     varchar(45)      default ''                not null comment '对应用户id',
    image_id    varchar(36)      default ''                not null comment '图片id',
    image_url   varchar(255)     default ''                not null comment '图片url',
    create_time datetime         default CURRENT_TIMESTAMP not null comment '创建时间',
    is_deleted  tinyint unsigned default '0'               not null comment '0-未删除;1-已删除',
    constraint uk_image_id
        unique (image_id)
)
    comment '评论帖子图片表' collate = utf8mb4_bin;

INSERT INTO utopia.image (id, user_id, image_id, image_url, create_time, is_deleted) VALUES (1, '0001', '20250127T192511757_0001', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/20250127T192511757_0001.jpg', '2025-01-27 19:25:13', 0);
INSERT INTO utopia.image (id, user_id, image_id, image_url, create_time, is_deleted) VALUES (2, '0001', '20250127T194155146_0001', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/discuss_post_image/20250127T194155146_0001.png', '2025-01-27 19:41:55', 0);
INSERT INTO utopia.image (id, user_id, image_id, image_url, create_time, is_deleted) VALUES (3, '0001', 'img_a107fe06add7488eb0213bd02ceb9d20', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/trade_post_image/img_a107fe06add7488eb0213bd02ceb9d20.jpg', '2025-01-31 01:21:36', 0);
INSERT INTO utopia.image (id, user_id, image_id, image_url, create_time, is_deleted) VALUES (4, '0001', 'img_9dfdf6e1d26a413eb3c1b3fcc46c58e1', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/trade_post_image/img_9dfdf6e1d26a413eb3c1b3fcc46c58e1.jpg', '2025-01-31 01:34:37', 0);
INSERT INTO utopia.image (id, user_id, image_id, image_url, create_time, is_deleted) VALUES (5, '0001', 'img_384f09b3e6aa4c0f87dd479b41959beb', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/trade_post_image/img_384f09b3e6aa4c0f87dd479b41959beb.jpg', '2025-01-31 01:35:14', 0);
INSERT INTO utopia.image (id, user_id, image_id, image_url, create_time, is_deleted) VALUES (6, '0001', 'img_dc1f7a7a1c2f44cdbec23d14bd46788c', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/trade_post_image/img_dc1f7a7a1c2f44cdbec23d14bd46788c.jpg', '2025-01-31 01:35:23', 0);
INSERT INTO utopia.image (id, user_id, image_id, image_url, create_time, is_deleted) VALUES (7, '0001', 'img_a1ccdb62cacb4c4dbb5fa391562dacea', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/trade_post_image/img_a1ccdb62cacb4c4dbb5fa391562dacea.jpg', '2025-01-31 01:37:47', 0);
INSERT INTO utopia.image (id, user_id, image_id, image_url, create_time, is_deleted) VALUES (8, '0001', 'img_27da7a227bc44094bb0c8d4937cceb2d', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/trade_post_image/img_27da7a227bc44094bb0c8d4937cceb2d.jpg', '2025-01-31 01:44:20', 0);
INSERT INTO utopia.image (id, user_id, image_id, image_url, create_time, is_deleted) VALUES (9, '0001', 'img_e7916dcaef16447d87300356d5e8eb5f', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/trade_post_image/img_e7916dcaef16447d87300356d5e8eb5f.jpg', '2025-01-31 01:44:28', 0);
INSERT INTO utopia.image (id, user_id, image_id, image_url, create_time, is_deleted) VALUES (10, '0001', 'img_45976c908e964ccdb13c1733b94ba799', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/trade_post_image/img_45976c908e964ccdb13c1733b94ba799.jpg', '2025-01-31 01:51:43', 0);
INSERT INTO utopia.image (id, user_id, image_id, image_url, create_time, is_deleted) VALUES (11, '0001', 'img_32b00fdd0f634d2a96defdcbe89aa873', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/trade_post_image/img_32b00fdd0f634d2a96defdcbe89aa873.png', '2025-01-31 01:54:12', 0);
INSERT INTO utopia.image (id, user_id, image_id, image_url, create_time, is_deleted) VALUES (12, '0001', 'img_64145d82b19b4c6eb83ee4e18b38e431', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/trade_post_image/img_64145d82b19b4c6eb83ee4e18b38e431.png', '2025-01-31 01:57:47', 0);
INSERT INTO utopia.image (id, user_id, image_id, image_url, create_time, is_deleted) VALUES (13, '0001', 'img_5b0f5cab48d9432784683bf128984766', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/trade_post_image/img_5b0f5cab48d9432784683bf128984766.png', '2025-01-31 01:59:52', 0);
INSERT INTO utopia.image (id, user_id, image_id, image_url, create_time, is_deleted) VALUES (14, '0001', 'img_2068f9cc299a4cb68de4b24c8ded35fb', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/trade_post_image/img_2068f9cc299a4cb68de4b24c8ded35fb.png', '2025-01-31 02:00:20', 0);
