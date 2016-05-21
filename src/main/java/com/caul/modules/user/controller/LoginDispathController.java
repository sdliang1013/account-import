package com.caul.modules.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginDispathController {

    @RequestMapping(value = "/login")
    public String toManage() {
        return "/login";
    }

    @RequestMapping(value = "/main")
    public String toMain() {
        return "/main";
    }

}