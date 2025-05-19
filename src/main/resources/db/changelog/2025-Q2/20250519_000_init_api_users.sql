create table users (
    id serial primary key,
    email varchar(150) unique not null,
    password varchar(200) not null,
    role varchar(15) not null,
    created_at timestamp
)