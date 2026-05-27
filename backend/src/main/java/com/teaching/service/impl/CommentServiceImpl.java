package com.teaching.service.impl;

import com.teaching.common.BusinessException;
import com.teaching.common.Constants;
import com.teaching.entity.ResourceComment;
import com.teaching.mapper.ResourceCommentMapper;
import com.teaching.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private ResourceCommentMapper commentMapper;

    @Override
    public List<ResourceComment> getCommentsByResourceId(Long resourceId) {
        List<ResourceComment> allComments = commentMapper.selectCommentsByResourceId(resourceId);
        List<ResourceComment> rootComments = allComments.stream()
                .filter(c -> c.getParentId() == null || c.getParentId() == 0)
                .collect(Collectors.toList());
        Map<Long, List<ResourceComment>> childrenMap = allComments.stream()
                .filter(c -> c.getParentId() != null && c.getParentId() != 0)
                .collect(Collectors.groupingBy(ResourceComment::getParentId));
        rootComments.forEach(root -> root.setChildren(childrenMap.getOrDefault(root.getId(), new ArrayList<>())));
        return rootComments;
    }

    @Override
    public void addComment(ResourceComment comment) {
        commentMapper.insert(comment);
    }

    @Override
    public void deleteComment(Long id, Long userId, String role) {
        ResourceComment comment = commentMapper.selectById(id);
        if (comment == null) {
            throw new BusinessException("评论不存在");
        }
        if (!Constants.ROLE_ADMIN.equals(role) && !comment.getUserId().equals(userId)) {
            throw new BusinessException("无权删除该评论");
        }
        commentMapper.deleteById(id);
    }
}
