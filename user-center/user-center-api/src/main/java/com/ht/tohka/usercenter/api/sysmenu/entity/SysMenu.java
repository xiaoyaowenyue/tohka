package com.ht.tohka.usercenter.api.sysmenu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@TableName("sys_menu")
public class SysMenu {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @NotNull(message = "父级菜单不能为空")
    private Integer pid;
    private String text;
    private String icon;
    private String link;
    private Date createTime;
    private Date updateTime;
}
