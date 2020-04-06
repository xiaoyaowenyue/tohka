package com.ht.tohka.usercenter.api.sysuser.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class ChangePasswordRequest {
    @Length(min = 6, message = "新密码长度不能少于6位数")
    private String password;
}
