package com.izrbh.artists.handler;

import com.izrbh.artists.Exception.CustomException;
import com.izrbh.artists.tools.JsonResult;
import com.izrbh.artists.tools.ResultCode;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 异常统一处理
 */
@RestControllerAdvice
public class RestExceptionHandler {


    @ExceptionHandler({Exception.class})
    @ResponseBody
    public JsonResult ExceptionHandler(Exception e, HttpServletRequest request) {
        JsonResult jsonResult = null;
        if(e instanceof org.springframework.web.servlet.NoHandlerFoundException){

            jsonResult = new JsonResult(false,ResultCode.NOT_FOUND);
        }else {
            jsonResult = new JsonResult(false, ResultCode.COMMON_FAIL);
            jsonResult.setErrorMsg(e.getMessage());
        }
        return jsonResult;
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseBody
    public JsonResult MissingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e, HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult(false, ResultCode.PARAM_IS_BLANK);
        jsonResult.setErrorMsg("必须的" + e.getParameterType() + "类型参数" + e.getParameterName() + "不存在");
        return jsonResult;
    }


    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    public JsonResult MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e, HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult(false, ResultCode.PARAM_TYPE_ERROR);
        StringBuffer errorMsg = new StringBuffer();
        BindingResult bindingResult = e.getBindingResult();
        bindingResult.getFieldErrors().stream().forEach(ex -> errorMsg.append("参数:").append(ex.getField()).append("，错误：").append(ex.getDefaultMessage()).append(";"));
        jsonResult.setErrorMsg(errorMsg.toString());
        return jsonResult;
    }


    @ExceptionHandler({CustomException.class})
    @ResponseBody
    public JsonResult CustomExceptionHandler(CustomException e, HttpServletRequest request) {
        return new JsonResult(false, e.getResultCode());
    }
}