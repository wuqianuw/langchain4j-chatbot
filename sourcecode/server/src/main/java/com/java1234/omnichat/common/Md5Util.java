package com.java1234.omnichat.common;

import cn.hutool.crypto.digest.DigestUtil;

/**
 * MD5加密工具类
 *
 * @author Java1234
 */
public class Md5Util {

    private Md5Util() {
    }

    /**
     * 对字符串进行MD5加密
     *
     * @param text 原始文本
     * @return MD5加密后的字符串
     */
    public static String encrypt(String text) {
        return DigestUtil.md5Hex(text);
    }

    /**
     * 校验密码是否匹配
     *
     * @param rawPassword     原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return encrypt(rawPassword).equals(encodedPassword);
    }
}
