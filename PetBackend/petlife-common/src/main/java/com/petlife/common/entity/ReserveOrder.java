package com.petlife.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("reserve_order")
public class ReserveOrder extends BaseEntity {

    private String orderNo;

    private Long userId;

    private Long petId;

    private Long storeId;

    private Long serviceId;

    private LocalDate reserveDate;

    private String reserveTime;

    private BigDecimal totalAmount;

    private BigDecimal depositAmount;

    private Integer payStatus;

    private Integer orderStatus;

    private String address;

    private String petDesc;

    private String remark;
}