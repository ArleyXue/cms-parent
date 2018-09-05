package com.arley.cms.console.shiro;

import com.arley.cms.console.pojo.Do.SysPermissionDO;
import com.arley.cms.console.pojo.Do.SysUserDO;
import com.arley.cms.console.service.SysPermissionService;
import com.arley.cms.console.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShiroAuthRealm extends AuthorizingRealm {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysPermissionService sysPermissionService;

    /**
     * 认证.登录
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取用户输入的token
        UsernamePasswordToken utoken=(UsernamePasswordToken) token;
        String username = utoken.getUsername();
        SysUserDO user = sysUserService.getOne(new QueryWrapper<SysUserDO>().lambda().eq(SysUserDO::getUserName, username));
        // 账号不存在
        if (null == user) {
            throw new UnknownAccountException();
        }
        if (0 == user.getUserState()) {
            throw new DisabledAccountException();
        }
        //放入shiro.调用CredentialsMatcher检验密码
        return new SimpleAuthenticationInfo(user, user.getPassword(),this.getClass().getName());
    }

    /**
     * 授权
     * @param principal
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        SysUserDO user = (SysUserDO)principal.getPrimaryPrincipal();
        // 获取此用户的所有权限
        List<SysPermissionDO> sysPermissionList = sysPermissionService.listHavePermission(user.getUserId());
        // 遍历组装
        Set<String> permissionSet = new HashSet<>();
        if (sysPermissionList != null && sysPermissionList.size() > 0) {
            sysPermissionList.forEach(p -> permissionSet.add(p.getMenuCode()));
        }
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        //将权限放入shiro中
        info.addStringPermissions(permissionSet);
        return info;
    }

}