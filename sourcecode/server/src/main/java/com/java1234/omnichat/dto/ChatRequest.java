package com.java1234.omnichat.dto;

import lombok.Data;

/**
 * 聊天请求DTO
 *
 * @author Java1234
 */
@Data
public class ChatRequest {

    /** 会话ID(新建会话时可为空) */
    private Long conversationId;

    /** 文本内容 */
    private String content;

    /** 内容类型: text/image/audio/video */
    private String contentType = "text";

    /** 附件URL(图片/音频/视频) */
    private String fileUrl;
}
