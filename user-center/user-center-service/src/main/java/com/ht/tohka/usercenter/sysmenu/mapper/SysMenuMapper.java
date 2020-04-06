package com.ht.tohka.usercenter.sysmenu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.ht.tohka.usercenter.api.sysmenu.entity.SysMenu;
import com.ht.tohka.usercenter.api.sysmenu.vo.SysMenuQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    /**
     * 分页查询
     */
    Page<SysMenu> selectByPage(@Param("query") SysMenuQuery query);

    List<SysMenu> selectByUserIdAndPid(@Param("userId") Integer userId, @Param("pid") Integer pid);

    void unBindRole(@Param("id") Integer id);

    List<SysMenu> selectByPid(@Param("pid") Integer pid);

    Integer countByIdAndRoleId(@Param("id") Integer id, @Param("roleId") Integer roleId);
}
