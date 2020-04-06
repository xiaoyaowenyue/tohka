package com.ht.authorization;

import java.util.Map;
import java.util.Set;

public interface Authentication {
    Integer getId();

    String getCredentials();

    Set<String> getPermissions();

    <T> T getAttr(String key);

    Map<String,Object> getAttributes();

}
