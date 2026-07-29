package com.petlife.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pet_vaccine")
public class PetVaccine extends BaseEntity {

    private Long petId;

    private String vaccineName;

    private LocalDateTime vaccineTime;

    private LocalDateTime validUntil;

    private String remark;
}