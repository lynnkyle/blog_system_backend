package com.lynnwork.sobblogsystem;

import com.lynnwork.sobblogsystem.mapper.DemoMapper;
import com.lynnwork.sobblogsystem.pojo.Demo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class SobBlogSystemApplicationTests {
    @Autowired
    private DemoMapper demoMapper;

    @Test
    public void mapperTest() {
        demoMapper.insert(new Demo("张三", "12346"));
        demoMapper.insert(new Demo("李四", "12346"));
        demoMapper.insert(new Demo("王五", "123456"));
        demoMapper.insert(new Demo("李六", "123456"));
        demoMapper.insert(new Demo("梅洋", "123456"));
        demoMapper.insert(new Demo("鹤滨", "123456"));
        demoMapper.insert(new Demo("皓哲", "123456"));
    }

}
