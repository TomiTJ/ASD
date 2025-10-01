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

-- Seed users
INSERT INTO users (full_name, email, password, role, status)
VALUES
    ('Admin User', 'admin@bank.local', 'admin123', 'ADMIN', 'ACTIVE'),
    ('Viewer User', 'viewer@bank.local', 'viewer123', 'READ_ONLY', 'ACTIVE'),
    ('Lesandu Perera', 'lesandu@gmail.com', 'lesandu123', 'ADMIN', 'ACTIVE'),
    ('Calvin Kishore', 'calvin@gmail.com', 'calvin123', 'READ_ONLY', 'ACTIVE'),
    ('Claire Chand', 'claire@gmail.com', 'claire123', 'READ_ONLY', 'ACTIVE'),
    ('Gautum Subhash', 'gautum@gmail.com', 'gautum123', 'READ_ONLY', 'ACTIVE');

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

