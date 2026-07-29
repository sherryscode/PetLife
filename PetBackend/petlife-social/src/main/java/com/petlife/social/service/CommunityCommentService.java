package com.petlife.social.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.petlife.common.entity.CommunityComment;

public interface CommunityCommentService extends IService<CommunityComment> {

    CommunityComment likeComment(Long commentId);
}