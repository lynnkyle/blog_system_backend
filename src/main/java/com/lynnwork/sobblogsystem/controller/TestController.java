package com.lynnwork.sobblogsystem.controller;

import com.lynnwork.sobblogsystem.pojo.Demo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/test")
public class TestController {
    @ResponseBody
    @GetMapping("login_in")
    public String login_in() {
        return "hello login";
    }

    @ResponseBody
    @GetMapping("register")
    public String register(@RequestParam Map<String, String> map) {
        return "hello register";
    }

    @ResponseBody
    @GetMapping("authentication")
    public String getMatrixParam() {
        return "hello authentication";
    }

    @ResponseBody
    @GetMapping
    public String home() {
        return "welcome to index";
    }
}
