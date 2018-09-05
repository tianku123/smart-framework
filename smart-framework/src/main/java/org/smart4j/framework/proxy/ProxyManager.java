package org.smart4j.framework.proxy;

import java.lang.reflect.Method;
import java.util.List;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 代理管理器
 * 输入一个目标类和一组Proxy接口实现，输出一个代理对象
 * 谁来调用ProxyManager呢？当然是切面类，切面类中需要在目标方法被调用的前后增加相应的逻辑。
 */
public class ProxyManager {

  public static <T> T createProxy(final Class<?> targetClass, final List<Proxy> proxyList) {
    /**
     * CGLib Enhancer#create 创建代理对象，
     * 将intercept 的参数传入ProxyChain的构造器中
     */
    return (T) Enhancer.create(targetClass, new MethodInterceptor() {
      public Object intercept(Object targetObject, Method targetMethod, Object[] methodParams,
          MethodProxy methodProxy)
          throws Throwable {
        return new ProxyChain(targetClass, targetObject, targetMethod, methodProxy, methodParams,
            proxyList).doProxyChain();
      }
    });
  }

}
