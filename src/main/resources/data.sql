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


-- Postgres Structure below
DROP TABLE IF EXISTS audit_event;

CREATE TABLE audit_event (
                             audit_event_id   UUID PRIMARY KEY,                  -- maps to auditEventId
                             actor_user_id    UUID      NOT NULL,                -- maps to userId
                             action           VARCHAR(40) NOT NULL,              -- enum Action
                             resource_type    VARCHAR(40) NOT NULL,              -- enum ResourceType
                             resource_id      UUID      NOT NULL,                -- maps to resourceId
                             request_id       UUID      NOT NULL,                -- trace id
                             created_at       TIMESTAMPTZ NOT NULL     -- maps to createdAt
);
INSERT INTO audit_event (
    audit_event_id, actor_user_id, action, resource_type,
    resource_id, request_id, created_at
) VALUES (
             '550e8400-e29b-41d4-a716-446655440000',
             '123e4567-e89b-12d3-a456-426614174000',
             'CREATE',
             'Customer',
             '789e4567-e89b-12d3-a456-426614174999',
             '321e4567-e89b-12d3-a456-426614174888',
             '2025-09-02T06:00:00Z'
         );