package com.codeb.ims.auth;

import com.codeb.ims.user.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService service;
    public AuthController(AuthService service){this.service=service;}
    @PostMapping("/register") @ResponseStatus(HttpStatus.CREATED)
    public AuthDtos.MessageResponse register(@Valid @RequestBody AuthDtos.RegisterRequest r){return service.register(r);}
    @PostMapping("/login") public AuthDtos.AuthResponse login(@Valid @RequestBody AuthDtos.LoginRequest r){return service.login(r);}
    @GetMapping("/me") public AuthDtos.UserResponse me(@AuthenticationPrincipal User u){return service.me(u);}
    @PostMapping("/verify-email") public AuthDtos.MessageResponse verify(@Valid @RequestBody AuthDtos.TokenRequest r){return service.verify(r.token());}
    @PostMapping("/forgot-password") public AuthDtos.MessageResponse forgot(@RequestBody java.util.Map<String,String> r){return service.forgot(r.getOrDefault("email",""));}
    @PostMapping("/reset-password") public AuthDtos.MessageResponse reset(@Valid @RequestBody AuthDtos.ResetRequest r){return service.reset(r);}
}
