package com.petlife.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.petlife.common.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}