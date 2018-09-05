package com.arley.cms.console.util;

import com.arley.cms.console.pojo.Do.SysUserDO;
import com.arley.cms.console.pojo.Do.SysPermissionDO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import java.util.List;

/**
 * shiro工具类
 * Created by XueXianlei on 2017/11/15
 */
public class ShiroUtils {

    /**
     * 获取登录用户
     * @return
     */
    public static SysUserDO getLoginUser(){
        Subject subject = SecurityUtils.getSubject();
        return (SysUserDO)subject.getPrincipal();
    }

    /**
     * 获取用户拥有的权限
     * @return
     */
    public static List<SysPermissionDO> getHavePermissionList(){
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        return (List) session.getAttribute("havePermissionList");
    }

    /**
     * 把用户拥有的权限放进session中
     * @param permissionList
     */
    public static void setHavePermissionList(List<SysPermissionDO> permissionList) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        session.setAttribute("havePermissionList", permissionList);
    }

}
