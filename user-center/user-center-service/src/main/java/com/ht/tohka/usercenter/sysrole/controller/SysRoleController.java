package com.ht.tohka.usercenter.sysrole.controller;

import com.ht.tohka.common.core.Result;
import com.ht.tohka.usercenter.sysmenu.service.SysMenuService;
import com.ht.tohka.usercenter.syspermission.service.SysPermissionService;
import com.ht.tohka.usercenter.sysrole.service.SysRoleService;
import com.ht.tohka.usercenter.api.sysrole.vo.SysRoleRequest;
import com.ht.tohka.usercenter.api.sysrole.vo.SysRoleQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api(value = "SysRoleController", tags = "角色管理")
@RequestMapping("/api/v1/sys/roles")
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysPermissionService sysPermissionService;

    @GetMapping
    @ApiOperation("分页条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码"),
            @ApiImplicitParam(name = "size", value = "每页显示的数量"),
    })
    public Result selectByPage(@RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "10") Integer size,
                                 SysRoleQuery query) {
        return Result.success(sysRoleService.selectByPage(page, size, query));
    }

    @GetMapping("{id:\\d+}")
    @ApiOperation("查询")
    public Result selectById(@PathVariable("id") Integer id) {
        return Result.success(sysRoleService.selectById(id));
    }

    @GetMapping("list")
    public Result selectAll() {
        return Result.success(sysRoleService.selectAll());
    }

    @PostMapping
    @ApiOperation("新增")
    public Result save(@Valid @RequestBody SysRoleRequest request) {
        sysRoleService.save(request);
        return Result.success();
    }

    @PutMapping("{id:\\d+}")
    @ApiOperation("修改")
    public Result update(@PathVariable("id") Integer id, @Valid @RequestBody SysRoleRequest request) {
        sysRoleService.update(id, request);
        return Result.success();
    }

    @DeleteMapping("{id:\\d+}")
    @ApiOperation("删除")
    public Result delete(@PathVariable("id") Integer id) {
        sysRoleService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("批量删除")
    public Result deleteBatchIds(@RequestParam("ids") List<Integer> ids) {
        sysRoleService.deleteBatchIds(ids);
        return Result.success();
    }

    /**
     * 查找角色权限
     * @param id
     * @return
     */
    @GetMapping("{id}/permissions")
    public Result rolePermissions(@PathVariable Integer id) {
        return Result.success(sysPermissionService.selectRolePermissions(id));
    }

    @GetMapping("{id}/menus")
    public Result roleMenus(@PathVariable Integer id) {
        return Result.success(sysMenuService.selectRoleMenus(id,0));
    }
}
