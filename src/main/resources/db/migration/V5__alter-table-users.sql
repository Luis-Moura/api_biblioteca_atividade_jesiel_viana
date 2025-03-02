ALTER TABLE users
    ADD role VARCHAR(255);

ALTER TABLE users
    ALTER COLUMN role SET NOT NULL;

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);