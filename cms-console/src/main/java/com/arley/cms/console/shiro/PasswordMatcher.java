package com.arley.cms.console.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

public class PasswordMatcher extends SimpleCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        // 把token向下强转成实现类
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        // 获取密码
        String inputPwd = new String(upToken.getPassword());
        // 对密码加密
        String md5Pwd = Encrypt.md5(inputPwd, upToken.getUsername());
        // 获取到数据库中的密码
        String dbPwd = info.getCredentials().toString();
        return this.equals(md5Pwd, dbPwd);
    }
    
}