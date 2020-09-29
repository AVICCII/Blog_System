package com.aviccii.cc.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Map;

/**
 * @author aviccii 2020/9/28
 * @Discrimination
 */
public class JwtUtil {

    //盐值
    private static String key = "0b60057b079d630e38c6cd456f3406bf";

    private static long ttl = 2*60*60*1000;

    public String getKey(){
        return key;
    }

    public long getTtl(){
        return ttl;
    }

    public void setTtl(long ttl) {
        JwtUtil.ttl = ttl;
    }

    /**
     *
     * @param claims 载荷
     * @param ttl
     * @return
     */
    public  static String createJWT(Map<String,Object> claims, long ttl){
        JwtUtil.ttl=ttl;
        return createJWT(claims);
    }

    public static String createJWT(Map<String,Object> claims){

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256,key);

        if (claims != null){
            builder.setClaims(claims);
        }

        if (ttl>0){
            builder.setExpiration(new Date(nowMillis+ttl));
        }

        return builder.compact();
    }

    public Claims parseJWT(String jwtStr){
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwtStr)
                .getBody();
    }
}
