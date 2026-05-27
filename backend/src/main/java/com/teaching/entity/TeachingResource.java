package com.teaching.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("teaching_resource")
public class TeachingResource {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String description;

    private Long categoryId;

    private String fileUrl;

    private String fileName;

    private Long fileSize;

    private String fileType;

    private String coverUrl;

    private Long uploadUserId;

    private Integer status;

    private Integer downloadCount;

    private Integer viewCount;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String uploadUserName;

    @TableField(exist = false)
    private String categoryName;
}
