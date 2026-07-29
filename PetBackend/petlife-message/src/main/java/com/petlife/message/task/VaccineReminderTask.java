package com.petlife.message.task;

import com.petlife.message.service.MessageService;
import com.petlife.common.response.Result;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Component
public class VaccineReminderTask {

    private final RedisTemplate<String, Object> redisTemplate;
    private final MessageService messageService;
    private final RestTemplate restTemplate;

    public VaccineReminderTask(RedisTemplate<String, Object> redisTemplate, 
                              MessageService messageService,
                              RestTemplate restTemplate) {
        this.redisTemplate = redisTemplate;
        this.messageService = messageService;
        this.restTemplate = restTemplate;
    }

    @Scheduled(cron = "0 0 9 * * ?")
    public void checkVaccineExpiration() {
        try {
            Result<?> result = restTemplate.getForObject(
                    "http://petlife-health/vaccine/listAll?page=1&size=1000", 
                    Result.class);
            
            if (result != null && result.getCode() == 200) {
                Map<String, Object> data = (Map<String, Object>) result.getData();
                List<Map<String, Object>> vaccines = (List<Map<String, Object>>) data.get("records");
                
                for (Map<String, Object> vaccine : vaccines) {
                    String validUntilStr = (String) vaccine.get("validUntil");
                    if (validUntilStr != null) {
                        LocalDateTime validUntil = LocalDateTime.parse(validUntilStr);
                        long daysUntilExpiration = ChronoUnit.DAYS.between(LocalDateTime.now(), validUntil);
                        
                        if (daysUntilExpiration >= 0 && daysUntilExpiration <= 7) {
                            Long userId = ((Number) vaccine.get("userId")).longValue();
                            String vaccineName = (String) vaccine.get("vaccineName");
                            
                            String title = "疫苗到期提醒";
                            String content = String.format("您的宠物疫苗 %s 将在 %d 天后到期，请及时接种",
                                    vaccineName, daysUntilExpiration);
                            
                            messageService.sendMessage(userId, title, content, 1, "/pet/vaccine");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 0 9 * * ?")
    public void checkReserveOrders() {
        try {
            Result<?> result = restTemplate.getForObject(
                    "http://petlife-order/order/list?page=1&size=1000", 
                    Result.class);
            
            if (result != null && result.getCode() == 200) {
                Map<String, Object> data = (Map<String, Object>) result.getData();
                List<Map<String, Object>> orders = (List<Map<String, Object>>) data.get("records");
                
                for (Map<String, Object> order : orders) {
                    Integer status = ((Number) order.get("orderStatus")).intValue();
                    if (status == 1) {
                        String orderTimeStr = (String) order.get("orderTime");
                        if (orderTimeStr != null) {
                            LocalDateTime orderTime = LocalDateTime.parse(orderTimeStr);
                            long hoursUntilOrder = ChronoUnit.HOURS.between(LocalDateTime.now(), orderTime);
                            
                            if (hoursUntilOrder > 0 && hoursUntilOrder <= 24) {
                                Long userId = ((Number) order.get("userId")).longValue();
                                
                                String title = "预约服务提醒";
                                String content = String.format("您的预约订单将在 %d 小时后开始，请按时前往门店",
                                        hoursUntilOrder);
                                
                                messageService.sendMessage(userId, title, content, 2, "/order");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}