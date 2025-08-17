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
}
