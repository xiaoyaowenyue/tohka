package com.ht.tohka.usercenter.api.sysrole.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 添加角色请求
 */
@Data
public class SysRoleRequest {
    @ApiModelProperty("角色名")
    @NotNull
    private String name;
    @ApiModelProperty("角色拥有的权限id")
    private List<Integer> permissionIds;
    @ApiModelProperty("角色菜单")
    private List<Integer> menuIds;
}
