-- Drop old tables if they exist
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS audit_event;
DROP TABLE IF EXISTS users;

-- Users table

CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       full_name TEXT NOT NULL,
                       email TEXT NOT NULL UNIQUE,
                       password TEXT NOT NULL,
                       role TEXT NOT NULL,       -- 'ADMIN' or 'READ_ONLY'
                       status TEXT NOT NULL,     -- 'ACTIVE' or 'DEACTIVATED'
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Seed known-good users with BCrypt hashed passwords
INSERT INTO users (full_name, email, password, role, status)
VALUES
    ('Admin User', 'admin@bank.local',
     '$2a$10$AbmIMwdw24zABHae0Edc5egtFfOJgwELfe7/Tzua5SnJSdrgQNBnC',
     'ADMIN', 'ACTIVE'),

    ('Viewer User', 'viewer@bank.local',
     '$2a$10$677L7hf55cMtzXGAuYJC0eEirNc1uJyXWstQ9U1NdTCHm9wOAZf9W',
     'READ_ONLY', 'ACTIVE');


-- Customers table
CREATE TABLE customers (
                           id SERIAL PRIMARY KEY,
                           name VARCHAR(80) NOT NULL,
                           email VARCHAR(120)
);

INSERT INTO customers (name, email) VALUES
                                        ('John', 'john@example.com'),
                                        ('Bob', 'bob@email.com');

-- Transactions table
CREATE TABLE transactions (
                              id SERIAL PRIMARY KEY,
                              customer_id INT NOT NULL,
                              amount NUMERIC,
                              FOREIGN KEY (customer_id) REFERENCES customers(id)
);

INSERT INTO transactions (customer_id, amount) VALUES
                                                   (1, 550),
                                                   (1, 25),
                                                   (1, 765),
                                                   (2, 2300),
                                                   (2, 30);

-- Audit events table
CREATE TABLE audit_event (
                             audit_event_id UUID PRIMARY KEY,
                             actor_user_id  UUID NOT NULL,
                             action         VARCHAR(20) NOT NULL,
                             resource_type  VARCHAR(50) NOT NULL,
                             resource_id    UUID NOT NULL,
                             request_id     UUID NOT NULL,
                             created_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Seed audit events
INSERT INTO audit_event (audit_event_id, actor_user_id, action, resource_type, resource_id, request_id, created_at)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'aaaa1111-aaaa-1111-aaaa-111111111111', 'CREATE', 'Customer', 'bbbb1111-bbbb-1111-bbbb-111111111111', 'cccc1111-cccc-1111-cccc-111111111111', '2025-09-03 08:00:00'),
    ('22222222-2222-2222-2222-222222222222', 'aaaa2222-aaaa-2222-aaaa-222222222222', 'UPDATE', 'Account', 'bbbb2222-bbbb-2222-bbbb-222222222222', 'cccc2222-cccc-2222-cccc-222222222222', '2025-09-03 09:15:00'),
    ('33333333-3333-3333-3333-333333333333', 'aaaa3333-aaaa-3333-aaaa-333333333333', 'DELETE', 'Transaction', 'bbbb3333-bbbb-3333-bbbb-333333333333', 'cccc3333-cccc-3333-cccc-333333333333', '2025-09-03 10:45:00');
