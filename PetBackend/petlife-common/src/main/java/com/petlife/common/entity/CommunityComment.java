package com.petlife.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("community_comment")
public class CommunityComment extends BaseEntity {

    private Long postId;

    private Long userId;

    private Long parentId;

    private String content;

    private Integer likes;

    private Integer status;
}