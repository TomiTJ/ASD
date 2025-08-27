DROP TABLE IF EXISTS customers;
CREATE TABLE customers (
                          id INTEGER PRIMARY KEY AUTOINCREMENT,
                          name TEXT NOT NULL,
                          email TEXT
);

INSERT INTO customers (id, name, email) VALUES (1, 'John', 'john@example.com');
INSERT INTO customers (id, name, email) VALUES (2,'Bob', 'bob@email.com');

DROP TABLE IF EXISTS transactions;
CREATE TABLE transactions (
                             id INTEGER PRIMARY KEY AUTOINCREMENT,
                             customer_id INTEGER NOT NULL,
                             amount REAL,
                             FOREIGN KEY (customer_id) REFERENCES customers(id)
);

INSERT INTO transactions (customer_id, amount) VALUES (1, 550);
INSERT INTO transactions (customer_id, amount) VALUES (1, 25);
INSERT INTO transactions (customer_id, amount) VALUES (1, 765);

INSERT INTO transactions (customer_id, amount) VALUES (2, 2300);
INSERT INTO transactions (customer_id, amount) VALUES (2, 30);
