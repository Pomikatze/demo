create sequence chain_seq start 1;
create sequence order_seq start 1;
create sequence organization_seq start 1;
create sequence base_seq start 1;

alter table organization_chain alter column id set default nextval('chain_seq');
alter table organization alter column id set default nextval('organization_seq');
alter table demo_order alter column id set default nextval('order_seq');
alter table working_base alter column id set default nextval('base_seq');