package com.petlife.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("role_menu")
public class RoleMenu extends BaseEntity {

    private Long roleId;

    private Long menuId;
}