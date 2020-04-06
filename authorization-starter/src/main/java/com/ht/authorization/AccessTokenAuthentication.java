package com.ht.authorization;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AccessTokenAuthentication implements Authentication {
    @Getter
    @Setter
    private Integer id;
    @Getter
    @Setter
    private String credentials;
    @Getter
    @Setter
    private Set<String> permissions;

    private final Map<String, Object> attributes = new ConcurrentHashMap<>();

    public AccessTokenAuthentication() {

    }
    public AccessTokenAuthentication(Integer id) {
        this.id = id;
    }

    @Override
    public String getCredentials() {
        return credentials;
    }

    @Override
    public <T> T getAttr(String key) {
        return (T) attributes.get(key);
    }

    public void setAttr(String key, Object value) {
        this.attributes.put(key, value);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
