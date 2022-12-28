package com.nofront.plantinggrassbe.Utils;

import com.auth0.jwk.JwkException;
import io.jsonwebtoken.*;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class JwtTokenUtils extends AbstractTokenUtils {
    private final String SECRET_KEY = "secretkey";

    public String generateAccessToken(String userId, String provider){
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParams(createHeaders())
                .setClaims(createClaims(userId, provider))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Duration.ofHours(12).toMillis()))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
    public String generateRefreshToken() {
        Date now = new Date();
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Duration.ofDays(60).toMillis()))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }


    private Claims getClaims(String token) {
        try{
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            // 토큰 유효성 확인
        } catch (SecurityException e) {
            System.out.println("e = " + e);
            System.out.println("Invalid JWT Signature.");
        } catch (MalformedJwtException e) {
            System.out.println("e = " + e);
            System.out.println("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            System.out.println("e = " + e);
            System.out.println("Expired JWT token.");
            throw e;
        } catch (UnsupportedJwtException e) {
            System.out.println("e = " + e);
            System.out.println("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            System.out.println("e = " + e);
            System.out.println("JWT token compact of handler are invalid.");
        }
        return null;
    }

    public Claims createClaims(String userId, String provider){
        Claims claims = Jwts.claims();
        claims.put("sub", userId);
        claims.put("iss", provider);
        return claims;
    }

    public Map<String, Object> createHeaders(){
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "RS256");
        return header;
    }

    @Override
    public boolean validate(String token) throws ParseException, JwkException, ExpiredJwtException {
        return getClaims(token) != null;
    }

    @Override
    public OauthInfo getOauthInfo(String token) throws ParseException {
        return OauthInfo.builder()
                .userId(getSubject(token))
                .provider(getIssuer(token))
                .build();
    }

}
