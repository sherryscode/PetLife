package com.petlife.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.petlife.common.entity.ReserveOrder;

public interface ReserveOrderService extends IService<ReserveOrder> {

    ReserveOrder createOrder(ReserveOrder order);
}