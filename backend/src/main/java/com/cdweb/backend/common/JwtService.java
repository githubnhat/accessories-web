package com.cdweb.backend.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cdweb.backend.entities.Roles;
import com.cdweb.backend.entities.Users;
import com.cdweb.backend.services.IUsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtService {

    private static final Algorithm algorithm = Algorithm.HMAC256(Constant.SECRET_KEY);
    @Value("${token.access-life-time}")
    private int accessTokenLifeTimeHours;
    @Value("${token.refresh-life-time}")
    private int refreshTokenLifeTimeHours;

//    public boolean isValidToken(String token) {
//        return !isNoneValidToken(token);
//    }

    private boolean isNoneValidToken(String token) {
        if (StringUtils.isEmpty(token)) return true;
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decoded = verifier.verify(token);
            Long expiredTime = decoded.getExpiresAt().getTime();
            return System.currentTimeMillis() >= expiredTime;

    }

    public boolean isNoneValidAccessToken(String token){
        if (isNoneValidToken(token)) {
            return true;
        } else {
            Boolean isAccessToken =  JWT.decode(token).getClaim("access_token").as(Boolean.class);
            return isAccessToken == null || !isAccessToken;
        }
    }

    public boolean isNoneValidRefreshToken(String token){
        if (isNoneValidToken(token)) {
            return true;
        } else {
            Boolean isRefreshToken =  JWT.decode(token).getClaim("refresh_token").as(Boolean.class);
            return isRefreshToken == null || !isRefreshToken;
        }
    }

    public Users getUserFromToken(String token) {
        if (isNoneValidToken(token)) return null;
        String roleCode = JWT.decode(token).getClaim("role").as(String.class);

        return Users.builder()
                .username(JWT.decode(token).getSubject())
                .roles(Roles.builder().roleCode(roleCode).build())
                .build();
    }


    public String generateAccessToken(Users user) {
        return this.getAccessToken(user, this.accessTokenLifeTimeHours);
    }

    public String generateRefreshToken(Users user) {
        return this.getRefreshToken(user, this.refreshTokenLifeTimeHours);
    }

    public int getRefreshTokenLifeTimeHours(){
        return this.refreshTokenLifeTimeHours;
    }

    private String getAccessToken(Users user, int expiredMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expiredMinutes);
        JWTCreator.Builder creator = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(calendar.getTime())
                .withIssuedAt(new Date())
                .withClaim("role", user.getRoles().getRoleCode())
                .withClaim("access_token", true);;
        return creator.sign(algorithm);
    }
    private String getRefreshToken(Users user, int expiredMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expiredMinutes);
        JWTCreator.Builder creator = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(calendar.getTime())
                .withIssuedAt(new Date())
                .withClaim("role", user.getRoles().getRoleCode())
                .withClaim("refresh_token", true);
        return creator.sign(algorithm);
    }

}
