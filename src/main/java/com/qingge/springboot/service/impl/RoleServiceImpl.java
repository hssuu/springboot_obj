package com.qingge.springboot.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qingge.springboot.entity.Menu;
import com.qingge.springboot.entity.Role;
import com.qingge.springboot.entity.RoleMenu;
import com.qingge.springboot.mapper.RoleMapper;
import com.qingge.springboot.mapper.RoleMenuMapper;
import com.qingge.springboot.service.IMenuService;
import com.qingge.springboot.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hjj
 * @since 2022-06-22
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private IMenuService menuService;
    @Transactional
    @Override
    public void saveRoleMenu(Integer roleId, List<Integer> menuIds) {
        //        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("role_id", roleId);
//        roleMenuMapper.delete(queryWrapper);

        // 先删除当前角色id所有的绑定关系
        roleMenuMapper.deleteByRoleId(roleId);

        List<Integer> menuIdsCopy= CollUtil.newArrayList(menuIds);
        // 再把前端传过来的菜单id数组绑定到当前的这个角色id上去
        for (Integer menuId : menuIds) {
            Menu menu = menuService.getById(menuId);
            if(menu.getPid() != null && !menuIdsCopy.contains(menu.getPid())){//二级菜单 传过来的menuid数组没有他的父亲id
                //加上这个父亲id
                RoleMenu roleMenu=new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menu.getPid());
                roleMenuMapper.insert(roleMenu);
                menuIdsCopy.add(menu.getPid());
            }
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenuMapper.insert(roleMenu);
        }
    }

    @Override
    public List<Integer> getRoleMenu(Integer roleId) {
        return roleMenuMapper.selectByRoleId(roleId);
    }
}
