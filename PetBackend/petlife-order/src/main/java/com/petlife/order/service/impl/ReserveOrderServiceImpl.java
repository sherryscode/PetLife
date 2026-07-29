package com.petlife.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petlife.order.mapper.ReserveOrderMapper;
import com.petlife.order.service.ReserveOrderService;
import com.petlife.common.entity.ReserveOrder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class ReserveOrderServiceImpl extends ServiceImpl<ReserveOrderMapper, ReserveOrder> implements ReserveOrderService {

    @Override
    public ReserveOrder createOrder(ReserveOrder order) {
        String orderNo = "RO" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) 
                + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        order.setOrderNo(orderNo);
        order.setOrderStatus(0);
        order.setPayStatus(0);
        order.setDepositAmount(order.getTotalAmount().multiply(new java.math.BigDecimal("0.3")));
        
        baseMapper.insert(order);
        return order;
    }
}