package com.study.userity.security;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.study.userity.dto.TokenDTO;
import com.study.userity.exception.InvalidJWTAuthenticationException;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtTokenProvider {
    
    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey = "secret";

    @Value("${security.jwt.token.expire-length:3600000}")
    private long expireLengthInMilliseconds = 3600000;

    private Date refreshTokenExpireLengthInMilliseconds =  new Date(new Date().getTime() + (expireLengthInMilliseconds * 3));

    Algorithm algorithm = null;
    private final UserDetailsService userDetailsService;

    public JwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

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

        public Authentication getAuthentication(String token){
            DecodedJWT decodedToken = decodeToken(token);
            UserDetails details = userDetailsService.loadUserByUsername(decodedToken.getSubject());
            return new UsernamePasswordAuthenticationToken(details, "", details.getAuthorities());
        }
            
        public DecodedJWT decodeToken(String token) {
            return JWT.require(algorithm).build().verify(token);
        }

        public String resolveTokenToString(HttpServletRequest request){
            String authorizationHeader = request.getHeader("Authorization");
            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
                return authorizationHeader.substring("Bearer ".length());
            }
            return null;
        }

        public boolean validateToken(String token) throws InvalidJWTAuthenticationException{
            DecodedJWT decodedToken = decodeToken(token);
            try {
                if(decodedToken.getExpiresAt().before(new Date())){
                    throw new InvalidJWTAuthenticationException("Expired token.");
                }
                return true;
            } catch (InvalidJWTAuthenticationException e) {
                return false;
            } catch (Exception e){
                throw new InvalidJWTAuthenticationException("Invalid token.");
            }
        }
}
