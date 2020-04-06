package com.ht.tohka.usercenter.sysuser.controller;

import com.ht.tohka.common.core.Result;
import com.ht.tohka.usercenter.api.sysuser.entity.SysUser;
import com.ht.tohka.usercenter.sysrole.service.SysRoleService;
import com.ht.tohka.usercenter.sysuser.service.SysUserService;
import com.ht.tohka.usercenter.api.sysuser.vo.SysUserQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/sys/users")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRoleService sysRoleService;

    @GetMapping
    public Result selectByPage(@RequestParam(defaultValue = "1") Integer page,
                               @RequestParam(defaultValue = "10") Integer size,
                               SysUserQuery query) {
        return Result.success(sysUserService.selectByPage(page, size, query));
    }

    @GetMapping("{id}/roles")
    public Result userRoles(@PathVariable Integer id) {
        return Result.success(sysRoleService.selectBySysUserId(id));
    }

    @PostMapping("{id}/roles")
    public Result bindRoles(@PathVariable Integer id, @RequestBody List<Integer> roleIds) {
        sysUserService.bindRoles(id, roleIds);
        return Result.success();
    }

    @PostMapping
    public Result save(@RequestBody SysUser sysUser) {
        if (!StringUtils.hasText(sysUser.getPassword()) || sysUser.getPassword().length() < 6) {
            Result.error(422, "密码长度不能少于6位");
        }
        sysUserService.save(sysUser);
        return Result.success();
    }

    @DeleteMapping("{id:\\d+}")
    public Result deleteById(@PathVariable Integer id) {
        sysUserService.deleteById(id);
        return Result.success();
    }

    @PutMapping("{id}")
    public Result update(@PathVariable Integer id, @RequestBody SysUser sysUser) {
        sysUserService.update(id, sysUser);
        return Result.success();
    }
}

