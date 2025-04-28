package motherson.crm.v3.config;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component

public class JwtUtils {

    // Replace with your secret key (must be at least 32 characters for HS256)
    private static final String JWT_SECRET = "AsecureAndVeryLongSecretKeyForJWT";
    private static final long JWT_EXPIRATION_MS = 86400000; // Token valid for 1 day

    // Generate a secure signing key
    private static Key getSigningKey() {
        return Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
    }

    /**
     * Generate a JWT token for a given username.
     *
     * @param username The subject (username) for whom the token is generated.
     * @return The generated JWT token.
     */
    public String generateToken(String phoneNumber) {
        return Jwts.builder()
                .setSubject(phoneNumber) // Username as the token subject
                .setIssuedAt(new Date()) // Current timestamp
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS)) // Expiration
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Signing with HS256 algorithm
                .compact();
    }

    /**
     * Extract the username from a given JWT token.
     *
     * @param token The JWT token.
     * @return The username (subject) from the token.
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject(); // Return the username (subject)
    }

    /**
     * Validate a JWT token.
     *
     * @param token The JWT token to validate.
     * @return True if the token is valid, false otherwise.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token); // Parsing validates the token
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Log the exception if needed
            System.out.println("Invalid JWT token: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if a JWT token has expired.
     *
     * @param token The JWT token to check.
     * @return True if the token has expired, false otherwise.
     */
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
            return expiration.before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            // Log the exception if needed
            System.out.println("Unable to determine token expiration: " + e.getMessage());
            return true; // Consider token expired if an exception occurs
        }
    }
}
