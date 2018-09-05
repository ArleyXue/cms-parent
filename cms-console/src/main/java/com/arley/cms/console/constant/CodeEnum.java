package com.arley.cms.console.constant;

/**
 * @author XueXianlei
 * @Description: code码
 * @date 2018/8/16 17:29
 */
public enum CodeEnum {
    // 成功
    SUCCESS("0000", "success"),
    FILE("0001", "fail"),
    EDIT_ADMIN("0100", "admin账号禁止此项操作"),
    ERROR("9999", "数据异常");



    private String code;
    private String msg;

    CodeEnum(String code, String msg) {
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
