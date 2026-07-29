package com.petlife.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pet_checkup")
public class PetCheckup extends BaseEntity {

    private Long petId;

    private String hospitalName;

    private String checkupType;

    private LocalDateTime checkupTime;

    private BigDecimal cost;

    private String result;

    private String remark;
}