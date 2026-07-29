package com.petlife.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.petlife.common.entity.ShopCart;

public interface ShopCartService extends IService<ShopCart> {

    ShopCart addToCart(Long userId, Long goodsId, Integer quantity);

    ShopCart updateQuantity(Long userId, Long goodsId, Integer quantity);

    void removeFromCart(Long userId, Long goodsId);
}