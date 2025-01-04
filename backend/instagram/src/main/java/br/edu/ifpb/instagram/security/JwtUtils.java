package br.edu.ifpb.instagram.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

@Component
public class JwtUtils {

    private final int jwtExpirationMs = 86400000;   // Tempo de expiração: 24h
    private byte[] jwtSecret;

    public JwtUtils() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA512");
            SecretKey secretKey = keyGen.generateKey();
            this.jwtSecret = secretKey.getEncoded();
        } catch (Exception e) {
            System.err.println("Erro ao criar o gerador de chaves: " + e.getMessage());
        }
    }

    // Gera o token JWT
    public String generateToken(Authentication authentication) {

        String username = authentication.getName();

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(Keys.hmacShaKeyFor(jwtSecret), SignatureAlgorithm.HS512)
                .compact();
    }

    // Valida o token JWT
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.err.println("Token inválido: " + e.getMessage());
            return false;
        }
    }

    // Extrai o username do token
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
