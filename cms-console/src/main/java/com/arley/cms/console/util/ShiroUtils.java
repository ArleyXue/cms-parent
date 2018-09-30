package com.arley.cms.console.util;

import com.arley.cms.console.pojo.vo.SysUserVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * shiro工具类
 * Created by XueXianlei on 2017/11/15
 */
public class ShiroUtils {

    /**
     * 获取登录用户
     * @return
     */
    public static SysUserVO getLoginUser(){
        Subject subject = SecurityUtils.getSubject();
        return (SysUserVO)subject.getPrincipal();
    }
}
