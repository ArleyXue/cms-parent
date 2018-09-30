package com.arley.cms.console.controller;

import com.arley.cms.console.constant.PublicCodeEnum;
import com.arley.cms.console.constant.PublicConstants;
import com.arley.cms.console.pojo.Do.SysUserDO;
import com.arley.cms.console.pojo.query.SysUserQuery;
import com.arley.cms.console.pojo.vo.SysRoleVO;
import com.arley.cms.console.pojo.vo.SysUserVO;
import com.arley.cms.console.service.SysRoleService;
import com.arley.cms.console.service.SysUserService;
import com.arley.cms.console.util.AnswerBody;
import com.arley.cms.console.util.DateUtils;
import com.arley.cms.console.util.Pagination;
import com.arley.cms.console.util.ShiroUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @author XueXianlei
 * @Description: 管理员控制层
 * @date 2018/8/19 10:10
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SessionDAO sessionDAO;
    /**
     * 分页查询管理员列表
     * @param sysUserQuery
     * @return
     */
    @RequestMapping(value = "/listAdminByPage.do")
    @ResponseBody
    public Pagination listAdminByPage(SysUserQuery sysUserQuery) {
        Pagination pagination = sysUserService.listSysUserByPage(sysUserQuery);
        return pagination;
    }

    /**
     * 添加管理员
     * @param sysUserVO
     * @return
     */
    @RequestMapping(value = "/addAdmin.do")
    @ResponseBody
    public AnswerBody addAdmin(SysUserVO sysUserVO, Integer roleId) {
        sysUserService.insertSysUser(sysUserVO, roleId);
        return AnswerBody.buildAnswerBody();
    }

    /**
     * 修改管理员
     * @param sysUserVO
     * @return
     */
    @RequestMapping(value = "/editAdmin.do")
    @ResponseBody
    public AnswerBody editAdmin(SysUserVO sysUserVO, Integer roleId, Integer resetPassword) {
        SysUserVO sysUser = sysUserService.getSysUser(sysUserVO.getUserId());
        if (Objects.equals(PublicConstants.ADMIN_USER_NAME, sysUser.getUserName())) {
            return AnswerBody.buildAnswerBody(PublicCodeEnum.EDIT_ADMIN);
        }
        sysUserService.updateSysUser(sysUserVO, roleId, resetPassword);
        return AnswerBody.buildAnswerBody();
    }

    /**
     * 删除管理员
     * @param userId
     * @return
     */
    @RequestMapping(value = "/deleteAdmin.do")
    @ResponseBody
    public AnswerBody deleteAdmin(Integer userId) {
        SysUserVO sysUser = sysUserService.getSysUser(userId);
        if (Objects.equals(PublicConstants.ADMIN_USER_NAME, sysUser.getUserName())) {
            return AnswerBody.buildAnswerBody(PublicCodeEnum.EDIT_ADMIN);
        }
        sysUserService.deleteSysUser(userId);
        return AnswerBody.buildAnswerBody();
    }

    /**
     * 检查用户名是否存在
     * @param userName
     * @return
     */
    @RequestMapping(value = "/checkIsExistUserName.do")
    @ResponseBody
    public AnswerBody checkIsExistUserName(String userName) {
        boolean isExist = sysUserService.checkIsExistUserName(userName);
        Map<String, Boolean> result = new HashMap<>(1);
        result.put("isExist", isExist);
        return AnswerBody.buildAnswerBody(result);
    }

    /**
     * 修改用户状态
     * @param userId
     * @return
     */
    @RequestMapping(value = "/updateUserState.do")
    @ResponseBody
    public AnswerBody updateUserState(Integer userId) {
        SysUserVO sysUser = sysUserService.getSysUser(userId);
        if (Objects.equals(PublicConstants.ADMIN_USER_NAME, sysUser.getUserName())) {
            return AnswerBody.buildAnswerBody(PublicCodeEnum.EDIT_ADMIN);
        }
        // 修改用户状态
        sysUserService.updateSysUserState(userId, sysUser.getUserState() == 1 ? 0 : 1);

        // 如果禁用把此用户踢下线
        if (0 == sysUser.getUserState()) {
            Collection<Session> activeSessions = sessionDAO.getActiveSessions();
            activeSessions.forEach(s -> {
                String userName = String.valueOf(s.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY));
                if (Objects.equals(userName, sysUser.getUserName())) {
                    s.setTimeout(0);
                }
            });
        }
        Map<String, Integer> result = new HashMap<>();
        result.put("userState", sysUser.getUserState());
        return AnswerBody.buildAnswerBody(result);
    }

    /**
     * 前往管理员修改页面
     * @return
     */
    @RequestMapping(value = "/toAdminEdit.do")
    public String toAdminEdit(Model model, Integer userId) {
        SysUserVO sysUserVO = sysUserService.getSysUser(userId);
        model.addAttribute("user", sysUserVO);
        // 查询此管理员拥有的权限
        SysRoleVO sysRoleVO = sysRoleService.getRoleBySysUserId(userId);
        model.addAttribute("userRole", sysRoleVO);
        // 查询所有角色
        List<SysRoleVO> roleVOList = sysRoleService.listRole();
        model.addAttribute("roleList", roleVOList);
        return "admin/adminEdit";
    }

    /**
     * 前往管理员添加页面
     * @return
     */
    @RequestMapping(value = "/toAdminAdd.do")
    public String toAdminAdd(Model model) {
        // 查询所有角色
        List<SysRoleVO> roleVOList = sysRoleService.listRole();
        model.addAttribute("roleList", roleVOList);
        return "admin/adminAdd";
    }

    /**
     * 前往管理员列表页面
     * @return
     */
    @RequestMapping(value = "/toAdminList.do")
    public String toAdminList() {
        return "admin/adminList";
    }

}
