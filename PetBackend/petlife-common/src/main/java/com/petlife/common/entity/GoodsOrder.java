package com.petlife.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("goods_order")
public class GoodsOrder extends BaseEntity {

    private String orderNo;

    private Long userId;

    private BigDecimal totalAmount;

    private Integer payStatus;

    private Integer shipStatus;

    private Integer orderStatus;

    private String receiverName;

    private String receiverPhone;

    private String receiverAddress;

    private String remark;
}