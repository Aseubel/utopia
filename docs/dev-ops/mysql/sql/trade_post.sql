create table trade_post
(
    id            bigint unsigned auto_increment comment '主键ID'
        primary key,
    trade_post_id varchar(36)      default ''                not null comment '帖子id',
    user_id       varchar(45)      default ''                not null comment '用户id',
    title         varchar(36)      default ''                not null comment '帖子标题',
    content       text                                       null comment '帖子内容',
    price         float            default 0                 not null comment '定价',
    type          tinyint unsigned default '0'               not null comment '0-出售;1-求购:2-赠送',
    status        tinyint unsigned default '0'               not null comment '0-未完成;1-已完成',
    create_time   datetime         default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime         default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted    tinyint unsigned default '0'               not null comment '0-未删除;1-已删除',
    constraint uk_trade_post_id
        unique (trade_post_id)
)
    comment '交易帖子表' collate = utf8mb4_bin;

create index idx_user_id
    on trade_post (user_id);

INSERT INTO utopia.trade_post (id, trade_post_id, user_id, title, content, price, type, status, create_time, update_time, is_deleted) VALUES (1, 'tp_8c4cbcea62d0452eb25471522a47ade0', '0001', '菲比图', '湖出菲比等身立牌', 15.5, 0, 0, '2025-01-31 01:27:54', '2025-01-31 01:27:54', 0);
INSERT INTO utopia.trade_post (id, trade_post_id, user_id, title, content, price, type, status, create_time, update_time, is_deleted) VALUES (2, 'tp_3c63fe45ba0e4b3f9c8e20dd2627c713', '0001', '小鸟游六花', '湖出小鸟游六花镭射票', 12, 0, 0, '2025-01-31 01:51:16', '2025-01-31 01:51:16', 0);
INSERT INTO utopia.trade_post (id, trade_post_id, user_id, title, content, price, type, status, create_time, update_time, is_deleted) VALUES (3, 'tp_a56526c1563c49719360734f7f73d1b6', '0001', '孤独摇滚', '湖出孤独摇滚主角团抱枕', 56, 0, 0, '2025-01-31 01:52:26', '2025-01-31 01:52:26', 0);
INSERT INTO utopia.trade_post (id, trade_post_id, user_id, title, content, price, type, status, create_time, update_time, is_deleted) VALUES (4, 'tp_1016e486f03a4c24a8425d9bceea7444', '0001', '求芙莉莲图', 'rt，求多张类似的芙莉莲动画优美原图', 33, 1, 0, '2025-01-31 01:55:33', '2025-01-31 02:01:07', 0);
INSERT INTO utopia.trade_post (id, trade_post_id, user_id, title, content, price, type, status, create_time, update_time, is_deleted) VALUES (5, 'tp_7dd4fac16e304b079d1aa2c8e92d5aad', '0001', '原图发放', '百张自截夏目友人帐动漫好图', 0, 2, 0, '2025-01-31 02:00:39', '2025-01-31 02:01:11', 0);
INSERT INTO utopia.trade_post (id, trade_post_id, user_id, title, content, price, type, status, create_time, update_time, is_deleted) VALUES (6, 'tp_9c4d9312b3b7422c80cd9bd8263d80e5', '0001', 'C语言学生管理系统代', '找期末作业代做', 50, 1, 0, '2025-01-31 02:03:51', '2025-01-31 02:03:51', 0);
