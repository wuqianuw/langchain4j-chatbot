package com.java1234.omnichat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 登录日志实体类
 *
 * @author Java1234
 */
@Data
@TableName("t_login_log")
public class LoginLog {

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 登录IP */
    private String ip;

    /** 登录时间 */
    private LocalDateTime loginTime;
}
