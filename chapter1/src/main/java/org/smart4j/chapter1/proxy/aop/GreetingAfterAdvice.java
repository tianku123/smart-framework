package org.smart4j.chapter1.proxy.aop;

import java.lang.reflect.Method;
import org.springframework.aop.AfterReturningAdvice;

/**
 * Created by admin on 2018/8/28.
 */
public class GreetingAfterAdvice implements AfterReturningAdvice {

  @Override
  public void afterReturning(Object o, Method method, Object[] objects, Object o1)
      throws Throwable {
    System.out.println("AfterReturningAdvice After");
  }
}
