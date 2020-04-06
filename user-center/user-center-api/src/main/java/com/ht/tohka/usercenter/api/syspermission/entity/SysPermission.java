package com.ht.tohka.usercenter.api.syspermission.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_permission")
public class SysPermission {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String scope;
    private String name;
    private String mark;
    private String url;
    private String method;
    private Date createTime;
    private Date updateTime;
}
