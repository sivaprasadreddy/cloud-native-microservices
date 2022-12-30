create table credit_cards
(
    id            bigserial not null,
    customer_name varchar   not null,
    card_number   varchar   not null,
    cvv           varchar   not null,
    expiry_month  numeric   not null,
    expiry_year   numeric   not null,
    primary key (id),
    CONSTRAINT cc_card_num_unique UNIQUE (card_number)
);

create table orders
(
    id                        bigserial not null,
    order_id                  varchar,
    customer_name             varchar,
    customer_email            varchar,
    customer_phone            varchar,
    delivery_address_line1    varchar,
    delivery_address_line2    varchar,
    delivery_address_city     varchar,
    delivery_address_state    varchar,
    delivery_address_zip_code varchar,
    delivery_address_country  varchar,
    status                    varchar,
    comments                  text,
    created_at                timestamp,
    updated_at                timestamp,
    primary key (id)
);

create table order_items
(
    id       bigserial not null,
    code     varchar   not null,
    name    varchar   not null,
    price    numeric   not null,
    quantity integer   not null,
    order_id bigint    not null references orders (id),
    primary key (id)
);
