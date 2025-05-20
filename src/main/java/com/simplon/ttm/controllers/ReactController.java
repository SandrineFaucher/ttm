package com.simplon.ttm.controllers;

import org.springframework.web.bind.annotation.RequestMapping;

public class ReactController {
    @RequestMapping(value = {"/{path:^(?!api|static|.*\\..*).*}", "/**/{path:^(?!api|static|.*\\..*).*}"})
    public String redirect() {
        return "forward:/index.html";
    }
}
