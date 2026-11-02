# Code-B IMS — End User Guide
## Login and Registration Module

### 1. Purpose
This module provides account registration and secure login for the Code-B Internal Management System. It supports two intended roles: Admin and Sales Person. Admin users are intended to manage users and system functions; Sales Persons receive restricted access.

### 2. Create an account
1. Open the application URL in a modern browser.
2. Select **Register**.
3. Enter your full name.
4. Enter a valid email address.
5. Create a password with at least eight characters.
6. Select Sales Person. Administrator accounts should be provisioned by an authorized administrator, not through public registration.
7. Select **Create account**.
8. If email delivery is enabled, open the verification email and follow its link. If the application is in local development mode, ask the developer to check the backend console for the development token.

### 3. Log in
1. Open the application.
2. Select **Login**.
3. Enter your registered email address and password.
4. Select **Sign in**.
5. On successful authentication, the dashboard displays your name and role.

### 4. Dashboard and roles
- **Admin:** Intended to have full access to system administration and user/invoice management.
- **Sales Person:** Intended to access only permitted sales and invoicing functions.

The first module currently demonstrates the authenticated role on the dashboard. Additional business modules are delivered separately.

### 5. Forgot password
1. Select **Forgot your password?** on the login screen.
2. Enter the email address used during registration.
3. Select **Send reset instructions**.
4. If SMTP is configured, follow the reset link received by email.
5. Enter the reset token and a new password on the reset form.
6. Sign in using the new password.

For security, the application shows a generic response whether or not the email address exists.

### 6. Logout and inactivity
Select **Logout** to end the current browser session. The frontend also logs the user out after 15 minutes without detected activity. The API token has a configurable one-hour default expiry.

### 7. Troubleshooting
- **Cannot connect:** Confirm the backend is running and the frontend API URL is correct.
- **Invalid email or password:** Check spelling and use the registered email.
- **Email not received:** Check spam and ask the administrator to verify SMTP configuration.
- **Database error:** Confirm MySQL is running and the configured database credentials are correct.
- **Expired reset/verification link:** Request a new link.

### 8. Security
Do not share your password or verification/reset links. Use a unique password. Always log out on shared devices. Production deployments must use HTTPS and protected environment variables.

### 9. Support
Contact the project administrator or internship mentor for account access and technical issues.
