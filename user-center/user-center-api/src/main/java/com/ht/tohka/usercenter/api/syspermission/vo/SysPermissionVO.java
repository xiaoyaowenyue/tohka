package com.ht.tohka.usercenter.api.syspermission.vo;

import com.ht.tohka.usercenter.api.syspermission.entity.SysPermission;
import lombok.Data;

@Data
public class SysPermissionVO extends SysPermission {
    private Integer key;
    private String title;
    // 是否是叶子节点
    private Boolean isLeaf = true;
    private Boolean checked; //是否选中
}
