package com.ht.tohka.usercenter.sysuser.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.ht.tohka.usercenter.api.sysuser.vo.SysUserQuery;
import com.ht.tohka.usercenter.api.sysuser.vo.SysUserVO;
import com.ht.tohka.usercenter.api.sysuser.entity.SysUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {
    Page<SysUserVO> selectByPage(@Param("query") SysUserQuery query);

    SysUser selectByUsername(@Param("username") String username);

    //绑定角色
    int bindRoles(@Param("sysUserId") Integer sysUserId, @Param("roleIds") List<Integer> roleIds);

    //解绑角色
    int unBindRoles(@Param("sysUserId") Integer sysUserId);
}
