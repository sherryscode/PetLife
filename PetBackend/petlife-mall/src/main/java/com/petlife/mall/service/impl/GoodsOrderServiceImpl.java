package com.petlife.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petlife.mall.mapper.GoodsOrderMapper;
import com.petlife.mall.service.GoodsOrderService;
import com.petlife.common.entity.GoodsOrder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class GoodsOrderServiceImpl extends ServiceImpl<GoodsOrderMapper, GoodsOrder> implements GoodsOrderService {

    @Override
    public GoodsOrder createOrder(GoodsOrder order) {
        String orderNo = "GO" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) 
                + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        order.setOrderNo(orderNo);
        order.setOrderStatus(0);
        order.setPayStatus(0);
        order.setShipStatus(0);
        
        baseMapper.insert(order);
        return order;
    }
}