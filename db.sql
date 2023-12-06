create database `spring-security` default character set utf8mb4 collate utf8mb4_general_ci;

create table t_user
(
    id       bigint auto_increment comment '主键' primary key,
    name     varchar(50)      not null comment '姓名',
    username varchar(50)      not null comment '用户名',
    password varchar(500)     not null comment '密码',
    enabled  bit default b'1' not null comment '是否启用：0-不启用，1-启用'
);

INSERT INTO `spring-security`.t_user (id, username, password, enabled) VALUES (1, 'admin', '123456', true);
INSERT INTO `spring-security`.t_user (id, username, password, enabled) VALUES (2, 'user', '123456', true);


create table t_role
(
    id   bigint auto_increment primary key,
    name varchar(50) null,
    tag  varchar(50) null
);

INSERT INTO `spring-security`.t_role (id, name, tag) VALUES (1, '超级管理员', 'ROLE_admin');
INSERT INTO `spring-security`.t_role (id, name, tag) VALUES (2, '普通用户', 'ROLE_user');

create table t_permission
(
    id        bigint       not null primary key,
    type      varchar(50)  null comment '权限类型：M-菜单、A-子菜单，U-请求',
    name      varchar(50)  null comment '权限名称',
    url       varchar(255) null comment '请求地址',
    tag       varchar(50)  null comment '权限描述符',
    parent_id bigint       null
);

INSERT INTO `spring-security`.t_permission (id, type, name, url, tag, parent_id) VALUES (1, 'U', '登录', '/login', 'login:login', null);
INSERT INTO `spring-security`.t_permission (id, type, name, url, tag, parent_id) VALUES (2, 'U', '进入登录页', '/login/page', 'login:login-page', null);
INSERT INTO `spring-security`.t_permission (id, type, name, url, tag, parent_id) VALUES (3, 'U', '退出', '/logout', 'login:logout', null);
INSERT INTO `spring-security`.t_permission (id, type, name, url, tag, parent_id) VALUES (4, 'U', '注册', '/register', 'reg:register', null);
INSERT INTO `spring-security`.t_permission (id, type, name, url, tag, parent_id) VALUES (5, 'U', '进入注册页', '/register/page', 'reg:register-page', null);
INSERT INTO `spring-security`.t_permission (id, type, name, url, tag, parent_id) VALUES (6, 'U', '进入主页', '/main/page', 'main:main-page', null);
INSERT INTO `spring-security`.t_permission (id, type, name, url, tag, parent_id) VALUES (7, 'M', '用户管理', null, 'user:manager', null);
INSERT INTO `spring-security`.t_permission (id, type, name, url, tag, parent_id) VALUES (8, 'A', '用户查询', '/user/list', 'user:list', 6);
INSERT INTO `spring-security`.t_permission (id, type, name, url, tag, parent_id) VALUES (9, 'U', '用户新增', '/user/add', 'user:add', 6);
INSERT INTO `spring-security`.t_permission (id, type, name, url, tag, parent_id) VALUES (10, 'U', '用户新增页面', '/user/add/page', 'user:add-page', 6);


create table r_user_role
(
    id      bigint auto_increment primary key,
    user_id bigint not null,
    role_id bigint not null,
    constraint r_user_role_t_role_id_fk foreign key (role_id) references t_role (id),
    constraint r_user_role_t_user_id_fk foreign key (user_id) references t_user (id)
);

INSERT INTO `spring-security`.r_user_role (id, user_id, role_id) VALUES (1, 1, 1);
INSERT INTO `spring-security`.r_user_role (id, user_id, role_id) VALUES (2, 2, 2);


create table r_role_permission
(
    id            bigint not null primary key,
    role_id       bigint null,
    permission_id bigint null,
    constraint r_role_permission_t_permission_id_fk foreign key (permission_id) references t_permission (id),
    constraint r_role_permission_t_role_id_fk foreign key (role_id) references t_role (id)
);

INSERT INTO `spring-security`.r_role_permission (id, role_id, permission_id) VALUES (1, 1, 1);
INSERT INTO `spring-security`.r_role_permission (id, role_id, permission_id) VALUES (2, 1, 2);
INSERT INTO `spring-security`.r_role_permission (id, role_id, permission_id) VALUES (3, 1, 3);
INSERT INTO `spring-security`.r_role_permission (id, role_id, permission_id) VALUES (4, 1, 4);
INSERT INTO `spring-security`.r_role_permission (id, role_id, permission_id) VALUES (5, 1, 5);
INSERT INTO `spring-security`.r_role_permission (id, role_id, permission_id) VALUES (6, 1, 6);
INSERT INTO `spring-security`.r_role_permission (id, role_id, permission_id) VALUES (7, 1, 7);
INSERT INTO `spring-security`.r_role_permission (id, role_id, permission_id) VALUES (8, 1, 8);
INSERT INTO `spring-security`.r_role_permission (id, role_id, permission_id) VALUES (9, 1, 9);
INSERT INTO `spring-security`.r_role_permission (id, role_id, permission_id) VALUES (10, 1, 10);