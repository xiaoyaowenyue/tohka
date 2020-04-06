package com.ht.tohka.usercenter.api.sys.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class LoginRequest {
    @NotNull(message = "请输入用户名")
    private String username;
    @Length(min = 6,message = "密码长度不能少于6位")
    private String password;
}
