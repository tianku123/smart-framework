package org.smart4j.framework.helper;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import org.smart4j.framework.annotation.Controller;
import org.smart4j.framework.annotation.Service;
import org.smart4j.framework.util.ClassUtil;

/**
 * 类操作助手类
 * 获取应用包名下的所有类、应用包名下所有Service类、应用包名下所有Controller类。
 * 此外，我们可以将带有Controller注解与Service注解的类所产生的对象，理解为由smart框架所管理的Bean，
 * 所以有必要增加一个获取应用包名下所有Bean类的方法
 *
 * @author rh
 * @since 1.0.0
 */
public final class ClassHelper {

  /**
   * 定义类集合（用户存放所加载的类）
   */
  private static final Set<Class<?>> CLASS_SET;

  static {
    String basePackage = ConfigHelper.getAppBasePackage();
    CLASS_SET = ClassUtil.getClassSet(basePackage);
  }

  /**
   * 获取应用名包下的所有类
   */
  public static Set<Class<?>> getClassSet() {
    return CLASS_SET;
  }

  /**
   * 获取应用名包下所有Service类
   */
  public static Set<Class<?>> getServiceClassSet() {
    Set<Class<?>> classSet = new HashSet<Class<?>>();
    for (Class<?> cls : CLASS_SET) {
      if (cls.isAnnotationPresent(Service.class)) {
        classSet.add(cls);
      }
    }
    return classSet;
  }

  /**
   * 获取应用名包下所有Controller类
   */
  public static Set<Class<?>> getControllerClassSet() {
    Set<Class<?>> classSet = new HashSet<Class<?>>();
    for (Class<?> cls : CLASS_SET) {
      if (cls.isAnnotationPresent(Controller.class)) {
        classSet.add(cls);
      }
    }
    return classSet;
  }

  /**
   * 获取应用名包下所有Bean类 （包括：Service， Controller）
   */
  public static Set<Class<?>> getBeanClassSet() {
    Set<Class<?>> classSet = new HashSet<Class<?>>();
    classSet.addAll(getServiceClassSet());
    classSet.addAll(getControllerClassSet());
    return classSet;
  }

  /**
   * 获取应用包名下某父类（或接口）的所有子类（或实现类）
   */
  public static Set<Class<?>> getClassSetBySuper(Class<?> superClass) {
    Set<Class<?>> classSet = new HashSet<Class<?>>();
    for (Class<?> cls : CLASS_SET) {
      if (superClass.isAssignableFrom(cls) && !superClass.equals(cls)) {
        classSet.add(cls);
      }
    }
    return classSet;
  }

  /**
   * 获取应用包名下带有某注解的所有类
   */
  public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> annotationClass) {
    Set<Class<?>> classSet = new HashSet<Class<?>>();
    for (Class<?> cls : CLASS_SET) {
      if (cls.isAnnotationPresent(annotationClass)) {
        classSet.add(cls);
      }
    }
    return classSet;
  }
}
