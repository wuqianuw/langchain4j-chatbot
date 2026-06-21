package com.java1234.omnichat.common;

import lombok.Data;

/**
 * 统一API响应结果封装
 *
 * @param <T> 数据类型
 * @author Java1234
 */
@Data
public class Result<T> {

    /** 状态码 */
    private Integer code;

    /** 提示信息 */
    private String message;

    /** 响应数据 */
    private T data;

    /**
     * 成功响应(无数据)
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 成功响应(带数据)
     *
     * @param data 响应数据
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    /**
     * 成功响应(自定义消息)
     *
     * @param message 提示信息
     * @param data    响应数据
     */
    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 失败响应
     *
     * @param message 错误信息
     */
    public static <T> Result<T> error(String message) {
        return error(500, message);
    }

    /**
     * 失败响应(自定义状态码)
     *
     * @param code    状态码
     * @param message 错误信息
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
