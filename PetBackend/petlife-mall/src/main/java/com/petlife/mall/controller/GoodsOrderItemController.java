package com.petlife.mall.controller;

import com.petlife.mall.service.GoodsOrderItemService;
import com.petlife.common.entity.GoodsOrderItem;
import com.petlife.common.response.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order/item")
public class GoodsOrderItemController {

    private final GoodsOrderItemService goodsOrderItemService;

    public GoodsOrderItemController(GoodsOrderItemService goodsOrderItemService) {
        this.goodsOrderItemService = goodsOrderItemService;
    }

    @PostMapping
    public Result<GoodsOrderItem> create(@RequestBody GoodsOrderItem item) {
        goodsOrderItemService.save(item);
        return Result.success("创建成功", item);
    }
}