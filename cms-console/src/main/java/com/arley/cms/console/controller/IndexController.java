package com.arley.cms.console.controller;

import com.arley.cms.console.pojo.Do.SysPermissionDO;
import com.arley.cms.console.pojo.Do.SysUserDO;
import com.arley.cms.console.pojo.vo.SysPermissionVO;
import com.arley.cms.console.service.SysPermissionService;
import com.arley.cms.console.util.DateUtil;
import com.arley.cms.console.util.ShiroUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author XueXianlei
 * @Description:
 * @date 2018/8/16 19:21
 */
@Controller
public class IndexController {

    @Autowired
    private SysPermissionService sysPermissionService;

    /**
     * 前往index页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/index.do")
    public String index(Model model) {
        SysUserDO user = ShiroUtils.getLoginUser();
        // 查询用户所拥有的用户权限
        List<SysPermissionDO> permissionList = ShiroUtils.getHavePermissionList();
        if (permissionList == null) {
            // 把用户拥有的权限放在session中
            permissionList = sysPermissionService.listHaveHierarchyPermission(user.getUserId());
            ShiroUtils.setHavePermissionList(permissionList);
        }
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
        SysUserDO user = ShiroUtils.getLoginUser();
        model.addAttribute("userName", user.getUserName());
        LocalDateTime loginTime = user.getLoginTime();
        model.addAttribute("loginTime", DateUtil.formatLocalDateTime(loginTime, "yyyy年MM月dd日 HH:mm:ss"));
        return "welcome";
    }
}
