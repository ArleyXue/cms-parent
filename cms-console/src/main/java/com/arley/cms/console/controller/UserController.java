package com.arley.cms.console.controller;

import com.arley.cms.console.constant.PublicCodeEnum;
import com.arley.cms.console.exception.CustomException;
import com.arley.cms.console.pojo.Do.LoginLogDO;
import com.arley.cms.console.pojo.Do.SysUserDO;
import com.arley.cms.console.service.LoginLogService;
import com.arley.cms.console.service.SysUserService;
import com.arley.cms.console.util.AnswerBody;
import com.arley.cms.console.util.RequestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    @RequestMapping(value = "/login.do", method = RequestMethod.POST)
    @ResponseBody
    public AnswerBody login(String userName, String password, HttpServletRequest request) {

        try {
            //获取subject对象
            Subject subject = SecurityUtils.getSubject();
            // 创建UsernamePasswordToken对象，封装用户名和密码
            UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
            subject.login(token);
            // 登录成功
            SysUserDO user = (SysUserDO)subject.getPrincipal();
            user.setLoginTime(LocalDateTime.now());
            user.setLoginNum(user.getLoginNum() + 1);
            user.setGmtModified(LocalDateTime.now());
            sysUserService.updateById(user);
            // 保存登录日志
            LoginLogDO loginLog = new LoginLogDO();
            loginLog.setUserName(userName);
            loginLog.setLoginTime(user.getLoginTime());
            loginLog.setLoginIp(RequestUtils.getClientIpAddr(request));
            loginLog.setContent("登录成功!");
            loginLogService.save(loginLog);
        } catch (UnknownAccountException e) {
            throw new CustomException(PublicCodeEnum.FILE.getCode(), "账号不存在!");
        } catch (DisabledAccountException e) {
            throw new CustomException(PublicCodeEnum.FILE.getCode(), "账号被禁用,请联系管理员!");
        } catch (AuthenticationException e) {
            throw new CustomException(PublicCodeEnum.FILE.getCode(), "账号或密码错误!");
        }
        return AnswerBody.buildAnswerBody();
    }
}
