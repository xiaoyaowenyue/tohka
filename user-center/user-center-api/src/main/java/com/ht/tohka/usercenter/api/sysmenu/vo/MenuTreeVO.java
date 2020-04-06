package com.ht.tohka.usercenter.api.sysmenu.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class MenuTreeVO {
    @JsonProperty("key")
    private Integer id;
    private Integer pid;
    @JsonProperty("title")
    private String text;
    private String icon;
    private Boolean checked;
    // 是否是叶子节点
    private Boolean isLeaf;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<MenuTreeVO> children;
}
