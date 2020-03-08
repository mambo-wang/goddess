package com.h3c.vdi.athena.homework.config.security;

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
                .antMatchers("/attachments/**","/users/register").permitAll()
                .anyRequest().authenticated()
//                .and().httpBasic()
                .and().addFilterBefore(new DefaultTokenAuthFilter(authenticationManager()), BasicAuthenticationFilter.class);
    }
}
