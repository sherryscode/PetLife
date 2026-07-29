package com.petlife.message.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petlife.message.entity.Message;
import com.petlife.message.service.MessageService;
import com.petlife.common.response.PageResult;
import com.petlife.common.response.Result;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;
    private final RedisTemplate<String, Object> redisTemplate;

    public MessageController(MessageService messageService, RedisTemplate<String, Object> redisTemplate) {
        this.messageService = messageService;
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/{id}")
    public Result<Message> getById(@PathVariable("id") Long id) {
        Message message = messageService.getById(id);
        if (message != null && message.getReadStatus() == 0) {
            message.setReadStatus(1);
            messageService.updateById(message);
            redisTemplate.opsForValue().decrement("unread_count:" + message.getUserId());
        }
        return Result.success(message);
    }

    @GetMapping("/user/{userId}")
    public Result<PageResult<Message>> getByUserId(
            @PathVariable("userId") Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Message::getUserId, userId);
        wrapper.orderByDesc(Message::getCreatedAt);

        Page<Message> pageResult = messageService.page(new Page<>(page, size), wrapper);
        return Result.success(PageResult.of(pageResult.getRecords(), pageResult.getTotal(), page, size));
    }

    @GetMapping("/unread/{userId}")
    public Result<Map<String, Object>> getUnreadCount(@PathVariable("userId") Long userId) {
        Object count = redisTemplate.opsForValue().get("unread_count:" + userId);
        Map<String, Object> result = new HashMap<>();
        result.put("unreadCount", count != null ? count : 0);
        return Result.success(result);
    }

    @PutMapping("/read/{userId}")
    public Result<Void> markAllRead(@PathVariable("userId") Long userId) {
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Message::getUserId, userId);
        wrapper.eq(Message::getReadStatus, 0);

        messageService.list(wrapper).forEach(message -> {
            message.setReadStatus(1);
            messageService.updateById(message);
        });

        redisTemplate.delete("unread_count:" + userId);
        return Result.success("已全部标记为已读");
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable("id") Long id) {
        messageService.removeById(id);
        return Result.success("删除成功");
    }

    @DeleteMapping("/user/{userId}")
    public Result<Void> deleteByUserId(@PathVariable("userId") Long userId) {
        messageService.remove(new LambdaQueryWrapper<Message>().eq(Message::getUserId, userId));
        redisTemplate.delete("unread_count:" + userId);
        return Result.success("已清空消息");
    }
}