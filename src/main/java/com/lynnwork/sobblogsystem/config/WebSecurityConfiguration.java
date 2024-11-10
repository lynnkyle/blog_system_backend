package com.lynnwork.sobblogsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    // 1.授权规则
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/test").permitAll()
                .antMatchers("/test/login_in").hasRole("vip1")
                .antMatchers("/test/register").hasRole("vip2")
                .antMatchers("/test/authentication").hasRole("vip3");
        // 1.登录
        http.formLogin().loginPage("/login.html").usernameParameter("user").passwordParameter("pwd").loginProcessingUrl("/login").defaultSuccessUrl("/test");
        // 2.禁止网站跨域访问
        http.csrf().disable();
        http.rememberMe();
        // 3.注销
        http.logout().logoutSuccessUrl("/");
    }

    // 2.认证规则
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 用户认证数据正常应该从数据库中读取
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("root").password(new BCryptPasswordEncoder().encode("123456")).roles("vip1", "vip2")
                .and()
                .withUser("kyle").password(new BCryptPasswordEncoder().encode("123456")).roles("vip2", "vip3");
    }
}
