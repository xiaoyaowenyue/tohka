package com.ht.tohka.gateway;

import com.ht.tohka.common.core.DefaultApiMatcher;
import com.ht.tohka.common.core.Result;
import com.ht.tohka.usercenter.api.auth.feign.RemoteAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class AuthFilter implements GlobalFilter, Ordered {
    @Autowired
    private RemoteAuthService remoteAuthService;
    @Autowired
    private Environment environment;
    private static final String IGNORE_URL_KEY = "com.ht.gateway.ignore-urls";

    // 忽略鉴权的地址
    private List<DefaultApiMatcher> ignoreMatchers;
    // 需要鉴权的接口地址
    private DefaultApiMatcher apiMatcher = new DefaultApiMatcher("/*/api/**");

    @PostConstruct
    public void init() {
        List<String> ignoreUrls = environment.getProperty(IGNORE_URL_KEY, ArrayList.class);
        if (ignoreUrls == null) {
            this.ignoreMatchers = Collections.emptyList();
        } else {
            ignoreMatchers = ignoreUrls.stream().map(DefaultApiMatcher::new)
                    .collect(Collectors.toList());
        }
    }

    @EventListener
    public void envListener(EnvironmentChangeEvent event) {
        if (event.getKeys().contains(IGNORE_URL_KEY)) {
            init();
        }
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getURI().getPath();

        // 直接放行
        if (ignoreMatchers.stream().anyMatch(match -> match.match(path, null))) {
            return chain.filter(exchange);
        }

        // 访问的是api才进行鉴权
        if (apiMatcher.match(path, null)) {
            String method = request.getMethodValue();
            String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            // 远程鉴权
            Result result = remoteAuthService.decide(bearerToken, path, method);
            if (result.getCode() == 200) {
                return chain.filter(exchange);
            }
            response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
            String json = "{\"code\":" + result.getCode() + ",\"msg\":\"" + result.getMsg() + "\",\"data\":null}";
            return response.writeWith(Mono.just(response.bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8))));
        }
        // 放行
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -100;
    }
}

