package org.smart4j.chapter1.proxy.cglib;

import java.lang.reflect.Method;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.smart4j.chapter1.proxy.staticproxy.Hello;
import org.smart4j.chapter1.proxy.staticproxy.HelloImpl;

/**
 * Created by admin on 2018/8/28.
 */
public class CGLibProxy implements MethodInterceptor {

  private static CGLibProxy instance = new CGLibProxy();

  private CGLibProxy() {}

  public static CGLibProxy getInstance() {
    return instance;
  }

  public <T> T getProxy(Class<T> cls) {
    return (T) Enhancer.create(cls, this);
  }

  @Override
  public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
      throws Throwable {
    before();
    Object result = proxy.invokeSuper(obj, args);
    after();
    return result;
  }

  public void before() {
    System.out.println("Before");
  }

  public void after() {
    System.out.println("After");
  }

  public static void main(String[] args) {
    Hello proxy = CGLibProxy.getInstance().getProxy(HelloImpl.class);
    proxy.say("cglib prox");

    /**
     * 另一种简写方式
     */
    proxy = (Hello) CGLibProxy.getInstance().simpleWrite(HelloImpl.class);
    proxy.say("cglib simple write");
  }

  public <T> T simpleWrite(Class<T> cls) {
    return (T)Enhancer.create(cls, new MethodInterceptor() {
      /**
       *
       * @param targetObject  this", the enhanced object
       * @param targetMethod  intercepted Method
       * @param methodParams  argument array; primitive types are wrapped
       * @param methodProxy   used to invoke super (non-intercepted method); may be called as many times as needed
       * @return
       * @throws Throwable
       */
      @Override
      public Object intercept(Object targetObject, Method targetMethod, Object[] methodParams, MethodProxy methodProxy)
          throws Throwable {
        /**
         * 生成的代理对象里面有这些方法
         * 1.执行before
         * 2.执行被代理原始方法
         * 3.执行after
         */
        before();
        Object result = methodProxy.invokeSuper(targetObject, methodParams);
        after();
        return result;
      }
    });
  }
}
