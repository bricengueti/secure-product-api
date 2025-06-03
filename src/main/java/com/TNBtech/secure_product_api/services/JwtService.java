package com.TNBtech.secure_product_api.services;

import com.TNBtech.secure_product_api.entities.Jwt;
import com.TNBtech.secure_product_api.entities.RefreshToken;
import com.TNBtech.secure_product_api.entities.User;
import com.TNBtech.secure_product_api.repositories.JwtRepository;
import com.TNBtech.secure_product_api.repositories.RefreshTokenRepository;
import com.TNBtech.secure_product_api.repositories.UserRepository;
import com.TNBtech.secure_product_api.services.UserService;
import com.TNBtech.secure_product_api.utils.EltNotFoundException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class JwtService {

    public static final String BEARER = "Bearer";

    private final UserRepository userRepository;
    private final JwtRepository jwtRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.expiration}")
    private long jwtExpirationMillis;

    @Value("${refresh.expiration}")
    private long refreshExpirationMillis;

    @Value("${jwt.secret}")
    private String secret;

    public JwtService(UserRepository userRepository, JwtRepository jwtRepository, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.jwtRepository = jwtRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }
    public Map<String, String> generate(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable : " + username));

        this.invalidateUserTokens(user);

        Map<String, String> jwtMap = new HashMap<>(generateJwt(user));

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpirationMillis));

        refreshTokenRepository.save(refreshToken);

        Jwt jwt = new Jwt();
        jwt.setToken(jwtMap.get("Bearer"));
        jwt.setUser(user);
        jwt.setRefreshToken(refreshToken);
        jwt.setRevoked(false);
        jwt.setExpired(false);

        jwtRepository.save(jwt);

        jwtMap.put("refresh_token", refreshToken.getToken());
        return jwtMap;
    }

    public Map<String, String> generateJwt(User user) {
        long currentTime = System.currentTimeMillis();
        long expiration = currentTime + jwtExpirationMillis;

        Map<String, Object> claims = Map.of(
                "username", user.getUsername(),
                Claims.EXPIRATION, new Date(expiration),
                Claims.SUBJECT, user.getUsername(),
                "token_id", UUID.randomUUID().toString()
        );

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(expiration))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();

        return Map.of(BEARER, token);
    }

    public RefreshToken validateRefreshToken(String tokenValue) {
        RefreshToken token = refreshTokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new EltNotFoundException("Invalid refresh token"));
        if (token.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token expired");
        }
        return token;
    }

    public void invalidateUserTokens(User user) {
        List<Jwt> tokens = jwtRepository
                .findAllByUserUsernameAndExpiredFalseAndRevokedFalse(user.getUsername())
                .peek(jwt -> {
                    jwt.setRevoked(true);
                    jwt.setExpired(true);
                })
                .collect(Collectors.toList());

        jwtRepository.saveAll(tokens);
    }


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public void logout() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof User)) {
            throw new RuntimeException("Invalid token principal");
        }

        User user = (User) principal;

        Jwt jwt = jwtRepository
                .findByUserUsernameAndExpiredAndRevoked(user.getUsername(), false, false)
                .orElseThrow(() -> new EltNotFoundException("Active JWT not found"));

        jwt.setRevoked(true);
        jwt.setExpired(true);
        jwtRepository.save(jwt);
    }


    @Scheduled(cron = "@daily")
    public void cleanExpiredTokens() {
        jwtRepository.deleteAllByExpiredAndRevoked(true, true);
    }
}
