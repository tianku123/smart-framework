package org.smart4j.chapter2.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by admin on 2018/8/22.
 */
public class StringUtil {

    /**
     * 判断字符串是否为空，有效判断空字符串
     */
    public static boolean isEmpty(String str) {
        if (str != null) {
            str = str.trim();
        }
        return StringUtils.isEmpty(str);
    }

    /**
     * 判断字符串是否非空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

}
