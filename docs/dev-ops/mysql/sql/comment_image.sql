create table comment_image
(
    id          bigint unsigned auto_increment comment '主键ID'
        primary key,
    comment_id  varchar(36)      default ''                not null comment '评论id',
    image_id    varchar(36)      default ''                not null comment '图片id',
    create_time datetime         default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime         default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted  tinyint unsigned default '0'               not null comment '0-未删除;1-已删除'
)
    comment '评论-图片中间表' collate = utf8mb4_bin;

create index idx_comment_id
    on comment_image (comment_id);

