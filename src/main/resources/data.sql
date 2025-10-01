DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS customers;
CREATE TABLE customers (
                           id SERIAL PRIMARY KEY,
                           name TEXT NOT NULL,
                           email TEXT
);

INSERT INTO customers (id, name, email) VALUES (1, 'John', 'john@example.com');
INSERT INTO customers (id, name, email) VALUES (2,'Bob', 'bob@email.com');


CREATE TABLE transactions (
                              id SERIAL PRIMARY KEY,
                              customer_id INTEGER NOT NULL,
                              amount REAL,
                              FOREIGN KEY (customer_id) REFERENCES customers(id)
);

INSERT INTO transactions (customer_id, amount) VALUES (1, 550);
INSERT INTO transactions (customer_id, amount) VALUES (1, 25);
INSERT INTO transactions (customer_id, amount) VALUES (1, 765);

INSERT INTO transactions (customer_id, amount) VALUES (2, 2300);
INSERT INTO transactions (customer_id, amount) VALUES (2, 30);



















-- tomi's audit db test --
create extension if not exists "pgcrypto";

DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS audit_event CASCADE;

CREATE TABLE users (
                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           username TEXT NOT NULL,
                           name TEXT NOT NULL,
                           password TEXT NOT NULL,
                           email TEXT,
                            active TEXT
);





INSERT INTO users (id, username, password, email, active)
VALUES ('11111111-1111-1111-1111-111111111111', 'dummyUser', 'password', 'dummy@example.com', true);




CREATE TABLE audit_event (
                             audit_event_id   TEXT PRIMARY KEY,
                             actor_user_id    TEXT NOT NULL,
                             action           TEXT NOT NULL,
                             resource_type    TEXT NOT NULL,
                             resource_id      TEXT NOT NULL,
                             request_id       TEXT NOT NULL,
                             created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                         );
INSERT INTO audit_event (
    audit_event_id,
    actor_user_id,
    action,
    resource_type,
    resource_id,
    request_id
) VALUES (
             '11111111-1111-1111-1111-111111111111',
             'aaaa1111-aaaa-1111-aaaa-111111111111',
             'CREATE',
             'Customer',
             'bbbb1111-bbbb-1111-bbbb-111111111111',
             'cccc1111-cccc-1111-cccc-111111111111'
         );