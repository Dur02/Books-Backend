package com.books.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "dur02.jwt")
public class JwtUtil {

    private String secret;
    private long expire;
    private String header;

    public String generateToken(Integer userId) {
        Date nowDate = new Date();
        Date expiredDate = new Date(nowDate.getTime() + expire*1000);

        return Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setSubject(userId+"")
                .setIssuedAt(nowDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS512,secret)
                .compact();
    }

    public Claims getClaimByToken(String token){
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e) {
            log.debug("validate is token error",e);
            return null;
        }
    }

    public boolean isTokenExpired(Date expiration) {return expiration.before(new Date());}
}
