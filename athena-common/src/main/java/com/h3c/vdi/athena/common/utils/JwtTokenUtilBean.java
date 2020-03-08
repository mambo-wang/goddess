package com.h3c.vdi.athena.common.utils;

import com.google.gson.Gson;
import com.h3c.vdi.athena.common.constant.TimeConstant;
import com.h3c.vdi.athena.common.model.DefaultUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Objects;

public class JwtTokenUtilBean {
    public static final String CLAIM_KEY_CREATED = "created";
    public static final String SECRET = "Athena";
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_HEAD = "Bearer ";

    private int timeoutMinutes;

    public void setTimeoutMinutes(int timeoutMinutes) {
        this.timeoutMinutes = timeoutMinutes;
    }

    public String getUsernameFromToken(String token) {
        String json;
        try {
            final Claims claims = getClaimsFromToken(token);
            json = claims.getSubject();
            DefaultUserDetails defaultUserDetails = convertTokenToUserDetails(json);
            return defaultUserDetails.getUsername();
        } catch (Exception e) {
            json = null;
        }
        return json;
    }

    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_HEAD, ""))
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + timeoutMinutes * TimeConstant.MINUTE_IN_MILLS);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return Objects.nonNull(expiration) && expiration.before(new Date());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    public String generateToken(UserDetails userDetails) {
        String subject = new Gson().toJson(userDetails);
        return TOKEN_HEAD + Jwts
                .builder()
                .setSubject(subject)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && !isTokenExpired(token);
    }

    public String refreshToken(String token) {
            if (isTokenExpired(token)) {
                return null;
            }
            String subject = Jwts.parser()
                    .setSigningKey(JwtTokenUtilBean.SECRET)
                    .parseClaimsJws(token.replace(JwtTokenUtilBean.TOKEN_HEAD, ""))
                    .getBody()
                    .getSubject();
            DefaultUserDetails userDetails = convertTokenToUserDetails(subject);
            return generateToken(userDetails);
    }

    private DefaultUserDetails convertTokenToUserDetails(String token) {
        return new Gson().fromJson(token, DefaultUserDetails.class);
    }

    /** token校验 */
    public Boolean validateToken(String token, UserDetails userDetails) {
        User user = (User) userDetails;
        final String username = getUsernameFromToken(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }
}

