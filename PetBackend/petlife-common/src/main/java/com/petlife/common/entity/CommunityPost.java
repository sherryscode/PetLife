package com.petlife.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("community_post")
public class CommunityPost extends BaseEntity {

    private Long userId;

    private String title;

    private String content;

    private String images;

    private Integer likes;

    private Integer comments;

    private Integer favorites;

    private Integer auditStatus;

    private Integer status;
}