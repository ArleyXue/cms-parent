package com.arley.cms.console.pojo.vo;

import java.time.LocalDateTime;

/**
 * @Description: 登录日志VO
 * @author XueXianlei
 * @date Created in 2018/2/7 0:06
 */
public class LoginLogVO {

    /**
     * id
     */
    private Long id;

    /**
     * 登录名
     */
    private String userName;

    /**
     * 登录ip
     */
    private String loginIp;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;

    /**
     * 描述
     */
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "LoginLogDO{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", loginIp='" + loginIp + '\'' +
                ", loginTime=" + loginTime +
                ", content='" + content + '\'' +
                '}';
    }
}
