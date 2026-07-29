package com.petlife.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("merchant")
public class Merchant extends BaseEntity {

    private String phone;

    private String password;

    private String merchantName;

    private String storeName;

    private String storeAddress;

    private String storePhone;

    private String businessScope;

    private String businessHours;

    private BigDecimal rating;

    private Integer auditStatus;

    private Integer status;
}