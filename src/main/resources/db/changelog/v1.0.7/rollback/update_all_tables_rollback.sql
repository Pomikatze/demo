alter table demo_order drop constraint order_user_ref;
alter table organization drop constraint organization_user_ref;
alter table organization_chain drop constraint chain_user_ref;
alter table plan drop constraint order_user_ref;
alter table working_base drop constraint base_user_ref;

alter table demo_order drop column demo_user;
alter table organization drop column demo_user;
alter table organization_chain drop column demo_user;
alter table plan drop column demo_user;
alter table working_base drop column demo_user;