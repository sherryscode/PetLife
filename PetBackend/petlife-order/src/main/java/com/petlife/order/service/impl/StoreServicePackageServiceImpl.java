package com.petlife.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petlife.order.mapper.StoreServiceMapper;
import com.petlife.order.service.StoreServicePackageService;
import com.petlife.common.entity.StoreService;
import org.springframework.stereotype.Service;

@Service
public class StoreServicePackageServiceImpl extends ServiceImpl<StoreServiceMapper, StoreService> implements StoreServicePackageService {
}