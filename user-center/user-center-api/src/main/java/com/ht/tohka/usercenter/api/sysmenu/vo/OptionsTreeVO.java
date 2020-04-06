package com.ht.tohka.usercenter.api.sysmenu.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OptionsTreeVO {
    @JsonProperty("value")
    private Integer id;
    @JsonProperty("label")
    private String text;
    private List<OptionsTreeVO> children;
    private Boolean isLeaf;
}
