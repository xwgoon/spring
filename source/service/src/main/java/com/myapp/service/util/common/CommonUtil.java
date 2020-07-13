package com.myapp.service.util.common;

import org.apache.commons.lang.StringUtils;

public class CommonUtil {

    public static void fail(String msg) {
        throw new RuntimeException(msg);
    }

    public static void checkNotEmpty(String s, String key) {
        if (StringUtils.isBlank(s)) {
            fail(key + "不能为空");
        }
    }

}
