create table user
(
    id          bigint unsigned auto_increment comment '主键ID'
        primary key,
    user_id     varchar(45)      default ''                not null comment '微信用户唯一标识',
    user_name   varchar(32)      default ''                not null comment '姓名',
    school_code varchar(5)       default ''                not null comment '院校代号',
    real_name   varchar(12)      default ''                not null comment '真实姓名',
    phone       varchar(11)      default ''                not null comment '手机号',
    gender      tinyint unsigned default '0'               not null comment '性别，0-隐藏；1-男；2-女',
    avatar      varchar(500)     default ''                not null comment '头像',
    signature   varchar(128)     default ''                not null comment '个性签名',
    create_time datetime         default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime         default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted  tinyint unsigned default '0'               not null comment '0-未删除;1-已删除',
    constraint uk_user_id
        unique (user_id)
)
    comment '用户表' collate = utf8mb4_bin;

INSERT INTO utopia.user (id, user_id, user_name, school_code, real_name, phone, gender, avatar, signature, create_time, update_time, is_deleted) VALUES (1, '0001', 'Aseubel', '11819', '杨之耀', '13709670518', 0, 'https://yangaseubel.oss-cn-guangzhou.aliyuncs.com/utopia/avatar/20250125T151132125_0001.jpg', '逸一时误一世', '2025-01-12 22:48:20', '2025-01-25 15:11:41', 0);
INSERT INTO utopia.user (id, user_id, user_name, school_code, real_name, phone, gender, avatar, signature, create_time, update_time, is_deleted) VALUES (2, 'ozK7z69qmE2hIRR5txLNz9jBgR0g', 'zempo', '11819', '郑泽皓', '', 0, '', '', '2025-01-13 21:32:06', '2025-01-13 21:50:38', 0);
