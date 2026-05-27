package com.teaching.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("resource_comment")
public class ResourceComment {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long resourceId;

    private Long userId;

    private String content;

    private Long parentId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String userAvatar;

    @TableField(exist = false)
    private List<ResourceComment> children;
}
