package com.arley.cms.console.controller;

import com.arley.cms.console.pojo.Do.SysPermissionDO;
import com.arley.cms.console.pojo.vo.SysPermissionVO;
import com.arley.cms.console.pojo.vo.SysUserVO;
import com.arley.cms.console.service.SysPermissionService;
import com.arley.cms.console.service.SysUserService;
import com.arley.cms.console.util.DateUtils;
import com.arley.cms.console.util.ShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * @author XueXianlei
 * @Description:
 * @date 2018/8/16 19:21
 */
@Controller
public class IndexController {

    @Autowired
    private SysPermissionService sysPermissionService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SessionDAO sessionDAO;

    /**
     * 前往index页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/index.do")
    public String index(Model model) {
        SysUserVO user = sysUserService.getSysUser(ShiroUtils.getLoginUser().getUserId());
        // 查询所有的权限
        List<SysPermissionVO> permissionList = sysPermissionService.listHaveHierarchyPermission();
        model.addAttribute("user", user);
        model.addAttribute("permissionList", permissionList);
        return "index";
    }

    /**
     * 前往welcome页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/welcome.do")
    public String welcome(Model model) {
        // 用户总数
        Integer totalCountUser = sysUserService.countSysUser();
        // 在线人数
        Collection<Session> activeSessions = sessionDAO.getActiveSessions();
        model.addAttribute("totalCountUser", activeSessions.size() + "/" + totalCountUser);

        // 当前用户信息
        SysUserVO user = sysUserService.getSysUser(ShiroUtils.getLoginUser().getUserId());
        model.addAttribute("userName", user.getUserName());
        LocalDateTime loginTime = user.getLoginTime();
        model.addAttribute("loginTime", DateUtils.formatLocalDateTime(loginTime, "yyyy年MM月dd日 HH:mm:ss"));
        return "welcome";
    }
}
