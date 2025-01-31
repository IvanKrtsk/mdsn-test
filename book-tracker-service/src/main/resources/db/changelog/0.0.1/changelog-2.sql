--liquibase formatted sql
--changeset author:ikrotsyuk
--comment create available books table
create table if not exists available_books (
    id SERIAL primary key,
    is_available boolean not null,
    borrowed_at timestamp,
    return_by timestamp
);