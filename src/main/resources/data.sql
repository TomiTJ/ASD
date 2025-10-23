-- Drop old tables if they exist
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS customers CASCADE;
DROP TABLE IF EXISTS audit_event;
DROP TABLE IF EXISTS users CASCADE;

-- Users table
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       full_name VARCHAR(80) NOT NULL,
                       email VARCHAR(120) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(20) NOT NULL,        -- 'ADMIN' or 'READ_ONLY'
                       status VARCHAR(20) NOT NULL,      -- 'ACTIVE' or 'DEACTIVATED'
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Seed users with BCrypt hashed passwords (all passwords are "password")
INSERT INTO users (full_name, email, password, role, status)
VALUES
    ('Admin User', 'admin@bank.local',
     '$2a$10$AbmIMwdw24zABHae0Edc5egtFfOJgwELfe7/Tzua5SnJSdrgQNBnC',
     'ADMIN', 'ACTIVE'),

    ('Viewer User', 'viewer@bank.local',
     '$2a$10$677L7hf55cMtzXGAuYJC0eEirNc1uJyXWstQ9U1NdTCHm9wOAZf9W',
     'READ_ONLY', 'ACTIVE'),

    ('Lesandu Perera','lesandu@gmail.com',
     '$2a$10$2xkrtEB9v3mK/Oy/5teF8OHoqzRZjYuuBI37OW0Tn7EkBTHlPxaa6',
     'ADMIN','ACTIVE'),

    ('Sarah Johnson', 'sarah.johnson@bank.local',
     '$2a$10$AbmIMwdw24zABHae0Edc5egtFfOJgwELfe7/Tzua5SnJSdrgQNBnC',
     'ADMIN', 'ACTIVE'),

    ('Michael Chen', 'michael.chen@bank.local',
     '$2a$10$AbmIMwdw24zABHae0Edc5egtFfOJgwELfe7/Tzua5SnJSdrgQNBnC',
     'READ_ONLY', 'ACTIVE'),

    ('Emma Wilson', 'emma.wilson@bank.local',
     '$2a$10$AbmIMwdw24zABHae0Edc5egtFfOJgwELfe7/Tzua5SnJSdrgQNBnC',
     'READ_ONLY', 'ACTIVE'),

    ('James Martinez', 'james.martinez@bank.local',
     '$2a$10$AbmIMwdw24zABHae0Edc5egtFfOJgwELfe7/Tzua5SnJSdrgQNBnC',
     'ADMIN', 'ACTIVE'),

    ('Olivia Brown', 'olivia.brown@bank.local',
     '$2a$10$AbmIMwdw24zABHae0Edc5egtFfOJgwELfe7/Tzua5SnJSdrgQNBnC',
     'READ_ONLY', 'ACTIVE'),

    ('William Davis', 'william.davis@bank.local',
     '$2a$10$AbmIMwdw24zABHae0Edc5egtFfOJgwELfe7/Tzua5SnJSdrgQNBnC',
     'READ_ONLY', 'DEACTIVATED'),

    ('Sophia Garcia', 'sophia.garcia@bank.local',
     '$2a$10$AbmIMwdw24zABHae0Edc5egtFfOJgwELfe7/Tzua5SnJSdrgQNBnC',
     'ADMIN', 'ACTIVE'),

    ('Liam Anderson', 'liam.anderson@bank.local',
     '$2a$10$AbmIMwdw24zABHae0Edc5egtFfOJgwELfe7/Tzua5SnJSdrgQNBnC',
     'READ_ONLY', 'ACTIVE'),

    ('Ava Taylor', 'ava.taylor@bank.local',
     '$2a$10$AbmIMwdw24zABHae0Edc5egtFfOJgwELfe7/Tzua5SnJSdrgQNBnC',
     'READ_ONLY', 'ACTIVE'),

    ('Noah Thomas', 'noah.thomas@bank.local',
     '$2a$10$AbmIMwdw24zABHae0Edc5egtFfOJgwELfe7/Tzua5SnJSdrgQNBnC',
     'ADMIN', 'DEACTIVATED'),

    ('Isabella Moore', 'isabella.moore@bank.local',
     '$2a$10$AbmIMwdw24zABHae0Edc5egtFfOJgwELfe7/Tzua5SnJSdrgQNBnC',
     'READ_ONLY', 'ACTIVE');




-- Customers table
CREATE TABLE customers (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(80) NOT NULL,
    email VARCHAR(120)
);

INSERT INTO customers (full_name, email) VALUES
                                                ('John Smith', 'john.smith@example.com'),
                                                ('Bob Williams', 'bob.williams@example.com'),
                                                ('Ava Patel', 'ava.patel@example.com'),
                                                ('Liam Nguyen', 'liam.nguyen@example.com'),
                                                ('Mia Chen', 'mia.chen@example.com'),
                                                ('Noah Johnson', 'noah.johnson@example.com'),
                                                ('Isla Thompson', 'isla.thompson@example.com'),
                                                ('Ethan Brown', 'ethan.brown@example.com'),
                                                ('Zoe Martin', 'zoe.martin@example.com'),
                                                ('Lucas Garcia', 'lucas.garcia@example.com');

-- Transactions table
CREATE TABLE transactions (
                              id SERIAL PRIMARY KEY,
                              customer_id INTEGER NOT NULL,
                              type VARCHAR(50),
                              amount NUMERIC(12, 2),
                              status VARCHAR(50),
                              created_at timestamptz,

                              FOREIGN KEY (customer_id) REFERENCES customers(id)
);

INSERT INTO transactions (customer_id, type, amount, status, created_at) VALUES
    (1, 'DEPOSIT', 550.55, 'PENDING', '2025-02-18 15:30:00+10'),
    (1, 'DEPOSIT',25.88, 'PENDING', '2025-03-23 08:30:00+10'),
    (1, 'DEPOSIT',765.21, 'PENDING', '2025-02-04 12:30:00+10'),
    (2, 'DEPOSIT',2300.32, 'PENDING', '2025-07-13 17:20:10+10'),
    (2, 'DEPOSIT',30.54, 'PENDING', '2025-09-22 11:50:20+10'),
--Customer 3: Ava Patel
    (3, 'DEPOSIT', 450.00, 'COMPLETED', '2025-01-11 13:00:00+10'),
    (3, 'TRANSFER', 200.00, 'FAILED', '2025-03-01 16:45:00+10'),
    (3, 'WITHDRAWAL', 78.50, 'COMPLETED', '2025-03-21 09:10:00+10'),

-- Customer 4: Liam Nguyen
    (4, 'DEPOSIT', 980.25, 'COMPLETED', '2025-02-09 10:15:00+10'),
    (4, 'WITHDRAWAL', 150.00, 'PENDING', '2025-04-25 12:20:00+10'),
    (4, 'TRANSFER', 300.00, 'COMPLETED', '2025-06-13 18:10:00+10'),

-- Customer 5: Mia Chen
    (5, 'DEPOSIT', 1250.00, 'COMPLETED', '2025-01-20 09:40:00+10'),
    (5, 'WITHDRAWAL', 210.75, 'PENDING', '2025-02-15 14:25:00+10'),
    (5, 'TRANSFER', 150.50, 'COMPLETED', '2025-04-10 16:45:00+10'),

-- Customer 6: Noah Johnson
    (6, 'DEPOSIT', 760.00, 'COMPLETED', '2025-03-11 13:20:00+10'),
    (6, 'WITHDRAWAL', 50.00, 'COMPLETED', '2025-05-05 08:10:00+10'),
    (6, 'TRANSFER', 500.00, 'FAILED', '2025-05-20 10:30:00+10'),

-- Customer 7: Isla Thompson
    (7, 'DEPOSIT', 1200.00, 'COMPLETED', '2025-01-15 09:05:00+10'),
    (7, 'WITHDRAWAL', 80.00, 'PENDING', '2025-02-10 10:45:00+10'),
    (7, 'WITHDRAWAL', 350.00, 'COMPLETED', '2025-03-01 12:00:00+10'),

-- Customer 8: Ethan Brown
    (8, 'DEPOSIT', 320.00, 'COMPLETED', '2025-03-19 15:10:00+10'),
    (8, 'TRANSFER', 100.00, 'FAILED', '2025-04-10 09:40:00+10'),
    (8, 'WITHDRAWAL', 40.00, 'COMPLETED', '2025-04-22 13:20:00+10'),

-- Customer 9: Zoe Martin
    (9, 'DEPOSIT', 2000.00, 'COMPLETED', '2025-02-01 10:10:00+10'),
    (9, 'WITHDRAWAL', 300.00, 'COMPLETED', '2025-02-12 11:30:00+10'),
    (9, 'TRANSFER', 500.00, 'PENDING', '2025-03-05 14:15:00+10'),

-- Customer 10: Lucas Garcia
    (10, 'DEPOSIT', 450.00, 'COMPLETED', '2025-01-25 09:00:00+10'),
    (10, 'TRANSFER', 150.00, 'COMPLETED', '2025-02-18 16:20:00+10'),
    (10, 'WITHDRAWAL', 85.00, 'FAILED', '2025-03-10 08:45:00+10');

-- Audit events table
CREATE TABLE audit_event (
                             audit_event_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                             user_id INT NOT NULL REFERENCES users(id),
                             action VARCHAR(40) NOT NULL,
                             resource_type VARCHAR(50) NOT NULL,
                             resource_id UUID,
                             request_id UUID DEFAULT gen_random_uuid(),
                             created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO audit_event
(audit_event_id, user_id, action, resource_type, resource_id, request_id, created_at)
VALUES
    (gen_random_uuid(), 1, 'LOGIN',   'USER',       gen_random_uuid(), gen_random_uuid(), NOW() - INTERVAL '15 minutes'),
    (gen_random_uuid(), 1, 'CREATE',  'ACCOUNT',    gen_random_uuid(), gen_random_uuid(), NOW() - INTERVAL '10 minutes'),
    (gen_random_uuid(), 2, 'UPDATE',  'CUSTOMER',   gen_random_uuid(), gen_random_uuid(), NOW() - INTERVAL '2 minutes');


-- Drop old table if needed
-- =========================================================
-- ACCOUNT table — DROP, CREATE, and SEED (PostgreSQL)
-- Depends on an existing `users` table with `id` (PK) and `email`
-- =========================================================

-- Drop old table if needed
DROP TABLE IF EXISTS account CASCADE;

-- CREATE accounts table (references customers, not users)
CREATE TABLE account (
                         id             BIGSERIAL PRIMARY KEY,
                         account_number VARCHAR(50)  NOT NULL UNIQUE,
                         customer_id    INTEGER      NOT NULL,
                         account_type   VARCHAR(20)  NOT NULL,
                         account_status VARCHAR(20)  NOT NULL,
                         balance        NUMERIC(14,2) NOT NULL DEFAULT 0,
                         created_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                         CONSTRAINT fk_account_customer
                             FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE RESTRICT,

                         CONSTRAINT chk_account_type
                             CHECK (account_type IN ('TRANSACTIONAL','SAVINGS','CREDIT','BUSINESS')),
                         CONSTRAINT chk_account_status
                             CHECK (account_status IN ('OPEN','FROZEN','CLOSED'))
);

CREATE INDEX idx_account_customer_id ON account(customer_id);

-- SEED accounts (using customer IDs 1-10 from your customers table)
INSERT INTO account (account_number, customer_id, account_type, account_status, balance) VALUES
                                                                                             ('ACC-1000456701', 1, 'TRANSACTIONAL', 'OPEN',   1250.75),
                                                                                             ('ACC-1000456702', 1, 'SAVINGS',       'OPEN',   2500.00),
                                                                                             ('ACC-1000456703', 2, 'TRANSACTIONAL', 'OPEN',    450.10),
                                                                                             ('ACC-1000456704', 3, 'SAVINGS',       'OPEN',   3200.00),
                                                                                             ('ACC-1000456705', 3, 'CREDIT',        'OPEN',   -350.50),
                                                                                             ('ACC-1000456706', 4, 'TRANSACTIONAL', 'OPEN',    890.25),
                                                                                             ('ACC-1000456707', 5, 'SAVINGS',       'OPEN',   5200.00),
                                                                                             ('ACC-1000456708', 5, 'BUSINESS',      'OPEN',  25000.00),
                                                                                             ('ACC-1000456709', 6, 'TRANSACTIONAL', 'FROZEN',  120.00),
                                                                                             ('ACC-1000456710', 7, 'SAVINGS',       'OPEN',   1800.90),
                                                                                             ('ACC-1000456711', 8, 'TRANSACTIONAL', 'OPEN',    340.00),
                                                                                             ('ACC-1000456712', 9, 'BUSINESS',      'OPEN',  78450.00),
                                                                                             ('ACC-1000456713', 10, 'SAVINGS',      'OPEN',   2100.00),
                                                                                             ('ACC-1000456714', 10, 'CREDIT',       'CLOSED',  -120.00);
