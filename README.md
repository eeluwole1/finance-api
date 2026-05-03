# Finance API

> A backend REST API simulating a financial services platform inspired by Canada Life — one of Canada's leading insurance, wealth management, and financial services companies.

---

## Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [How the Modules Connect](#how-the-modules-connect)
- [API Endpoints](#api-endpoints)
  - [Auth](#auth)
  - [Client](#client)
  - [Account](#account)
  - [Policy](#policy)
  - [Beneficiary](#beneficiary)
  - [Payment](#payment)
  - [Claims](#claims)
  - [Policy Loans](#policy-loans)
  - [Transaction](#transaction)
  - [Rewards](#rewards)
- [Data Relationships](#data-relationships)
- [Endpoint Flow Examples](#endpoint-flow-examples)

---

## Overview

`finance-api` is a Spring Boot REST API that simulates the core backend systems of a Canadian insurance and wealth management company. It handles:

- **Authentication** — Secure JWT-based register and login
- **Clients** — Register and manage policyholders
- **Accounts** — Savings, chequing, and investment accounts with deposit/withdraw
- **Policies** — Life, health, auto, and home insurance policies
- **Beneficiaries** — Policy beneficiaries who receive payouts
- **Payments** — Premium payments with full status tracking (NSF, refunded, etc.)
- **Claims** — Insurance claim submissions with adjudication workflow
- **Policy Loans** — Borrowing against life insurance cash value
- **Transactions** — Deposits, withdrawals, and transfers between clients
- **Rewards** — Loyalty points for on-time payments and healthy behaviour

**73 endpoints** across 10 modules, secured with JWT authentication.

---

## Tech Stack

| Layer          | Technology                          |
|----------------|-------------------------------------|
| Language       | Java 17                             |
| Framework      | Spring Boot 3.5.13                  |
| Build Tool     | Gradle                              |
| ORM            | Spring Data JPA / Hibernate         |
| Database       | PostgreSQL                          |
| Security       | Spring Security 6 + JWT (JJWT 0.12.3) |
| Boilerplate    | Lombok                              |
| Documentation  | Springdoc OpenAPI (Swagger UI)      |
| Config         | spring-dotenv (.env file support)   |

---

## Project Structure

```
src/main/java/com/eeluwole/finance_api/
├── auth/
│   ├── User.java
│   ├── UserRepository.java
│   ├── UserDetailsServiceImpl.java
│   ├── JwtUtil.java
│   ├── JwtAuthFilter.java
│   ├── SecurityConfig.java
│   ├── AuthService.java
│   ├── AuthController.java
│   └── dto/
│       ├── RegisterRequest.java
│       ├── LoginRequest.java
│       └── AuthResponse.java
├── client/
│   ├── Client.java
│   ├── ClientRepository.java
│   ├── ClientService.java
│   ├── ClientController.java
│   └── dto/
├── account/
│   ├── Account.java
│   ├── AccountRepository.java
│   ├── AccountService.java
│   ├── AccountController.java
│   └── dto/
├── policy/
│   ├── Policy.java
│   ├── PolicyRepository.java
│   ├── PolicyService.java
│   ├── PolicyController.java
│   └── dto/
├── beneficiary/
│   ├── Beneficiary.java
│   ├── BeneficiaryRepository.java
│   ├── BeneficiaryService.java
│   ├── BeneficiaryController.java
│   └── dto/
├── payment/
│   ├── Payment.java
│   ├── PaymentRepository.java
│   ├── PaymentService.java
│   ├── PaymentController.java
│   └── dto/
├── claims/
│   ├── Claim.java
│   ├── ClaimRepository.java
│   ├── ClaimService.java
│   ├── ClaimController.java
│   └── dto/
├── advanced/
│   ├── Advanced.java
│   ├── AdvancedRepository.java
│   ├── AdvancedService.java
│   ├── AdvancedController.java
│   └── dto/
├── transaction/
│   ├── Transaction.java
│   ├── TransactionRepository.java
│   ├── TransactionService.java
│   ├── TransactionController.java
│   └── dto/
├── rewards/
│   ├── Reward.java
│   ├── RewardRepository.java
│   ├── RewardService.java
│   ├── RewardController.java
│   └── dto/
└── FinanceApiApplication.java
```

---

## Getting Started

### Prerequisites
- Java 17+
- Gradle
- PostgreSQL (running on port 5432 or 5434)
- Git

### 1. Clone the repository
```bash
git clone https://github.com/eeluwole1/finance-api.git
cd finance-api
```

### 2. Create a `.env` file in the project root
```env
DB_URL=jdbc:postgresql://localhost:5434/financedb
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
JWT_EXPIRATION=86400000
```

> **Never commit your `.env` file.** It is listed in `.gitignore`.

### 3. Create the database
In pgAdmin or psql:
```sql
CREATE DATABASE financedb;
```

### 4. Run the application
```bash
./gradlew bootRun
```

The server starts at `http://localhost:8080`.  
Hibernate will auto-create all tables on first run (`ddl-auto=update`).  
Seed data (3 clients, 4 policies) is inserted automatically via `data.sql`.

### 5. Open Swagger UI
```
http://localhost:8080/swagger-ui/index.html
```

All 10 controllers and 73 endpoints are listed and testable from the browser.

---

## How the Modules Connect

Every module in this API is connected through the **Client**. A client is the central entity — everything else belongs to or originates from a client.

```
AUTH (User registers or logs in → receives JWT token)
  ↓
CLIENT (John Smith is registered as a policyholder)
  │
  ├── ACCOUNT (John opens a savings/investment account)
  │     └── TRANSACTION (deposits, withdrawals, transfers)
  │
  ├── POLICY (John gets a Life Insurance policy — $500,000 coverage)
  │     ├── BENEFICIARY (Jane Smith — receives 100% payout on death)
  │     ├── PAYMENT (monthly $120 premium — can bounce/NSF)
  │     ├── CLAIM (hospital bill — submitted, reviewed, approved)
  │     └── POLICY LOAN (John borrows $5,000 against policy cash value)
  │
  └── REWARDS (John earns loyalty points for on-time payments)
```

---

## API Endpoints

Base URL: `http://localhost:8080/api/v1`

---

### Auth

> Handles user registration and login. Returns a JWT token used to authenticate all subsequent requests.

| Method | Endpoint                  | Description                          |
|--------|---------------------------|--------------------------------------|
| POST   | `/auth/register`          | Register a new user — returns JWT    |
| POST   | `/auth/login`             | Login with email/password — returns JWT |

**Example register:**
```json
POST /api/v1/auth/register
{
  "firstName": "Eluwole",
  "lastName": "Dev",
  "email": "eluwole@email.com",
  "password": "password123"
}
```

**Example response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "email": "eluwole@email.com",
  "firstName": "Eluwole",
  "lastName": "Dev",
  "role": "USER"
}
```

> The `token` is valid for **24 hours** (86400000 ms). Use it as a Bearer token on all other requests.

---

### Client

> The core entity. Every policy, payment, claim, and account belongs to a client. Represents a Canada Life policyholder.

| Method | Endpoint                          | Description                                    |
|--------|-----------------------------------|------------------------------------------------|
| GET    | `/clients`                        | Get all clients                                |
| GET    | `/clients/{id}`                   | Get a single client by ID                      |
| GET    | `/clients/status/{status}`        | Get clients by status                          |
| POST   | `/clients`                        | Register a new client                          |
| PUT    | `/clients/{id}`                   | Update client information                      |
| PATCH  | `/clients/{id}/status?status=`    | Change client status                           |
| DELETE | `/clients/{id}`                   | Remove a client                                |

**Client statuses:** `ACTIVE` · `INACTIVE` · `SUSPENDED`

---

### Account

> Savings, chequing, and investment accounts held by a client. Supports real-time deposits and withdrawals with balance tracking.

| Method | Endpoint                                  | Description                          |
|--------|-------------------------------------------|--------------------------------------|
| GET    | `/accounts`                               | Get all accounts                     |
| GET    | `/accounts/{id}`                          | Get a single account                 |
| GET    | `/accounts/client/{clientId}`             | Get all accounts for a client        |
| GET    | `/accounts/status/{status}`               | Filter accounts by status            |
| POST   | `/accounts`                               | Open a new account                   |
| PATCH  | `/accounts/{id}/deposit?amount=`          | Deposit funds into an account        |
| PATCH  | `/accounts/{id}/withdraw?amount=`         | Withdraw funds from an account       |
| PATCH  | `/accounts/{id}/status?status=`           | Activate, freeze, or close account   |
| DELETE | `/accounts/{id}`                          | Delete an account                    |

**Account types:** `SAVINGS` · `CHEQUING` · `INVESTMENT`

**Account statuses:** `ACTIVE` · `FROZEN` · `CLOSED`

---

### Policy

> An insurance contract between Canada Life and a client. A client can hold multiple policies. The policy defines coverage type, premium amount, and term dates.

| Method | Endpoint                              | Description                          |
|--------|---------------------------------------|--------------------------------------|
| GET    | `/policies`                           | Get all policies                     |
| GET    | `/policies/{id}`                      | Get a single policy                  |
| GET    | `/policies/client/{clientId}`         | Get all policies for a client        |
| GET    | `/policies/status/{status}`           | Filter policies by status            |
| POST   | `/policies`                           | Create a new policy                  |
| PUT    | `/policies/{id}`                      | Update policy details                |
| PATCH  | `/policies/{id}/status?status=`       | Change policy status                 |
| DELETE | `/policies/{id}`                      | Delete a policy                      |

**Policy types:** `LIFE` · `HEALTH` · `AUTO` · `HOME`

**Policy statuses:** `ACTIVE` · `SUSPENDED` · `CANCELLED` · `EXPIRED`

---

### Beneficiary

> The person designated to receive the insurance payout when a client passes away. Multiple beneficiaries can split a payout by percentage.

| Method | Endpoint                                  | Description                          |
|--------|-------------------------------------------|--------------------------------------|
| GET    | `/beneficiaries`                          | Get all beneficiaries                |
| GET    | `/beneficiaries/{id}`                     | Get a single beneficiary             |
| GET    | `/beneficiaries/policy/{policyId}`        | Get all beneficiaries on a policy    |
| POST   | `/beneficiaries`                          | Add a beneficiary to a policy        |
| PUT    | `/beneficiaries/{id}`                     | Update beneficiary details           |
| PATCH  | `/beneficiaries/{id}/status?status=`      | Activate or deactivate               |
| DELETE | `/beneficiaries/{id}`                     | Remove a beneficiary                 |

**Example:**
```json
POST /api/v1/beneficiaries
{
  "policyId": 1,
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane@email.com",
  "phone": "416-555-0001",
  "relationship": "SPOUSE",
  "percentage": 100.0
}
```

**Beneficiary statuses:** `ACTIVE` · `INACTIVE`

---

### Payment

> Records of premium payments made by clients to keep their policies active. Payments can be pending, processed, bounced (NSF), or refunded.

| Method | Endpoint                                | Description                          |
|--------|-----------------------------------------|--------------------------------------|
| GET    | `/payments`                             | Get all payments                     |
| GET    | `/payments/{id}`                        | Get a single payment                 |
| GET    | `/payments/client/{clientId}`           | Get all payments by a client         |
| GET    | `/payments/policy/{policyId}`           | Get all payments on a policy         |
| GET    | `/payments/status/{status}`             | Filter payments by status            |
| POST   | `/payments`                             | Record a new payment                 |
| PATCH  | `/payments/{id}/status?status=`         | Update payment status                |
| DELETE | `/payments/{id}`                        | Delete a payment                     |

**Payment statuses:** `PENDING` · `PROCESSED` · `BOUNCED` · `REFUNDED`

**Payment methods:** `BANK_TRANSFER` · `CREDIT_CARD` · `DIRECT_DEBIT` · `CHEQUE`

---

### Claims

> When a client experiences a covered event (illness, accident, etc.), they submit a claim to receive compensation. Claims go through a status-driven adjudication workflow.

| Method | Endpoint                              | Description                          |
|--------|---------------------------------------|--------------------------------------|
| GET    | `/claims`                             | Get all claims                       |
| GET    | `/claims/{id}`                        | Get a single claim                   |
| GET    | `/claims/client/{clientId}`           | Get all claims by a client           |
| GET    | `/claims/policy/{policyId}`           | Get all claims on a policy           |
| GET    | `/claims/status/{status}`             | Filter claims by status              |
| POST   | `/claims`                             | Submit a new claim                   |
| PATCH  | `/claims/{id}/status?status=`         | Advance claim through workflow       |
| DELETE | `/claims/{id}`                        | Delete a claim                       |

**Claim statuses:** `SUBMITTED` · `UNDER_REVIEW` · `APPROVED` · `DENIED` · `PAID`

**Claim types:** `HEALTH` · `LIFE` · `AUTO` · `HOME` · `DISABILITY`

---

### Policy Loans

> A loan a client can take against the accumulated cash value of their life insurance policy. No credit check required — the policy itself is the collateral.

| Method | Endpoint                              | Description                          |
|--------|---------------------------------------|--------------------------------------|
| GET    | `/loans`                              | Get all policy loans                 |
| GET    | `/loans/{id}`                         | Get a single loan                    |
| GET    | `/loans/client/{clientId}`            | Get all loans for a client           |
| GET    | `/loans/status/{status}`              | Filter loans by status               |
| POST   | `/loans`                              | Create a new policy loan             |
| PATCH  | `/loans/{id}/status?status=`          | Update loan status                   |
| DELETE | `/loans/{id}`                         | Delete a loan                        |

**Loan statuses:** `ACTIVE` · `REPAID` · `DEFAULTED`

---

### Transaction

> Financial movements linked to a client — deposits, withdrawals, and transfers. Transfer transactions can reference a destination client for inter-client transfers.

| Method | Endpoint                                | Description                          |
|--------|-----------------------------------------|--------------------------------------|
| GET    | `/transactions`                         | Get all transactions                 |
| GET    | `/transactions/{id}`                    | Get a single transaction             |
| GET    | `/transactions/client/{clientId}`       | Get all transactions for a client    |
| GET    | `/transactions/type/{type}`             | Filter by type                       |
| GET    | `/transactions/status/{status}`         | Filter by status                     |
| POST   | `/transactions`                         | Create a new transaction             |
| PATCH  | `/transactions/{id}/status?status=`     | Update transaction status            |
| DELETE | `/transactions/{id}`                    | Delete a transaction                 |

**Transaction types:** `DEPOSIT` · `WITHDRAWAL` · `TRANSFER`

**Transaction statuses:** `PENDING` · `COMPLETED` · `FAILED`

**Example transfer:**
```json
POST /api/v1/transactions
{
  "clientId": 1,
  "toClientId": 2,
  "amount": 250.00,
  "type": "TRANSFER",
  "description": "Rent payment to Jane"
}
```

---

### Rewards

> A loyalty program that rewards clients for on-time premium payments and engagement. Points are tracked per client and can be redeemed.

| Method | Endpoint                              | Description                          |
|--------|---------------------------------------|--------------------------------------|
| GET    | `/rewards`                            | Get all reward records               |
| GET    | `/rewards/{id}`                       | Get a single reward record           |
| GET    | `/rewards/client/{clientId}`          | Get all rewards for a client         |
| GET    | `/rewards/client/{clientId}/points`   | Get total points balance for a client|
| GET    | `/rewards/type/{type}`                | Filter rewards by type               |
| POST   | `/rewards`                            | Award points to a client             |
| PATCH  | `/rewards/{id}/status?status=`        | Update reward status                 |
| DELETE | `/rewards/{id}`                       | Delete a reward record               |

**Reward types:** `EARNED` · `REDEEMED` · `BONUS` · `EXPIRED`

---

## Data Relationships

```
User (auth)
    ↓ authenticates via JWT
Client
    ├── has many → Policy
    │                 ├── has many → Payment
    │                 ├── has many → Claim
    │                 ├── has many → Beneficiary
    │                 └── has many → Advanced (Policy Loans)
    ├── has many → Account
    ├── has many → Transaction (fromClient / toClient)
    └── has many → Reward
```

---

## Endpoint Flow Examples

### Flow 1: New Client Gets Life Insurance
```
1. POST /api/v1/auth/register         → Create user account, receive JWT
2. POST /api/v1/clients               → Register John Smith as policyholder
3. POST /api/v1/policies              → Create Life Insurance policy for John
4. POST /api/v1/beneficiaries         → Add Jane Smith as 100% beneficiary
5. POST /api/v1/payments              → Record first monthly premium ($120)
6. PATCH /api/v1/payments/1/status    → status=PROCESSED (payment cleared)
7. POST /api/v1/rewards               → Award 100 points for on-time payment
```

### Flow 2: Payment Bounces (NSF)
```
1. POST /api/v1/payments              → Record monthly premium attempt
2. PATCH /api/v1/payments/2/status    → status=BOUNCED (insufficient funds)
3. PATCH /api/v1/clients/1/status     → status=SUSPENDED
4. POST /api/v1/payments              → Client retries payment
5. PATCH /api/v1/payments/3/status    → status=PROCESSED (success)
6. PATCH /api/v1/clients/1/status     → status=ACTIVE (restored)
```

### Flow 3: Client Files a Health Claim
```
1. POST /api/v1/claims                → John submits hospital bill ($3,000)
2. PATCH /api/v1/claims/1/status      → status=UNDER_REVIEW
3. PATCH /api/v1/claims/1/status      → status=APPROVED
4. PATCH /api/v1/claims/1/status      → status=PAID
```

### Flow 4: Client Takes a Policy Loan
```
1. POST /api/v1/loans                 → John requests $5,000 against policy
2. PATCH /api/v1/loans/1/status       → status=ACTIVE (loan disbursed)
3. PATCH /api/v1/loans/1/status       → status=REPAID (loan paid back)
```

### Flow 5: Account & Transactions
```
1. POST /api/v1/accounts              → Open a savings account for John
2. PATCH /api/v1/accounts/1/deposit   → ?amount=1000 (John deposits $1,000)
3. PATCH /api/v1/accounts/1/deposit   → ?amount=500  (John deposits $500)
4. PATCH /api/v1/accounts/1/withdraw  → ?amount=200  (John withdraws $200)
5. POST /api/v1/transactions          → TRANSFER $250 from John to Jane
6. GET  /api/v1/transactions/client/1 → View John's full transaction history
```

---

## Seed Data

The API auto-inserts sample data on every startup via `data.sql`:

| Client        | Email                  | Status |
|---------------|------------------------|--------|
| John Smith    | john@email.com         | ACTIVE |
| Jane Doe      | jane@email.com         | ACTIVE |
| Michael Brown | michael@email.com      | ACTIVE |

| Policy  | Type   | Client  | Coverage    | Premium | Status  |
|---------|--------|---------|-------------|---------|---------|
| POL-001 | LIFE   | John    | $500,000    | $120/mo | ACTIVE  |
| POL-002 | HEALTH | Jane    | $100,000    | $85/mo  | ACTIVE  |
| POL-003 | AUTO   | Michael | $50,000     | $210/mo | EXPIRED |
| POL-004 | HOME   | John    | $750,000    | $175/mo | ACTIVE  |

---

## Author

**Elusiyan Mathew Eluwole**  
Built as a pre-onboarding project for a Software Developer position at Canada Life (May 2026)

---

*Inspired by Canada Life — Insurance, Wealth Management, and Financial Services*
