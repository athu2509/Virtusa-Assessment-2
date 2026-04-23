DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    uid        INT PRIMARY KEY,
    full_name  VARCHAR(100),
    email      VARCHAR(100),
    country    VARCHAR(50),
    joined_on  DATE
);

CREATE TABLE events (
    eid         INT PRIMARY KEY,
    uid         INT,
    action_type VARCHAR(50),
    happened_on DATE,
    FOREIGN KEY (uid) REFERENCES users(uid)
);

CREATE TABLE transactions (
    tid        INT PRIMARY KEY,
    uid        INT,
    paid       DECIMAL(10, 2),
    paid_on    DATE,
    plan_type  VARCHAR(50),
    FOREIGN KEY (uid) REFERENCES users(uid)
);


INSERT INTO users VALUES
(1,  'Alice Johnson', 'alice@mail.com',  'USA',   '2024-01-05'),
(2,  'Bob Smith',     'bob@mail.com',    'UK',    '2024-01-12'),
(3,  'Carol White',   'carol@mail.com',  'India', '2024-02-03'),
(4,  'David Brown',   'david@mail.com',  'USA',   '2024-02-18'),
(5,  'Eva Green',     'eva@mail.com',    'India', '2024-03-01'),
(6,  'Frank Lee',     'frank@mail.com',  'UK',    '2024-03-15'),
(7,  'Grace Kim',     'grace@mail.com',  'USA',   '2024-04-02'),
(8,  'Henry Adams',   'henry@mail.com',  'India', '2024-04-20'),
(9,  'Isla Turner',   'isla@mail.com',   'USA',   '2024-05-05'),
(10, 'Jake Wilson',   'jake@mail.com',   'UK',    '2024-05-22');

INSERT INTO events VALUES
(1,  1, 'session', '2024-01-10'),
(2,  1, 'session', '2024-02-08'),
(3,  1, 'session', '2024-03-12'),
(4,  2, 'session', '2024-01-15'),
(5,  3, 'session', '2024-02-10'),
(6,  3, 'session', '2024-03-05'),
(7,  3, 'session', '2024-04-18'),
(8,  4, 'session', '2024-02-20'),
(9,  4, 'session', '2024-04-25'),
(10, 5, 'session', '2024-03-10'),
(11, 6, 'session', '2024-03-20'),
(12, 6, 'session', '2024-04-10'),
(13, 6, 'session', '2024-05-15'),
(14, 7, 'session', '2024-04-05'),
(15, 8, 'session', '2024-04-22'),
(16, 8, 'session', '2024-05-18'),
(17, 9, 'session', '2024-05-08'),
(18, 10,'session', '2024-05-25');

INSERT INTO transactions VALUES
(1,  1, 49.99, '2024-01-20', 'subscription'),
(2,  1, 49.99, '2024-02-20', 'subscription'),
(3,  1, 99.00, '2024-03-15', 'upgrade'),
(4,  2, 49.99, '2024-01-18', 'subscription'),
(5,  3, 49.99, '2024-02-15', 'subscription'),
(6,  3, 49.99, '2024-03-15', 'subscription'),
(7,  4, 49.99, '2024-02-25', 'subscription'),
(8,  5, 29.99, '2024-03-12', 'one-time'),
(9,  6, 49.99, '2024-03-22', 'subscription'),
(10, 6, 49.99, '2024-04-22', 'subscription'),
(11, 7, 29.99, '2024-04-08', 'one-time'),
(12, 8, 49.99, '2024-04-25', 'subscription'),
(13, 8, 49.99, '2024-05-25', 'subscription'),
(14, 9, 29.99, '2024-05-10', 'one-time'),
(15,10, 49.99, '2024-05-28', 'subscription');
