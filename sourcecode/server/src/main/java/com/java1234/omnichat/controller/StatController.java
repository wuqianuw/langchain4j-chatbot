package com.java1234.omnichat.controller;

import com.java1234.omnichat.common.RequireLogin;
import com.java1234.omnichat.common.RequireRole;
import com.java1234.omnichat.common.Result;
import com.java1234.omnichat.dto.StatOverview;
import com.java1234.omnichat.service.StatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统计控制器(管理员后台)
 *
 * @author Java1234
 */
@RestController
@RequestMapping("/stat")
@RequireLogin
@RequireRole("ADMIN")
@RequiredArgsConstructor
public class StatController {

    private final StatService statService;

    /**
     * 获取管理后台统计数据概览
     *
     * @return 统计数据(含图表数据)
     */
    @GetMapping("/overview")
    public Result<StatOverview> overview() {
        return Result.success(statService.getOverview());
    }
}
