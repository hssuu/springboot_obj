package com.qingge.springboot.entity;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 
 * </p>
 *
 * @author hjj
 * @since 2022-06-17
 */
@ToString
@Getter
@Setter
@TableName("sys_user")
@ApiModel(value = "User对象", description = "")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("ID")
        @TableId(value = "id", type = IdType.AUTO)

      private Integer id;

      @ApiModelProperty("学生姓名")

      private String stuname;


      @ApiModelProperty("学生学号")

      private String stunumber;


      @ApiModelProperty("密码")
      private String password;

      @ApiModelProperty("授课教师")
      private String teacher;


      @ApiModelProperty("学生已发布的论文的数量")
      private String counter;


      @ApiModelProperty("电话号码")
      private String tel;

      @ApiModelProperty("学院")
      private String college;

      @ApiModelProperty("专业")
      private String subject;

      @ApiModelProperty("创建时间")
      private LocalDateTime creatTime;


      @ApiModelProperty("头像")
      private String avatar_url;
  @ApiModelProperty("角色")
  private String role;
}
