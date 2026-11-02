# Deployment Guide (student checklist)

Hosting providers change their free plans frequently. Confirm current pricing, sleep limits, database availability, and Java build support before choosing a provider. This project is not deployed by this package.

## Recommended deployment shape
- Frontend: a static hosting service that supports Vite builds.
- Backend: a Java 17 hosting service or VM/container host.
- Database: managed MySQL 8 service or a separately hosted MySQL instance.

## Backend environment variables
- `PORT`: port supplied by hosting provider
- `DB_URL`: JDBC URL, e.g. `jdbc:mysql://HOST:3306/DB?useSSL=true&serverTimezone=UTC`
- `DB_USERNAME`: database username
- `DB_PASSWORD`: database password
- `JWT_SECRET`: long random secret (32+ bytes)
- `JWT_EXPIRATION_MS`: token lifetime in milliseconds
- `FRONTEND_URL`: deployed frontend origin
- `MAIL_ENABLED`: `true` only after SMTP is configured
- `MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`, `MAIL_PASSWORD`: SMTP credentials

## Backend build/start
Build command: `mvn clean package`
Start command: `java -jar target/ims-auth-1.0.0.jar`

## Frontend build
Set `VITE_API_URL` to the deployed backend base URL.
Build command: `npm ci && npm run build`
Publish directory: `dist`

## Deployment steps
1. Create a MySQL database and user. Do not use the local Docker credentials in production.
2. Deploy the backend from the `backend` directory or configure the host's root/build paths.
3. Set all backend environment variables in the hosting dashboard.
4. Deploy the frontend and set `VITE_API_URL` before building.
5. Update backend CORS allowed origin in `SecurityConfig.java` to the exact deployed frontend origin. Do not use `*` in production.
6. Configure SMTP and set `MAIL_ENABLED=true`; test verification and reset emails.
7. Open the live frontend URL and test registration, verification, login, dashboard, reset, and logout.
8. Check backend logs and database records. Never expose secrets in screenshots.
9. Add the live URLs to the GitHub README and assignment PDF.

## Important limitations to address before production
- The starter restricts public ADMIN registration by converting public ADMIN requests to SALES_PERSON. Implement a secure admin-provisioning path before claiming full admin management.
- The frontend idle timer is a usability safeguard, not server-enforced inactivity invalidation. Add server-side refresh/session revocation if the evaluator requires strict inactivity expiry.
- Add rate limiting, stronger password policy, integration tests, and security review.
- Hibernate `ddl-auto: update` is convenient for development. Use managed database migrations for production.
