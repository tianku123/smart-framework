package org.smart4j.framework.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串工具类
 */
public final class StringUtil {

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

  public static String[] splitString(String body, String s) {
    if (StringUtil.isNotEmpty(body) && StringUtil.isNotEmpty(s)) {
      return body.split(s);
    }
    return null;
  }
}
