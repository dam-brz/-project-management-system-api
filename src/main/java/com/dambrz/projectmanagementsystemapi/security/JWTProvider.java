package com.dambrz.projectmanagementsystemapi.security;

import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.dambrz.projectmanagementsystemapi.security.SecurityConstraints.EXPIRATION_TIME;
import static com.dambrz.projectmanagementsystemapi.security.SecurityConstraints.SECRET;

@Component
public class JWTProvider {

    public String generateToken(Authentication authentication) {

        UserDetailsImplementation user = (UserDetailsImplementation) authentication.getPrincipal();
        Date now = new Date((System.currentTimeMillis()));
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
        String userId = Long.toString(user.getId());

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);
        claims.put("username", user.getUsername());

        return Jwts.builder()
                .setSubject(userId)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            System.out.println("SIGNATURE Invalid JWT Signature");
        } catch (MalformedJwtException ex) {
            System.out.println("MALFORMED Invalid JWT Signature");
        } catch (ExpiredJwtException ex) {
            System.out.println("EXPIRED Invalid JWT Signature");
        } catch (UnsupportedJwtException ex) {
            System.out.println("UNSUPPORTED Invalid JWT Signature");
        }
        return false;
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        String id = (String) claims.get("id");

        return Long.parseLong(id);
    }

    public String getUsernameFromToken(String token) {
        return (String) Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody()
                .get("username");
    }
}
