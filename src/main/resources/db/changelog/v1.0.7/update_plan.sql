alter table plan rename column user_login to plan_check;
alter table plan alter column plan_check type varchar(1);