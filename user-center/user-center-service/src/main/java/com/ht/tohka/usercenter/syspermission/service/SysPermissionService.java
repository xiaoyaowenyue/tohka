package com.ht.tohka.usercenter.syspermission.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ht.tohka.common.core.PageResult;
import com.ht.tohka.usercenter.api.syspermission.entity.SysPermission;
import com.ht.tohka.usercenter.api.syspermission.vo.SysPermissionVO;
import com.ht.tohka.usercenter.syspermission.mapper.SysPermissionMapper;
import com.ht.tohka.usercenter.api.syspermission.vo.SysPermissionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;


@Service
public class SysPermissionService {

    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    @Autowired
    private ApplicationEventPublisher publisher;

    public PageResult<SysPermission> selectByPage(Integer page, Integer size, SysPermissionQuery query) {
        PageHelper.startPage(page, size);
        Page<SysPermission> sysPermissions = sysPermissionMapper.selectByPage(query);
        return new PageResult(sysPermissions);
    }

    public List<SysPermission> findAll() {
        List<SysPermission> permissions = sysPermissionMapper.selectList(null);
        if (permissions != null) {
            return permissions;
        }
        return Collections.EMPTY_LIST;
    }


    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        sysPermissionMapper.unBindRole(id);
        sysPermissionMapper.deleteById(id);
//        SysPermission permission = new SysPermission();
//        permission.setId(id);
//        publisher.publishEvent(new PermissionChangedEvent(permission, PermissionChangeTypeEnum.DELETE));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteBatchIds(List<Integer> ids) {
        if (ids != null) {
            ids.forEach(this::deleteById);
        }
    }

    public SysPermission selectById(Integer id) {
        return sysPermissionMapper.selectById(id);
    }

    public void save(SysPermission permission) {
        sysPermissionMapper.insert(permission);
//        publisher.publishEvent(new PermissionChangedEvent(permission, PermissionChangeTypeEnum.ADD));
    }

    public void update(SysPermission permission) {
        sysPermissionMapper.updateById(permission);
//        publisher.publishEvent(new PermissionChangedEvent(permission, PermissionChangeTypeEnum.UPDATE));
    }


    /**
     * 查询用户的权限(标识)
     * @param sysUserId
     * @return
     */
    public List<SysPermission> selectBySysUserId(Integer sysUserId) {
        return sysPermissionMapper.selectBySysUserId(sysUserId);
    }

    // 角色权限查询
    public List<SysPermissionVO> selectRolePermissions(Integer roleId) {
        return sysPermissionMapper.selectRolePermissions(roleId);
    }
}
