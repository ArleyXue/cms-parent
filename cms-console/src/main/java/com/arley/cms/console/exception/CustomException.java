package com.arley.cms.console.exception;

/**
 * @author XueXianlei
 * @Description: 自定义异常类
 * @date Created in 2018/4/4 15:48
 */
public class CustomException extends RuntimeException {

    private String code;

    private String msg;

    public CustomException(String code, String msg) {
        super(code + " | " + msg);
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
