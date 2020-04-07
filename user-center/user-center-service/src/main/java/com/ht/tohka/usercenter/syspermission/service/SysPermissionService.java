package com.ht.tohka.usercenter.syspermission.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ht.tohka.common.core.PageResult;
import com.ht.tohka.usercenter.api.syspermission.entity.SysPermission;
import com.ht.tohka.usercenter.api.syspermission.vo.SysPermissionVO;
import com.ht.tohka.usercenter.sys.event.PmChangeEvent;
import com.ht.tohka.usercenter.sys.event.PmChangeTopic;
import com.ht.tohka.usercenter.syspermission.mapper.SysPermissionMapper;
import com.ht.tohka.usercenter.api.syspermission.vo.SysPermissionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;


@Service
public class SysPermissionService {

    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    @Autowired
    private PmChangeTopic pmChangeTopic;
   /* @Autowired //单体架构事件驱动方式
    private ApplicationEventPublisher publisher;*/

    public PageResult<SysPermission> selectByPage(Integer page, Integer size, SysPermissionQuery query) {
        PageHelper.startPage(page, size);
        Page<SysPermission> sysPermissions = sysPermissionMapper.selectByPage(query);
        return new PageResult<>(sysPermissions);
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
        SysPermission oldPermission = sysPermissionMapper.selectById(id);
        sysPermissionMapper.unBindRole(id);
        sysPermissionMapper.deleteById(id);
        // 发送到消息队列，通知所有授权中心服务实例卸载旧权限
//        publisher.publishEvent(new PmChangeEvent(null, oldPermission));
        pmChangeTopic.output().send(MessageBuilder.withPayload(new PmChangeEvent(null, oldPermission)).build());
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
        // 通知系统加载新权限
//        publisher.publishEvent(new PmChangeEvent(permission, null));
        pmChangeTopic.output().send(MessageBuilder.withPayload(new PmChangeEvent(null, permission)).build());
    }

    public void update(SysPermission permission) {
        SysPermission oldPermission = sysPermissionMapper.selectById(permission.getId());
        sysPermissionMapper.updateById(permission);
        // 通知系统权限有变更，重新加载该权限
//        publisher.publishEvent(new PmChangeEvent(oldPermission, permission));
        pmChangeTopic.output().send(MessageBuilder.withPayload(new PmChangeEvent(oldPermission, permission)).build());
    }


    /**
     * 查询用户的权限(标识)
     *
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
