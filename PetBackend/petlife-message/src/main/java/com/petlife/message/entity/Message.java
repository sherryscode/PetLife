package com.petlife.message.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.petlife.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("message")
public class Message extends BaseEntity {

    private Long userId;

    private String title;

    private String content;

    private Integer messageType;

    private Integer readStatus;

    private String url;
}