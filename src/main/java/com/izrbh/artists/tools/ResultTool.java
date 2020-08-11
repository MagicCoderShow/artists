package com.izrbh.artists.tools;


/**
 * @ClassName ResultTool
 * @Description 返回体构造工具
 * @Author xuping
 * @date 2020.07.15 23:45
 */
public class ResultTool {
    public static JsonResult success() {
        return new JsonResult(true);
    }

    public static <T> JsonResult<T> success(T data) {
        return new JsonResult(true, data);
    }

    public static JsonResult fail() {
        return new JsonResult(false);
    }

    public static JsonResult fail(ResultCode resultEnum) {
        return new JsonResult(false, resultEnum);
    }
}