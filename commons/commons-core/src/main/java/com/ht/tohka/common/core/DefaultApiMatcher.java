package com.ht.tohka.common.core;

import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;


public class DefaultApiMatcher {
    private String pattern;
    private String method;
    // 使用spring mvc默认的路径匹配器，支持精确匹配和模糊匹配 /api/** ，/*.jsp ， /api/{id:\\d+}
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();


    public DefaultApiMatcher(String pattern) {
        this(pattern, null);
    }

    public DefaultApiMatcher(String pattern, String method) {
        Assert.hasText(pattern, "pattern must be applied");
        this.pattern = pattern;
        if (method != null) {
            this.method = method.toUpperCase();
        }
    }

    public boolean match(String path, String method) {
        boolean isPathMatch = pathMatcher.match(pattern, path);
        boolean isMethodMatch;
        if (method == null) {
            isMethodMatch = true;
        } else {
            isMethodMatch = HttpMethod.resolve(this.method) == HttpMethod.resolve(method);
        }
        return isPathMatch && isMethodMatch;
    }


    // *** 重写hash方法和equals方法 *** //
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DefaultApiMatcher)) {
            return false;
        }

        DefaultApiMatcher other = (DefaultApiMatcher) obj;
        return this.pattern.equals(other.pattern) && HttpMethod.resolve(this.method) == HttpMethod.resolve(other.method);
    }

    @Override
    public int hashCode() {
        int code = 31 ^ this.pattern.hashCode();
        HttpMethod httpMethod = HttpMethod.resolve(this.method);
        if (httpMethod != null) {
            code ^= httpMethod.hashCode();
        }
        return code;
    }
}

