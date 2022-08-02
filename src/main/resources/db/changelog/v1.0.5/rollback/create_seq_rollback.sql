alter table organization_chain alter column id set default nextval('organization_chain_id_seq');
alter table organization alter column id set default nextval('organization_id_seq');
alter table demo_order alter column id set default nextval('demo_order_id_seq');
alter table working_base alter column id set default nextval('working_base_id_seq');

drop sequence if exists chain_seq;
drop sequence if exists order_seq;
drop sequence if exists organization_seq;
drop sequence if exists base_seq;
