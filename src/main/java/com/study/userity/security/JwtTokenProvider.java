package com.study.userity.security;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.study.userity.dto.TokenDTO;

import jakarta.annotation.PostConstruct;

@Service
public class JwtTokenProvider {
    
    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey = "secret";

    @Value("${security.jwt.token.expire-length:3600000}")
    private long expireLengthInMilliseconds = 3600000;

    private Date refreshTokenExpireLengthInMilliseconds =  new Date(new Date().getTime() + (expireLengthInMilliseconds * 3));

    Algorithm algorithm = null;

    @PostConstruct
    public void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }

    public TokenDTO createAccessToken(String username, List<String> roles){
        Date now = new Date();
        Date validity = new Date(now.getTime() + expireLengthInMilliseconds);
        String accessToken = getAccessToken(username, roles, now, validity);
        String refreshToken = getRefreshToken(username, roles, now);
        return new TokenDTO(username, true, now, validity, accessToken, refreshToken);
        }
        
        private String getAccessToken(String username, List<String> roles, Date now, Date validity) {
            String issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
            return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withSubject(username)
                .withIssuer(issuerUrl)
                .sign(algorithm)
                .strip();
        }
        
        private String getRefreshToken(String username, List<String> roles, Date now) {
            return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(refreshTokenExpireLengthInMilliseconds)
                .withSubject(username)
                .sign(algorithm)
                .strip();
        }
}
