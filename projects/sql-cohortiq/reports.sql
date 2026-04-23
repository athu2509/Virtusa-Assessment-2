-- Q1: which users came back after joining? group by join month
WITH base_cohort AS (
    SELECT uid,
           DATE_FORMAT(joined_on, '%Y-%m') AS join_month
    FROM users
),
user_sessions AS (
    SELECT uid,
           DATE_FORMAT(happened_on, '%Y-%m') AS session_month
    FROM events
    WHERE action_type = 'session'
),
month_diff AS (
    SELECT
        b.uid,
        b.join_month,
        s.session_month,
        (YEAR(STR_TO_DATE(s.session_month, '%Y-%m')) - YEAR(STR_TO_DATE(b.join_month, '%Y-%m'))) * 12
        + (MONTH(STR_TO_DATE(s.session_month, '%Y-%m')) - MONTH(STR_TO_DATE(b.join_month, '%Y-%m')))
        AS months_after_joining
    FROM base_cohort b
    JOIN user_sessions s ON b.uid = s.uid
)
SELECT
    join_month,
    months_after_joining,
    COUNT(DISTINCT uid) AS users_still_active
FROM month_diff
GROUP BY join_month, months_after_joining
ORDER BY join_month, months_after_joining;


-- Q2: running spend per user + who spends most in each plan type
SELECT
    u.full_name,
    t.plan_type,
    t.paid_on,
    t.paid,
    SUM(t.paid) OVER (
        PARTITION BY t.uid
        ORDER BY t.paid_on
        ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
    ) AS total_spent_so_far,
    RANK() OVER (
        PARTITION BY t.plan_type
        ORDER BY t.paid DESC
    ) AS rank_in_plan
FROM transactions t
JOIN users u ON t.uid = u.uid
ORDER BY t.plan_type, rank_in_plan;


-- Q3: users who haven't logged in for over 60 days — flag them
SELECT
    u.uid,
    u.full_name,
    u.email,
    MAX(e.happened_on) AS last_seen,
    DATEDIFF(CURDATE(), MAX(e.happened_on)) AS days_gone
FROM users u
LEFT JOIN events e ON u.uid = e.uid AND e.action_type = 'session'
GROUP BY u.uid, u.full_name, u.email
HAVING last_seen IS NULL OR DATEDIFF(CURDATE(), MAX(e.happened_on)) > 60
ORDER BY days_gone DESC;


-- Q4: how much did we make each month, and which month was best
SELECT
    DATE_FORMAT(paid_on, '%Y-%m') AS month,
    COUNT(tid)                     AS num_payments,
    ROUND(SUM(paid), 2)            AS monthly_revenue,
    RANK() OVER (ORDER BY SUM(paid) DESC) AS best_month_rank
FROM transactions
GROUP BY month
ORDER BY month;


-- Q5: which plan type makes us the most money
SELECT
    plan_type,
    COUNT(*)              AS total_sales,
    ROUND(SUM(paid), 2)   AS revenue,
    ROUND(AVG(paid), 2)   AS avg_per_sale
FROM transactions
GROUP BY plan_type
ORDER BY revenue DESC;
