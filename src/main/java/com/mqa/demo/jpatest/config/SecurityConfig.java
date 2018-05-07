package com.mqa.demo.jpatest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author mengqa
 * @create 2018-05-07 14:15
 **/
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/fonts/**", "/index").permitAll() // 都可以访问
                .antMatchers("/users/**").hasRole("ADMIN") // 需要相关的角色才能访问
                .and()
                .formLogin()
                .loginPage("/login").failureUrl("/login-error"); // 自定义登录页面
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
       auth.inMemoryAuthentication() // 内存中
                .withUser("mqa").password("{noop}123456").roles("ADMIN");
    }

}
