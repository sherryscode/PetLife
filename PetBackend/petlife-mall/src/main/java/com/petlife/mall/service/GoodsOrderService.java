package com.petlife.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.petlife.common.entity.GoodsOrder;

public interface GoodsOrderService extends IService<GoodsOrder> {

    GoodsOrder createOrder(GoodsOrder order);
}