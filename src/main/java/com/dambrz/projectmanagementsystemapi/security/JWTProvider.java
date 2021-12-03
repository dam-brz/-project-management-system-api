package com.dambrz.projectmanagementsystemapi.security;

import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.dambrz.projectmanagementsystemapi.exceptions.ExceptionMessageContent.*;
import static com.dambrz.projectmanagementsystemapi.security.SecurityConstraints.EXPIRATION_TIME;
import static com.dambrz.projectmanagementsystemapi.security.SecurityConstraints.SECRET;

@Component
public class JWTProvider {

    static Logger logger = Logger.getLogger(JWTProvider.class.getName());

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
            logger.log(Level.WARNING, SIGNATURE_INVALID_JWT_SIGNATURE);
        } catch (MalformedJwtException ex) {
            logger.log(Level.WARNING, MALFORMED_INVALID_JWT_SIGNATURE);
        } catch (ExpiredJwtException ex) {
            logger.log(Level.WARNING, EXPIRED_INVALID_JWT_SIGNATURE);
        } catch (UnsupportedJwtException ex) {
            logger.log(Level.WARNING, UNSUPPORTED_INVALID_JWT_SIGNATURE);
        }
        return false;
    }

    public String getUsernameFromToken(String token) {
        return (String) Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody()
                .get("username");
    }
}
