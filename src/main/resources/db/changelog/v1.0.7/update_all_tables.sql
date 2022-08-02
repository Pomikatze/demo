alter table demo_order add column demo_user bigint;
alter table organization add column demo_user bigint;
alter table organization_chain add column demo_user bigint;
alter table plan add column demo_user bigint;
alter table working_base add column demo_user bigint;

alter table demo_order add constraint order_user_ref foreign key (demo_user) references test_user (id);
alter table organization add constraint organization_user_ref foreign key (demo_user) references test_user (id);
alter table organization_chain add constraint chain_user_ref foreign key (demo_user) references test_user (id);
alter table plan add constraint plan_user_ref foreign key (demo_user) references test_user (id);
alter table working_base add constraint base_user_ref foreign key (demo_user) references test_user (id);