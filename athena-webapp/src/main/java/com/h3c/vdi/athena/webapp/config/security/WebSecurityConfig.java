package com.h3c.vdi.athena.webapp.config.security;

import com.h3c.vdi.athena.common.filter.DefaultTokenAuthFilter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


/**
 * Created by w14014 on 2018/3/2.
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers("/**/**").permitAll()
                .anyRequest().authenticated()
                .and().addFilterBefore(new DefaultTokenAuthFilter(authenticationManager()), BasicAuthenticationFilter.class);
    }
}
