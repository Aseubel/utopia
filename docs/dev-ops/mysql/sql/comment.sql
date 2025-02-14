create table comment
(
    id           bigint unsigned auto_increment comment '主键ID'
        primary key,
    root_id      varchar(36)      default ''                not null comment '根/最顶评论id',
    reply_to     varchar(36)      default ''                not null comment '回复的评论id',
    comment_id   varchar(36)      default ''                not null comment '评论id',
    post_id      varchar(36)      default ''                not null comment '帖子id',
    user_id      varchar(36)      default ''                not null comment '用户id',
    content      text                                       null comment '内容',
    like_count   int unsigned     default '0'               not null comment '点赞数',
    unlike_count int unsigned     default '0'               not null comment '踩数',
    reply_count  int unsigned     default '0'               not null comment '回复评论数量',
    comment_time datetime         default CURRENT_TIMESTAMP not null comment '评论时间',
    update_time  datetime         default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted   tinyint unsigned default '0'               not null comment '0-未删除;1-已删除',
    constraint uk_comment_id
        unique (comment_id)
)
    comment '评论表' collate = utf8mb4_bin;

create index idx_post_id
    on comment (post_id);

create index idx_root_id
    on comment (root_id);

create index idx_user_id
    on comment (user_id);

INSERT INTO utopia.comment (id, root_id, reply_to, comment_id, post_id, user_id, content, like_count, unlike_count, reply_count, comment_time, update_time, is_deleted) VALUES (1, 'pl-1', '', 'pl-1', 'dp_071993559c7a4537b54d3374fe5b5a93', '0001', '单机贴吧', 0, 0, 0, '2025-02-14 20:12:59', '2025-02-14 20:12:59', 0);
INSERT INTO utopia.comment (id, root_id, reply_to, comment_id, post_id, user_id, content, like_count, unlike_count, reply_count, comment_time, update_time, is_deleted) VALUES (2, 'pl-2', '', 'pl-2', 'dp_071993559c7a4537b54d3374fe5b5a93', 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '很难不支持', 0, 0, 0, '2025-02-14 20:12:59', '2025-02-14 20:12:59', 0);
INSERT INTO utopia.comment (id, root_id, reply_to, comment_id, post_id, user_id, content, like_count, unlike_count, reply_count, comment_time, update_time, is_deleted) VALUES (3, 'pl-3', '', 'pl-3', 'dp_071993559c7a4537b54d3374fe5b5a93', '0001', '3', 0, 0, 0, '2025-02-14 20:12:59', '2025-02-14 20:12:59', 0);
INSERT INTO utopia.comment (id, root_id, reply_to, comment_id, post_id, user_id, content, like_count, unlike_count, reply_count, comment_time, update_time, is_deleted) VALUES (4, 'pl-4', '', 'pl-4', 'dp_071993559c7a4537b54d3374fe5b5a93', '0001', '33', 0, 0, 0, '2025-02-14 20:12:59', '2025-02-14 20:12:59', 0);
INSERT INTO utopia.comment (id, root_id, reply_to, comment_id, post_id, user_id, content, like_count, unlike_count, reply_count, comment_time, update_time, is_deleted) VALUES (5, 'pl-5', '', 'pl-5', 'dp_071993559c7a4537b54d3374fe5b5a93', '0001', '333', 0, 0, 0, '2025-02-14 20:12:59', '2025-02-14 20:12:59', 0);
