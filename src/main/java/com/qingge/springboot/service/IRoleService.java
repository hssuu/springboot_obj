package com.qingge.springboot.service;

import com.qingge.springboot.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hjj
 * @since 2022-06-22
 */
public interface IRoleService extends IService<Role> {


    void saveRoleMenu(Integer roleId, List<Integer> menuIds);

    List<Integer> getRoleMenu(Integer roleId);
}
