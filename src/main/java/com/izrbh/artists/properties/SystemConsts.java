package com.izrbh.artists.properties;

/**
 * 系统常量
 */
public class SystemConsts {
    //缓存前缀
    public static final String PREFIX_CACHE = "artists:cache:";
    //登录密码加盐
    public static final String LOGIN_SALT = "artists-salt";
    //jwt accessToken 缓存前缀
    public final static String PREFIX_ACCESS_TOKEN_CACHE = PREFIX_CACHE+"token:access:";
    //shiro缓存前缀
    public final static String PREFIX_REFRESH_TOKEN_CACHE = PREFIX_CACHE+"token:refresh:";
    //JWT认证请求头属性
    public static final String JWT_HEADER="Authorization";
    //JWT-userid
    public static final String JWT_USERID = "userid";
    //JWT-account
    public static final String JWT_ACCOUNT = "username";
    //JWT-TYPE
    public final static String JWT_TYPE_ACCESS = "access";
    //JWT-TYPE
    public final static String JWT_TYPE_REFRESH = "refresh";
    //JWT-currentTimeMillis
    public final static String CURRENT_TIME_MILLIS = "CurrentTimeMillis";
}