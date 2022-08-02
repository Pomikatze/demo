create table test
(
    id          bigserial not null primary key,
    name        varchar(128),
    comment     varchar(256),
    create_dttm timestamp,
    modify_dttm timestamp
);

create table test_role
(
    id   bigserial not null primary key,
    name varchar(64)
);

create table test_user
(
    id       bigserial not null primary key,
    name     varchar(128),
    password varchar(64),
    role     bigint,
    constraint test_role_ref foreign key (role)
        references test_role (id)
);