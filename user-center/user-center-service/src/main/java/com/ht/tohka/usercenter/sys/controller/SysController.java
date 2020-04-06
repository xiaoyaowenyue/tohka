package com.ht.tohka.usercenter.sys.controller;

import com.ht.authorization.AuthenticationContext;
import com.ht.tohka.common.core.ApiException;
import com.ht.tohka.common.core.Result;
import com.ht.tohka.usercenter.api.sys.vo.LoginRequest;
import com.ht.tohka.usercenter.api.sys.vo.UserInfo;
import com.ht.tohka.usercenter.api.sysmenu.vo.SysMenuTreeResponse;
import com.ht.tohka.usercenter.api.sysuser.entity.SysUser;
import com.ht.tohka.usercenter.sys.service.AccessTokenService;
import com.ht.tohka.usercenter.sys.service.SysService;
import com.ht.tohka.usercenter.sysmenu.service.SysMenuService;
import com.ht.tohka.usercenter.sysuser.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class SysController {
    @Autowired
    SysService sysService;
    @Autowired
    AccessTokenService accessTokenService;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysMenuService sysMenuService;

    /**
     * 判断用户是否拥有访问权限，网关远程调用
     *
     * @param path
     * @param method
     * @return
     */
    @GetMapping("api/v1/sys/auth")
    public Result decide(@RequestParam String path, @RequestParam String method) {
        sysService.decide(path, method);
        return Result.success();
    }

    /**
     * 获取用户信息，以及菜单
     */
    @GetMapping("api/v1/sys/userInfo")
    public Result userInfo() {
        Integer userId = AuthenticationContext.current().getId();
        // 查询用户信息
        SysUser sysUser = sysUserService.selectById(userId);
        //查询用户菜单信息
        List<SysMenuTreeResponse> menus = sysMenuService.selectUserMenus(userId);
        // 封装vo并返回
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(sysUser.getEmail());
        userInfo.setNickname(sysUser.getNickname());
        userInfo.setAvatar(sysUser.getAvatar());
        userInfo.setMenus(menus);
        return Result.success(userInfo);
    }

    // 登录不需要拦截，所以不用api开头
    @PostMapping("login")
    public Result login(@RequestBody @Valid LoginRequest loginRequest) {
        SysUser sysUser = sysUserService.selectByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new ApiException(401, "账号不存在"));
        if (!BCrypt.checkpw(loginRequest.getPassword(), sysUser.getPassword())) {
            return Result.error(401, "密码错误");
        }
        return Result.success(accessTokenService.createAccessToken(sysUser));
    }

    /**
     * 注销登录
     *
     * @return
     */
    @GetMapping("api/logout")
    public Result logout() {
        accessTokenService.eraseAccessToken();
        return Result.success();
    }


}
