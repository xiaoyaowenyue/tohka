package com.ht.authorization;

import com.ht.authorization.exception.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class BearerTokenExtractor {

    public static String extractToken(HttpServletRequest request) {
        // 从http header中获取bearerToken
        String bearerToken = request.getHeader("Authorization");
        if (!StringUtils.hasText(bearerToken) || !bearerToken.startsWith("Bearer ")) {
            log.error("非法请求,ip:{}", request.getLocalAddr());
            throw new AuthenticationException("token must be applied");
        }
        bearerToken = bearerToken.substring(7);
        return bearerToken;
    }

}
