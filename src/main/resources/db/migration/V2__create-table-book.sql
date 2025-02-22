CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE book
(
    id        UUID DEFAULT gen_random_uuid() NOT NULL,
    title     VARCHAR(200)                   NOT NULL,
    author    VARCHAR(150)                   NOT NULL,
    publisher VARCHAR(150)                   NOT NULL,
    year      INTEGER                        NOT NULL,
    amount    INTEGER                        NOT NULL,
    CONSTRAINT pk_book PRIMARY KEY (id)
);