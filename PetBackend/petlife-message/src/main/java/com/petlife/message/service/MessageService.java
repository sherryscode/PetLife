package com.petlife.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.petlife.message.entity.Message;

public interface MessageService extends IService<Message> {

    void sendMessage(Long userId, String title, String content, Integer messageType, String url);

    void sendBatchMessage(Long[] userIds, String title, String content, Integer messageType, String url);
}