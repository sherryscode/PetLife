package com.petlife.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pet")
public class Pet extends BaseEntity {

    private Long userId;

    private String petName;

    private String breed;

    private Integer age;

    private Integer gender;

    private BigDecimal weight;

    private String avatar;

    private LocalDate adoptDate;

    private Integer status;
}