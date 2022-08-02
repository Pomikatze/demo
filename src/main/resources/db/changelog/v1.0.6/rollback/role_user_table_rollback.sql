alter table test_user drop constraint test_role_ref;

alter table test_user add constraint test_role_ref foreign key (role) references test_role (role_id);

alter table test_role drop constraint test_user_ref;

alter table test_role drop column user_id;

drop table if exists role_user;