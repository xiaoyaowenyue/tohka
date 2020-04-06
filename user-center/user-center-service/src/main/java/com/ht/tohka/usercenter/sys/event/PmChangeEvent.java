package com.ht.tohka.usercenter.sys.event;

import com.ht.tohka.usercenter.api.syspermission.entity.SysPermission;
import org.springframework.context.ApplicationEvent;

public class PmChangeEvent extends ApplicationEvent {
    private PmChangeType type;

    public PmChangeEvent(SysPermission sysPermission, PmChangeType type) {
        super(sysPermission);
    }

    public PmChangeType getType() {
        return type;
    }

    public SysPermission getPermission() {
        return (SysPermission) source;
    }
}
