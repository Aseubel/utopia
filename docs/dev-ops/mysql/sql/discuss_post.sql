create table discuss_post
(
    id              bigint unsigned auto_increment comment '主键ID'
        primary key,
    discuss_post_id varchar(36)      default ''                not null comment '帖子id',
    user_id         varchar(45)      default ''                not null comment '微信用户唯一标识',
    school_code     varchar(5)       default ''                not null comment '所属院校代号',
    title           varchar(36)      default ''                not null comment '帖子标题',
    content         text                                       null comment '帖子内容',
    like_count      int unsigned     default '0'               not null comment '点赞数',
    favorite_count  int unsigned     default '0'               not null comment '收藏数',
    comment_count   int unsigned     default '0'               not null comment '评论数',
    type            tinyint unsigned default '0'               not null comment '0-普通；1-置顶',
    status          tinyint unsigned default '0'               not null comment '0-普通；1-封禁',
    create_time     datetime         default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time     datetime         default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted      tinyint unsigned default '0'               not null comment '0-未删除;1-已删除',
    constraint uk_discuss_post_id_school_code
        unique (discuss_post_id, school_code)
)
    comment '帖子表' collate = utf8mb4_bin;

create index idx_update_time
    on discuss_post (update_time desc);

INSERT INTO utopia.discuss_post (id, discuss_post_id, user_id, school_code, title, content, like_count, favorite_count, comment_count, type, status, create_time, update_time, is_deleted) VALUES (1, 'abcd1', '0001', '11819', '要改大了吗1.0？', 'rt，xdx今天看见公众号文章，有没有ldx说说情况', 0, 0, 0, 0, 0, '2025-01-25 16:53:10', '2025-01-25 16:53:10', 0);
INSERT INTO utopia.discuss_post (id, discuss_post_id, user_id, school_code, title, content, like_count, favorite_count, comment_count, type, status, create_time, update_time, is_deleted) VALUES (2, 'abcd2', '0001', '11819', '要改大了吗2.0？', 'rt，xdx今天看见公众号文章，有没有ldx说说情况', 0, 0, 0, 0, 0, '2025-01-25 16:53:13', '2025-01-25 16:53:13', 0);
INSERT INTO utopia.discuss_post (id, discuss_post_id, user_id, school_code, title, content, like_count, favorite_count, comment_count, type, status, create_time, update_time, is_deleted) VALUES (3, 'abcd3', '0001', '11819', '要改大了吗3.0？', 'rt，xdx今天看见公众号文章，有没有ldx说说情况', 0, 0, 0, 0, 0, '2025-01-25 16:53:18', '2025-01-25 16:53:18', 0);
INSERT INTO utopia.discuss_post (id, discuss_post_id, user_id, school_code, title, content, like_count, favorite_count, comment_count, type, status, create_time, update_time, is_deleted) VALUES (4, 'abcd4', '0001', '11819', '要改大了吗4.0？', 'rt，xdx今天看见公众号文章，有没有ldx说说情况', 0, 0, 0, 0, 0, '2025-01-25 16:53:23', '2025-01-25 16:53:23', 0);
INSERT INTO utopia.discuss_post (id, discuss_post_id, user_id, school_code, title, content, like_count, favorite_count, comment_count, type, status, create_time, update_time, is_deleted) VALUES (5, 'abcd5', '0001', '11819', '要改大了吗5.0？', 'rt，xdx今天看见公众号文章，有没有ldx说说情况', 0, 0, 0, 0, 0, '2025-01-25 16:53:27', '2025-01-25 16:53:27', 0);
INSERT INTO utopia.discuss_post (id, discuss_post_id, user_id, school_code, title, content, like_count, favorite_count, comment_count, type, status, create_time, update_time, is_deleted) VALUES (6, 'abcd6', '0001', '11819', '要改大了吗6.0？', 'rt，xdx今天看见公众号文章，有没有ldx说说情况', 0, 0, 0, 0, 0, '2025-01-25 16:53:32', '2025-01-25 16:53:32', 0);
INSERT INTO utopia.discuss_post (id, discuss_post_id, user_id, school_code, title, content, like_count, favorite_count, comment_count, type, status, create_time, update_time, is_deleted) VALUES (7, 'abcd7', '0001', '11819', '要改大了吗7.0？', 'rt，xdx今天看见公众号文章，有没有ldx说说情况', 0, 0, 0, 0, 0, '2025-01-25 16:53:36', '2025-01-25 16:53:36', 0);
INSERT INTO utopia.discuss_post (id, discuss_post_id, user_id, school_code, title, content, like_count, favorite_count, comment_count, type, status, create_time, update_time, is_deleted) VALUES (8, 'abcd8', '0001', '11819', '要改大了吗8.0？', 'rt，xdx今天看见公众号文章，有没有ldx说说情况', 0, 0, 0, 0, 0, '2025-01-25 16:53:41', '2025-01-25 16:53:41', 0);
INSERT INTO utopia.discuss_post (id, discuss_post_id, user_id, school_code, title, content, like_count, favorite_count, comment_count, type, status, create_time, update_time, is_deleted) VALUES (9, 'abcd9', '0001', '11819', '要改大了吗9.0？', 'rt，xdx今天看见公众号文章，有没有ldx说说情况', 0, 0, 0, 0, 0, '2025-01-25 16:53:44', '2025-01-25 16:53:44', 0);
INSERT INTO utopia.discuss_post (id, discuss_post_id, user_id, school_code, title, content, like_count, favorite_count, comment_count, type, status, create_time, update_time, is_deleted) VALUES (10, 'abcd10', '0001', '11819', '要改大了吗10.0？', 'rt，xdx今天看见公众号文章，有没有ldx说说情况', 0, 0, 0, 0, 0, '2025-01-25 16:53:46', '2025-01-25 16:53:46', 0);
INSERT INTO utopia.discuss_post (id, discuss_post_id, user_id, school_code, title, content, like_count, favorite_count, comment_count, type, status, create_time, update_time, is_deleted) VALUES (11, 'abcd11', '0001', '11819', '要改大了吗11.0？', 'rt，xdx今天看见公众号文章，有没有ldx说说情况', 0, 0, 0, 0, 0, '2025-01-25 16:53:49', '2025-01-25 16:53:49', 0);
INSERT INTO utopia.discuss_post (id, discuss_post_id, user_id, school_code, title, content, like_count, favorite_count, comment_count, type, status, create_time, update_time, is_deleted) VALUES (12, 'abcd12', '0001', '11819', '要改大了吗12.0？', 'rt，xdx今天看见公众号文章，有没有ldx说说情况', 0, 0, 0, 0, 0, '2025-01-25 16:53:52', '2025-01-25 16:53:52', 0);
INSERT INTO utopia.discuss_post (id, discuss_post_id, user_id, school_code, title, content, like_count, favorite_count, comment_count, type, status, create_time, update_time, is_deleted) VALUES (13, 'abcd13', '0001', '11819', '要改大了吗13.0？', 'rt，xdx今天看见公众号文章，有没有ldx说说情况', 0, 0, 0, 0, 0, '2025-01-25 16:53:55', '2025-01-25 16:53:55', 0);
INSERT INTO utopia.discuss_post (id, discuss_post_id, user_id, school_code, title, content, like_count, favorite_count, comment_count, type, status, create_time, update_time, is_deleted) VALUES (14, 'abcd14', '0001', '11819', '要改大了吗14.0？', 'rt，xdx今天看见公众号文章，有没有ldx说说情况', 0, 0, 0, 0, 0, '2025-01-25 16:53:58', '2025-01-25 16:53:58', 0);
INSERT INTO utopia.discuss_post (id, discuss_post_id, user_id, school_code, title, content, like_count, favorite_count, comment_count, type, status, create_time, update_time, is_deleted) VALUES (15, 'abcd15', '0001', '11819', '要改大了吗15.0？', 'rt，xdx今天看见公众号文章，有没有ldx说说情况', 0, 0, 0, 0, 0, '2025-01-25 16:54:01', '2025-01-25 16:54:01', 0);
INSERT INTO utopia.discuss_post (id, discuss_post_id, user_id, school_code, title, content, like_count, favorite_count, comment_count, type, status, create_time, update_time, is_deleted) VALUES (16, 'abcd16', '0001', '11819', '要改大了吗16.0？', 'rt，xdx今天看见公众号文章，有没有ldx说说情况', 0, 0, 0, 0, 0, '2025-01-25 16:54:03', '2025-01-25 16:54:03', 0);
INSERT INTO utopia.discuss_post (id, discuss_post_id, user_id, school_code, title, content, like_count, favorite_count, comment_count, type, status, create_time, update_time, is_deleted) VALUES (17, 'dp_0781bf5a34d6484b87a7d8805a48b72a', '0002', '11819', '333', '3333', 0, 0, 0, 0, 0, '2025-01-27 19:16:21', '2025-01-27 19:17:26', 1);
INSERT INTO utopia.discuss_post (id, discuss_post_id, user_id, school_code, title, content, like_count, favorite_count, comment_count, type, status, create_time, update_time, is_deleted) VALUES (18, 'dp_0bc7d404c74e4e7bbf6bc60be452849e', '0002', '11819', '333', '3333', 0, 0, 0, 0, 0, '2025-01-27 19:17:43', '2025-01-27 19:17:51', 1);
INSERT INTO utopia.discuss_post (id, discuss_post_id, user_id, school_code, title, content, like_count, favorite_count, comment_count, type, status, create_time, update_time, is_deleted) VALUES (20, 'dp_071993559c7a4537b54d3374fe5b5a93', '0001', '11819', '我是炮姐的狗！', '你指尖跃动的电光，是我此生不变的信仰🐱‍🏍', 0, 1, 5, 0, 0, '2025-01-27 19:31:59', '2025-01-27 19:31:59', 0);
INSERT INTO utopia.discuss_post (id, discuss_post_id, user_id, school_code, title, content, like_count, favorite_count, comment_count, type, status, create_time, update_time, is_deleted) VALUES (21, 'dp_d2db2d3e7ee84f729bf7de3220c28510', '0001', '11819', '我永远喜欢幼刀！', 'Ciallo～(∠・ω< )⌒★Ciallo～(∠・ω< )⌒★Ciallo～(∠・ω< )⌒★Ciallo～(∠・ω< )⌒★Ciallo～(∠・ω< )⌒★', 0, 1, 0, 0, 0, '2025-01-27 19:43:30', '2025-01-27 19:43:30', 0);
