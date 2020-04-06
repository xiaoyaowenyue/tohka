package com.ht.tohka.usercenter.api.sysmenu.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysMenuQuery {
    @ApiModelProperty("id")
    private Integer id;
    private String text;
}
