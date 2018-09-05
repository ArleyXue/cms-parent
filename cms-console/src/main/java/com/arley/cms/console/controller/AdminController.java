package com.arley.cms.console.controller;

import com.arley.cms.console.constant.CodeEnum;
import com.arley.cms.console.constant.PublicConstants;
import com.arley.cms.console.pojo.Do.SysUserDO;
import com.arley.cms.console.pojo.query.SysUserQuery;
import com.arley.cms.console.service.SysUserService;
import com.arley.cms.console.util.AnswerBody;
import com.arley.cms.console.util.DateUtil;
import com.arley.cms.console.util.Pagination;
import com.arley.cms.console.util.ShiroUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
     * 修改用户状态
     * @param userId
     * @return
     */
    @RequestMapping(value = "/updateUserState.do")
    @ResponseBody
    public AnswerBody updateUserState(Integer userId) {
        SysUserDO sysUser = sysUserService.getById(userId);
        if (Objects.equals(PublicConstants.ADMIN_USER_NAME, sysUser.getUserName())) {
            return AnswerBody.getInstance(CodeEnum.EDIT_ADMIN);
        }

        sysUser.setUserState(sysUser.getUserState() == 1 ? 0 : 1);
        sysUser.setGmtModified(DateUtil.getLocalDateTime());
        sysUser.setModifier(ShiroUtils.getLoginUser().getUserName());
        sysUserService.updateById(sysUser);

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
        return AnswerBody.getInstance(result);
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
