package org.smart4j.chapter1.proxy.aop;


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 环绕增强类需要实现 org.aopalliance.intercept.MethodInterceptor 接口。这个接口不是Spring提供的，
 * 它是AOP联盟（一个很“高大上”的技术联盟）写的，Spring借用了它。
 */
public class GreetingAroundAdvice implements MethodInterceptor {

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    before();
    Object result = invocation.proceed();
    after();
    return result;
  }

  private void before() {
    System.out.println("org.aopalliance.intercept.MethodInterceptor Before");
  }

  private void after() {
    System.out.println("org.aopalliance.intercept.MethodInterceptor After");
  }
}
