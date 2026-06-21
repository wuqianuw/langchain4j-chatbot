package com.java1234.omnichat.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java1234.omnichat.dto.StatOverview;
import com.java1234.omnichat.entity.Conversation;
import com.java1234.omnichat.entity.Message;
import com.java1234.omnichat.entity.User;
import com.java1234.omnichat.mapper.ConversationMapper;
import com.java1234.omnichat.mapper.MessageMapper;
import com.java1234.omnichat.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计服务实现类
 *
 * @author Java1234
 */
@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {

    private final UserMapper userMapper;
    private final ConversationMapper conversationMapper;
    private final MessageMapper messageMapper;

    /**
     * 获取管理后台统计数据概览
     *
     * @return 统计数据
     */
    @Override
    public StatOverview getOverview() {
        StatOverview overview = new StatOverview();
        overview.setUserCount(userMapper.selectCount(null));
        overview.setConversationCount(conversationMapper.selectCount(null));
        overview.setMessageCount(messageMapper.selectCount(null));

        // 今日消息数
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        overview.setTodayMessageCount(messageMapper.selectCount(
                new QueryWrapper<Message>().ge("create_time", todayStart)));

        overview.setMessageTrend(getMessageTrend());
        overview.setModalityStats(getModalityStats());
        overview.setRoleStats(getRoleStats());
        overview.setActiveUsers(getActiveUsers());

        return overview;
    }

    /**
     * 获取近7日消息趋势
     */
    private List<Map<String, Object>> getMessageTrend() {
        List<Map<String, Object>> trend = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.plusDays(1).atStartOfDay();
            Long count = messageMapper.selectCount(
                    new QueryWrapper<Message>().ge("create_time", start).lt("create_time", end));
            Map<String, Object> item = new HashMap<>();
            item.put("date", date.format(formatter));
            item.put("count", count);
            trend.add(item);
        }
        return trend;
    }

    /**
     * 获取消息模态占比统计
     */
    private List<Map<String, Object>> getModalityStats() {
        List<Map<String, Object>> stats = new ArrayList<>();
        String[] types = {"text", "image", "audio", "video"};
        String[] names = {"文本", "图片", "音频", "视频"};
        for (int i = 0; i < types.length; i++) {
            Long count = messageMapper.selectCount(
                    new QueryWrapper<Message>().eq("content_type", types[i]));
            Map<String, Object> item = new HashMap<>();
            item.put("name", names[i]);
            item.put("value", count);
            stats.add(item);
        }
        return stats;
    }

    /**
     * 获取用户角色占比统计
     */
    private List<Map<String, Object>> getRoleStats() {
        List<Map<String, Object>> stats = new ArrayList<>();
        Long adminCount = userMapper.selectCount(new QueryWrapper<User>().eq("role", "ADMIN"));
        Long userCount = userMapper.selectCount(new QueryWrapper<User>().eq("role", "USER"));
        Map<String, Object> admin = new HashMap<>();
        admin.put("name", "管理员");
        admin.put("value", adminCount);
        stats.add(admin);
        Map<String, Object> user = new HashMap<>();
        user.put("name", "普通用户");
        user.put("value", userCount);
        stats.add(user);
        return stats;
    }

    /**
     * 获取活跃用户排行(按消息数)
     */
    private List<Map<String, Object>> getActiveUsers() {
        List<Map<String, Object>> result = new ArrayList<>();
        List<User> users = userMapper.selectList(new QueryWrapper<User>().eq("role", "USER").last("LIMIT 10"));
        for (User user : users) {
            Long msgCount = messageMapper.selectCount(
                    new QueryWrapper<Message>().eq("user_id", user.getId()));
            Map<String, Object> item = new HashMap<>();
            String displayName = user.getNickname();
            if (displayName == null || displayName.isBlank()) {
                displayName = user.getUsername();
            }
            item.put("username", displayName);
            item.put("messageCount", msgCount);
            result.add(item);
        }
        result.sort((a, b) -> Long.compare((Long) b.get("messageCount"), (Long) a.get("messageCount")));
        return result;
    }
}
