package com.ht.tohka.usercenter.api.sys.vo;

import com.ht.tohka.usercenter.api.sysmenu.vo.SysMenuTreeResponse;
import lombok.Data;

import java.util.List;

@Data
public class UserInfo {
    private String nickname;
    private String email;
    private String avatar;
    private List<SysMenuTreeResponse> menus;
}
