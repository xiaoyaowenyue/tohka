package com.ht.tohka.usercenter.api.syspermission.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysPermissionQuery {
    @ApiModelProperty("id")
    private Integer id;
    private String name;
}
