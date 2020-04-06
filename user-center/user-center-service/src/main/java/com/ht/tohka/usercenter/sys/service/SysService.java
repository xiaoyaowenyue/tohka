package com.ht.tohka.usercenter.sys.service;

import com.ht.authorization.AccessTokenAuthentication;
import com.ht.authorization.AuthenticationContext;
import com.ht.authorization.exception.PermissionDeniedException;
import com.ht.tohka.common.core.DefaultApiMatcher;
import com.ht.tohka.common.core.Result;
import com.ht.tohka.usercenter.api.syspermission.entity.SysPermission;
import com.ht.tohka.usercenter.sys.event.PmChangeEvent;
import com.ht.tohka.usercenter.syspermission.service.SysPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SysService implements ApplicationListener<PmChangeEvent> {

    // 管理内存中的权限
    @Override
    public void onApplicationEvent(PmChangeEvent event) {
        SysPermission permission = event.getPermission();
        DefaultApiMatcher matcher = new DefaultApiMatcher(permission.getUrl(), permission.getMethod());
        switch (event.getType()) {
            case ADD:
                inMemoryPermissions.put(matcher, permission.getMark());
                break;
            case DELETE:
                inMemoryPermissions.remove(matcher);
                break;
        }
    }

    @Autowired
    private SysPermissionService sysPermissionService;

    // 系统所有敏感url，加载到内存(或redis)
    // url -> 权限标识  建立映射关系
    private static final Map<DefaultApiMatcher, String> inMemoryPermissions = new ConcurrentHashMap<>();

    @PostConstruct
    public void loadAll() {
        List<SysPermission> permissions = sysPermissionService.findAll();
        if (permissions != null) {
            for (SysPermission permission : permissions) {
                DefaultApiMatcher matcher = new DefaultApiMatcher(permission.getUrl(), permission.getMethod());
                inMemoryPermissions.put(matcher, permission.getMark());
            }
        }
    }

    /**
     * 用户权限决策
     *
     * @param path
     * @param method http请求方法，大写，eg: GET
     * @return
     */
    public void decide(String path, String method) {

        AccessTokenAuthentication authentication = (AccessTokenAuthentication) AuthenticationContext.current();
        Set<String> userPermissionMarks = authentication.getPermissions();

        for (DefaultApiMatcher matcher : inMemoryPermissions.keySet()) {
            if (matcher.match(path, method)) { // 用户当前访问的资源是系统敏感资源
                // 判断访问该url需要哪个权限
                String sysPermissionMark = inMemoryPermissions.get(matcher);

                // 如果用户拥有该权限，返回成功
                if (userPermissionMarks.contains(sysPermissionMark)) {
                    return;
                }
                // 用户没有该权限，返回失败
                throw new PermissionDeniedException("拒绝访问");
            }
        }
    }


}
