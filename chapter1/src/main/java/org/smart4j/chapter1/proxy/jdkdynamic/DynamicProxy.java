package org.smart4j.chapter1.proxy.jdkdynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.smart4j.chapter1.proxy.staticproxy.Hello;
import org.smart4j.chapter1.proxy.staticproxy.HelloImpl;

/**
 * jdk 动态代理
 */
public class DynamicProxy implements InvocationHandler {

  private Object target;

  public DynamicProxy(Object target) {
    this.target = target;
  }

  public <T> T getProxy() {
    return (T) Proxy.newProxyInstance(
        target.getClass().getClassLoader(),
        target.getClass().getInterfaces(),
        this
    );
  }
  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    before();
    Object result = method.invoke(target, args);
    after();
    return result;
  }

  private void before() {
    System.out.println("Before");
  }

  private void after() {
    System.out.println("After");
  }

  public static void main(String[] args) {
    Hello hello = new HelloImpl();

    DynamicProxy dynamicProxy = new DynamicProxy(hello);

    Hello helloProxy = dynamicProxy.getProxy();

    helloProxy.say("Jack");
  }
}
