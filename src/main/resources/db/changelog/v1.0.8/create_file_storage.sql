create sequence if not exists file_storage_seq start 1;

create table if not exists file_storage
(
    storage_id bigint not null default nextval('file_storage_seq'::regclass),
    token uuid not null,
    file_path varchar not null,
    file_name varchar not null,
    extension varchar(5),
    user_name varchar(128),
    constraint pk_file_storage primary key (storage_id)
);

comment on table file_storage is 'Файловое хранилище';
comment on column file_storage.storage_id is 'Идентификатор';
comment on column file_storage.token is 'Уникальный токен';
comment on column file_storage.file_path is 'Местонахождение на диске';
comment on column file_storage.file_path is 'Название файла';
comment on column file_storage.extension is 'Расширение файла';
comment on column file_storage.user_name is 'Имя пользователя';