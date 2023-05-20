package com.example.javarest.provider;

import com.example.javarest.entity.UserEntity;
import com.example.javarest.exceprion.TokenException;
import com.example.javarest.exceprion.UserAlreadyExistException;
import lombok.NonNull;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component
public class JwtProvider {

    private final SecretKey jwtAccessSecret;

    public JwtProvider(
            @Value("${jwt.secret.access}") String jwtAccessSecret
    ) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
    }

    public String generateAccessToken(@NonNull UserEntity user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(accessExpiration)
                .signWith(jwtAccessSecret)
                .claim("id", String.valueOf(user.getId()))
                .claim("username", user.getUsername())
                .compact();
    }

    public boolean validateAccessToken(@NonNull String accessToken) throws TokenException {
        return validateToken(accessToken, jwtAccessSecret);
    }

    private boolean validateToken(@NonNull String token, @NonNull Key secret) throws TokenException {
        try {
            Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token).getBody();
            return true;
        } catch (ExpiredJwtException e) {
            throw new TokenException("Token expired");
        } catch (UnsupportedJwtException e) {
            throw new TokenException("Unsupported jwt");
        } catch (MalformedJwtException e) {
            throw new TokenException("Malformed jwt");
        } catch (SignatureException e) {
            throw new TokenException("Invalid signature");
        } catch (Exception e) {
            throw new TokenException("invalid token");
        }
    }

    public Claims getAccessClaims(@NonNull String token) {
        return getClaims(token, jwtAccessSecret);
    }

    private Claims getClaims(@NonNull String token, @NonNull Key secret) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public Long getTokenFromRequest(HttpServletRequest request) throws TokenException {
        try {
            final String token = request.getHeader(AUTHORIZATION);
            if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
                String jwtToken = token.substring(7);
                validateAccessToken(jwtToken);
                Claims claims = getAccessClaims(jwtToken);
                return Long.valueOf(String.valueOf(claims.get("id")));
            }
            return null;
        } catch (TokenException e) {
            throw new TokenException("Invalid signature");
        } catch (Exception e) {
            throw new TokenException("invalid token");
        }

    }

}