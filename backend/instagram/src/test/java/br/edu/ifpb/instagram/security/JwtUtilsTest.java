package br.edu.ifpb.instagram.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ActiveProfiles("test")
public class JwtUtilsTest {

    private JwtUtils jwtUtils;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        authentication = Mockito.mock(Authentication.class);
    }

    @Test
    void testGenerateToken_ShouldReturnValidJwt_WhenAuthenticationIsValid() {
        String username = "matheus";
        when(authentication.getName()).thenReturn(username);
        String token = jwtUtils.generateToken(authentication);
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(jwtUtils.validateToken(token));
    }

    @Test
    void testGetUsernameFromToken_ShouldReturnUsername_WhenTokenIsValid() {
        String expectedUsername = "matheus";
        when(authentication.getName()).thenReturn(expectedUsername);
        String token = jwtUtils.generateToken(authentication);
        String actualUsername = jwtUtils.getUsernameFromToken(token);
        assertNotNull(actualUsername);
        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    void testGetUsernameFromToken_ShouldThrowException_WhenTokenIsInvalid() {
        String invalidToken = "token invalid test";
        assertThrows(RuntimeException.class, () -> {
            jwtUtils.getUsernameFromToken(invalidToken);
        });
    }

    @Test
    void testValidateToken_ShouldReturnTrue_ForValidToken() {
        when(authentication.getName()).thenReturn("mthstest");
        String validToken = jwtUtils.generateToken(authentication);
        assertTrue(jwtUtils.validateToken(validToken));
    }

    @Test
    void testValidateToken_ShouldReturnFalse_ForInvalidSignatureToken() {
        String invalidSecretKey = "outraChaveMuitoSeguraDePeloMenos64CaracteresParaHS512JwtAlgoritmo";
        byte[] invalidJwtSecret = java.util.Base64.getEncoder().encode(invalidSecretKey.getBytes());
        String tokenWithInvalidSignature = io.jsonwebtoken.Jwts.builder()
                .setSubject("mthstest")
                .setIssuedAt(new java.util.Date())
                .setExpiration(new java.util.Date(System.currentTimeMillis() + 10000))
                .signWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(invalidJwtSecret), io.jsonwebtoken.SignatureAlgorithm.HS512)
                .compact();

        assertFalse(jwtUtils.validateToken(tokenWithInvalidSignature));
    }

    @Test
    void testValidateToken_ShouldReturnFalse_ForExpiredToken() {
        String SECRET_KEY = "umaChaveMuitoSeguraDePeloMenos64CaracteresParaHS512JwtAlgoritmo";
        byte[] jwtSecret = java.util.Base64.getEncoder().encode(SECRET_KEY.getBytes());
        String expiredToken = io.jsonwebtoken.Jwts.builder()
                .setSubject("mthstest")
                .setIssuedAt(new java.util.Date())
                .setExpiration(new java.util.Date(System.currentTimeMillis() - 1000))
                .signWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(jwtSecret), io.jsonwebtoken.SignatureAlgorithm.HS512)
                .compact();
        assertFalse(jwtUtils.validateToken(expiredToken));
    }

    @Test
    void testValidateToken_ShouldReturnFalse_ForNullOrEmptyToken() {
        boolean isNullTokenValid = jwtUtils.validateToken(null);
        boolean isEmptyTokenValid = jwtUtils.validateToken("");
        assertFalse(isNullTokenValid, "null token should be invalid");
        assertFalse(isEmptyTokenValid, "empty token should be invalid");
    }
}
