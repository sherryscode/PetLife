package com.petlife.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.petlife.auth.mapper.*;
import com.petlife.common.entity.GoodsOrder;
import com.petlife.common.entity.Merchant;
import com.petlife.common.entity.ReserveOrder;
import com.petlife.common.entity.User;
import com.petlife.common.response.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    private final UserMapper userMapper;
    private final MerchantMapper merchantMapper;
    private final GoodsOrderMapper goodsOrderMapper;
    private final ReserveOrderMapper reserveOrderMapper;

    public StatisticsController(UserMapper userMapper, MerchantMapper merchantMapper,
                                GoodsOrderMapper goodsOrderMapper, ReserveOrderMapper reserveOrderMapper) {
        this.userMapper = userMapper;
        this.merchantMapper = merchantMapper;
        this.goodsOrderMapper = goodsOrderMapper;
        this.reserveOrderMapper = reserveOrderMapper;
    }

    @GetMapping("/overview")
    public Result<Map<String, Object>> overview() {
        Map<String, Object> result = new HashMap<>();
        
        long userCount = userMapper.selectCount(new LambdaQueryWrapper<User>());
        long merchantCount = merchantMapper.selectCount(new LambdaQueryWrapper<Merchant>());
        long goodsOrderCount = goodsOrderMapper.selectCount(new LambdaQueryWrapper<GoodsOrder>());
        long reserveOrderCount = reserveOrderMapper.selectCount(new LambdaQueryWrapper<ReserveOrder>());
        
        result.put("userCount", userCount);
        result.put("merchantCount", merchantCount);
        result.put("totalOrderCount", goodsOrderCount + reserveOrderCount);
        
        BigDecimal totalRevenue = BigDecimal.ZERO;
        List<GoodsOrder> orders = goodsOrderMapper.selectList(new LambdaQueryWrapper<GoodsOrder>());
        for (GoodsOrder order : orders) {
            if (order.getTotalAmount() != null) {
                totalRevenue = totalRevenue.add(order.getTotalAmount());
            }
        }
        result.put("totalRevenue", totalRevenue.doubleValue());
        
        return Result.success(result);
    }

    @GetMapping("/userGrowth")
    public Result<List<Map<String, Object>>> userGrowth(@RequestParam(defaultValue = "7") Integer days) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        Map<String, Long> dateCountMap = new LinkedHashMap<>();
        LocalDate today = LocalDate.now();
        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            String dateStr = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
            dateCountMap.put(dateStr, 0L);
        }
        
        List<User> users = userMapper.selectList(new LambdaQueryWrapper<User>());
        for (User user : users) {
            if (user.getCreatedAt() != null) {
                String dateStr = user.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE);
                if (dateCountMap.containsKey(dateStr)) {
                    dateCountMap.put(dateStr, dateCountMap.get(dateStr) + 1);
                }
            }
        }
        
        for (Map.Entry<String, Long> entry : dateCountMap.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("date", entry.getKey());
            item.put("count", entry.getValue());
            result.add(item);
        }
        
        return Result.success(result);
    }

    @GetMapping("/orderStatistics")
    public Result<Map<String, Object>> orderStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        long pending = 0, paid = 0, shipped = 0, completed = 0, cancelled = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        List<GoodsOrder> orders = goodsOrderMapper.selectList(new LambdaQueryWrapper<GoodsOrder>());
        for (GoodsOrder order : orders) {
            Integer orderStatus = order.getOrderStatus();
            Integer payStatus = order.getPayStatus();
            
            if (order.getTotalAmount() != null) {
                totalAmount = totalAmount.add(order.getTotalAmount());
            }
            
            if (orderStatus != null) {
                switch (orderStatus) {
                    case 0 -> pending++;
                    case 1 -> paid++;
                    case 2 -> shipped++;
                    case 3 -> completed++;
                    case 4 -> cancelled++;
                }
            } else if (payStatus != null) {
                if (payStatus == 0) pending++;
                else paid++;
            }
        }
        
        result.put("pending", pending);
        result.put("paid", paid);
        result.put("shipped", shipped);
        result.put("completed", completed);
        result.put("cancelled", cancelled);
        result.put("totalAmount", totalAmount.doubleValue());
        
        return Result.success(result);
    }

    @GetMapping("/orderTrend")
    public Result<List<Map<String, Object>>> orderTrend(@RequestParam(defaultValue = "7") Integer days) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        Map<String, Long> dateCountMap = new LinkedHashMap<>();
        Map<String, BigDecimal> dateAmountMap = new LinkedHashMap<>();
        
        LocalDate today = LocalDate.now();
        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            String dateStr = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
            dateCountMap.put(dateStr, 0L);
            dateAmountMap.put(dateStr, BigDecimal.ZERO);
        }
        
        List<Map<String, Object>> trendData = goodsOrderMapper.selectOrderTrend(days);
        for (Map<String, Object> row : trendData) {
            String orderDate = (String) row.get("order_date");
            if (dateCountMap.containsKey(orderDate)) {
                dateCountMap.put(orderDate, ((Number) row.get("order_count")).longValue());
                dateAmountMap.put(orderDate, BigDecimal.valueOf(((Number) row.get("total_amount")).doubleValue()));
            }
        }
        
        for (Map.Entry<String, Long> entry : dateCountMap.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("date", entry.getKey());
            item.put("count", entry.getValue());
            item.put("amount", dateAmountMap.get(entry.getKey()).doubleValue());
            result.add(item);
        }
        
        return Result.success(result);
    }

    @GetMapping("/merchant/overview")
    public Result<Map<String, Object>> merchantOverview(@RequestParam("merchantId") Long merchantId) {
        Map<String, Object> result = new HashMap<>();
        
        List<GoodsOrder> todayOrders = goodsOrderMapper.selectTodayByMerchantId(merchantId);
        
        long todayOrderCount = todayOrders.size();
        BigDecimal todayRevenue = BigDecimal.ZERO;
        for (GoodsOrder order : todayOrders) {
            if (order.getTotalAmount() != null) {
                todayRevenue = todayRevenue.add(order.getTotalAmount());
            }
        }
        
        result.put("todayOrderCount", todayOrderCount);
        result.put("todayRevenue", todayRevenue.doubleValue());
        
        return Result.success(result);
    }

    @GetMapping("/merchant/orderTrend")
    public Result<List<Map<String, Object>>> merchantOrderTrend(
            @RequestParam("merchantId") Long merchantId,
            @RequestParam(defaultValue = "7") Integer days) {
        
        List<Map<String, Object>> result = new ArrayList<>();
        
        Map<String, Long> dateCountMap = new LinkedHashMap<>();
        Map<String, BigDecimal> dateAmountMap = new LinkedHashMap<>();
        
        LocalDate today = LocalDate.now();
        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            String dateStr = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
            dateCountMap.put(dateStr, 0L);
            dateAmountMap.put(dateStr, BigDecimal.ZERO);
        }
        
        List<Map<String, Object>> trendData = goodsOrderMapper.selectOrderTrendByMerchantId(merchantId, days);
        for (Map<String, Object> row : trendData) {
            String orderDate = (String) row.get("order_date");
            if (dateCountMap.containsKey(orderDate)) {
                dateCountMap.put(orderDate, ((Number) row.get("order_count")).longValue());
                dateAmountMap.put(orderDate, BigDecimal.valueOf(((Number) row.get("total_amount")).doubleValue()));
            }
        }
        
        for (Map.Entry<String, Long> entry : dateCountMap.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("date", entry.getKey());
            item.put("count", entry.getValue());
            item.put("amount", dateAmountMap.get(entry.getKey()).doubleValue());
            result.add(item);
        }
        
        return Result.success(result);
    }
}
