create sequence plan_seq start 1;

create table plan
(
    id bigint not null primary key default nextval('plan_seq'),
    toDo text,
    toDo_dttm timestamp,
    user_login varchar(128)
);