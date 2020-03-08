package com.h3c.vdi.athena.configserver.config.security;

import com.h3c.vdi.athena.common.filter.DefaultTokenAuthFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * @author w14014
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
//                .antMatchers("/config/**").permitAll()
                .anyRequest().authenticated()
                .and().addFilterBefore(new DefaultTokenAuthFilter(authenticationManager()), BasicAuthenticationFilter.class);
    }
}
