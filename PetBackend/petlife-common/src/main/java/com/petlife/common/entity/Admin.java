package com.petlife.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("admin")
public class Admin extends BaseEntity {

    private String username;

    private String password;

    private String nickname;

    private String avatar;

    private Integer status;
}