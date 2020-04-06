package com.ht.tohka.usercenter.sys.service;

import com.ht.authorization.AccessToken;
import com.ht.authorization.AccessTokenAuthentication;
import com.ht.authorization.AuthenticationContext;
import com.ht.tohka.usercenter.api.syspermission.entity.SysPermission;
import com.ht.tohka.usercenter.api.sysuser.entity.SysUser;
import com.ht.tohka.usercenter.syspermission.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.ht.authorization.constant.AuthorizationConstant.ACCESS_TOKEN_KEY_PREFIX;
import static com.ht.authorization.constant.AuthorizationConstant.USER_INFO_KEY_PREFIX;
import static com.ht.authorization.constant.AuthorizationConstant.USER_PERMISSION_KEY_PREFIX;

/**
 * 用于创建和销毁accessToken
 */
@Service
public class AccessTokenService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SysPermissionService sysPermissionService;

    @Value("${com.ht.security.token-ttl:36000}")
    private Long tokenTtl;

    /**
     * 创建accessToken
     */
    public AccessToken createAccessToken(SysUser sysUser) {
        BoundValueOperations<String, String> tokenOps = stringRedisTemplate.boundValueOps(ACCESS_TOKEN_KEY_PREFIX + sysUser.getId());

        //重复登录会导致前一个token失效
        String accessToken = tokenOps.get();
        if (StringUtils.hasText(accessToken)) {
            redisTemplate.delete(USER_INFO_KEY_PREFIX + accessToken);
        }

        // 生成accessToken并缓存
        accessToken = UUID.randomUUID().toString().replaceAll("-", "");
        tokenOps.set(accessToken, tokenTtl + 100, TimeUnit.SECONDS);

        // 构造认证对象并缓存
        AccessTokenAuthentication authentication = new AccessTokenAuthentication(sysUser.getId());
        authentication.setAttr("username", sysUser.getUsername());
        authentication.setAttr("nickname", sysUser.getNickname());
        authentication.setAttr("email", sysUser.getEmail());
        redisTemplate.boundValueOps(USER_INFO_KEY_PREFIX + accessToken).set(authentication, tokenTtl + 100, TimeUnit.SECONDS);

        // 缓存用户权限(标识)
        Set<String> permissions = sysPermissionService.selectBySysUserId(sysUser.getId()).stream()
                .map(SysPermission::getMark).collect(Collectors.toSet());
        redisTemplate.boundValueOps(USER_PERMISSION_KEY_PREFIX + sysUser.getId()).set(permissions, tokenTtl + 100, TimeUnit.SECONDS);

        return new AccessToken(accessToken, tokenTtl);
    }

    public void eraseAccessToken() {
        Integer userId = AuthenticationContext.current().getId();
        String accessToken = AuthenticationContext.current().getCredentials();
        stringRedisTemplate.delete(ACCESS_TOKEN_KEY_PREFIX + userId);
        redisTemplate.delete(USER_INFO_KEY_PREFIX + accessToken);
        redisTemplate.delete(USER_PERMISSION_KEY_PREFIX + userId);
    }

}
