package com.petlife.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.petlife.common.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> selectMenusByRoleId(Long roleId);
}