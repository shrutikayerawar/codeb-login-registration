package com.codeb.ims.auth;

import com.codeb.ims.token.*;
import com.codeb.ims.user.*;
import com.codeb.ims.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository users; private final ActionTokenRepository tokens;
    private final PasswordEncoder encoder; private final JwtService jwt;
    private final JavaMailSender mail; private final boolean mailEnabled; private final String frontendUrl;
    public AuthService(UserRepository users, ActionTokenRepository tokens, PasswordEncoder encoder, JwtService jwt,
        JavaMailSender mail, @Value("${app.mail-enabled:false}") boolean mailEnabled,
        @Value("${app.frontend-url}") String frontendUrl) {
        this.users=users; this.tokens=tokens; this.encoder=encoder; this.jwt=jwt;
        this.mail=mail; this.mailEnabled=mailEnabled; this.frontendUrl=frontendUrl;
    }
    public AuthDtos.MessageResponse register(AuthDtos.RegisterRequest r) {
        if(users.existsByEmailIgnoreCase(r.email())) throw new ResponseStatusException(HttpStatus.CONFLICT,"Email is already registered");
        // Public signup must not grant ADMIN in production. Restrict this to SALES_PERSON.
        Role role = r.role()==Role.ADMIN ? Role.SALES_PERSON : r.role();
        User u=users.save(new User(r.fullName().trim(),r.email().trim().toLowerCase(),encoder.encode(r.password()),role));
        createAndSend(u,"VERIFY_EMAIL");
        return new AuthDtos.MessageResponse("Account created. Check your email for verification instructions. If email is not configured, ask the administrator to enable SMTP.");
    }
    public AuthDtos.AuthResponse login(AuthDtos.LoginRequest r) {
        User u=users.findByEmailIgnoreCase(r.email()).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid email or password"));
        if(!encoder.matches(r.password(),u.getPasswordHash())) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid email or password");
        if(!u.getStatus().equals("active")) throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Account is inactive");
        if(!u.isEmailVerified() && mailEnabled) throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Please verify your email before logging in");
        return new AuthDtos.AuthResponse(jwt.generate(u),u.getUserId(),u.getFullName(),u.getEmail(),u.getRole().name(),u.isEmailVerified());
    }
    public AuthDtos.UserResponse me(User u){return new AuthDtos.UserResponse(u.getUserId(),u.getFullName(),u.getEmail(),u.getRole().name(),u.isEmailVerified());}
    public AuthDtos.MessageResponse verify(String token) {
        ActionToken t=validToken(token,"VERIFY_EMAIL"); t.getUser().setEmailVerified(true); users.save(t.getUser()); t.markUsed(); tokens.save(t);
        return new AuthDtos.MessageResponse("Email verified. You can now log in.");
    }
    public AuthDtos.MessageResponse forgot(String email) {
        users.findByEmailIgnoreCase(email).ifPresent(u->createAndSend(u,"RESET_PASSWORD"));
        return new AuthDtos.MessageResponse("If the account exists, password reset instructions have been sent.");
    }
    public AuthDtos.MessageResponse reset(AuthDtos.ResetRequest r) {
        ActionToken t=validToken(r.token(),"RESET_PASSWORD"); t.getUser().setPasswordHash(encoder.encode(r.newPassword()));
        users.save(t.getUser()); t.markUsed(); tokens.save(t);
        return new AuthDtos.MessageResponse("Password updated successfully.");
    }
    private ActionToken validToken(String raw,String purpose) {
        ActionToken t=tokens.findByTokenAndPurposeAndUsedFalse(raw,purpose)
          .orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid or expired token"));
        if(t.getExpiresAt().isBefore(Instant.now())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid or expired token");
        return t;
    }
    private void createAndSend(User u,String purpose) {
        String value=UUID.randomUUID().toString();
        tokens.save(new ActionToken(value,u,purpose,Instant.now().plusSeconds(1800)));
        if(mailEnabled) {
            String path=purpose.equals("VERIFY_EMAIL")?"/verify-email?token=":"/reset-password?token=";
            SimpleMailMessage m=new SimpleMailMessage();
            m.setTo(u.getEmail()); m.setSubject(purpose.equals("VERIFY_EMAIL")?"Verify your Code-B IMS account":"Reset your Code-B IMS password");
            m.setText("Use this link within 30 minutes: "+frontendUrl+path+value);
            mail.send(m);
        } else {
            // Development convenience only. Tokens are intentionally not returned to clients.
            System.out.println("[DEV ONLY] "+purpose+" token for "+u.getEmail()+": "+value);
        }
    }
}
