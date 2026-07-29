package com.petlife.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petlife.mall.mapper.GoodsOrderItemMapper;
import com.petlife.mall.service.GoodsOrderItemService;
import com.petlife.common.entity.GoodsOrderItem;
import org.springframework.stereotype.Service;

@Service
public class GoodsOrderItemServiceImpl extends ServiceImpl<GoodsOrderItemMapper, GoodsOrderItem> implements GoodsOrderItemService {
}