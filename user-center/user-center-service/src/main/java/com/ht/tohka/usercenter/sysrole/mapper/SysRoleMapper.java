package com.ht.tohka.usercenter.sysrole.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.ht.tohka.usercenter.api.sysrole.entity.SysRole;
import com.ht.tohka.usercenter.api.sysrole.vo.SysRoleQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysRoleMapper extends BaseMapper<SysRole> {
    /**
     * 分页查询
     */
    Page<SysRole> selectByPage(@Param("query") SysRoleQuery query);

    List<SysRole> selectBySysUserId(@Param("sysUserId") Integer sysUserId);

    // 绑定权限
    int bindPermission(@Param("sysRoleId") Integer sysRoleId, @Param("permissionIds") List<Integer> permissionIds);

    // 解绑权限
    int unBindPermission(@Param("id") Integer roleId);

    // 解绑菜单
    void unBindMenu(@Param("id") Integer id);

    void bindMenu(@Param("id") Integer id, @Param("menuIds") List<Integer> menuIds);

    // 解绑用户
    void unBindUser(@Param("id") Integer id);
}
