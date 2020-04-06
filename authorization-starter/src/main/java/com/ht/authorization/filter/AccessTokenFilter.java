package com.ht.authorization.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.authorization.Authentication;
import com.ht.authorization.AuthenticationContext;
import com.ht.authorization.AuthenticationManager;
import com.ht.authorization.BearerTokenExtractor;
import com.ht.authorization.exception.AuthenticationException;
import com.ht.tohka.common.core.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class AccessTokenFilter extends OncePerRequestFilter {
    private AuthenticationManager authenticationManager;
    private ObjectMapper objectMapper = new ObjectMapper();

    public AccessTokenFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取认证信息，存储到上下文
        try {
            String bearerToken = BearerTokenExtractor.extractToken(request);
            Authentication authentication = authenticationManager.parse(bearerToken);
            AuthenticationContext.setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            String result = objectMapper.writeValueAsString(Result.error(401, e.getMessage()));
            response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
            PrintWriter out = response.getWriter();
            out.write(result);
            out.close();
        } finally {
            AuthenticationContext.clearContext();
        }
    }


}
