package com.ht.tohka.usercenter.api.sysuser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Data
public class SysUser {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String nickname;
    private String email;
    private String password;
    private String avatar;
    private Date createTime;
    private Date updateTime;

}
