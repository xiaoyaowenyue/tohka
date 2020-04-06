package com.ht.tohka.common.core;

import lombok.Getter;

import java.io.Serializable;

/**
 * 统一返回结果
 */
@Getter
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 8992436576262574064L;

    private int code;

    private String msg;

    private T data;


    public static <T> Result<T> error(String msg) {
        return error(500, msg);
    }

    public static <T> Result<T> error(int code, String msg) {
        Result<T> result = new Result<>();
        result.code = code;
        result.msg = msg;
        return result;
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.code = 200;
        result.data = data;
        result.msg = "success";
        return result;
    }

}
