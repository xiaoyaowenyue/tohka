package com.ht.tohka.usercenter.sysmenu.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ht.tohka.common.core.ApiException;
import com.ht.tohka.common.core.PageResult;
import com.ht.tohka.usercenter.api.sysmenu.entity.SysMenu;
import com.ht.tohka.usercenter.sysmenu.mapper.SysMenuMapper;
import com.ht.tohka.usercenter.api.sysmenu.vo.MenuTreeVO;
import com.ht.tohka.usercenter.api.sysmenu.vo.OptionsTreeVO;
import com.ht.tohka.usercenter.api.sysmenu.vo.SysMenuQuery;
import com.ht.tohka.usercenter.api.sysmenu.vo.SysMenuTreeResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysMenuService {
    public static final Integer SYS_MENU_ROOT_ID = 0;

    @Autowired
    private SysMenuMapper sysMenuMapper;

    public PageResult<SysMenu> selectByPage(Integer page, Integer size, SysMenuQuery query) {
        PageHelper.startPage(page, size);
        Page<SysMenu> sysMenus = sysMenuMapper.selectByPage(query);
        return new PageResult<>(sysMenus);
    }

    /**
     * 获取用户所有菜单
     *
     * @param userId
     * @return
     */
    public List<SysMenuTreeResponse> selectUserMenus(Integer userId) {
        return getSubMenuAsTreeVo(userId, SYS_MENU_ROOT_ID);
    }

    /**
     * 获取用户某一级菜单及其子菜单
     *
     * @param userId
     * @param pid
     * @return
     */
    private List<SysMenuTreeResponse> getSubMenuAsTreeVo(Integer userId, Integer pid) {
        List<SysMenu> menus = sysMenuMapper.selectByUserIdAndPid(userId, pid);
        if (menus == null || menus.isEmpty()) {
            return Collections.emptyList();
        } else {
            List<SysMenuTreeResponse> treeVos = menus.stream().map(m -> {
                SysMenuTreeResponse treeVo = new SysMenuTreeResponse();
                BeanUtils.copyProperties(m, treeVo);
                return treeVo;
            }).collect(Collectors.toList());
            treeVos.forEach(vo -> vo.setChildren(getSubMenuAsTreeVo(userId, vo.getId())));
            return treeVos;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        List<SysMenu> subMenus = sysMenuMapper.selectByPid(id);
        if (subMenus != null && !subMenus.isEmpty()) {
            subMenus.forEach(menu -> deleteById(menu.getId()));
        }
        // 解绑角色
        sysMenuMapper.unBindRole(id);
        sysMenuMapper.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteBatchIds(List<Integer> ids) {
        ids.forEach(this::deleteById);
    }

    public SysMenu selectById(Integer id) {
        return sysMenuMapper.selectById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(SysMenu sysMenu) {
        sysMenuMapper.insert(sysMenu);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(SysMenu sysMenu) {
        //不能修改父级菜单id
        SysMenu record = sysMenuMapper.selectById(sysMenu.getId());
        if (record == null) {
            throw new ApiException(404, "菜单不存在");
        }
        if (!record.getPid().equals(sysMenu.getPid())) {
            throw new ApiException(500, "父级菜单不允许修改");
        }
        sysMenuMapper.updateById(sysMenu);
    }

    /**
     * 查询角色有哪些菜单权限
     *
     * @param roleId
     * @param pid
     * @return
     */
    public List<MenuTreeVO> selectRoleMenus(Integer roleId, Integer pid) {
        List<SysMenu> menus = sysMenuMapper.selectByPid(pid);
        if (menus == null || menus.isEmpty()) {
            return Collections.emptyList();
        }
        return menus.stream().map(x -> convert2treeVo(roleId, x)).collect(Collectors.toList());
    }

    /**
     * 转化为树形结构的菜单vo
     *
     * @param roleId
     * @param sysMenu
     * @return
     */
    private MenuTreeVO convert2treeVo(Integer roleId, SysMenu sysMenu) {
        MenuTreeVO treeVo = new MenuTreeVO();
        BeanUtils.copyProperties(sysMenu, treeVo);
        Integer count = sysMenuMapper.countByIdAndRoleId(sysMenu.getId(), roleId);
        if (count == null || count.equals(0)) {
            treeVo.setChecked(false);
        } else {
            treeVo.setChecked(true);
        }
        // 递归查找子菜单
        List<MenuTreeVO> subVos = selectRoleMenus(roleId, sysMenu.getId());
        if (subVos == null || subVos.isEmpty()) {
            treeVo.setIsLeaf(true);
        } else {
            treeVo.setIsLeaf(false);
            // 如果存在子菜单，让子菜单来决定当前菜单的选中状态
            treeVo.setChecked(null);
            treeVo.setChildren(subVos);
        }
        return treeVo;
    }

    /**
     * 新增菜单时，选择父级菜单，把可选的值列出来，父级菜单最多2级
     *
     * @return
     */
    public OptionsTreeVO selectOptionsTree() {
        // "根"菜单
        SysMenu home = new SysMenu();
        home.setId(0);
        home.setText("Alain");
        int level = 3;
        return convert2OptionsTreeVo(home, level);
    }

    private OptionsTreeVO convert2OptionsTreeVo(SysMenu sysMenu, int level) {
        OptionsTreeVO vo = new OptionsTreeVO();
        vo.setId(sysMenu.getId());
        vo.setText(sysMenu.getText());
        List<SysMenu> list = sysMenuMapper.selectByPid(sysMenu.getId());
        if (list == null || list.isEmpty()) {
            vo.setIsLeaf(true);
        } else {
            if (level - 1 != 0) {
                vo.setIsLeaf(false);
                List<OptionsTreeVO> children = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    SysMenu subMenu = list.get(i);

                    OptionsTreeVO subOptionTreeVo = convert2OptionsTreeVo(subMenu, level - 1);
                    if (subOptionTreeVo != null) {
                        children.add(subOptionTreeVo);
                    }
                }
                vo.setChildren(children);
            } else {
                vo.setIsLeaf(true);
            }
        }
        return vo;
    }

    public List<Integer> findParentIds(Integer id) {
        if (id != null && id == 0) { //HOME菜单id
            return Collections.singletonList(id);
        }
        SysMenu menu = sysMenuMapper.selectById(id);
        if (menu == null) {
            throw new ApiException(404, "菜单不存在");
        }
        List<Integer> idList = new ArrayList<>();
        SysMenu parentMenu = sysMenuMapper.selectById(menu.getPid());
        while (parentMenu != null) {
            idList.add(parentMenu.getId());
            parentMenu = sysMenuMapper.selectById(parentMenu.getPid());
        }
        //按菜单级别从高到低排放 比如HOME是最高级的菜单
        List<Integer> list = new ArrayList<>(idList.size());
        list.add(0); //HOMME菜单，实际上不存在
        for (int i = idList.size() - 1; i >= 0; i--) {
            list.add(idList.get(i));
        }
        return list;
    }
}
