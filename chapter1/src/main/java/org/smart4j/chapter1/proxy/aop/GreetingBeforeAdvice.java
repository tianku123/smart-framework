package org.smart4j.chapter1.proxy.aop;

import java.lang.reflect.Method;
import org.springframework.aop.MethodBeforeAdvice;

/**
 * Spring AOP
 */
public class GreetingBeforeAdvice implements MethodBeforeAdvice {

  @Override
  public void before(Method method, Object[] objects, Object o) throws Throwable {
    System.out.println("MethodBeforeAdvice Before");
  }
}
