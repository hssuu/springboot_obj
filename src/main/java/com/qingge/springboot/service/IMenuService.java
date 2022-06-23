package com.qingge.springboot.service;

import com.qingge.springboot.entity.Menu;
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
public interface IMenuService extends IService<Menu> {

    List<Menu> findMenus(String name);
}
