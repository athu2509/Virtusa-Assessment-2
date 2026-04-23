# CohortIQ — User Retention & Revenue Dashboard

Built this to solve a real problem a SaaS startup would actually have — are the users we acquired sticking around, and which payment plans make us the most money?

Three tables: users, events (login sessions), transactions. Simple structure, but the queries on top of it are where things get interesting.

## What each query does

Q1 — Cohort Retention: groups users by their join month and checks if they came back in later months. Answers "of everyone who joined in January, how many were still active in February? March?"

Q2 — Running Spend + Rank: shows every payment a user made with a running total building up after each one. Also ranks who spent the most per plan type. This one uses window functions — SUM() OVER and RANK() OVER.

Q3 — Dead User Detection: flags anyone who hasn't logged in for 60+ days. The marketing team's problem now.

Q4 — Monthly Revenue: total revenue per month, ranked best to worst using RANK().

Q5 — Plan Breakdown: subscription vs one-time vs upgrade — which one actually makes the most money. GROUP BY + SUM.

## How to run

Paste setup.sql into onecompiler.com/mysql and hit Run. Then paste reports.sql and run that. Each query shows up as a result table.

## Stack

MySQL 8, CTEs, window functions (SUM OVER, RANK OVER), DATE_FORMAT, DATEDIFF, LEFT JOIN, HAVING
