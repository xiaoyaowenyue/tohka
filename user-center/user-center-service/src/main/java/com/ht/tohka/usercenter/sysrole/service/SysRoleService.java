package com.ht.tohka.usercenter.sysrole.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ht.tohka.common.core.ApiException;
import com.ht.tohka.common.core.PageResult;
import com.ht.tohka.usercenter.api.sysrole.entity.SysRole;
import com.ht.tohka.usercenter.api.sysrole.vo.SysRoleQuery;
import com.ht.tohka.usercenter.api.sysrole.vo.SysRoleRequest;
import com.ht.tohka.usercenter.sysrole.mapper.SysRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SysRoleService {
    @Autowired
    private SysRoleMapper baseMapper;

    public PageResult<SysRole> selectByPage(Integer page, Integer size, SysRoleQuery query) {
        PageHelper.startPage(page, size);
        Page<SysRole> sysRoles = baseMapper.selectByPage(query);
        return new PageResult<>(sysRoles);
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(SysRoleRequest request) {
        SysRole sysRole = new SysRole();
        sysRole.setName(request.getName());
        baseMapper.insert(sysRole);
        // 绑定权限
        if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {
            baseMapper.bindPermission(sysRole.getId(), request.getPermissionIds());
        }
        // 绑定菜单
        if (request.getMenuIds() != null && !request.getMenuIds().isEmpty()) {
            baseMapper.bindMenu(sysRole.getId(), request.getMenuIds());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Integer id) {
        // 解绑权限
        baseMapper.unBindPermission(id);
        // 解绑菜单
        baseMapper.unBindMenu(id);
        // 解绑用户
        baseMapper.unBindUser(id);
        // 删除角色
        return baseMapper.deleteById(id);
    }

    public List<SysRole> selectBySysUserId(Integer sysUserId) {
        return baseMapper.selectBySysUserId(sysUserId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Integer id, SysRoleRequest request) {
        SysRole sysRole = Optional.ofNullable(baseMapper.selectById(id)).orElseThrow(() -> new ApiException(404, "指定的角色不存在"));
        sysRole.setName(request.getName());
        baseMapper.updateById(sysRole);

        if (request.getPermissionIds() != null) {
            baseMapper.unBindPermission(id);
            if (!request.getPermissionIds().isEmpty()) {
                baseMapper.bindPermission(sysRole.getId(), request.getPermissionIds());
            }
        }

        if (request.getMenuIds() != null) {
            baseMapper.unBindMenu(id);
            if (!request.getMenuIds().isEmpty()) {
                baseMapper.bindMenu(sysRole.getId(), request.getMenuIds());
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteBatchIds(List<Integer> ids) {
        ids.forEach(this::deleteById);
    }

    public SysRole selectById(Integer id) {
        return baseMapper.selectById(id);
    }

    public List<SysRole> selectAll() {
        return baseMapper.selectList(null);
    }

}
