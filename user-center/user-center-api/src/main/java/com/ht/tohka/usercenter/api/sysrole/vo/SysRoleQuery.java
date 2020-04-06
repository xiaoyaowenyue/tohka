package com.ht.tohka.usercenter.api.sysrole.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysRoleQuery {
    @ApiModelProperty("id")
    private Integer id;
    private String name;
}
