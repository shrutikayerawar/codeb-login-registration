package com.codeb.ims.user;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(nullable = false, length = 100)
    private String fullName;
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    @Column(nullable = false)
    private String passwordHash;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 30)
    private Role role;
    @Column(nullable = false, length = 20)
    private String status = "active";
    @Column(nullable = false)
    private boolean emailVerified = false;
    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    protected User() {}
    public User(String fullName, String email, String passwordHash, Role role) {
        this.fullName = fullName; this.email = email; this.passwordHash = passwordHash; this.role = role;
    }
    public Long getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public Role getRole() { return role; }
    public String getStatus() { return status; }
    public boolean isEmailVerified() { return emailVerified; }
    public void setEmailVerified(boolean value) { this.emailVerified = value; }
    public Instant getCreatedAt() { return createdAt; }
    public void setPasswordHash(String value) { this.passwordHash = value; }
}
