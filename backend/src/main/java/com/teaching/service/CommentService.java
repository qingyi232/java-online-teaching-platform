package com.teaching.service;

import com.teaching.entity.ResourceComment;

import java.util.List;

public interface CommentService {

    List<ResourceComment> getCommentsByResourceId(Long resourceId);

    void addComment(ResourceComment comment);

    void deleteComment(Long id, Long userId, String role);
}
