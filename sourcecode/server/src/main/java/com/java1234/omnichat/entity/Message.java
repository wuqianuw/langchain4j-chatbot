package com.java1234.omnichat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 消息实体类
 *
 * @author Java1234
 */
@Data
@TableName("t_message")
public class Message {

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 会话ID */
    private Long conversationId;

    /** 用户ID */
    private Long userId;

    /** 角色: user/assistant */
    private String role;

    /** 内容类型: text/image/audio/video */
    private String contentType;

    /** 文本内容 */
    private String content;

    /** 附件URL */
    private String fileUrl;

    /** 消耗Token数 */
    private Integer tokens;

    /** 创建时间 */
    private LocalDateTime createTime;
}
