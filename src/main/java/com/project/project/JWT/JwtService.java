package com.project.project.JWT;


import com.project.project.user_config.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.security.Key;
import java.util.function.Function;


@Service
public class JwtService {
    private final Long EXPIRED_IN = (long) (100000 * 60 * 24);

//    @Value("${jwt.access.key}")
    private final String JwtKey = "53A73E5F1C4E0A2D3B5F2D784E6A1B423D6F247D1F6E5C3A596D635A75327855";

    /**
     * The DateExpiration(String) extracts the date expiration.
     * @param token access token.
     * @return extraction date.
     */
    private Date DateExpiration(String token) {
        return ExtractClaim(token, Claims::getExpiration);
    }

    /**
     * The DateExpiration(String) extracts all data from access token.
     * @param token access token.
     * @return all data, that extract.
     */
    public Claims ExtractAll(String token) {
        return Jwts
                .parser()
                .setSigningKey(Handler())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * The ExtractClaim(String, Function) extracts all data from access token.
     * @param token access token.
     * @param claimsResolvers
     * @return all data, that extract.
     */
    private <T> T ExtractClaim(String token,
                               Function<Claims, T> claimsResolvers) {
        final Claims claims = ExtractAll(token);
        return claimsResolvers.apply(claims);
    }

    /**
     * The GenerateToken(Map, UserDetails) generates token.
     * @param additionalData secondary data.
     * @param userDetails main data.
     * @return jwt token.
     */
    private String GenerateToken(Map<String, Object> additionalData,
                                     UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(additionalData)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() +
                               EXPIRED_IN))
                .signWith(Handler(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * The GenerateTokenValue(UserDetails) generates token.
     * @param userDetails main data.
     * @return jwt token.
     */
    public String GenerateTokenValue(UserDetails userDetails) {
        Map<String, Object> additionalDataMap = new HashMap<>();
        if(userDetails instanceof User user) {
            additionalDataMap.put("role", user.getRole());
        }

        return GenerateToken(additionalDataMap, userDetails);
    }

    /**
     * The Handler() sets the generation key.
     * @return jwt key.
     */
    private Key Handler() {
        return Keys
                .hmacShaKeyFor(Decoders
                        .BASE64
                        .decode(JwtKey)
                );
    }

    /**
     * The IsTokenExpired(String) checks if token is expired.
     * @param token access token.
     * @return true, if token expired.
     */
    private boolean IsTokenExpired(String token) {
        return DateExpiration(token)
                .before(new Date());
    }

    /**
     * The IsTokenValid(Map, UserDetails) checks validation of the token.
     * @param token access token.
     * @param userDetails main user data.
     * @return true, if token is valid.
     */
    public boolean IsTokenValid(String token, UserDetails userDetails) {
        return (UsernameExtraction(token)
                .equals(userDetails
                        .getUsername())) &&
                !IsTokenExpired(token);
    }

    /**
     * The UsernameExtraction(token) extracts username from token.
     * @param token access token.
     * @return username.
     */
    public String UsernameExtraction(String token) {
        return ExtractClaim(token, Claims::getSubject);
    }
}
