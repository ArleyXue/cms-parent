package com.arley.cms.console.pojo.query;

/**
 * @author XueXianlei
 * @Description: 角色Query
 * @date 2018/9/5 11:01
 */
public class SysRoleQuery {

    private Integer page;

    private Integer limit;

    private Integer roleId;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
