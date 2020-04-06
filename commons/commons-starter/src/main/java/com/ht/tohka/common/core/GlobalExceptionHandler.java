package com.ht.tohka.common.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return Result.error(422, "缺少" + e.getParameterName() + "参数");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result requestNotReadable(Exception e) {
        return Result.error(400, "Bad Request");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Result illegalArgumentException(IllegalArgumentException e) {
        return Result.error(422, "非法参数");
    }

    @ExceptionHandler(ApiException.class)
    public Result apiException(ApiException e) {
        return Result.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error("系统异常:", e);
        return Result.error(500, "系统异常");
    }

}
