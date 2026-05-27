package com.teaching.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.teaching.common.Result;
import com.teaching.entity.QaReply;
import com.teaching.entity.QuestionAnswer;
import com.teaching.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/list")
    public Result<IPage<QuestionAnswer>> getQuestionPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long userId) {
        return Result.success(questionService.getQuestionPage(page, size, title, status, userId));
    }

    @GetMapping("/detail/{id}")
    public Result<QuestionAnswer> getQuestionDetail(@PathVariable Long id) {
        return Result.success(questionService.getQuestionDetail(id));
    }

    @PostMapping
    public Result<?> addQuestion(@RequestBody QuestionAnswer question, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        question.setUserId(userId);
        questionService.addQuestion(question);
        return Result.success("提问成功");
    }

    @PutMapping
    public Result<?> updateQuestion(@RequestBody QuestionAnswer question) {
        questionService.updateQuestion(question);
        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return Result.success("删除成功");
    }

    @PutMapping("/solve/{id}")
    public Result<?> solveQuestion(@PathVariable Long id) {
        questionService.solveQuestion(id);
        return Result.success("已标记为已解决");
    }

    @GetMapping("/reply/list/{questionId}")
    public Result<List<QaReply>> getReplies(@PathVariable Long questionId) {
        return Result.success(questionService.getReplies(questionId));
    }

    @PostMapping("/reply")
    public Result<?> addReply(@RequestBody QaReply reply, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        reply.setUserId(userId);
        questionService.addReply(reply);
        return Result.success("回复成功");
    }

    @DeleteMapping("/reply/{id}")
    public Result<?> deleteReply(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");
        questionService.deleteReply(id, userId, role);
        return Result.success("删除成功");
    }
}
