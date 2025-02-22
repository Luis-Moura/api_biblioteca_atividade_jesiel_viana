CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE users
(
    id    UUID DEFAULT gen_random_uuid() NOT NULL,
    name  VARCHAR(255)                   NOT NULL,
    email VARCHAR(255)                   NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);