package com.lynnwork.sobblogsystem.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lynnwork.sobblogsystem.mapper.DemoMapper;
import com.lynnwork.sobblogsystem.pojo.Demo;
import com.lynnwork.sobblogsystem.service.impl.DemoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * tb_demo 前端控制器
 * </p>
 *
 * @author lynnkyle
 * @since 2024-10-28
 */
@RestController
@RequestMapping("/demo")
public class DemoController {
    
}

