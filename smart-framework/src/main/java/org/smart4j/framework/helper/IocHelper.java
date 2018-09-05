package org.smart4j.framework.helper;

import java.lang.reflect.Field;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;
import org.smart4j.framework.annotation.Inject;
import org.smart4j.framework.util.CollectionUtil;
import org.smart4j.framework.util.ReflectionUtil;

/**
 * 依赖注入助手类
 *
 * 在Controller层定义Service成员变量，通过Inject注解注入实例。
 * 不是开发者自己通过new的方式来实例化，而是通过框架自身来实例化，像这种实例化过程，成为Ioc（Inversion of Control，控制反转）。
 * 控制不是由开发者来决定的，而是反转给框架了。
 * 一般地，我们也将控制反转成为DI（Dependency Injection，依赖注入），可以理解为将某个类需要依赖的成员注入到这个类中。
 *
 * 实现思路：先通过 BeanHelper 获取所有Bean Map结构，记录了类与对象的映射关系。然后遍历这个映射关系，
 * 分别取出Bean类 与 Bean实例，进而通过反射获取类中所有的成员变量。继续遍历这些成员变量，在循环中判断当前成员变量
 * 是否带有Inject注解，如果带有注解，则从Bean Map中根据Bean类取出Bean实例。最有通过ReflectionUtil#setField方法来修改当前成员变量的值。
 * @author rh
 * @since 1.0.0
 */
public final class IocHelper {

  /**
   * Ioc 容器的初始化
   */
  static {
    // 获取所有的Bean类与Bean实例之间的映射关系（简称 Bean Map）
    Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
    if (CollectionUtil.isNotEmpty(beanMap)) {
      // 遍历
      for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
        // 取出 Bean类 与 Bean实例
        Class<?> beanClass = beanEntry.getKey();
        Object beanInstance = beanEntry.getValue();
        // 获取 Bean 类定义的所有成员变量 （简称：Bean Field）
        Field[] beanFields = beanClass.getDeclaredFields();
        if (ArrayUtils.isNotEmpty(beanFields)) {
          // 遍历 Bean Field
          for (Field beanField : beanFields) {
            // 判断当前 Bean Field是否带有Inject注解
            if (beanField.isAnnotationPresent(Inject.class)) {
              // 在 Bean Map 中获取 Bean Field对应的实例
              Class<?> beanFieldClass = beanField.getType();
              Object beanFieldInstance = beanMap.get(beanFieldClass);
              if (beanFieldInstance != null) {
                /**
                 * 通过反射初始化 BeanField 的值,控制反转，依赖注入
                 */
                ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
              }
            }
          }
        }
      }
    }
  }
}
