package com.h3c.vdi.athena.keystone.config.security;

import com.h3c.vdi.athena.common.filter.DefaultTokenAuthFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.annotation.Resource;

/**
 * Created by JemmyZhang on 2018/2/13
 */
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(jsr250Enabled = true)//开启基于JSP-250注解的权限认证
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    AuthenticationManager authenticationManager;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests().antMatchers("/auth_error").hasRole("/BBK")
                .and().authorizeRequests().antMatchers(HttpMethod.POST, "/users").permitAll()
                .antMatchers(HttpMethod.GET, "/users/secure**").permitAll()
                .antMatchers(HttpMethod.GET, "/users/find_by_login_name**").permitAll()
                .anyRequest().permitAll()
                .and().addFilterBefore(new DefaultTokenAuthFilter(authenticationManager()), BasicAuthenticationFilter.class);
    }
}
