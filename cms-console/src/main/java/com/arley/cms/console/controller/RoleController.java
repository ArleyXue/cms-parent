package com.arley.cms.console.controller;

import com.arley.cms.console.pojo.query.SysRoleQuery;
import com.arley.cms.console.pojo.vo.SysRoleVO;
import com.arley.cms.console.service.SysRoleService;
import com.arley.cms.console.util.AnswerBody;
import com.arley.cms.console.util.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author XueXianlei
 * @Description: 角色控制层
 * @date 2018/8/21 17:07
 */
@Controller
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private SysRoleService sysRoleService;


    /**
     * 分页查询角色列表
     * @param roleQuery
     * @return
     */
    @RequestMapping(value = "/listRoleByPage.do")
    @ResponseBody
    public Pagination listRoleByPage(SysRoleQuery roleQuery) {
        return sysRoleService.listRoleByPay(roleQuery);
    }

    /**
     * 删除角色
     * @param roleId
     * @return
     */
    @RequestMapping(value = "/deleteRole.do")
    @ResponseBody
    public AnswerBody deleteRole(Integer roleId) {
        sysRoleService.deleteRoleById(roleId);
        return AnswerBody.getInstance();
    }

    /**
     * 修改角色
     * @param sysRoleVO
     * @param permissionIds
     * @return
     */
    @RequestMapping(value = "/editRole.do")
    @ResponseBody
    public AnswerBody editRole(SysRoleVO sysRoleVO, String permissionIds) {
        sysRoleService.updateRole(sysRoleVO, permissionIds);
        return AnswerBody.getInstance();
    }

    /**
     * 添加角色
     * @param sysRoleVO
     * @param permissionIds
     * @return
     */
    @RequestMapping(value = "/addRole.do")
    @ResponseBody
    public AnswerBody addRole(SysRoleVO sysRoleVO, String permissionIds) {
        sysRoleService.insertRole(sysRoleVO, permissionIds);
        return AnswerBody.getInstance();
    }

    /**
     * 前往修改角色页面
     * @return
     */
    @RequestMapping(value = "/toRoleEdit.do")
    public String toRoleEdit(Integer roleId, Model model) {
        SysRoleVO roleVO = sysRoleService.getRoleById(roleId);
        model.addAttribute("role", roleVO);
        return "admin/roleEdit";
    }

    /**
     * 前往添加角色页面
     * @return
     */
    @RequestMapping(value = "/toRoleAdd.do")
    public String toRoleAdd() {
        return "admin/roleAdd";
    }

    /**
     * 前往角色列表页面
     * @return
     */
    @RequestMapping(value = "/toRoleList.do")
    public String toRoleList(Model model) {
        List<SysRoleVO> roleVOList = sysRoleService.listRole();
        model.addAttribute("roleList", roleVOList);
        return "admin/roleList";
    }
    
}
