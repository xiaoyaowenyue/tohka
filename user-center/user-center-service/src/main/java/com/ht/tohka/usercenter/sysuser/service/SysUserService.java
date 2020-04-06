package com.ht.tohka.usercenter.sysuser.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ht.authorization.AuthenticationContext;
import com.ht.tohka.common.core.ApiException;
import com.ht.tohka.common.core.PageResult;
import com.ht.tohka.usercenter.api.sysuser.entity.SysUser;
import com.ht.tohka.usercenter.sysuser.mapper.SysUserMapper;
import com.ht.tohka.usercenter.api.sysuser.vo.ChangePasswordRequest;
import com.ht.tohka.usercenter.api.sysuser.vo.SysUserQuery;
import com.ht.tohka.usercenter.api.sysuser.vo.SysUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Transactional(rollbackFor = Exception.class)
    public int save(SysUser user) {
        String hashpw = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(10));
        user.setPassword(hashpw);
        return sysUserMapper.insert(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Integer id, SysUser sysUser) {
        SysUser oldUser = Optional.ofNullable(sysUserMapper.selectById(id))
                .orElseThrow(() -> new ApiException(404, "用户不存在"));
        oldUser.setNickname(sysUser.getNickname());
        oldUser.setUsername(sysUser.getUsername());
        oldUser.setAvatar(sysUser.getAvatar());
        oldUser.setEmail(sysUser.getEmail());
        if (StringUtils.hasText(sysUser.getPassword())&&sysUser.getPassword().length()>=6) {
            oldUser.setPassword(BCrypt.hashpw(sysUser.getPassword(), BCrypt.gensalt(10)));
        } else if (StringUtils.hasText(sysUser.getPassword())) {
            throw new ApiException(422, "密码长度不能少于6位");
        }
        sysUserMapper.updateById(oldUser);
    }

    public PageResult<SysUserVO> selectByPage(Integer page, Integer size, SysUserQuery query) {
        PageHelper.startPage(page, size);
        Page<SysUserVO> list = sysUserMapper.selectByPage(query);
        return new PageResult<>(list);
    }

    @Transactional(rollbackFor = Exception.class)
    public void bindRoles(Integer id, List<Integer> roleIds) {
        //先解绑角色
        sysUserMapper.unBindRoles(id);
        if (roleIds != null && !roleIds.isEmpty()) {
            sysUserMapper.bindRoles(id, roleIds);
        }
    }

    public SysUser selectById(Integer id) {
        return sysUserMapper.selectById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        sysUserMapper.unBindRoles(id);
        sysUserMapper.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteBatchIds(List<Integer> ids) {
        ids.forEach(this::deleteById);
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        SysUser sysUser = selectById(AuthenticationContext.current().getId());
        sysUser.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt(10)));
        sysUserMapper.updateById(sysUser);
    }

    public Optional<SysUser> selectByUsername(String username) {
        return Optional.ofNullable(sysUserMapper.selectByUsername(username));
    }
}
