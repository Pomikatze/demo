create table test_log (
    id bigserial not null primary key,
    log text,
    create_dttm timestamp
);