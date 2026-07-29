package com.petlife.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petlife.auth.mapper.MenuMapper;
import com.petlife.auth.service.MenuService;
import com.petlife.common.entity.Menu;
import org.springframework.stereotype.Service;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
}