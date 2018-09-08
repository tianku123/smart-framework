package org.smart4j.framework.helper;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.annotation.Aspect;
import org.smart4j.framework.annotation.Service;
import org.smart4j.framework.proxy.AspectProxy;
import org.smart4j.framework.proxy.Proxy;
import org.smart4j.framework.proxy.ProxyManager;
import org.smart4j.framework.proxy.TransactionProxy;

/**
 * AOP 核心类
 */
public final class AopHelper {

  private static final Logger LOGGER = LoggerFactory.getLogger(AopHelper.class);

  // 初始化aop框架
  static {
    try {
      // key：切面类，value：被代理类集合
      Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
      // key：被代理类，value：切面类实例的集合
      Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);
      for (Map.Entry<Class<?>, List<Proxy>> targetEntry : targetMap.entrySet()) {
        // 被代理类
        Class<?> targetClass = targetEntry.getKey();
        // 切面类实例的集合
        List<Proxy> proxyList = targetEntry.getValue();
        Object proxy = ProxyManager.createProxy(targetClass, proxyList);
        BeanHelper.setBean(targetClass, proxy);
      }
    } catch (Exception e) {
      LOGGER.error("aop failure", e);
    }
  }

  /**
   * 带有 Aspect 注解的所有类
   */
  private static Set<Class<?>> createTargetClassSet(Aspect aspect) throws Exception {
    Set<Class<?>> targetClassSet = new HashSet<Class<?>>();
    Class<? extends Annotation> annotation = aspect.value();
    if (annotation != null && !annotation.equals(Aspect.class)) {
      targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));
    }
    return targetClassSet;
  }

  /**
   * 代理类及其目标类集合之间的映射关系，一个代理类可对应一个或多个目标类
   * key：切面类，value：被代理类
   */
  private static Map<Class<?>, Set<Class<?>>> createProxyMap() throws Exception {
    Map<Class<?>, Set<Class<?>>> proxyMap = new HashMap<Class<?>, Set<Class<?>>>();
    // key：切面类（继承了AspectProxy类），value：被代理类的集合
    addAspectProxy(proxyMap);
    // key：事务切面类（TransactionProxy），value：被Service类的集合
    addTransactionProxy(proxyMap);
    return proxyMap;
  }

  /**
   * 事务代理
   * @param proxyMap
   */
  private static void addTransactionProxy(Map<Class<?>, Set<Class<?>>> proxyMap) {
    Set<Class<?>> serviceClassSet = ClassHelper.getClassSetByAnnotation(Service.class);
    proxyMap.put(TransactionProxy.class, serviceClassSet);
  }

  /**
   * 切面代理
   * @param proxyMap
   * @throws Exception
   */
  private static void addAspectProxy(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception {
    /**
     * 获取所有代理类（切面类），也就是继承 AspectProxy类的切面类
     */
    Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class);
    /**
     * 只有加上注解 Aspect的代理类才会生效，
     * 遍历代理类，获取有效的的代理类
     */
    for (Class<?> proxyClass : proxyClassSet) {
      if (proxyClass.isAnnotationPresent(Aspect.class)) {// 有 Aspect注解的代理类
        Aspect aspect = proxyClass.getAnnotation(Aspect.class);// 获取 Aspect注解
        // 带有 Aspect 注解的所有类
        Set<Class<?>> targetClassSet = createTargetClassSet(aspect);
        proxyMap.put(proxyClass, targetClassSet);
      }
    }
  }

  /**
   * 根据代理类与目标类集合之间的映射关系，分析出目标类与代理对象列表之间的映射关系
   * @param proxyMap key：切面类，value：被代理类集合
   *
   * @return key：被代理类，value：切面类实例的集合
   */
  private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, Set<Class<?>>> proxyMap)
      throws Exception {
    // key：被代理类，value：切面类实例的集合
    Map<Class<?>, List<Proxy>> targetMap = new HashMap<Class<?>, List<Proxy>>();
    for (Map.Entry<Class<?>, Set<Class<?>>> proxyEntry : proxyMap.entrySet()) {
      // 切面类
      Class<?> proxyClass = proxyEntry.getKey();
      // 被代理类集合
      Set<Class<?>> targetClassSet = proxyEntry.getValue();
      for (Class<?> targetClass : targetClassSet) {
        // 实例化切面类，aop切面或事务Transaction切面
        Proxy proxy = (Proxy) proxyClass.newInstance();
        if (targetMap.containsKey(targetClass)) {
          targetMap.get(targetClass).add(proxy);
        } else {
          List<Proxy> proxyList = new ArrayList<Proxy>();
          proxyList.add(proxy);
          targetMap.put(targetClass, proxyList);
        }
      }
    }
    return targetMap;
  }
}
