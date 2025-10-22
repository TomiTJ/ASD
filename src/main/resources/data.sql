-- Drop old tables if they exist
DROP TABLE IF EXISTS audit_event CASCADE;
DROP TABLE IF EXISTS transaction CASCADE;
DROP TABLE IF EXISTS account CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- then CREATE TABLE ... in the correct order (users first, then account, etc.)

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


-- Drop old table if needed
-- =========================================================
-- ACCOUNT table — DROP, CREATE, and SEED (PostgreSQL)
-- Depends on an existing `users` table with `id` (PK) and `email`
-- =========================================================

-- 1) DROP (safe if not present)
DROP TABLE IF EXISTS account CASCADE;

-- 2) CREATE
CREATE TABLE account (
                         id             BIGSERIAL PRIMARY KEY,
                         accountNumber  INTEGER      NOT NULL UNIQUE,
                         userId         INTEGER      NOT NULL,
                         accountType    VARCHAR(20)  NOT NULL,
                         accountStatus  VARCHAR(20)  NOT NULL,
                         balance        NUMERIC(14,2) NOT NULL DEFAULT 0,

                         CONSTRAINT fk_account_user
                             FOREIGN KEY (userId) REFERENCES users(id) ON DELETE RESTRICT,

    -- Enforce enum values (must match your Java enums exactly)
                         CONSTRAINT chk_account_type
                             CHECK (accountType IN ('TRANSACTIONAL','SAVINGS','CREDIT','BUSINESS')),
                         CONSTRAINT chk_account_status
                             CHECK (accountStatus IN ('OPEN','FROZEN','CLOSED'))
);

CREATE INDEX idx_account_userId ON account(userId);

-- 3) SEED (uses INSERT ... SELECT to resolve userId via email)
--    If an email does not exist in users, that row will be skipped.

INSERT INTO account (accountNumber, userId, accountType, accountStatus, balance)
SELECT 1000456701, u.id, 'SAVINGS',        'OPEN',   1250.75 FROM users u WHERE u.email = 'ava.patel@example.com';

INSERT INTO account (accountNumber, userId, accountType, accountStatus, balance)
SELECT 1000456702, u.id, 'TRANSACTIONAL',  'OPEN',     85.10  FROM users u WHERE u.email = 'ava.patel@example.com';

INSERT INTO account (accountNumber, userId, accountType, accountStatus, balance)
SELECT 1000456703, u.id, 'SAVINGS',        'OPEN',   2200.00 FROM users u WHERE u.email = 'liam.nguyen@example.com';

INSERT INTO account (accountNumber, userId, accountType, accountStatus, balance)
SELECT 1000456704, u.id, 'CREDIT',         'OPEN',   -350.00 FROM users u WHERE u.email = 'liam.nguyen@example.com';

INSERT INTO account (accountNumber, userId, accountType, accountStatus, balance)
SELECT 1000456705, u.id, 'TRANSACTIONAL',  'OPEN',    640.25 FROM users u WHERE u.email = 'mia.chen@example.com';

INSERT INTO account (accountNumber, userId, accountType, accountStatus, balance)
SELECT 1000456706, u.id, 'SAVINGS',        'FROZEN',  980.00 FROM users u WHERE u.email = 'noah.williams@example.com';

INSERT INTO account (accountNumber, userId, accountType, accountStatus, balance)
SELECT 1000456707, u.id, 'BUSINESS',       'OPEN',  15200.00 FROM users u WHERE u.email = 'isla.thompson@example.com';

INSERT INTO account (accountNumber, userId, accountType, accountStatus, balance)
SELECT 1000456708, u.id, 'TRANSACTIONAL',  'OPEN',     12.34 FROM users u WHERE u.email = 'ethan.brown@example.com';

INSERT INTO account (accountNumber, userId, accountType, accountStatus, balance)
SELECT 1000456709, u.id, 'SAVINGS',        'OPEN',   3050.90 FROM users u WHERE u.email = 'zoe.martin@example.com';

INSERT INTO account (accountNumber, userId, accountType, accountStatus, balance)
SELECT 1000456710, u.id, 'CREDIT',         'OPEN',   -120.00 FROM users u WHERE u.email = 'lucas.garcia@example.com';

INSERT INTO account (accountNumber, userId, accountType, accountStatus, balance)
SELECT 1000456711, u.id, 'SAVINGS',        'OPEN',    410.00 FROM users u WHERE u.email = 'aria.johnson@example.com';

INSERT INTO account (accountNumber, userId, accountType, accountStatus, balance)
SELECT 1000456712, u.id, 'BUSINESS',       'OPEN',  78450.00 FROM users u WHERE u.email = 'leo.robinson@example.com';

-- Extra examples (closed/frozen)
INSERT INTO account (accountNumber, userId, accountType, accountStatus, balance)
SELECT 1000456713, u.id, 'TRANSACTIONAL',  'CLOSED',    0.00 FROM users u WHERE u.email = 'ethan.brown@example.com';

INSERT INTO account (accountNumber, userId, accountType, accountStatus, balance)
SELECT 1000456714, u.id, 'CREDIT',         'FROZEN',  -999.99 FROM users u WHERE u.email = 'ava.patel@example.com';
