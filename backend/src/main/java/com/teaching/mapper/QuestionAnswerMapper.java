package com.teaching.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.teaching.entity.QuestionAnswer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface QuestionAnswerMapper extends BaseMapper<QuestionAnswer> {

    IPage<QuestionAnswer> selectQuestionPage(Page<QuestionAnswer> page,
                                              @Param("title") String title,
                                              @Param("status") Integer status,
                                              @Param("userId") Long userId);

    QuestionAnswer selectQuestionDetail(@Param("id") Long id);
}
