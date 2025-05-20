create table messages (
    id serial primary key,
    text text,
    created_at timestamp,
    user_id int references users(id) on delete cascade
)