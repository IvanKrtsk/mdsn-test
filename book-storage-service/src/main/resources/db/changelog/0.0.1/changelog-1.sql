--liquibase formatted sql
--changeset author:ikrotsyuk
--comment create books table
CREATE TABLE IF NOT EXISTS books (
    id SERIAL PRIMARY KEY,
    isbn VARCHAR(20) NOT NULL,
    name VARCHAR(255) NOT NULL,
    genre VARCHAR(100),
    description TEXT,
    author VARCHAR(255),
    is_deleted BOOLEAN DEFAULT FALSE
);