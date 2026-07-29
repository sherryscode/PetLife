package com.petlife.mall.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petlife.mall.service.GoodsOrderService;
import com.petlife.mall.service.GoodsOrderItemService;
import com.petlife.mall.service.GoodsService;
import com.petlife.common.entity.Goods;
import com.petlife.common.entity.GoodsOrder;
import com.petlife.common.entity.GoodsOrderItem;
import com.petlife.common.response.PageResult;
import com.petlife.common.response.Result;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/order")
public class GoodsOrderController {

    private final GoodsOrderService goodsOrderService;
    private final GoodsOrderItemService goodsOrderItemService;
    private final GoodsService goodsService;

    public GoodsOrderController(GoodsOrderService goodsOrderService, GoodsOrderItemService goodsOrderItemService, GoodsService goodsService) {
        this.goodsOrderService = goodsOrderService;
        this.goodsOrderItemService = goodsOrderItemService;
        this.goodsService = goodsService;
    }

    @GetMapping("/{id}")
    public Result<GoodsOrder> getById(@PathVariable("id") Long id) {
        GoodsOrder order = goodsOrderService.getById(id);
        return Result.success(order);
    }

    @PostMapping
    public Result<GoodsOrder> create(@RequestBody GoodsOrder order) {
        GoodsOrder created = goodsOrderService.createOrder(order);
        return Result.success("创建成功", created);
    }

    @PutMapping("/{id}")
    public Result<GoodsOrder> update(@PathVariable("id") Long id, @RequestBody GoodsOrder order) {
        order.setId(id);
        goodsOrderService.updateById(order);
        return Result.success("更新成功", order);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable("id") Long id) {
        goodsOrderService.removeById(id);
        return Result.success("删除成功");
    }

    @GetMapping("/list")
    public Result<PageResult<GoodsOrder>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Integer orderStatus,
            @RequestParam(required = false) String orderNo) {

        LambdaQueryWrapper<GoodsOrder> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(GoodsOrder::getUserId, userId);
        }
        if (orderStatus != null) {
            wrapper.eq(GoodsOrder::getOrderStatus, orderStatus);
        }
        if (orderNo != null && !orderNo.isEmpty()) {
            wrapper.like(GoodsOrder::getOrderNo, orderNo);
        }
        wrapper.orderByDesc(GoodsOrder::getCreatedAt);

        Page<GoodsOrder> pageResult = goodsOrderService.page(new Page<>(page, size), wrapper);
        return Result.success(PageResult.of(pageResult.getRecords(), pageResult.getTotal(), page, size));
    }

    @PutMapping("/{id}/pay")
    public Result<GoodsOrder> pay(@PathVariable("id") Long id) {
        GoodsOrder order = goodsOrderService.getById(id);
        order.setPayStatus(1);
        order.setOrderStatus(1);
        goodsOrderService.updateById(order);
        return Result.success("支付成功", order);
    }

    @PutMapping("/{id}/ship")
    public Result<GoodsOrder> ship(@PathVariable("id") Long id) {
        GoodsOrder order = goodsOrderService.getById(id);
        order.setShipStatus(1);
        order.setOrderStatus(2);
        goodsOrderService.updateById(order);
        return Result.success("已发货", order);
    }

    @PutMapping("/{id}/confirm")
    public Result<GoodsOrder> confirm(@PathVariable("id") Long id) {
        GoodsOrder order = goodsOrderService.getById(id);
        order.setShipStatus(2);
        order.setOrderStatus(3);
        goodsOrderService.updateById(order);
        return Result.success("已确认收货", order);
    }

    @PutMapping("/{id}/cancel")
    public Result<GoodsOrder> cancel(@PathVariable("id") Long id) {
        GoodsOrder order = goodsOrderService.getById(id);
        order.setOrderStatus(4);
        goodsOrderService.updateById(order);
        return Result.success("订单已取消", order);
    }

    @GetMapping("/{id}/items")
    public Result<List<GoodsOrderItem>> getOrderItems(@PathVariable("id") Long id) {
        LambdaQueryWrapper<GoodsOrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GoodsOrderItem::getOrderId, id);
        List<GoodsOrderItem> items = goodsOrderItemService.list(wrapper);
        return Result.success(items);
    }

    @GetMapping("/merchant/list")
    public Result<PageResult<GoodsOrder>> listByMerchant(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam("merchantId") Long merchantId,
            @RequestParam(required = false) Integer orderStatus) {

        LambdaQueryWrapper<Goods> goodsWrapper = new LambdaQueryWrapper<>();
        goodsWrapper.eq(Goods::getMerchantId, merchantId);
        List<Goods> goodsList = goodsService.list(goodsWrapper);
        
        List<Long> goodsIds = new ArrayList<>();
        for (Goods goods : goodsList) {
            goodsIds.add(goods.getId());
        }
        
        if (goodsIds.isEmpty()) {
            return Result.success(PageResult.of(new ArrayList<>(), 0L, page, size));
        }
        
        LambdaQueryWrapper<GoodsOrderItem> itemWrapper = new LambdaQueryWrapper<>();
        itemWrapper.in(GoodsOrderItem::getGoodsId, goodsIds);
        List<GoodsOrderItem> items = goodsOrderItemService.list(itemWrapper);
        
        List<Long> orderIds = new ArrayList<>();
        for (GoodsOrderItem item : items) {
            orderIds.add(item.getOrderId());
        }
        
        if (orderIds.isEmpty()) {
            return Result.success(PageResult.of(new ArrayList<>(), 0L, page, size));
        }
        
        LambdaQueryWrapper<GoodsOrder> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.in(GoodsOrder::getId, orderIds);
        if (orderStatus != null) {
            orderWrapper.eq(GoodsOrder::getOrderStatus, orderStatus);
        }
        orderWrapper.orderByDesc(GoodsOrder::getCreatedAt);
        
        Page<GoodsOrder> pageResult = goodsOrderService.page(new Page<>(page, size), orderWrapper);
        return Result.success(PageResult.of(pageResult.getRecords(), pageResult.getTotal(), page, size));
    }
}