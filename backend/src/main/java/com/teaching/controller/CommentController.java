package com.teaching.controller;

import com.teaching.common.Result;
import com.teaching.entity.ResourceComment;
import com.teaching.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/list/{resourceId}")
    public Result<List<ResourceComment>> getComments(@PathVariable Long resourceId) {
        return Result.success(commentService.getCommentsByResourceId(resourceId));
    }

    @PostMapping
    public Result<?> addComment(@RequestBody ResourceComment comment, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        comment.setUserId(userId);
        commentService.addComment(comment);
        return Result.success("评论成功");
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteComment(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");
        commentService.deleteComment(id, userId, role);
        return Result.success("删除成功");
    }
}
