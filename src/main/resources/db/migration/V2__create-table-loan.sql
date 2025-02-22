CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE loan
(
    id        UUID DEFAULT gen_random_uuid() NOT NULL,
    title     VARCHAR(255)                   NOT NULL,
    author    VARCHAR(255)                   NOT NULL,
    publisher VARCHAR(255)                   NOT NULL,
    year      INTEGER                        NOT NULL,
    amount    INTEGER                        NOT NULL,
    CONSTRAINT pk_loan PRIMARY KEY (id)
);