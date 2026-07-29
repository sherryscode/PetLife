package com.petlife.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petlife.mall.mapper.ShopCartMapper;
import com.petlife.mall.service.ShopCartService;
import com.petlife.common.entity.ShopCart;
import org.springframework.stereotype.Service;

@Service
public class ShopCartServiceImpl extends ServiceImpl<ShopCartMapper, ShopCart> implements ShopCartService {

    @Override
    public ShopCart addToCart(Long userId, Long goodsId, Integer quantity) {
        ShopCart existing = baseMapper.selectOne(new LambdaQueryWrapper<ShopCart>()
                .eq(ShopCart::getUserId, userId)
                .eq(ShopCart::getGoodsId, goodsId));

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
            baseMapper.updateById(existing);
            return existing;
        }

        ShopCart cart = new ShopCart();
        cart.setUserId(userId);
        cart.setGoodsId(goodsId);
        cart.setQuantity(quantity);
        cart.setSelected(1);
        baseMapper.insert(cart);
        return cart;
    }

    @Override
    public ShopCart updateQuantity(Long userId, Long goodsId, Integer quantity) {
        ShopCart cart = baseMapper.selectOne(new LambdaQueryWrapper<ShopCart>()
                .eq(ShopCart::getUserId, userId)
                .eq(ShopCart::getGoodsId, goodsId));

        if (cart != null) {
            cart.setQuantity(quantity);
            baseMapper.updateById(cart);
        }
        return cart;
    }

    @Override
    public void removeFromCart(Long userId, Long goodsId) {
        baseMapper.delete(new LambdaQueryWrapper<ShopCart>()
                .eq(ShopCart::getUserId, userId)
                .eq(ShopCart::getGoodsId, goodsId));
    }
}