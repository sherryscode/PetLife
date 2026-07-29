package com.petlife.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petlife.auth.mapper.UserMapper;
import com.petlife.auth.service.UserService;
import com.petlife.common.entity.User;
import com.petlife.common.exception.BusinessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User login(String phone, String password) {
        User user = baseMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getPhone, phone)
                .eq(User::getStatus, 1));

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException("手机号或密码错误");
        }

        return user;
    }

    @Override
    public User register(String phone, String password) {
        User existing = baseMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getPhone, phone));

        if (existing != null) {
            throw new BusinessException("该手机号已注册");
        }

        User user = new User();
        user.setPhone(phone);
        user.setPassword(passwordEncoder.encode(password));
        user.setNickname("用户");
        user.setStatus(1);

        baseMapper.insert(user);
        return user;
    }
}