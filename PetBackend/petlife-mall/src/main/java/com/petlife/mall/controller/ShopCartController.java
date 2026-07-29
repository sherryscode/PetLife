package com.petlife.mall.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.petlife.mall.service.ShopCartService;
import com.petlife.common.entity.ShopCart;
import com.petlife.common.response.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class ShopCartController {

    private final ShopCartService shopCartService;

    public ShopCartController(ShopCartService shopCartService) {
        this.shopCartService = shopCartService;
    }

    @GetMapping("/user/{userId}")
    public Result<List<ShopCart>> getByUserId(@PathVariable("userId") Long userId) {
        List<ShopCart> carts = shopCartService.list(new LambdaQueryWrapper<ShopCart>()
                .eq(ShopCart::getUserId, userId));
        return Result.success(carts);
    }

    @PostMapping
    public Result<ShopCart> add(@RequestBody ShopCart cart) {
        ShopCart created = shopCartService.addToCart(cart.getUserId(), cart.getGoodsId(), cart.getQuantity());
        return Result.success("添加成功", created);
    }

    @PutMapping
    public Result<ShopCart> update(@RequestBody ShopCart cart) {
        ShopCart updated = shopCartService.updateQuantity(cart.getUserId(), cart.getGoodsId(), cart.getQuantity());
        return Result.success("更新成功", updated);
    }

    @DeleteMapping
    public Result<Void> remove(@RequestParam("userId") Long userId, @RequestParam("goodsId") Long goodsId) {
        shopCartService.removeFromCart(userId, goodsId);
        return Result.success("删除成功");
    }

    @DeleteMapping("/user/{userId}")
    public Result<Void> clear(@PathVariable("userId") Long userId) {
        shopCartService.remove(new LambdaQueryWrapper<ShopCart>()
                .eq(ShopCart::getUserId, userId));
        return Result.success("购物车已清空");
    }

    @PutMapping("/select")
    public Result<Void> updateSelected(@RequestBody ShopCart cart) {
        ShopCart existing = shopCartService.getOne(new LambdaQueryWrapper<ShopCart>()
                .eq(ShopCart::getUserId, cart.getUserId())
                .eq(ShopCart::getGoodsId, cart.getGoodsId()));
        if (existing != null) {
            existing.setSelected(cart.getSelected());
            shopCartService.updateById(existing);
        }
        return Result.success("更新成功");
    }
}