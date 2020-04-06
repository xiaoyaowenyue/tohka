package com.ht.tohka.usercenter.syspermission.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.ht.tohka.usercenter.api.syspermission.entity.SysPermission;
import com.ht.tohka.usercenter.api.syspermission.vo.SysPermissionQuery;
import com.ht.tohka.usercenter.api.syspermission.vo.SysPermissionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysPermissionMapper extends BaseMapper<SysPermission> {
    /**
     *  分页查询
     */
    Page<SysPermission> selectByPage(@Param("query") SysPermissionQuery query);

    List<SysPermission> selectBySysUserId(@Param("sysUserId") Integer sysUserId);

    List<SysPermission> selectBySysRoleId(@Param("sysRoleId") Integer sysRoleId);

    List<SysPermission> selectByPid(@Param("pid") Integer pid);

    Integer countByIdAndRoleId(@Param("id") Integer id, @Param("roleId") Integer roleId);

    // 解绑角色
    void unBindRole(@Param("id") Integer id);

    // 查找角色拥有的权限(全量)
    List<SysPermissionVO> selectRolePermissions(@Param("roleId") Integer roleId);
}
