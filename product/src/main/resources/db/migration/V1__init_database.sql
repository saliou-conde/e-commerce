CREATE table if not exists category
(
    id integer not null primary key,
    description varchar(255),
    name varchar(255)
);

CREATE table if not exists product
(
    id integer not null primary key,
    description varchar(255),
    name varchar(255),
    available_quantity double precision not null ,
    price numeric(38, 2),
    category_id integer constraint fk1mlasdalksjdalksdj references category
);

create sequence if not exists category_seq increment 1;
create sequence if not exists product_seq increment 1;