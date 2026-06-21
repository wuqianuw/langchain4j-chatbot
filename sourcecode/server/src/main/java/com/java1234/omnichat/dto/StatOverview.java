package com.java1234.omnichat.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 统计数据概览DTO
 *
 * @author Java1234
 */
@Data
public class StatOverview {

    /** 用户总数 */
    private Long userCount;

    /** 会话总数 */
    private Long conversationCount;

    /** 消息总数 */
    private Long messageCount;

    /** 今日消息数 */
    private Long todayMessageCount;

    /** 近7日消息趋势 */
    private List<Map<String, Object>> messageTrend;

    /** 消息模态占比 */
    private List<Map<String, Object>> modalityStats;

    /** 用户角色占比 */
    private List<Map<String, Object>> roleStats;

    /** 活跃用户排行 */
    private List<Map<String, Object>> activeUsers;
}
