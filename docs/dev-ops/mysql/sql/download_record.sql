create table download_record
(
    id          bigint unsigned auto_increment comment '主键ID'
        primary key,
    user_id     varchar(45)      default ''                not null comment '用户id',
    sfile_id    varchar(36)      default ''                not null comment '文件id',
    create_time datetime         default CURRENT_TIMESTAMP not null comment '创建时间',
    is_deleted  tinyint unsigned default '0'               not null comment '0-未删除;1-已删除'
)
    comment '共享文件下载记录表' collate = utf8mb4_bin;

create index idx_user_id
    on download_record (user_id);

