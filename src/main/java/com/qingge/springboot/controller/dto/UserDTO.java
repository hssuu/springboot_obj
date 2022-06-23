package com.qingge.springboot.controller.dto;

import com.qingge.springboot.entity.Menu;
import lombok.Data;

import java.util.List;

/*
接受前端登录请求的参数
 */
@Data
public class UserDTO {
    private  String stuname;
    private String stunumber;
    private String password;
    private String tel;
    private String avatarUrl;
    private String token;
    private String role;
    private List<Menu> menus;
}
