package com.teaching.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("question_answer")
public class QuestionAnswer {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String content;

    private Long userId;

    private Long resourceId;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String resourceTitle;

    @TableField(exist = false)
    private Integer replyCount;
}
