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