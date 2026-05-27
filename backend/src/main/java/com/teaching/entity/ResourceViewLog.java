package com.teaching.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("resource_view_log")
public class ResourceViewLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long resourceId;

    private Long userId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
