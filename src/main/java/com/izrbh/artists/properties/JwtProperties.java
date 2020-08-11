package com.izrbh.artists.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author xuping
 * @date 2020/8/9
 */
@Component
@EnableConfigurationProperties(JwtProperties.class)
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    //token过期时间 毫秒
    private long tokenExpireTime;

    //RefreshToken过期时间 毫秒
    private long refreshTokenExpireTime;

    //token加密密钥
    private String accessTokenSecret;

    //refresh加密密钥
    private String refreshTokenSecret;

    public long getTokenExpireTime() {
        return tokenExpireTime;
    }

    public void setTokenExpireTime(long tokenExpireTime) {
        this.tokenExpireTime = tokenExpireTime;
    }

    public long getRefreshTokenExpireTime() {
        return refreshTokenExpireTime;
    }

    public void setRefreshTokenExpireTime(long refreshTokenExpireTime) {
        this.refreshTokenExpireTime = refreshTokenExpireTime;
    }

    public String getAccessTokenSecret() {
        return accessTokenSecret;
    }

    public void setAccessTokenSecret(String accessTokenSecret) {
        this.accessTokenSecret = accessTokenSecret;
    }

    public String getRefreshTokenSecret() {
        return refreshTokenSecret;
    }

    public void setRefreshTokenSecret(String refreshTokenSecret) {
        this.refreshTokenSecret = refreshTokenSecret;
    }
}
