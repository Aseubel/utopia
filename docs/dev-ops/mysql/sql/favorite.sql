create table favorite
(
    id          bigint unsigned auto_increment comment '主键ID'
        primary key,
    user_id     varchar(45)      default ''                not null comment '用户id',
    post_id     varchar(36)      default ''                not null comment '帖子id',
    status      tinyint unsigned default '1'               not null comment '收藏状态，0-未收藏;1-已收藏',
    create_time datetime         default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime         default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted  tinyint unsigned default '0'               not null comment '0-未删除;1-已删除'
)
    comment '收藏表' collate = utf8mb4_bin;

create index idx_user_id_post_id
    on favorite (user_id, post_id);

INSERT INTO utopia.favorite (id, user_id, post_id, status, create_time, update_time, is_deleted) VALUES (1, '0001', 'dp_071993559c7a4537b54d3374fe5b5a93', 1, '2025-02-07 20:27:39', '2025-02-14 20:46:08', 0);
INSERT INTO utopia.favorite (id, user_id, post_id, status, create_time, update_time, is_deleted) VALUES (2, '0001', 'dp_6ae2f9fecefb49279b7be391dce565be', 1, '2025-02-07 20:27:55', '2025-02-07 20:27:55', 0);
INSERT INTO utopia.favorite (id, user_id, post_id, status, create_time, update_time, is_deleted) VALUES (3, '0001', 'dp_d2db2d3e7ee84f729bf7de3220c28510', 1, '2025-02-07 20:28:05', '2025-02-07 20:28:05', 0);
INSERT INTO utopia.favorite (id, user_id, post_id, status, create_time, update_time, is_deleted) VALUES (4, '0001', 'dp_a41f410535c34dc3bf65c2d0718aa5d9', 1, '2025-02-07 20:28:13', '2025-02-07 20:28:13', 0);
INSERT INTO utopia.favorite (id, user_id, post_id, status, create_time, update_time, is_deleted) VALUES (5, 'ozK7z69qmE2hIRR5txLNz9jBgR0g0001', 'dp_071993559c7a4537b54d3374fe5b5a93', 1, '2025-02-14 20:42:05', '2025-02-14 20:46:08', 0);
