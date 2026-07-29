package com.petlife.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("post_like")
public class PostLike extends BaseEntity {

    private Long userId;

    private Long postId;

    private Integer status;
}