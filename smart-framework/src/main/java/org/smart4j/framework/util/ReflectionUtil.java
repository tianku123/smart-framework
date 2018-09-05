package org.smart4j.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 使用ClassHelper类可以获取所加载的类，但无法通过类来实例化对象。因此需要
 * 提供一个反射工具类，让它封装Java反射相关的API，对外提供更好用的工具方法。
 *
 * 反射工具类
 * @author rh
 * @since 1.0.0
 */
public final class ReflectionUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);

  /**
   * 创建实例
   */
  public static Object newInstance(Class<?> cls) {
    Object instance;
    try {
      instance = cls.newInstance();
    } catch (Exception e) {
      LOGGER.error("new instance failure", e);
      throw new RuntimeException(e);
    }
    return instance;
  }

  /**
   * 调用方法
   */
  public static Object invokeMethod(Object obj, Method method, Object...args) {
    Object result;
    try {
      method.setAccessible(true);
      result = method.invoke(obj, args);
    } catch (Exception e) {
      LOGGER.error("invoke method failure", e);
      throw new RuntimeException(e);
    }
    return result;
  }

  /**
   * 设置成员变量的值
   */
  public static void setField (Object obj, Field field, Object value) {
    try {
      field.setAccessible(true);
      field.set(obj, value);
    } catch (Exception e) {
      LOGGER.error("set field failure", e);
      throw new RuntimeException(e);
    }
  }
}
