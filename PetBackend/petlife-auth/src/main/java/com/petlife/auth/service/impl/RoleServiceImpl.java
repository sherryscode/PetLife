package com.petlife.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petlife.auth.mapper.RoleMapper;
import com.petlife.auth.service.RoleService;
import com.petlife.common.entity.Role;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
}