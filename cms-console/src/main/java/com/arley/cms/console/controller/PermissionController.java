package com.arley.cms.console.controller;

import com.arley.cms.console.pojo.vo.XTreeVO;
import com.arley.cms.console.pojo.Do.SysPermissionDO;
import com.arley.cms.console.service.SysPermissionService;
import com.arley.cms.console.util.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Set;

/**
 * @author XueXianlei
 * @Description: 菜单控制层
 * @date 2018/8/22 9:35
 */
@Controller
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private SysPermissionService sysPermissionService;


    /**
     * 获取所有菜单
     * @return
     */
    @RequestMapping(value = "/listPermission.do")
    @ResponseBody
    public Pagination listAdminByPage() {
        List<SysPermissionDO> permissionList = sysPermissionService.list(null);
        Pagination<SysPermissionDO> pagination = new Pagination<>();
        pagination.setData(permissionList);
        pagination.setCount(permissionList.size());
        return pagination;
    }

    /**
     * 修改菜单
     * @param sysPermission
     * @return
     */
    @RequestMapping(value = "/editPermission.do")
    @ResponseBody
    public AnswerBody editPermission(SysPermissionDO sysPermission) {
        SysPermissionDO permission = sysPermissionService.getById(sysPermission.getPermissionId());
        permission.setMenuName(sysPermission.getMenuName());
        permission.setMenuCode(sysPermission.getMenuCode());
        permission.setMenuIcon(sysPermission.getMenuIcon());
        permission.setMenuPriority(sysPermission.getMenuPriority());
        permission.setMenuUrl(sysPermission.getMenuUrl());
        permission.setMenuType(sysPermission.getMenuType());
        permission.setModifier(ShiroUtils.getLoginUser().getUserName());
        permission.setGmtModified(DateUtil.getLocalDateTime());
        sysPermissionService.updateById(permission);
        return AnswerBody.getInstance();
    }

    /**
     * 添加菜单
     * @param sysPermission
     * @return
     */
    @RequestMapping(value = "/addPermission.do")
    @ResponseBody
    public AnswerBody addPermission(SysPermissionDO sysPermission) {
        sysPermission.setModifier(ShiroUtils.getLoginUser().getUserName());
        sysPermission.setMenuState(1);
        sysPermission.setGmtCreate(DateUtil.getLocalDateTime());
        sysPermission.setGmtModified(DateUtil.getLocalDateTime());
        sysPermissionService.save(sysPermission);
        return AnswerBody.getInstance();
    }

    /**
     * 获取树状结构的菜单
     * @param roleId
     * @return
     */
    @RequestMapping(value = "/listXTreePermission.do")
    @ResponseBody
    public Set<XTreeVO> listXTreePermission(Integer roleId) {
        List<SysPermissionDO> menuList = sysPermissionService.list(new QueryWrapper<SysPermissionDO>().lambda().orderByAsc(SysPermissionDO::getMenuPriority));
        List<SysPermissionDO> roleMenuList = null;
        if (null != roleId) {
            roleMenuList = sysPermissionService.listPermissionByRoleId(roleId);
        }
        return MenuUtil.makeXTreeList(menuList, roleMenuList);
    }

    /**
     * 前往修改菜单页面
     * @param parentId
     * @param model
     * @return
     */
    @RequestMapping(value = "/toPermissionEdit.do")
    public String toPermissionEdit(Integer parentId, Model model) {
        SysPermissionDO permission = sysPermissionService.getById(parentId);
        model.addAttribute("menu", permission);
        return "admin/menuEdit";
    }

    /**
     * 前往添加菜单页面
     * @param parentId
     * @param model
     * @return
     */
    @RequestMapping(value = "/toPermissionAdd.do")
    public String toPermissionAdd(Integer parentId, Model model) {
        SysPermissionDO permission = new SysPermissionDO();
        permission.setMenuName("(无)");
        if (null != parentId) {
            permission = sysPermissionService.getById(parentId);
        }
        model.addAttribute("permission", permission);
        return "admin/menuAdd";
    }

    /**
     * 前往菜单列表页面
     * @return
     */
    @RequestMapping(value = "/toPermissionList.do")
    public String toPermissionList(Model model) {
        List<SysPermissionDO> menuList = sysPermissionService.list(new QueryWrapper<SysPermissionDO>().lambda().orderByAsc(SysPermissionDO::getMenuPriority));
        model.addAttribute("menuList", menuList);
        return "admin/menuList";
    }
}
