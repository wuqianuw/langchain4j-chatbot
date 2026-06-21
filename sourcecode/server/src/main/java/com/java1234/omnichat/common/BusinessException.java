package com.java1234.omnichat.common;

/**
 * 业务异常类
 *
 * @author Java1234
 */
public class BusinessException extends RuntimeException {

    private final Integer code;

    /**
     * 构造业务异常
     *
     * @param message 错误信息
     */
    public BusinessException(String message) {
        this(500, message);
    }

    /**
     * 构造业务异常(自定义状态码)
     *
     * @param code    状态码
     * @param message 错误信息
     */
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
