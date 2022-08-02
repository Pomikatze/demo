alter table plan alter column plan_check type varchar(256);
alter table plan rename column plan_check to user_login;
