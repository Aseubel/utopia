create table avatar
(
    id          bigint unsigned auto_increment comment '主键ID'
        primary key,
    user_id     varchar(45)      default ''                not null comment '对应用户id',
    avatar_id   varchar(36)      default ''                not null comment '头像id',
    avatar_url  varchar(255)     default ''                not null comment '头像url',
    create_time datetime         default CURRENT_TIMESTAMP not null comment '创建时间',
    is_deleted  tinyint unsigned default '0'               not null comment '0-未删除;1-已删除'
)
    comment '头像表' collate = utf8mb4_bin;

INSERT INTO utopia.avatar (id, user_id, avatar_id, avatar_url, create_time, is_deleted) VALUES (1, '0001', '20250125T151132125_0001', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250125T151132125_0001.jpg', '2025-01-25 15:11:33', 0);
INSERT INTO utopia.avatar (id, user_id, avatar_id, avatar_url, create_time, is_deleted) VALUES (2, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '20250214T201124852_ozK7z69qmE2hIRR5txLNz9jBgR0g', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250214T201124852_ozK7z69qmE2hIRR5txLNz9jBgR0g.jpeg', '2025-02-14 20:15:45', 0);
INSERT INTO utopia.avatar (id, user_id, avatar_id, avatar_url, create_time, is_deleted) VALUES (3, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', '20250214T201624325_ozK7z69qmE2hIRR5txLNz9jBgR0g', 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250214T201624325_ozK7z69qmE2hIRR5txLNz9jBgR0g.jpeg', '2025-02-14 20:16:24', 0);
