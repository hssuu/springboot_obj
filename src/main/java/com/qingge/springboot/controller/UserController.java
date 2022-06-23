package com.qingge.springboot.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingge.springboot.common.Constants;
import com.qingge.springboot.common.Result;
import com.qingge.springboot.controller.dto.UserDTO;
import com.qingge.springboot.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.qingge.springboot.service.IUserService;
import com.qingge.springboot.entity.User;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hjj
 * @since 2022-06-17
 */
@RestController
@RequestMapping("/user")
        public class UserController {
    
@Resource
private IUserService userService;
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<User> list = userService.list();
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("id","id");
        writer.addHeaderAlias("stuname", "学生姓名");
        writer.addHeaderAlias("stunumber", "学号");
        writer.addHeaderAlias("college","学院");
        writer.addHeaderAlias("subject", "专业");
        writer.addHeaderAlias("counter", "课程");
        writer.addHeaderAlias("teacher", "授课教师");
        writer.addHeaderAlias("tel", "课程成绩");
        writer.setOnlyAlias(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

    }
    /*
     * excel 导入
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
//        List<User> list = reader.readAll(User.class);
//        userService.saveBatch(list);

        // 方式2：忽略表头的中文，直接读取表的内容
        List<List<Object>> list = reader.read(1);
        List<User>users = CollUtil.newArrayList();

        for (List<Object> row : list) {
            User user = new User();
            user.setStuname(row.get(0).toString());
            user.setStunumber(row.get(1).toString());
            user.setCollege(row.get(2).toString());
            user.setSubject(row.get(3).toString());
            user.setCounter(row.get(4).toString());
            user.setTeacher(row.get(5).toString());
            user.setTel(row.get(6).toString());
            users.add(user);
        }
        userService.saveBatch(users);
        return Result.success(true);
    }
    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO){
        String username = userDTO.getStunumber();
        String password = userDTO.getPassword();
        if(StrUtil.isBlank(username)||StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400,"参数错误");
        }
        UserDTO dto = userService.login(userDTO);
        return Result.success(dto);
    }
//注册
    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO) {
        String username = userDTO.getStunumber();
        String password = userDTO.getPassword();
        if(StrUtil.isBlank(username)||StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400,"参数错误");
        }

        return Result.success(userService.register(userDTO));

    }
// 新增或者更新
@PostMapping
public Result save(@RequestBody User user) {

        return Result.success(userService.saveOrUpdate(user));
        }

@DeleteMapping("/{id}")
public Result delete(@PathVariable Integer id) {

        return Result.success(userService.removeById(id));
        }

@PostMapping("/del/batch")
public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(userService.removeByIds(ids));
        }

@GetMapping
public Result findAll() {
        return Result.success(userService.list());
        }

@GetMapping("/{id}")
public Result findOne(@PathVariable Integer id) {
        return Result.success(userService.getById(id));
        }

    @GetMapping("/username/{stunumber}")
    public Result findOne(@PathVariable String stunumber) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("stunumber",stunumber);
        return Result.success(userService.getOne(queryWrapper));
    }

@GetMapping("/page")
public Result findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String stuname,
                           @RequestParam(defaultValue = "") String stunumber,
                           @RequestParam(defaultValue = "") String college,
                           @RequestParam(defaultValue = "") String subject,
                           @RequestParam(defaultValue = "") String teacher,
                           @RequestParam(defaultValue = "") String counter,
                           @RequestParam(defaultValue = "") String tel) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
    if (!"".equals(stuname)) {
        queryWrapper.like("stuname", stuname);
    }
    if (!"".equals(stunumber)) {
        queryWrapper.like("stunumber", stunumber);
    }
    if (!"".equals(stunumber)) {
        queryWrapper.like("college", college);
    }
    if (!"".equals(stunumber)) {
        queryWrapper.like("subject", subject);
    }

    if (!"".equals(counter)) {
        queryWrapper.like("counter", counter);
    }
    if(!"".equals(tel)){
        queryWrapper.like("tel",tel);
    }
    //获取当前用户信息
    User currentUser = TokenUtils.getCurrentUser();
    System.out.println("获取用户信息========================"+currentUser.getStuname());
    queryWrapper.eq("teacher",currentUser.getStuname());
    return Result.success(userService.page(new Page<>(pageNum, pageSize), queryWrapper));
        }

        }

