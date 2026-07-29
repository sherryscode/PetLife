package com.petlife.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("goods")
public class Goods extends BaseEntity {

    private Long merchantId;

    private String goodsName;

    private Integer category;

    private BigDecimal price;

    private BigDecimal originalPrice;

    private Integer stock;

    private String description;

    private String images;

    private Integer sales;

    private Integer isHot;

    private Integer auditStatus;

    private Integer status;
}