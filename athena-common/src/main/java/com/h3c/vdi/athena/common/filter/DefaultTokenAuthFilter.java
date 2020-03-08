package com.h3c.vdi.athena.common.filter;

import com.google.gson.Gson;
import com.h3c.vdi.athena.common.constant.TimeConstant;
import com.h3c.vdi.athena.common.model.DefaultUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * Created by JemmyZhang on 2018/2/13
 */
public class DefaultTokenAuthFilter extends BasicAuthenticationFilter {

    private long DEFAULT_TOKEN_EXPIRE_TIME= TimeConstant.MINUTE_IN_MILLS * 30;

    private Logger logger= LoggerFactory.getLogger(DefaultTokenAuthFilter.class);

    public DefaultTokenAuthFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        logger.info("theAuthorization is {}", header);
        if (Objects.isNull(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (Objects.nonNull(authorization)) {
            try {
                Jws<Claims> claimsJws = Jwts.parser().setSigningKey("Athena").parseClaimsJws(authorization.replace("Bearer ", ""));
                Date expiration = claimsJws.getBody().getExpiration();
                if (isTokenExpired(expiration)) {
                    return null;
                }
                String subject = claimsJws.getBody().getSubject();
                DefaultUserDetails userDetails = convertTokenToUserDetails(subject);
                if (Objects.nonNull(userDetails)) {
                    return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                }
            } catch (SignatureException e) {
                //add some logs here
                return null;
            }
        }
        return null;
    }

    private boolean isTokenExpired(Date expiration) {
        Date now = Calendar.getInstance().getTime();
        return now.compareTo(expiration) > 0;
    }

    private DefaultUserDetails convertTokenToUserDetails(String token) {
        return new Gson().fromJson(token, DefaultUserDetails.class);
    }
}
