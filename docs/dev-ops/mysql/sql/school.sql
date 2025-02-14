create table school
(
    id                 bigint unsigned auto_increment comment '主键ID'
        primary key,
    school_code        varchar(5)       default ''  not null comment '院校代号',
    school_name        varchar(64)      default ''  not null comment '学校名称',
    discuss_post_count int unsigned     default '0' not null comment '讨论帖数目',
    trade_post_count   int unsigned     default '0' not null comment '交易帖数目',
    student_count      int unsigned     default '0' not null comment '学生(用户)人数',
    is_deleted         tinyint unsigned default '0' not null comment '0-未删除;1-已删除',
    constraint uk_school_code
        unique (school_code),
    constraint uk_school_name
        unique (school_name)
)
    comment '用户表' collate = utf8mb4_bin;

INSERT INTO utopia.school (id, school_code, school_name, discuss_post_count, trade_post_count, student_count, is_deleted) VALUES (1, '11819', '东莞理工学院', 0, 0, 2, 0);
