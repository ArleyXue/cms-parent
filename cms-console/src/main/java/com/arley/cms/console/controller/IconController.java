package com.arley.cms.console.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author XueXianlei
 * @Description:
 * @date 2018/9/30 18:24
 */
@Controller
@RequestMapping("/icon")
public class IconController {

    @RequestMapping(value = "/toIcons.do")
    public String toIcons() {
        return "system/icons";
    }
}
