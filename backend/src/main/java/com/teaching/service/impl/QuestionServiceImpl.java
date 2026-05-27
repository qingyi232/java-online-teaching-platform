package com.teaching.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teaching.common.BusinessException;
import com.teaching.common.Constants;
import com.teaching.entity.QaReply;
import com.teaching.entity.QuestionAnswer;
import com.teaching.mapper.QaReplyMapper;
import com.teaching.mapper.QuestionAnswerMapper;
import com.teaching.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionAnswerMapper questionMapper;

    @Autowired
    private QaReplyMapper replyMapper;

    @Override
    public IPage<QuestionAnswer> getQuestionPage(int page, int size, String title, Integer status, Long userId) {
        return questionMapper.selectQuestionPage(new Page<>(page, size), title, status, userId);
    }

    @Override
    public QuestionAnswer getQuestionDetail(Long id) {
        QuestionAnswer question = questionMapper.selectQuestionDetail(id);
        if (question == null) {
            throw new BusinessException("问题不存在");
        }
        return question;
    }

    @Override
    public void addQuestion(QuestionAnswer question) {
        question.setStatus(Constants.QA_UNSOLVED);
        questionMapper.insert(question);
    }

    @Override
    public void updateQuestion(QuestionAnswer question) {
        questionMapper.updateById(question);
    }

    @Override
    public void deleteQuestion(Long id) {
        questionMapper.deleteById(id);
    }

    @Override
    public void solveQuestion(Long id) {
        QuestionAnswer question = questionMapper.selectById(id);
        if (question == null) {
            throw new BusinessException("问题不存在");
        }
        question.setStatus(Constants.QA_SOLVED);
        questionMapper.updateById(question);
    }

    @Override
    public List<QaReply> getReplies(Long questionId) {
        return replyMapper.selectRepliesByQuestionId(questionId);
    }

    @Override
    public void addReply(QaReply reply) {
        replyMapper.insert(reply);
    }

    @Override
    public void deleteReply(Long id, Long userId, String role) {
        QaReply reply = replyMapper.selectById(id);
        if (reply == null) {
            throw new BusinessException("回复不存在");
        }
        if (!Constants.ROLE_ADMIN.equals(role) && !Constants.ROLE_TEACHER.equals(role) && !reply.getUserId().equals(userId)) {
            throw new BusinessException("无权删除该回复");
        }
        replyMapper.deleteById(id);
    }
}
