package com.ht.tohka.usercenter.api.sysmenu.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SysMenuTreeResponse {
    private Integer id;
    private String text;
    // null不进行序列化
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String link;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String icon;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<SysMenuTreeResponse> children = new ArrayList<>();
}
