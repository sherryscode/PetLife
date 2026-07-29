package com.petlife.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pet_case")
public class PetCase extends BaseEntity {

    private Long petId;

    private String hospitalName;

    private String disease;

    private String medicine;

    private String diagnosis;

    private LocalDateTime visitTime;

    private BigDecimal cost;
}