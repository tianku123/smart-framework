package org.smart4j.chapter1.proxy.springaop;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.DelegatingIntroductionInterceptor;

/**
 * Created by admin on 2018/8/29.
 */
public class HelloIntroAdvice extends DelegatingIntroductionInterceptor implements Apology {

  @Override
  public Object invoke(MethodInvocation mi) throws Throwable {
    return super.invoke(mi);
  }

  @Override
  public void saySorry(String name) {
    System.out.println("Sorry! " + name);
  }
}
