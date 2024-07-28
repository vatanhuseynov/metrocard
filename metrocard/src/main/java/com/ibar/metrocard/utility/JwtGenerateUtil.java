package com.ibar.metrocard.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ibar.metrocard.auth.dto.DecodedAccessToken;
import com.ibar.metrocard.auth.dto.DecodedRefreshToken;
import com.ibar.metrocard.config.property.AppProperties;
import com.ibar.metrocard.user.model.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtGenerateUtil {
    private final AppProperties appProperties;

    public Algorithm getAlgorithm(String key) {
        return Algorithm.HMAC256(key);
    }



    public String createAccessToken(Long userId, String username,List<Role> roles) {
        var authRoles = roles.stream().map(Role::getName).toList();

        return JWT.create()
                .withSubject(username)
                .withClaim("userId", userId)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + appProperties.getJwtExpiration()))
                .withClaim("roles",authRoles)  //Əgər birdən çox rolumuz olarsa rahat olsun deyə əlavə etmişəm
                .sign(getAlgorithm(appProperties.getJwtSecret()));
    }

    public String createRefreshToken(Long userId, String username) {
        Algorithm algorithm = getAlgorithm(appProperties.getJwtRefreshSecret());
        return JWT.create()
                .withSubject(username)
                .withClaim("userId", userId)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + appProperties.getJwtRefreshExpiration()))
                .sign(algorithm);
    }
    

    public DecodedAccessToken decodeToken(String token){
        Algorithm algorithm =getAlgorithm(appProperties.getJwtSecret());
        DecodedJWT decodedJWT;
        try{
            decodedJWT = JWT.require(algorithm).build().verify(token);
        }catch (Exception e){
            decodedJWT = JWT.require(algorithm).build().verify(token.substring(7));
        }
        var username = decodedJWT.getSubject();
        var userId = decodedJWT.getClaim("userId").asLong();
        var roles = decodedJWT.getClaim("roles").asList(String.class);
        return DecodedAccessToken.builder()
                .username(username)
                .userId(userId)
                .roles(roles)
                .build();
    }

    public DecodedRefreshToken decodeRefreshToken(String token) {
        try {
            Algorithm algorithm = getAlgorithm(appProperties.getJwtRefreshSecret());
            var decodedJwt = JWT.require(algorithm).build().verify(token);
            var username = decodedJwt.getSubject();
            var id = decodedJwt.getClaim("id").asLong();
            return new DecodedRefreshToken(id,username);
        } catch (Exception e) {
            throw new ResponseStatusException(FORBIDDEN, "Sessiyanız bitmişdir.");
        }
    }


}
