create table products
(
    id          bigserial not null,
    code        varchar   not null,
    name        varchar,
    description varchar,
    image_url   varchar,
    price       numeric,
    primary key (id),
    UNIQUE(code)
);

