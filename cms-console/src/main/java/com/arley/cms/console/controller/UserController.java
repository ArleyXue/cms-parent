package com.arley.cms.console.controller;

import com.arley.cms.console.constant.PublicCodeEnum;
import com.arley.cms.console.exception.CustomException;
import com.arley.cms.console.pojo.Do.LoginLogDO;
import com.arley.cms.console.pojo.Do.SysUserDO;
import com.arley.cms.console.pojo.vo.LoginLogVO;
import com.arley.cms.console.pojo.vo.SysUserVO;
import com.arley.cms.console.service.LoginLogService;
import com.arley.cms.console.service.SysUserService;
import com.arley.cms.console.util.AnswerBody;
import com.arley.cms.console.util.DateUtils;
import com.arley.cms.console.util.RequestUtils;
import com.arley.cms.console.util.ShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author XueXianlei
 * @Description:
 * @date 2018/8/16 15:52
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private LoginLogService loginLogService;

    /**
     * 前往登录页面
     * @return
     */
    @RequestMapping(value = "/login.do", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    /**
     * 登录
     * @param userName
     * @param password
     * @param request
     * @return
     */
    @RequestMapping(value = "/login.do", method = RequestMethod.POST)
    @ResponseBody
    public AnswerBody login(String userName, String password, HttpServletRequest request) {
        AnswerBody body;
        // 保存登录日志
        LoginLogVO loginLog = new LoginLogVO();
        try {
            //获取subject对象
            Subject subject = SecurityUtils.getSubject();
            // 创建UsernamePasswordToken对象，封装用户名和密码
            UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
            subject.login(token);
            // 登录成功
            SysUserVO user = (SysUserVO)subject.getPrincipal();
            sysUserService.updateForLogin(user.getUserId());
            body = AnswerBody.buildAnswerBody(PublicCodeEnum.SUCCESS.getCode(), "登录成功");
        } catch (UnknownAccountException e) {
            body = AnswerBody.buildAnswerBody(PublicCodeEnum.FILE.getCode(), "账号不存在!");
        } catch (DisabledAccountException e) {
            body = AnswerBody.buildAnswerBody(PublicCodeEnum.FILE.getCode(), "账号被禁用,请联系管理员!");
        } catch (AuthenticationException e) {
            body = AnswerBody.buildAnswerBody(PublicCodeEnum.FILE.getCode(), "账号或密码错误!");
        }

        loginLog.setUserName(userName);
        loginLog.setLoginTime(DateUtils.getLocalDateTime());
        loginLog.setLoginIp(RequestUtils.getClientIpAddr(request));
        loginLog.setContent(body.getResultDesc());
        loginLogService.insertLoginLog(loginLog);
        return body;
    }

    /**
     * 修改个人信息
     * @param sysUserVO
     * @return
     */
    @RequestMapping(value = "/updateUserInfo.do")
    @ResponseBody
    public AnswerBody updateUserInfo(SysUserVO sysUserVO) {
        sysUserService.updateUserInfo(sysUserVO);
        return AnswerBody.buildAnswerBody();
    }

    /**
     * 修改密码
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @RequestMapping(value = "/updatePassword.do")
    @ResponseBody
    public AnswerBody updatePassword(String oldPassword, String newPassword) {
        sysUserService.updatePassword(oldPassword, newPassword);
        return AnswerBody.buildAnswerBody();
    }

    /**
     * 前往用户信息页面
     * @return
     */
    @RequestMapping(value = "/toUserInfo.do")
    public String toUserInfo(Model model) {
        SysUserVO user = sysUserService.getSysUser(ShiroUtils.getLoginUser().getUserId());
        model.addAttribute("user", user);
        return "sysUser/userInfo";
    }

    /**
     * 前往修改密码页面
     * @return
     */
    @RequestMapping(value = "/toPasswordUpdate.do")
    public String toPasswordUpdate(Model model) {
        // 查询所有角色
        SysUserVO user = sysUserService.getSysUser(ShiroUtils.getLoginUser().getUserId());
        model.addAttribute("userName", user.getUserName());
        return "sysUser/changePassword";
    }
}
