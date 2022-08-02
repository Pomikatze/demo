create table SUDIS (
    id bigserial not null primary key,
    login varchar(64),
    session_id varchar(64),
    token varchar(128),
    create_dttm timestamp
);