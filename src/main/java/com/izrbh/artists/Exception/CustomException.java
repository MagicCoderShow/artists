package com.izrbh.artists.Exception;

import com.izrbh.artists.tools.ResultCode;

/**
 * 通用异常异常
 *
 * @Author xuping
 * @date 2020/8/9
 */
public class CustomException extends RuntimeException {
    private ResultCode resultCode;
    public CustomException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
    }
}
