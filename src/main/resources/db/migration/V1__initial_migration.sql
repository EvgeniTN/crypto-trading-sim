create table users
(
    id bigint auto_increment primary key,
    username varchar(255) not null unique,
    password varchar(255) not null,
    balance decimal(24,18) not null default 0.00
);

create table holdings
(
    id smallint auto_increment primary key,
    symbol varchar(25) not null,
    quantity decimal(24,18) not null default 0.0,
    user_id bigint not null,
    constraint holdings_user_id_fk
        foreign key (user_id) references users (id) on delete cascade,
    unique key unique_user_symbol (user_id, symbol)
);

create table transactions
(
    id bigint auto_increment primary key,
    buy boolean not null,
    price decimal(24,18) not null,
    quantity decimal(24,18),
    total decimal(24,18),
    profit_loss decimal(24,18),
    timestamp datetime not null default current_timestamp,
    user_id bigint not null,
    constraint transactions_user_id_fk
        foreign key (user_id) references users (id) on delete cascade
)