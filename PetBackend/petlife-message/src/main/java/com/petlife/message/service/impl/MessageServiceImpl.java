package com.petlife.message.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petlife.message.entity.Message;
import com.petlife.message.mapper.MessageMapper;
import com.petlife.message.service.MessageService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    private final RedisTemplate<String, Object> redisTemplate;

    public MessageServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void sendMessage(Long userId, String title, String content, Integer messageType, String url) {
        Message message = new Message();
        message.setUserId(userId);
        message.setTitle(title);
        message.setContent(content);
        message.setMessageType(messageType);
        message.setReadStatus(0);
        message.setUrl(url);

        baseMapper.insert(message);

        redisTemplate.opsForList().leftPush("message:" + userId, message);
        redisTemplate.opsForValue().increment("unread_count:" + userId);
    }

    @Override
    public void sendBatchMessage(Long[] userIds, String title, String content, Integer messageType, String url) {
        for (Long userId : userIds) {
            sendMessage(userId, title, content, messageType, url);
        }
    }
}