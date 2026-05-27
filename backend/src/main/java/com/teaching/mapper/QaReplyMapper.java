package com.teaching.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.teaching.entity.QaReply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QaReplyMapper extends BaseMapper<QaReply> {

    List<QaReply> selectRepliesByQuestionId(@Param("questionId") Long questionId);
}
