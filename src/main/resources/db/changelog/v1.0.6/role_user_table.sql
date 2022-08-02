create table role_user
(
    role_id bigint not null unique,
    user_id bigint not null unique,
    primary key (role_id, user_id),
    constraint role_ref foreign key (role_id)
        references test_role (role_id),
    constraint user_ref foreign key (user_id)
        references test_user (id)
);

alter table test_user drop constraint test_role_ref;

alter table test_user add constraint test_role_ref foreign key (role) references role_user (role_id);

alter table test_role
    add column user_id bigint;

alter table test_role
    add constraint test_user_ref foreign key (user_id) references role_user (user_id);