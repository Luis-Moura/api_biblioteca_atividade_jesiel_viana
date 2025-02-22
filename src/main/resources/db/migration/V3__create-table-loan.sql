CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE loan
(
    id          UUID DEFAULT gen_random_uuid() NOT NULL,
    loan_date   date                           NOT NULL,
    return_date date                           NOT NULL,
    user_id     UUID                           NOT NULL,
    book_id     UUID                           NOT NULL,
    CONSTRAINT pk_loan PRIMARY KEY (id),
    CONSTRAINT FK_LOAN_ON_BOOK FOREIGN KEY (book_id) REFERENCES book (id),
    CONSTRAINT FK_LOAN_ON_USER FOREIGN KEY (user_id) REFERENCES users (id)
);
