package Controllers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

public class JwtHandler {
    // Your 384-bit secret key (base64 encoded)
    private static final String secret = "YWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYQ==";

    // Decode the base64-encoded key
    private static final byte[] decodedKey = Base64.getDecoder().decode(secret);

    // Create a Key object
    private static final Key key = new SecretKeySpec(decodedKey, SignatureAlgorithm.HS384.getJcaName());

    public static String createJwtToken(String userId, String username, String password, String email, long expirationMillis) {
        // Define the expiration time
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiryDate = new Date(nowMillis + expirationMillis);

        // Create the JWT
        return Jwts.builder()
                .setId(userId)
                .setIssuedAt(now)
                .setSubject("Login Credentials")
                .claim("username", username)
                .claim("password", password)
                .claim("email", email)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS384) // Use HS384 for signing
                .compact();
    }

    public static Claims decodeJwtToken(String jwt) {
        // Parse and validate the JWT token
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt);

        // Get the claims from the token
        Claims claims = claimsJws.getBody();
        return claims;
    }
}
