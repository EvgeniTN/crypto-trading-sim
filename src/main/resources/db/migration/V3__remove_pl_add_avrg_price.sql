alter table transactions
    drop profit_loss;

alter table holdings
    add average_price decimal(24,18) not null default 0.00;