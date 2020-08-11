package com.izrbh.artists.tools;

/**
 * @ClassName ResultCode
 * @Description 返回码定义
 * 规定:
 * #1表示成功
 * #1001～1999 区间表示参数错误
 * #2001～2999 区间表示用户错误
 * #3001～3999 区间表示接口异常
 * @Author xuping
 * @date 2020.07.15 23:45
 */
public enum ResultCode {
    /* 成功 */
    SUCCESS(200, "成功"),
    /* 系统异常 */
    SYSTEM_ERROR(500, "服务器异常，请重试几次"),

    /* 默认失败 */
    COMMON_FAIL(999, "失败"),

    /* 业务错误 */
    NO_PERMISSION(401, "权限不足"),
    /* 404 */
    NOT_FOUND(404, "请求内容不存在"),

    /* 参数错误：1000～1999 */
    PARAM_NOT_VALID(1001, "参数无效"),
    PARAM_IS_BLANK(1002, "参数为空"),
    PARAM_TYPE_ERROR(1003, "参数类型错误"),
    PARAM_NOT_COMPLETE(1004, "参数缺失"),

    /* 用户错误 */
    USER_NOT_LOGIN(2001, "用户未登录"),
    USER_ACCOUNT_EXPIRED(2002, "账号已过期"),
    USER_CREDENTIALS_ERROR(2003, "账号或密码错误"),
    USER_CREDENTIALS_EXPIRED(2004, "令牌已过期"),
    USER_ACCOUNT_DISABLE(2005, "账号不可用"),
    USER_ACCOUNT_LOCKED(2006, "账号被锁定");


    private Integer code;
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据code获取message
     *
     * @param code
     * @return
     */
    public static String getMessageByCode(Integer code) {
        for (ResultCode ele : values()) {
            if (ele.getCode().equals(code)) {
                return ele.getMessage();
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}