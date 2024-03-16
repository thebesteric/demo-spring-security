SpringSecurity 内置了一个通过 SpringJDBC 来访问 MySQL 的 demo 案例：用户，权限 \
脚本：spring-security-core-6.2.0.jar!/org/springframework/security/core/userdetails/jdbc/users.ddl
```sql
create table users(username varchar(50) not null primary key,password varchar(500) not null,enabled boolean not null);
create table authorities (username varchar(50) not null,authority varchar(50) not null,constraint fk_authorities_users foreign key(username) references users(username));
create unique index ix_auth_username on authorities (username,authority);
```