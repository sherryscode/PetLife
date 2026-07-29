package com.petlife.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petlife.order.mapper.StoreMapper;
import com.petlife.order.service.StoreService;
import com.petlife.common.entity.Store;
import org.springframework.stereotype.Service;

@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements StoreService {
}