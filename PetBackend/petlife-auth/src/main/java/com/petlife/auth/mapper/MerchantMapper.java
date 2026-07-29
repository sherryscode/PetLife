package com.petlife.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.petlife.common.entity.Merchant;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MerchantMapper extends BaseMapper<Merchant> {
}