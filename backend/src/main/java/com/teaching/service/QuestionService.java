package com.teaching.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.teaching.entity.QaReply;
import com.teaching.entity.QuestionAnswer;

import java.util.List;

public interface QuestionService {

    IPage<QuestionAnswer> getQuestionPage(int page, int size, String title, Integer status, Long userId);

    QuestionAnswer getQuestionDetail(Long id);

    void addQuestion(QuestionAnswer question);

    void updateQuestion(QuestionAnswer question);

    void deleteQuestion(Long id);

    void solveQuestion(Long id);

    List<QaReply> getReplies(Long questionId);

    void addReply(QaReply reply);

    void deleteReply(Long id, Long userId, String role);
}
