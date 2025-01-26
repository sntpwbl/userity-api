package com.study.userity.security;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.algorithms.Algorithm;
import com.study.userity.dto.TokenDTO;

import jakarta.annotation.PostConstruct;

@Service
public class JwtTokenProvider {
    
    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey = "secret";

    @Value("${security.jwt.token.expire-length:3600000}")
    private long expireLengthInMilliseconds = 3600000;

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
        String refreshToken = getAccessToken(username, roles, now);
        return new TokenDTO(username, true, now, validity, accessToken, refreshToken);
        }
        
        // TODO: Implement both of get token methods
        private String getAccessToken(String username, List<String> roles, Date now, Date validity) {
            throw new UnsupportedOperationException("Unimplemented method 'getAccessToken'");
        }
        private String getAccessToken(String username, List<String> roles, Date now) {
            throw new UnsupportedOperationException("Unimplemented method 'getAccessToken'");
        }
}
