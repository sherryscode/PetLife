package com.petlife.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pet_lost")
public class PetLost extends BaseEntity {

    private Long userId;

    private String petName;

    private String breed;

    private Integer gender;

    private Integer age;

    private String avatar;

    private String lostAddress;

    private LocalDateTime lostTime;

    private String contactPhone;

    private BigDecimal reward;

    private String description;

    private Integer isTop;

    private Integer auditStatus;

    private Integer status;
}