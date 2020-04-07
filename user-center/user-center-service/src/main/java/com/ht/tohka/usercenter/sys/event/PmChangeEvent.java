package com.ht.tohka.usercenter.sys.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ht.tohka.usercenter.api.syspermission.entity.SysPermission;
import org.springframework.context.ApplicationEvent;

/**
 * 权限变更事件，用于通知授权中心刷新权限
 */
public class PmChangeEvent {
    /**
     * 新权限会被加载
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SysPermission newPermission;
    /**
     * 旧权限会被移除
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SysPermission oldPermission;

    public PmChangeEvent(SysPermission newPermission, SysPermission oldPermission) {
        this.newPermission = newPermission;
        this.oldPermission = oldPermission;
    }

    public SysPermission getNewPermission() {
        return newPermission;
    }

    public SysPermission getOldPermission() {
        return oldPermission;
    }
}
