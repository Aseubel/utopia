create table `like`
(
    id          bigint unsigned auto_increment comment '主键ID'
        primary key,
    user_id     varchar(45)      default ''                not null comment '用户id',
    to_id       varchar(36)      default ''                not null comment '被点赞帖子/评论id',
    status      tinyint unsigned default '1'               not null comment '点赞状态，0-未点赞;1-已点赞',
    create_time datetime         default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime         default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted  tinyint unsigned default '0'               not null comment '0-未删除;1-已删除'
)
    comment '点赞表' collate = utf8mb4_bin;

create index idx_user_id_to_id
    on `like` (user_id, to_id);

INSERT INTO utopia.`like` (id, user_id, to_id, status, create_time, update_time, is_deleted) VALUES (1, '0001', 'dp_071993559c7a4537b54d3374fe5b5a93', 1, '2025-02-14 21:29:54', '2025-02-15 13:14:20', 0);
