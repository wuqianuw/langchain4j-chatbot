package com.java1234.omnichat.dto;

import lombok.Data;

/**
 * 登录响应DTO
 *
 * @author Java1234
 */
@Data
public class LoginResponse {

    /** JWT令牌 */
    private String token;

    /** 用户ID */
    private Long userId;

    /** 用户名 */
    private String username;

    /** 昵称 */
    private String nickname;

    /** 角色 */
    private String role;

    /** 头像 */
    private String avatar;
}
