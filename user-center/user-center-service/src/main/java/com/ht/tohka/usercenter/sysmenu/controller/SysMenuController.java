package com.ht.tohka.usercenter.sysmenu.controller;

import com.ht.tohka.common.core.Result;
import com.ht.tohka.usercenter.api.sysmenu.entity.SysMenu;
import com.ht.tohka.usercenter.sysmenu.service.SysMenuService;
import com.ht.tohka.usercenter.api.sysmenu.vo.RoleMenuRequest;
import com.ht.tohka.usercenter.api.sysmenu.vo.SysMenuQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@RestController
@Api(value = "SysMenuController", tags = "菜单管理")
@RequestMapping("/api/v1/sys/menus")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;

    @GetMapping
    @ApiOperation("分页条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码"),
            @ApiImplicitParam(name = "size", value = "每页显示的数量"),
    })
    public Result selectByPage(@RequestParam(defaultValue = "1") Integer page,
                               @RequestParam(defaultValue = "10") Integer size,
                               SysMenuQuery query) {
        return Result.success(sysMenuService.selectByPage(page, size, query));
    }

    @GetMapping("options/tree")
    @ApiOperation("查询菜单选项")
    public Result findOptions() {
        return Result.success(Collections.singletonList(sysMenuService.selectOptionsTree()));
    }

    @GetMapping("parents/id_list")
    @ApiOperation("递归查询父级菜单")
    public Result findParentIds(@RequestParam Integer id) {
        return Result.success(sysMenuService.findParentIds(id));
    }

    @GetMapping("{id:\\d+}")
    @ApiOperation("查询")
    public Result selectById(@PathVariable("id") Integer id) {
        return Result.success(sysMenuService.selectById(id));
    }

    @PostMapping
    @ApiOperation("新增")
    public Result save(@RequestBody SysMenu sysMenu) {
        sysMenuService.save(sysMenu);
        return Result.success();
    }

    @PutMapping("{id:\\d+}")
    @ApiOperation("修改")
    public Result update(@PathVariable Integer id, @RequestBody SysMenu sysMenu) {
        sysMenu.setId(id);
        sysMenuService.update(sysMenu);
        return Result.success();
    }

    @DeleteMapping("{id:\\d+}")
    @ApiOperation("删除")
    public Result delete(@PathVariable("id") Integer id) {
        sysMenuService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("批量删除")
    public Result deleteBatchIds(@RequestParam("ids") List<Integer> ids) {
        sysMenuService.deleteBatchIds(ids);
        return Result.success();
    }

}
