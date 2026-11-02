# Test Checklist

Run these checks locally and repeat them against the deployed URL.

- [ ] Register a new Sales Person using a valid email.
- [ ] Attempt duplicate registration with the same email; expect a conflict message.
- [ ] Attempt registration with a malformed email; expect validation failure.
- [ ] Attempt registration with a password shorter than 8 characters; expect validation failure.
- [ ] Verify email using the emailed token (SMTP must be enabled).
- [ ] Log in with correct credentials; expect dashboard.
- [ ] Log in with wrong password; expect an error and no dashboard.
- [ ] Call `/api/auth/me` without a token; expect unauthorized.
- [ ] Call `/api/auth/me` with a valid token; expect current user details.
- [ ] Request password reset; confirm generic response.
- [ ] Reset password using a valid token; log in with the new password.
- [ ] Confirm invalid/expired tokens are rejected.
- [ ] Confirm Logout clears browser token.
- [ ] Wait 15 minutes without activity; confirm frontend logs out.
- [ ] Confirm mobile layout is usable.
- [ ] Confirm HTTPS on deployed URLs.
