package com.petlife.social.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petlife.social.mapper.CommunityCommentMapper;
import com.petlife.social.service.CommunityCommentService;
import com.petlife.common.entity.CommunityComment;
import org.springframework.stereotype.Service;

@Service
public class CommunityCommentServiceImpl extends ServiceImpl<CommunityCommentMapper, CommunityComment> implements CommunityCommentService {

    @Override
    public CommunityComment likeComment(Long commentId) {
        CommunityComment comment = baseMapper.selectById(commentId);
        comment.setLikes(comment.getLikes() + 1);
        baseMapper.updateById(comment);
        return comment;
    }
}