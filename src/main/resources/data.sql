-- Drop old tables if they exist
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS audit_event;
DROP TABLE IF EXISTS users;

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
    (2, 'DEPOSIT',30.54, 'PENDING', '2025-09-22 11:50:20+10');
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


-- Large dataset for transactions table for dashboard testing
INSERT INTO transactions (customer_id, type, amount, status, created_at) VALUES
                                                                             (1, 'DEPOSIT',      100.00,  'COMPLETED', '2024-01-03 09:01:00+10'),
                                                                             (2, 'WITHDRAWAL',   250.50,  'COMPLETED', '2024-01-12 13:20:00+10'),
                                                                             (1, 'TRANSFER',     500.00,  'COMPLETED', '2024-01-22 16:40:00+10'),
                                                                             (2, 'DEPOSIT',      1200.75, 'COMPLETED', '2024-02-05 11:10:00+10'),
                                                                             (1, 'PAYMENT',      320.00,  'FAILED',    '2024-02-10 14:00:00+10'),
                                                                             (2, 'FEE',          18.00,   'COMPLETED', '2024-02-15 08:50:00+10'),
                                                                             (1, 'WITHDRAWAL',   90.00,   'COMPLETED', '2024-03-01 10:15:00+10'),
                                                                             (2, 'TRANSFER',     6000.00, 'PENDING',   '2024-03-10 12:30:00+10'),
                                                                             (1, 'DEPOSIT',      800.00,  'COMPLETED', '2024-03-18 17:45:00+10'),
                                                                             (2, 'PAYMENT',      75.00,   'COMPLETED', '2024-03-25 15:20:00+10'),
                                                                             (1, 'FEE',          22.50,   'COMPLETED', '2024-04-01 09:05:00+10'),
                                                                             (2, 'WITHDRAWAL',   300.00,  'FAILED',    '2024-04-12 13:30:00+10'),
                                                                             (1, 'TRANSFER',     1200.00, 'COMPLETED', '2024-04-20 18:00:00+10'),
                                                                             (2, 'DEPOSIT',      9800.00, 'COMPLETED', '2024-05-03 11:55:00+10'),
                                                                             (1, 'PAYMENT',      120.00,  'PENDING',   '2024-05-10 14:15:00+10'),
                                                                             (2, 'FEE',          15.00,   'COMPLETED', '2024-05-18 08:35:00+10'),
                                                                             (1, 'WITHDRAWAL',   500.00,  'COMPLETED', '2024-06-01 10:45:00+10'),
                                                                             (2, 'TRANSFER',     350.00,  'COMPLETED', '2024-06-12 12:05:00+10'),
                                                                             (1, 'DEPOSIT',      1500.00, 'COMPLETED', '2024-06-20 16:25:00+10'),
                                                                             (2, 'PAYMENT',      300.00,  'FAILED',    '2024-06-28 15:50:00+10'),
                                                                             (1, 'FEE',          20.00,   'COMPLETED', '2024-07-03 09:10:00+10'),
                                                                             (2, 'WITHDRAWAL',   200.00,  'COMPLETED', '2024-07-13 13:40:00+10'),
                                                                             (1, 'TRANSFER',     400.00,  'COMPLETED', '2024-07-22 17:20:00+10'),
                                                                             (2, 'DEPOSIT',      2100.00, 'COMPLETED', '2024-07-30 11:30:00+10'),
                                                                             (1, 'PAYMENT',      80.00,   'COMPLETED', '2024-08-06 14:45:00+10'),
                                                                             (2, 'FEE',          19.75,   'COMPLETED', '2024-08-14 08:25:00+10'),
                                                                             (1, 'WITHDRAWAL',   50.00,   'FAILED',    '2024-08-22 10:55:00+10'),
                                                                             (2, 'TRANSFER',     800.00,  'COMPLETED', '2024-08-30 12:15:00+10'),
                                                                             (1, 'DEPOSIT',      970.00,  'COMPLETED', '2024-09-07 16:35:00+10'),
                                                                             (2, 'PAYMENT',      150.00,  'PENDING',   '2024-09-15 15:05:00+10'),
                                                                             (1, 'FEE',          12.00,   'COMPLETED', '2024-09-24 09:20:00+10'),
                                                                             (2, 'WITHDRAWAL',   1000.00, 'COMPLETED', '2024-10-01 13:55:00+10'),
                                                                             (1, 'TRANSFER',     2000.00, 'COMPLETED', '2024-10-09 17:10:00+10'),
                                                                             (2, 'DEPOSIT',      5000.00, 'COMPLETED', '2024-10-17 11:40:00+10'),
                                                                             (1, 'PAYMENT',      60.00,   'FAILED',    '2024-10-25 14:30:00+10'),
                                                                             (2, 'FEE',          10.00,   'COMPLETED', '2024-11-02 08:00:00+10'),
                                                                             (1, 'WITHDRAWAL',   400.00,  'COMPLETED', '2024-11-10 10:20:00+10'),
                                                                             (2, 'TRANSFER',     7500.00, 'COMPLETED', '2024-11-18 12:50:00+10'),
                                                                             (1, 'DEPOSIT',      350.00,  'COMPLETED', '2024-11-26 16:15:00+10'),
                                                                             (2, 'PAYMENT',      175.00,  'COMPLETED', '2024-12-04 15:40:00+10'),
                                                                             (1, 'FEE',          25.00,   'COMPLETED', '2024-12-12 09:35:00+10'),
                                                                             (2, 'WITHDRAWAL',   600.00,  'COMPLETED', '2024-12-20 13:10:00+10'),
                                                                             (1, 'TRANSFER',     1400.00, 'FAILED',    '2024-12-28 17:30:00+10'),
                                                                             (2, 'DEPOSIT',      210.00,  'COMPLETED', '2025-01-05 11:25:00+10'),
                                                                             (1, 'PAYMENT',      200.00,  'COMPLETED', '2025-01-13 14:50:00+10'),
                                                                             (2, 'FEE',          14.00,   'COMPLETED', '2025-01-21 08:45:00+10'),
                                                                             (1, 'WITHDRAWAL',   700.00,  'COMPLETED', '2025-01-29 10:30:00+10'),
                                                                             (2, 'TRANSFER',     480.00,  'COMPLETED', '2025-02-06 12:00:00+10'),
                                                                             (1, 'DEPOSIT',      9999.99, 'COMPLETED', '2025-02-14 16:10:00+10'),
                                                                             (2, 'PAYMENT',      90.00,   'FAILED',    '2025-02-22 15:15:00+10'),
                                                                             (1, 'FEE',          30.00,   'COMPLETED', '2025-03-02 09:40:00+10'),
                                                                             (2, 'WITHDRAWAL',   800.00,  'COMPLETED', '2025-03-10 13:20:00+10'),
                                                                             (1, 'TRANSFER',     10000.00,'COMPLETED', '2025-03-18 17:50:00+10'),
                                                                             (2, 'DEPOSIT',      3500.00, 'COMPLETED', '2025-03-26 11:15:00+10'),
                                                                             (1, 'PAYMENT',      250.00,  'PENDING',   '2025-04-03 14:25:00+10'),
                                                                             (2, 'FEE',          11.00,   'COMPLETED', '2025-04-11 08:55:00+10'),
                                                                             (1, 'WITHDRAWAL',   1200.00, 'COMPLETED', '2025-04-19 10:05:00+10'),
                                                                             (2, 'TRANSFER',     920.00,  'COMPLETED', '2025-04-27 12:35:00+10'),
                                                                             (1, 'DEPOSIT',      450.00,  'COMPLETED', '2025-05-05 16:00:00+10'),
                                                                             (2, 'PAYMENT',      110.00,  'COMPLETED', '2025-05-13 15:25:00+10'),
                                                                             (1, 'FEE',          18.50,   'COMPLETED', '2025-05-21 09:15:00+10'),
                                                                             (2, 'WITHDRAWAL',   3000.00, 'FAILED',    '2025-05-29 13:45:00+10'),
                                                                             (1, 'TRANSFER',     750.00,  'COMPLETED', '2025-06-06 17:05:00+10'),
                                                                             (2, 'DEPOSIT',      2750.00, 'COMPLETED', '2025-06-14 11:50:00+10'),
                                                                             (1, 'PAYMENT',      95.00,   'COMPLETED', '2025-06-22 14:35:00+10'),
                                                                             (2, 'FEE',          17.00,   'COMPLETED', '2025-06-30 08:20:00+10'),
                                                                             (1, 'WITHDRAWAL',   1600.00, 'COMPLETED', '2025-07-08 10:55:00+10'),
                                                                             (2, 'TRANSFER',     200.00,  'COMPLETED', '2025-07-16 12:10:00+10'),
                                                                             (1, 'DEPOSIT',      890.00,  'COMPLETED', '2025-07-24 16:20:00+10'),
                                                                             (2, 'PAYMENT',      140.00,  'FAILED',    '2025-08-01 15:35:00+10'),
                                                                             (1, 'FEE',          27.00,   'COMPLETED', '2025-08-09 09:25:00+10'),
                                                                             (2, 'WITHDRAWAL',   2500.00, 'COMPLETED', '2025-08-17 13:00:00+10'),
                                                                             (1, 'TRANSFER',     650.00,  'COMPLETED', '2025-08-25 17:40:00+10'),
                                                                             (2, 'DEPOSIT',      4200.00, 'COMPLETED', '2025-09-02 11:35:00+10'),
                                                                             (1, 'PAYMENT',      185.00,  'COMPLETED', '2025-09-10 14:50:00+10'),
                                                                             (2, 'FEE',          13.25,   'COMPLETED', '2025-09-18 08:30:00+10'),
                                                                             (1, 'WITHDRAWAL',   1050.00, 'COMPLETED', '2025-09-26 10:40:00+10'),
                                                                             (2, 'TRANSFER',     570.00,  'PENDING',   '2025-10-04 12:25:00+10'),
                                                                             (1, 'DEPOSIT',      3200.00, 'COMPLETED', '2025-10-12 16:30:00+10'),
                                                                             (2, 'PAYMENT',      210.00,  'COMPLETED', '2025-10-20 15:15:00+10'),
                                                                             (1, 'FEE',          24.00,   'COMPLETED', '2025-10-28 09:55:00+10');

