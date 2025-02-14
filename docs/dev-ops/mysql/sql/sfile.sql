create table sfile
(
    id             bigint unsigned auto_increment comment '主键ID'
        primary key,
    sfile_id       varchar(36)      default ''                not null comment '文件id',
    sfile_name     varchar(64)      default ''                not null comment '文件名',
    sfile_url      varchar(255)     default ''                not null comment '文件url',
    sfile_size     bigint unsigned  default '0'               not null comment '文件大小',
    download_count int unsigned     default '0'               not null comment '下载次数',
    sfile_type     varchar(32)      default ''                not null comment '文件所属类型',
    create_by      varchar(36)      default ''                not null comment '创建者',
    update_by      varchar(36)      default ''                not null comment '更新者',
    create_time    datetime         default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time    datetime         default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted     tinyint unsigned default '0'               not null comment '0-未删除;1-已删除',
    constraint uk_sfile_id
        unique (sfile_id)
)
    comment '文件表' collate = utf8mb4_bin;

INSERT INTO utopia.sfile (id, sfile_id, sfile_name, sfile_url, sfile_size, download_count, sfile_type, create_by, update_by, create_time, update_time, is_deleted) VALUES (1, 'cc137389-2801-4b7b-a41d-98409d81f3be', '3-2023年《数据库系统原理》期末考试卷(1).pdf', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/数据库/3-2023年《数据库系统原理》期末考试卷(1).pdf', 159460, 0, '数据库', '0001', '0001', '2025-01-26 11:20:10', '2025-01-26 18:19:01', 1);
