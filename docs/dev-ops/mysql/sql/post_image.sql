create table post_image
(
    id          bigint unsigned auto_increment comment '主键ID'
        primary key,
    post_id     varchar(36)      default ''                not null comment '帖子id',
    image_id    varchar(36)      default ''                not null comment '图片id',
    create_time datetime         default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime         default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted  tinyint unsigned default '0'               not null comment '0-未删除;1-已删除'
)
    comment '帖子-图片中间表' collate = utf8mb4_bin;

create index idx_post_id
    on post_image (post_id);

INSERT INTO utopia.post_image (id, post_id, image_id, create_time, update_time, is_deleted) VALUES (1, 'dp_071993559c7a4537b54d3374fe5b5a93', '20250127T192511757_0001', '2025-01-27 19:31:59', '2025-01-27 19:31:59', 0);
INSERT INTO utopia.post_image (id, post_id, image_id, create_time, update_time, is_deleted) VALUES (2, 'dp_d2db2d3e7ee84f729bf7de3220c28510', '20250127T194155146_0001', '2025-01-27 19:43:30', '2025-01-27 19:43:30', 0);
INSERT INTO utopia.post_image (id, post_id, image_id, create_time, update_time, is_deleted) VALUES (3, 'tp_8c4cbcea62d0452eb25471522a47ade0', 'img_a107fe06add7488eb0213bd02ceb9d20', '2025-01-31 01:27:54', '2025-01-31 01:27:54', 0);
INSERT INTO utopia.post_image (id, post_id, image_id, create_time, update_time, is_deleted) VALUES (4, 'tp_3c63fe45ba0e4b3f9c8e20dd2627c713', 'img_dc1f7a7a1c2f44cdbec23d14bd46788c', '2025-01-31 01:51:16', '2025-01-31 01:51:16', 0);
INSERT INTO utopia.post_image (id, post_id, image_id, create_time, update_time, is_deleted) VALUES (5, 'tp_a56526c1563c49719360734f7f73d1b6', 'img_45976c908e964ccdb13c1733b94ba799', '2025-01-31 01:52:26', '2025-01-31 01:52:26', 0);
INSERT INTO utopia.post_image (id, post_id, image_id, create_time, update_time, is_deleted) VALUES (6, 'tp_1016e486f03a4c24a8425d9bceea7444', 'img_32b00fdd0f634d2a96defdcbe89aa873', '2025-01-31 01:55:33', '2025-01-31 01:55:33', 0);
INSERT INTO utopia.post_image (id, post_id, image_id, create_time, update_time, is_deleted) VALUES (7, 'tp_7dd4fac16e304b079d1aa2c8e92d5aad', 'img_2068f9cc299a4cb68de4b24c8ded35fb', '2025-01-31 02:00:39', '2025-01-31 02:00:39', 0);
INSERT INTO utopia.post_image (id, post_id, image_id, create_time, update_time, is_deleted) VALUES (8, 'tp_7dd4fac16e304b079d1aa2c8e92d5aad', 'img_5b0f5cab48d9432784683bf128984766', '2025-01-31 02:00:39', '2025-01-31 02:00:39', 0);
INSERT INTO utopia.post_image (id, post_id, image_id, create_time, update_time, is_deleted) VALUES (9, 'tp_7dd4fac16e304b079d1aa2c8e92d5aad', 'img_64145d82b19b4c6eb83ee4e18b38e431', '2025-01-31 02:00:39', '2025-01-31 02:00:39', 0);
INSERT INTO utopia.post_image (id, post_id, image_id, create_time, update_time, is_deleted) VALUES (10, 'dp_a41f410535c34dc3bf65c2d0718aa5d9', '20250127T194155146_0001', '2025-02-07 15:37:35', '2025-02-07 15:37:35', 0);
INSERT INTO utopia.post_image (id, post_id, image_id, create_time, update_time, is_deleted) VALUES (11, 'dp_6ae2f9fecefb49279b7be391dce565be', '20250127T194155146_0001', '2025-02-07 15:41:17', '2025-02-07 15:41:17', 0);
