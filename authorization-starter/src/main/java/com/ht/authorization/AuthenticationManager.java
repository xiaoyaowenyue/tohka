package com.ht.authorization;

import com.ht.authorization.exception.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.Map;
import java.util.Set;

import static com.ht.authorization.constant.AuthorizationConstant.USER_INFO_KEY_PREFIX;
import static com.ht.authorization.constant.AuthorizationConstant.USER_PERMISSION_KEY_PREFIX;

/**
 * 管理authentication
 */
public class AuthenticationManager {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 根据用户提供的token转换成authentication
     *
     * @param accessToken
     * @return
     */
    public AccessTokenAuthentication parse(String accessToken) throws AuthenticationException {
        // 查找用户信息
        AccessTokenAuthentication authentication = (AccessTokenAuthentication) redisTemplate.boundValueOps(USER_INFO_KEY_PREFIX + accessToken).get();
        if (authentication == null) {
            throw new AuthenticationException("令牌已过期，请重新登录");
        }
        // 查找权限信息
        Set<String> permissionMark = (Set<String>) redisTemplate.boundValueOps(USER_PERMISSION_KEY_PREFIX + authentication.getId()).get();
        authentication.setPermissions(permissionMark);
        authentication.setCredentials(accessToken); // 当前token也放进去
        return authentication;
    }

    /**
     * 更新redis中缓存的用户属性（部分更新）
     *
     * @param accessToken
     * @param userAttrs
     */
    public void updateSelectiveUserInfoAttrs(String accessToken, Map<String, Object> userAttrs) {
        BoundValueOperations<String, Object> ops = getUserInfoOps(accessToken);
        Long expire = ops.getExpire();
        AccessTokenAuthentication authentication = (AccessTokenAuthentication) ops.get();
        if (expire != null && authentication != null) {
            userAttrs.forEach(authentication::setAttr);
            ops.set(authentication, Duration.ofSeconds(expire));
        }
    }

    /**
     * 更新redis中缓存的用户属性（全部更新）
     *
     * @param accessToken
     * @param userAttrs
     */
    public void updateUserInfoAttrs(String accessToken, Map<String, Object> userAttrs) {
        BoundValueOperations<String, Object> ops = getUserInfoOps(accessToken);
        Long expire = ops.getExpire();
        AccessTokenAuthentication authentication = (AccessTokenAuthentication) ops.get();

        if (expire != null && authentication != null) {
            authentication.getAttributes().clear();
            authentication.getAttributes().putAll(userAttrs);
            ops.set(authentication, Duration.ofSeconds(expire));
        }
    }

    private BoundValueOperations<String, Object> getUserInfoOps(String accessToken) {
        return redisTemplate.boundValueOps(USER_INFO_KEY_PREFIX + accessToken);
    }


}
