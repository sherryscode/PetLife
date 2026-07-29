package com.petlife.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("menu")
public class Menu extends BaseEntity {

    private Long parentId;

    private String menuName;

    private String path;

    private String component;

    private String icon;

    private Integer menuType;

    private Integer sortOrder;

    private Integer status;
}