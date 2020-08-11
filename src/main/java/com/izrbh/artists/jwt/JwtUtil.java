package com.izrbh.artists.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.izrbh.artists.properties.JwtProperties;
import com.izrbh.artists.properties.SystemConsts;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Description JWT 工具类
 */
@Component
public class JwtUtil {
    @Resource
    JwtProperties jwtProperties;

    /**
     * 生成 access token
     *
     * @param userId   用户id
     * @param userName 用户名
     * @param type     生成的token类型
     * @return 加密的token
     */
    public String createToken(String userId, String userName, String type) {

        Date date = new Date(System.currentTimeMillis() + 1000*(SystemConsts.JWT_TYPE_ACCESS.equals(type) ? jwtProperties.getTokenExpireTime() : jwtProperties.getRefreshTokenExpireTime()));
        Algorithm algorithm = Algorithm.HMAC256(SystemConsts.JWT_TYPE_ACCESS.equals(type) ? jwtProperties.getAccessTokenSecret() : jwtProperties.getRefreshTokenSecret());
        // 附带username信息
        return JWT.create()
                .withClaim(SystemConsts.JWT_USERID, userId)
                .withClaim(SystemConsts.JWT_ACCOUNT, userName)
                .withClaim(SystemConsts.CURRENT_TIME_MILLIS, String.valueOf(System.currentTimeMillis()))
                //到期时间
                .withExpiresAt(date)
                //创建一个新的JWT，并使用给定的算法进行标记
                .sign(algorithm);
    }


    /**
     * 校验 token 是否正确
     *
     * @param token token
     * @param type  token类型
     * @return 是否正确
     */
    public boolean verify(String token, String type) {
        try {
            String userId = getClaim(token, SystemConsts.JWT_USERID);
            String userName = getClaim(token, SystemConsts.JWT_ACCOUNT);
            String currentTimeMillis = getClaim(token, SystemConsts.CURRENT_TIME_MILLIS);
            Algorithm algorithm = Algorithm.HMAC256(SystemConsts.JWT_TYPE_ACCESS.equals(type) ? jwtProperties.getAccessTokenSecret() : jwtProperties.getRefreshTokenSecret());
            //在token中附带了username信息
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim(SystemConsts.JWT_USERID, userId)
                    .withClaim(SystemConsts.JWT_ACCOUNT, userName)
                    .withClaim(SystemConsts.CURRENT_TIME_MILLIS, currentTimeMillis)
                    .build();
            //验证 token
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 获得token中的信息，无需secret解密也能获得
     *
     * @param token
     * @return token中对应claim的值
     * @claimName 需要获取秘钥中key的名
     */
    public String getClaim(String token, String claimName) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(claimName).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }
}