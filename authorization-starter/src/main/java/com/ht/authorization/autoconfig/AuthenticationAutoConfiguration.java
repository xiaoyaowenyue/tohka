package com.ht.authorization.autoconfig;

import com.ht.authorization.AuthenticationManager;
import com.ht.authorization.filter.AccessTokenFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthenticationAutoConfiguration {

    // 注册认证管理器
    @Bean
    public AuthenticationManager authenticationManager() {
        return new AuthenticationManager();
    }

    // 注册accessToken过滤器
    @Bean
    public FilterRegistrationBean accessTokenFilter() {
        FilterRegistrationBean<AccessTokenFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new AccessTokenFilter(authenticationManager()));
        bean.addUrlPatterns("/api/*");
        return bean;
    }
}
