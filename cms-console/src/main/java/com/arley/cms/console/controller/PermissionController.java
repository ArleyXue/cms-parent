package com.arley.cms.console.controller;

import com.arley.cms.console.pojo.Do.SysPermissionDO;
import com.arley.cms.console.pojo.vo.SysPermissionVO;
import com.arley.cms.console.pojo.vo.XTreeVO;
import com.arley.cms.console.service.SysPermissionService;
import com.arley.cms.console.util.AnswerBody;
import com.arley.cms.console.util.MenuUtils;
import com.arley.cms.console.util.Pagination;
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
    public Pagination listPermission() {
        List<SysPermissionVO> sysPermissionVOList = sysPermissionService.listPermission();
        Pagination<SysPermissionVO> pagination = new Pagination<>();
        pagination.setData(sysPermissionVOList);
        pagination.setCount(sysPermissionVOList.size());
        return pagination;
    }

    /**
     * 修改菜单
     * @param sysPermissionVO
     * @return
     */
    @RequestMapping(value = "/editPermission.do")
    @ResponseBody
    public AnswerBody editPermission(SysPermissionVO sysPermissionVO) {
        sysPermissionService.updatePermission(sysPermissionVO);
        return AnswerBody.buildAnswerBody();
    }

    /**
     * 添加菜单
     * @param sysPermissionVO
     * @return
     */
    @RequestMapping(value = "/addPermission.do")
    @ResponseBody
    public AnswerBody addPermission(SysPermissionVO sysPermissionVO) {
        sysPermissionService.insertPermission(sysPermissionVO);
        return AnswerBody.buildAnswerBody();
    }

    /**
     * 获取树状结构的菜单
     * @param roleId
     * @return
     */
    @RequestMapping(value = "/listXTreePermission.do")
    @ResponseBody
    public Set<XTreeVO> listXTreePermission(Integer roleId) {
        List<SysPermissionVO> menuList = sysPermissionService.listPermission();
        List<SysPermissionVO> roleMenuList = null;
        if (null != roleId) {
            roleMenuList = sysPermissionService.listPermissionByRoleId(roleId);
        }
        return MenuUtils.makeXTreeList(menuList, roleMenuList);
    }

    /**
     * 前往修改菜单页面
     * @param permissionId
     * @param model
     * @return
     */
    @RequestMapping(value = "/toPermissionEdit.do")
    public String toPermissionEdit(Integer permissionId, Model model) {
        SysPermissionVO sysPermissionVO = sysPermissionService.getPermission(permissionId);
        String parentMenuName = "(无)";
        if (null != sysPermissionVO.getParentId()) {
            SysPermissionVO parentPermissionVo = sysPermissionService.getPermission(sysPermissionVO.getParentId());
            parentMenuName = parentPermissionVo.getMenuName();
        }
        model.addAttribute("menu", sysPermissionVO);
        model.addAttribute("parentMenuName", parentMenuName);
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
        SysPermissionVO permission = new SysPermissionVO();
        permission.setMenuName("(无)");
        if (null != parentId) {
            permission = sysPermissionService.getPermission(parentId);
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
        List<SysPermissionVO> menuList = sysPermissionService.listPermission();
        model.addAttribute("menuList", menuList);
        return "admin/menuList";
    }
}
