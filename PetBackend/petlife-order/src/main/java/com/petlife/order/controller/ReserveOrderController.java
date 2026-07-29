package com.petlife.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petlife.order.service.ReserveOrderService;
import com.petlife.common.entity.ReserveOrder;
import com.petlife.common.response.PageResult;
import com.petlife.common.response.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class ReserveOrderController {

    private final ReserveOrderService reserveOrderService;

    public ReserveOrderController(ReserveOrderService reserveOrderService) {
        this.reserveOrderService = reserveOrderService;
    }

    @GetMapping("/{id}")
    public Result<ReserveOrder> getById(@PathVariable("id") Long id) {
        ReserveOrder order = reserveOrderService.getById(id);
        return Result.success(order);
    }

    @PostMapping
    public Result<ReserveOrder> create(@RequestBody ReserveOrder order) {
        ReserveOrder created = reserveOrderService.createOrder(order);
        return Result.success("创建成功", created);
    }

    @PutMapping("/{id}")
    public Result<ReserveOrder> update(@PathVariable("id") Long id, @RequestBody ReserveOrder order) {
        order.setId(id);
        reserveOrderService.updateById(order);
        return Result.success("更新成功", order);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable("id") Long id) {
        reserveOrderService.removeById(id);
        return Result.success("删除成功");
    }

    @GetMapping("/list")
    public Result<PageResult<ReserveOrder>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long storeId,
            @RequestParam(required = false) Integer orderStatus) {

        LambdaQueryWrapper<ReserveOrder> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(ReserveOrder::getUserId, userId);
        }
        if (storeId != null) {
            wrapper.eq(ReserveOrder::getStoreId, storeId);
        }
        if (orderStatus != null) {
            wrapper.eq(ReserveOrder::getOrderStatus, orderStatus);
        }

        Page<ReserveOrder> pageResult = reserveOrderService.page(new Page<>(page, size), wrapper);
        return Result.success(PageResult.of(pageResult.getRecords(), pageResult.getTotal(), page, size));
    }

    @PutMapping("/{id}/status")
    public Result<ReserveOrder> updateStatus(@PathVariable("id") Long id, @RequestBody Map<String, Integer> params) {
        ReserveOrder order = reserveOrderService.getById(id);
        order.setOrderStatus(params.get("orderStatus"));
        reserveOrderService.updateById(order);
        return Result.success("状态更新成功", order);
    }
}