package com.arley.cms.console.controller;

import com.arley.cms.console.pojo.query.LoginLogQuery;
import com.arley.cms.console.service.LoginLogService;
import com.arley.cms.console.util.Pagination;
import com.arley.cms.console.util.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author XueXianlei
 * @Description: 日志控制层
 * @date 2018/9/30 11:08
 */
@Controller
@RequestMapping("/log")
public class LogController {

    @Autowired
    private LoginLogService loginLogService;

    /**
     * 分页查询登录日志
     * @param loginLogQuery
     * @return
     */
    @RequestMapping(value = "/listLoginLogByPage.do")
    @ResponseBody
    public Pagination listLoginLogByPage(LoginLogQuery loginLogQuery) {
        if (null != loginLogQuery.getType()) {
            loginLogQuery.setUserName(ShiroUtils.getLoginUser().getUserName());
        }
        Pagination pagination = loginLogService.listLoginLogByPage(loginLogQuery);
        return pagination;
    }

    /**
     * 去往登录日志页面
     * @return
     */
    @RequestMapping(value = "/toLoginLogList.do")
    public String toLoginLogList(Integer type, Model model) {
        model.addAttribute("type", type);
        return "system/loginLogList";
    }
}
