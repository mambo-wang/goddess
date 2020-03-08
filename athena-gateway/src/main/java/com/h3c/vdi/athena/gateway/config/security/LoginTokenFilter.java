package com.h3c.vdi.athena.gateway.config.security;

import com.google.gson.Gson;
import com.h3c.vdi.athena.common.constant.TimeConstant;
import com.h3c.vdi.athena.common.model.DefaultUserDetails;
import com.h3c.vdi.athena.common.utils.JwtTokenUtilBean;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.lang.Collections;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jemmyzhang on 2018/2/23.
 */
public class LoginTokenFilter extends UsernamePasswordAuthenticationFilter {

    LoginTokenFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    LoginTokenFilter(AuthenticationManager authenticationManager, int timeoutMinutes) {
        super.setAuthenticationManager(authenticationManager);
        this.timeoutMinutes = timeoutMinutes;
    }

    public final int DEFAULT_TIMEOUT_MINUTES = 10;

    private int timeoutMinutes = DEFAULT_TIMEOUT_MINUTES;

    /**
     * 生成token
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        DefaultUserDetails userDetails = convertToUserDetails(user);
        String subject = new Gson().toJson(userDetails);
        String token = Jwts
                .builder()
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() + timeoutMinutes * TimeConstant.MINUTE_IN_MILLS))
                .signWith(SignatureAlgorithm.HS512, "Athena")
                .compact();
        response.addHeader("Authorization", JwtTokenUtilBean.TOKEN_HEAD + token);
        super.successfulAuthentication(request, response, chain, authResult);
    }

    private DefaultUserDetails convertToUserDetails(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        List<String> authorityValues = new ArrayList<>();
        Collection<GrantedAuthority> authorities = user.getAuthorities();
        if (!Collections.isEmpty(authorities)) {
            authorityValues.addAll(authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        }
        return new DefaultUserDetails(username, password, authorityValues);
    }
}
