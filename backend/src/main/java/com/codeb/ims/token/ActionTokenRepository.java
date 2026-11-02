package com.codeb.ims.token;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface ActionTokenRepository extends JpaRepository<ActionToken, Long> {
    Optional<ActionToken> findByTokenAndPurposeAndUsedFalse(String token, String purpose);
}
