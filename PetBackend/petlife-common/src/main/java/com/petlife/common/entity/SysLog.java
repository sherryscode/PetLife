package com.petlife.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_log")
public class SysLog extends BaseEntity {

    private Long userId;

    private String userName;

    private String operation;

    private String module;

    private String description;

    private String url;

    private String method;

    private String params;

    private String result;

    private String ip;

    private String userAgent;

    private Long executionTime;

    private Integer status;

    private String errorMsg;
}