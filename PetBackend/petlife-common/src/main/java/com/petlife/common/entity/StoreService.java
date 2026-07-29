package com.petlife.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("store_service")
public class StoreService extends BaseEntity {

    private Long storeId;

    private String serviceName;

    private Integer serviceType;

    private BigDecimal price;

    private String description;

    private Integer duration;

    private Integer status;

    private Integer sortOrder;
}