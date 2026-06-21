package com.java1234.omnichat.service;

import com.java1234.omnichat.dto.StatOverview;

/**
 * 统计服务接口
 *
 * @author Java1234
 */
public interface StatService {

    /**
     * 获取管理后台统计数据概览
     *
     * @return 统计数据
     */
    StatOverview getOverview();
}
