package com.petlife.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.petlife.common.entity.User;

public interface UserService extends IService<User> {

    User login(String phone, String password);

    User register(String phone, String password);
}