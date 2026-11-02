# Code-B IMS — Login & Registration Module

A starter implementation of the internship's first module: user registration, login, role-based dashboard, and account-management flows.

## Stack
- Java 17, Spring Boot 3.3, Spring Security
- MySQL 8
- React + Vite
- JWT authentication

## Included
- Register with name, email, password and role
- Login with email/password
- BCrypt password hashing
- JWT-protected `/api/auth/me`
- Role-aware dashboard (ADMIN / SALES_PERSON)
- Forgot/reset-password API structure (email delivery is a configuration step)
- Email-verification API structure (SMTP configuration is a deployment step)
- Frontend inactivity logout
- Docker Compose for local MySQL

## Important before submission
This is a project starter, not a deployed service. You must set your own secrets, configure SMTP, run tests, create your GitHub repository, and deploy it. Never commit real passwords, JWT secrets, SMTP credentials, or database credentials.

## Run locally

### 1. Requirements
Install:
- JDK 17+
- Maven 3.9+
- Node.js 20+
- Docker Desktop (or a local MySQL 8 server)

### 2. Start MySQL
From the project root:
```bash
docker compose up -d
```

### 3. Start backend
```bash
cd backend
mvn spring-boot:run
```
Backend: `http://localhost:8080`

### 4. Start frontend
In a second terminal:
```bash
cd frontend
npm install
npm run dev
```
Frontend: `http://localhost:5173`

## Default registration
Open the frontend and register an account. For the first local test, choose `ADMIN`. In a real production system, public registration must not allow users to self-assign ADMIN; create the first administrator through a secure seed process and restrict role assignment to admins.

## API
| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/auth/register` | Create account |
| POST | `/api/auth/login` | Authenticate |
| GET | `/api/auth/me` | Current authenticated user |
| POST | `/api/auth/verify-email` | Verify email using token |
| POST | `/api/auth/forgot-password` | Request reset token |
| POST | `/api/auth/reset-password` | Reset password using token |

## Production checklist
1. Set a strong `JWT_SECRET` (at least 32 random bytes).
2. Configure production MySQL credentials through environment variables.
3. Configure SMTP environment variables for email verification and password reset.
4. Disable public ADMIN role selection; provision admins securely.
5. Use HTTPS and set appropriate CORS origins.
6. Add rate limiting, audit logging, CAPTCHA if needed, and integration tests.
7. Verify all flows on the live deployment before submitting.

See `docs/END_USER_GUIDE.md` and `docs/DEPLOYMENT_GUIDE.md`.
