package com.codeb.ims.token;

import com.codeb.ims.user.User;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "action_tokens")
public class ActionToken {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true, length = 100) private String token;
    @ManyToOne(optional = false) private User user;
    @Column(nullable = false, length = 20) private String purpose;
    @Column(nullable = false) private Instant expiresAt;
    private boolean used = false;
    protected ActionToken() {}
    public ActionToken(String token, User user, String purpose, Instant expiresAt) {
        this.token=token; this.user=user; this.purpose=purpose; this.expiresAt=expiresAt;
    }
    public User getUser(){return user;}
    public String getPurpose(){return purpose;}
    public Instant getExpiresAt(){return expiresAt;}
    public boolean isUsed(){return used;}
    public void markUsed(){this.used=true;}
}
