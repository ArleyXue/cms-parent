package com.arley.cms.console.controller;

import com.arley.cms.console.constant.PublicCodeEnum;
import com.arley.cms.console.constant.PublicConstants;
import com.arley.cms.console.exception.CustomException;
import com.arley.cms.console.pojo.query.SysRoleQuery;
import com.arley.cms.console.pojo.vo.SysRoleVO;
import com.arley.cms.console.pojo.vo.SysUserVO;
import com.arley.cms.console.service.SysRoleService;
import com.arley.cms.console.util.AnswerBody;
import com.arley.cms.console.util.Pagination;
import com.arley.cms.console.util.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Objects;

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
        return sysRoleService.listRoleByPage(roleQuery);
    }

    /**
     * 删除角色
     * @param roleId
     * @return
     */
    @RequestMapping(value = "/deleteRole.do")
    @ResponseBody
    public AnswerBody deleteRole(Integer roleId) {
        // 如果不是admin 不能删除
        if (Objects.equals(PublicConstants.SUPER_ROLE_ID, roleId)) {
            SysUserVO loginUser = ShiroUtils.getLoginUser();
            if (!Objects.equals(loginUser.getUserName(), PublicConstants.ADMIN_USER_NAME)) {
                throw new CustomException(PublicCodeEnum.EDIT_ADMIN.getCode(), "仅超级管理员用户可操作此记录!", CustomException.LOGGER_WARN_TYPE);
            }
        }
        sysRoleService.deleteRoleById(roleId);
        return AnswerBody.buildAnswerBody();
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
        // 如果不是admin 不能操作
        if (Objects.equals(PublicConstants.SUPER_ROLE_ID, sysRoleVO.getRoleId())) {
            SysUserVO loginUser = ShiroUtils.getLoginUser();
            if (!Objects.equals(loginUser.getUserName(), PublicConstants.ADMIN_USER_NAME)) {
                throw new CustomException(PublicCodeEnum.EDIT_ADMIN.getCode(), "仅超级管理员用户可操作此记录!", CustomException.LOGGER_WARN_TYPE);
            }
        }
        sysRoleService.updateRole(sysRoleVO, permissionIds);
        return AnswerBody.buildAnswerBody();
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
        return AnswerBody.buildAnswerBody();
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
        SysUserVO loginUser = ShiroUtils.getLoginUser();
        List<SysRoleVO> roleVOList = sysRoleService.listRole();
        model.addAttribute("roleList", roleVOList);
        model.addAttribute("checkSuperRole", Objects.equals(PublicConstants.ADMIN_USER_NAME, loginUser.getUserName()));
        model.addAttribute("superRoleId", PublicConstants.SUPER_ROLE_ID);
        return "admin/roleList";
    }
    
}
