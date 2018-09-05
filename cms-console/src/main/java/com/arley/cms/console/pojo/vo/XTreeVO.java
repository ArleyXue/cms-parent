package com.arley.cms.console.pojo.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 菜单封装类
 * @author XueXianlei
 * @date Created in 2018/2/7 0:06
 */
public class XTreeVO implements Serializable {

    private Integer value;

    private String title;

    private Integer parentId;

    private Boolean checked;

    private Boolean disabled;

    private List<XTreeVO> data;

    public XTreeVO(Integer value, String title, Integer parentId, Boolean checked, Boolean disabled) {
        this.value = value;
        this.title = title;
        this.parentId = parentId;
        this.checked = checked;
        this.disabled = disabled;
        this.data = new ArrayList<>();
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public List<XTreeVO> getData() {
        return data;
    }

    public void setData(List<XTreeVO> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "XTreeVO{" +
                "value=" + value +
                ", title='" + title + '\'' +
                ", parentId=" + parentId +
                ", checked=" + checked +
                ", disabled=" + disabled +
                ", data=" + data +
                '}';
    }
}
