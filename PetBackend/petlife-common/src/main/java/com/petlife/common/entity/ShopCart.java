package com.petlife.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("shop_cart")
public class ShopCart extends BaseEntity {

    private Long userId;

    private Long goodsId;

    private Integer quantity;

    private Integer selected;
}