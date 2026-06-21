package com.java1234.omnichat.controller;

import cn.hutool.core.io.FileUtil;
import com.java1234.omnichat.common.RequireLogin;
import com.java1234.omnichat.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件上传控制器
 * 统一上传到 D:/uploads7 目录
 *
 * @author Java1234
 */
@Slf4j
@RestController
@RequestMapping("/file")
@RequireLogin
public class FileController {

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${upload.url-prefix}")
    private String urlPrefix;

    /**
     * 上传文件(图片/音频/视频)
     *
     * @param file 上传的文件
     * @return 文件访问URL
     */
    @PostMapping("/upload")
    public Result<Map<String, String>> upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return Result.error("文件不能为空");
        }

        // 按日期分目录存储
        String dateDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String originalName = file.getOriginalFilename();
        String ext = originalName != null && originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf("."))
                : "";
        String newFileName = UUID.randomUUID().toString().replace("-", "") + ext;

        String saveDir = uploadPath + File.separator + dateDir;
        FileUtil.mkdir(saveDir);

        File destFile = new File(saveDir, newFileName);
        file.transferTo(destFile);

        String url = urlPrefix + "/" + dateDir.replace("\\", "/") + "/" + newFileName;
        String contentType = detectContentType(ext);

        Map<String, String> data = new HashMap<>();
        data.put("url", url);
        data.put("fileName", newFileName);
        data.put("contentType", contentType);

        log.info("文件上传成功: {}", url);
        return Result.success(data);
    }

    /**
     * 根据文件扩展名检测内容类型
     */
    private String detectContentType(String ext) {
        String lower = ext.toLowerCase();
        if (lower.matches("\\.(jpg|jpeg|png|gif|webp|bmp)")) return "image";
        if (lower.matches("\\.(mp3|wav|ogg|aac|flac)")) return "audio";
        if (lower.matches("\\.(mp4|avi|mov|webm|mkv)")) return "video";
        return "text";
    }
}
