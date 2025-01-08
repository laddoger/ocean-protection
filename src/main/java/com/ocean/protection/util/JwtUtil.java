package com.ocean.protection.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    private static final long EXPIRE_TIME = 24 * 60 * 60 * 1000; // 24小时
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(Long userId, String username) {
        try {
            Date now = new Date();
            Date expiration = new Date(now.getTime() + EXPIRE_TIME);

            String token = Jwts.builder()
                    .setSubject(username)
                    .claim("userId", userId)
                    .setIssuedAt(now)
                    .setExpiration(expiration)
                    .signWith(SECRET_KEY)
                    .compact();
            
            // 验证生成的token
            Claims claims = parseToken(token);
            if (claims != null) {
                return token;
            } else {
                throw new RuntimeException("Token generation failed: Unable to parse generated token");
            }
        } catch (Exception e) {
            throw new RuntimeException("Token generation failed: " + e.getMessage());
        }
    }

    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }
} 