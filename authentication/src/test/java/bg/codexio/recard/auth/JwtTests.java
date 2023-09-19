package bg.codexio.recard.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class JwtTests {

    private static final long EXPIRATION_TIME_MS = 1000;

    @Test
    public void login_onJwtExpired_ThrowsJwtException() throws NoSuchAlgorithmException {
        final var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        final var keyPair = keyPairGenerator.generateKeyPair();
        final var expiration = new Date(System.currentTimeMillis() - EXPIRATION_TIME_MS);
        final var token = Jwts.builder()
                .setExpiration(expiration)
                .signWith(keyPair.getPrivate()) // Sign with private key
                .compact();

        assertThrows(ExpiredJwtException.class, () -> Jwts.parserBuilder()
                .setSigningKey(keyPair.getPublic()) // Verify with public key
                .build()
                .parseClaimsJws(token));
    }
}