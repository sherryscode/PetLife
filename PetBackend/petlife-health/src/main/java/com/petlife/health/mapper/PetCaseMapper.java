package com.petlife.health.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.petlife.common.entity.PetCase;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PetCaseMapper extends BaseMapper<PetCase> {
}