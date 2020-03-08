package com.h3c.vdi.athena.netdisk.config.security;

import com.h3c.vdi.athena.common.filter.DefaultTokenAuthFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * Created by JemmyZhang on 2018/2/13
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                //下载文件不传token
                .antMatchers("/netdisk/downloadFile**").permitAll()
                .antMatchers("/netdisk/uploadFile**").permitAll()
                .antMatchers("/netdisk/uploadFile/batch**").permitAll()
                .anyRequest().authenticated()
                .and().addFilterBefore(new DefaultTokenAuthFilter(authenticationManager()), BasicAuthenticationFilter.class);
    }
}
