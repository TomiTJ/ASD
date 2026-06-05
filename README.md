# Bank Admin Application

A full-stack banking administration system built with Spring Boot, PostgreSQL, and deployed on AWS. The system provides a secure admin portal for managing bank accounts, customers, transactions, loans, and audit logs.

## Live Demo
**URL:** http://bankadmin-alb-533061711.ap-southeast-1.elb.amazonaws.com

| Role | Email | Password |
|---|---|---|
| Admin | admin@bank.local | password |
| Read Only | viewer@bank.local | password |

---

## Features

- **User & Role Management** — Create and manage admin/read-only users with BCrypt password hashing
- **Customer & Account Management** — Full CRUD for customers and bank accounts (Transactional, Savings, Credit, Business)
- **Transaction Processing** — View, filter, and export transactions; real account-to-account transfer with balance validation
- **Audit Logging** — Tracks all user actions with timestamps, resource types, and request IDs
- **Loan Management** — Submit, approve, and reject loan applications
- **Reports** — Export data to CSV, Excel, and PDF formats
- **Security** — Session-based authentication, CSRF protection, role-based access control

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 3.5, Spring Security, Spring Data JPA |
| Database | PostgreSQL 16 |
| ORM | Hibernate 6 |
| Frontend | Thymeleaf, HTML/CSS/JS |
| Containerisation | Docker |
| Cloud | AWS ECS Fargate, AWS RDS, AWS ECR, AWS ALB |
| CI/CD | GitHub Actions |

---

## Architecture

```
GitHub → GitHub Actions → Docker Build → ECR → ECS Fargate
                                                     ↓
Users → ALB (permanent URL) → ECS Task (Spring Boot) → RDS PostgreSQL
```

---

## Running Locally

### Prerequisites
- Java 17 (Temurin/Adoptium)
- Maven 3.9+
- Docker & Docker Compose

### Option A — Docker Compose (recommended)
```bash
git clone https://github.com/TomiTJ/ASD.git
cd ASD
docker compose up --build
```
App runs at `http://localhost:8080` with a local PostgreSQL instance.

### Option B — IntelliJ / Local PostgreSQL
1. Create a PostgreSQL database and user with admin privileges
2. Update `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/<your-db>
   spring.datasource.username=<your-user>
   spring.datasource.password=<your-password>
   ```
3. Run `BankAdminApplication.java` from IntelliJ

> Dependencies are managed via Maven (`pom.xml`) — no manual installs needed.

---

## Deployment

The app is containerised and deployed automatically via GitHub Actions on every push to `master`:

1. Builds a `linux/amd64` Docker image
2. Pushes to AWS ECR
3. Forces a new ECS Fargate deployment

### AWS Infrastructure
- **ECS Fargate** — serverless container hosting (0.5 vCPU, 1GB RAM)
- **RDS PostgreSQL 16** — managed database (db.t3.micro)
- **ECR** — private Docker image registry
- **ALB** — Application Load Balancer for permanent URL and load balancing

---
