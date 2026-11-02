package com.codeb.ims.auth;

import com.codeb.ims.user.Role;
import jakarta.validation.constraints.*;

public class AuthDtos {
    public record RegisterRequest(@NotBlank @Size(max=100) String fullName,
        @NotBlank @Email @Size(max=100) String email,
        @NotBlank @Size(min=8,max=72) String password, @NotNull Role role) {}
    public record LoginRequest(@NotBlank @Email String email, @NotBlank String password) {}
    public record TokenRequest(@NotBlank String token) {}
    public record ResetRequest(@NotBlank String token, @NotBlank @Size(min=8,max=72) String newPassword) {}
    public record AuthResponse(String token, Long userId, String fullName, String email, String role, boolean emailVerified) {}
    public record MessageResponse(String message) {}
    public record UserResponse(Long userId, String fullName, String email, String role, boolean emailVerified) {}
}
