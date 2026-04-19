create table if not exists "user"
(
    id          bigint primary key auto_increment,
    username    varchar(50) not null unique,
    password    varchar(50) not null,
    create_time timestamp   not null
);

create table if not exists todo_category
(
    id          bigint primary key auto_increment,
    user_id     bigint      not null,
    name        varchar(30) not null,
    create_time timestamp   not null,
    constraint fk_category_user foreign key (user_id) references "user" (id)
);

create table if not exists todo
(
    id          bigint primary key auto_increment,
    user_id     bigint       not null,
    category_id bigint       null,
    title       varchar(100) not null,
    notes       text         null,
    status      tinyint      not null,
    deadline    timestamp    null,
    create_time timestamp    not null,
    update_time timestamp    not null,
    constraint fk_todo_user foreign key (user_id) references "user" (id),
    constraint fk_todo_category foreign key (category_id) references todo_category (id)
);
