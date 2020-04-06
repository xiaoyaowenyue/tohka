package com.ht.tohka.usercenter.syspermission.controller;

import com.ht.tohka.common.core.Result;
import com.ht.tohka.usercenter.api.syspermission.entity.SysPermission;
import com.ht.tohka.usercenter.syspermission.service.SysPermissionService;
import com.ht.tohka.usercenter.api.syspermission.vo.SysPermissionQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value = "SysPermissionController", tags = "权限管理")
@RequestMapping("/api/v1/sys/permissions")
public class SysPermissionController {
    @Autowired
    private SysPermissionService permissionService;

    @GetMapping
    @ApiOperation("分页条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码"),
            @ApiImplicitParam(name = "size", value = "每页显示的数量"),
    })
    public Result selectByPageVo(@RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "10") Integer size,
                                 SysPermissionQuery query) {
        return Result.success(permissionService.selectByPage(page, size, query));
    }

    @GetMapping("{id:\\d+}")
    @ApiOperation("查询")
    public Result selectById(@PathVariable("id") Integer id) {
        return Result.success(permissionService.selectById(id));
    }

    @PostMapping
    @ApiOperation("新增")
    public Result save(@RequestBody SysPermission permission) {
        permissionService.save(permission);
        return Result.success();
    }

    @PutMapping("{id:\\d+}")
    @ApiOperation("修改")
    public Result update(@PathVariable("id") Integer id, @RequestBody SysPermission permission) {
        permission.setId(id);
        permissionService.update(permission);
        return Result.success();
    }

    @DeleteMapping("{id:\\d+}")
    @ApiOperation("删除")
    public Result delete(@PathVariable("id") Integer id) {
        permissionService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("批量删除")
    public Result deleteBatchIds(@RequestParam("ids") List<Integer> ids) {
        permissionService.deleteBatchIds(ids);
        return Result.success();
    }
}
