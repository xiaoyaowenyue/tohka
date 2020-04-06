package com.ht.tohka.usercenter.api.sysmenu.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RoleMenuRequest {
    private Integer roleId;
    @ApiModelProperty("菜单父级id")
    @NotNull(message = "pid不能为空")
    private Integer pid;
}
