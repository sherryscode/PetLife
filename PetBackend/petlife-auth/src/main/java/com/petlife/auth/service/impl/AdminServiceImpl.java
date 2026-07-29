package com.petlife.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petlife.auth.mapper.AdminMapper;
import com.petlife.auth.service.AdminService;
import com.petlife.common.entity.Admin;
import com.petlife.common.exception.BusinessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Admin login(String username, String password) {
        Admin admin = baseMapper.selectOne(new LambdaQueryWrapper<Admin>()
                .eq(Admin::getUsername, username)
                .eq(Admin::getStatus, 1));

        if (admin == null || !passwordEncoder.matches(password, admin.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        return admin;
    }
}