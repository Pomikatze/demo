create table organization_chain
(
    id           bigserial not null primary key,
    name_chain   varchar(128),
    name_manager varchar(256),
    phone        varchar(64),
    mail         varchar(128)
);

create table organization
(
    id           bigserial not null primary key,
    name         varchar(256),
    chain_id     bigint,
    address      varchar(512),
    name_manager varchar(256),
    phone        varchar(64),
    mail         varchar(128),
    constraint demo_organization foreign key (chain_id)
        references organization_chain (id)
);

create table demo_order
(
    id                 bigserial not null primary key,
    order_id           bigint,
    oder_date          date,
    product            varchar(256),
    product_min_weight bigint,
    product_count      bigint,
    organization_id    bigint,
    constraint demo_organization_order foreign key (organization_id)
        references organization (id)
);

create table working_base
(
    id              bigserial not null primary key,
    organization_id bigint,
    chain_id        bigint,
    call_date       timestamp,
    ship_date       timestamp,
    need_call       timestamp,
    comment         text,
    constraint demo_base foreign key (organization_id)
        references organization (id),
    constraint demo_chain foreign key (chain_id)
        references organization_chain (id)
);

