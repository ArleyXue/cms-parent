package com.arley.cms.console.util;

import java.util.List;

/**
 * @author XueXianlei
 * @Description: layui 分页返回数据结构
 * @date 2018/8/19 11:17
 */
public class Pagination<T> {

    private int code;

    private String msg;

    private long count;

    private List<T> data;

    public Pagination() {
        code = 0;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
