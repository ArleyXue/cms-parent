package com.arley.cms.console.util;


import com.alibaba.fastjson.annotation.JSONField;
import com.arley.cms.console.constant.CodeEnum;

/**
 * @author XueXianlei
 * @Description:
 * @date Created in 2018/4/8 15:11
 */
public class AnswerBody {
    // 返回结果代号
    @JSONField(ordinal=1)
    private String resultCode;
    // 返回结果描述
    @JSONField(ordinal=2)
    private String resultDesc;
    //返回结果数据
    @JSONField(ordinal=3)
    private Object resultData;

    private AnswerBody(String resultCode, String resultDesc) {
        this.resultCode = resultCode;
        this.resultDesc = resultDesc;
        this.resultData = new Object();
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public Object getResultData() {
        return resultData;
    }

    public void setResultData(Object resultData) {
        this.resultData = resultData;
    }

    @Override
    public String toString() {
        return "AnswerBody{" +
                "resultCode='" + resultCode + '\'' +
                ", resultDesc='" + resultDesc + '\'' +
                ", resultData=" + resultData +
                '}';
    }

    /**
     * 获得成功code的body
     * @return
     */
    public static AnswerBody getInstance() {
        return new AnswerBody("0000", "success");
    }

    /**
     * 获得成功code的body
     * @return
     */
    public static AnswerBody getInstance(Object data) {
        AnswerBody body = new AnswerBody("0000", "success");
        body.setResultData(data);
        return body;
    }

    /**
     * 获得指定错误code的body
     * @return
     */
    public static AnswerBody getInstance(CodeEnum code) {
        return new AnswerBody(code.getCode(), code.getMsg());
    }

    /**
     * 获得指定code的body
     * @return
     */
    public static AnswerBody getInstance(String resultCode, String resultDesc) {
        return new AnswerBody(resultCode, resultDesc);
    }
}
