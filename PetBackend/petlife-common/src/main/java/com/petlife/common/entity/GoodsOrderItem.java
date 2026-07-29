package com.petlife.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("goods_order_item")
public class GoodsOrderItem extends BaseEntity {

    private Long orderId;

    private Long goodsId;

    private String goodsName;

    private BigDecimal price;

    private Integer quantity;
}