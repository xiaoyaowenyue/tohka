package com.ht.tohka.gateway;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "com.ht.gateway")
public class GatewayConfig {
    /**
     * route define. key: route id. value: route uri
     */
    private Map<String, String> route = new HashMap<>();

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        RouteLocatorBuilder.Builder routes = builder.routes();
        route.forEach((k, v) -> routes.route(k, r -> r.path(String.format("/%s/**", k)).filters(f -> f.stripPrefix(1)).uri(v)));
        return routes.build();
    }

    @Bean
    public AuthFilter authFilter() {
        return new AuthFilter();
    }
}
