package org.smart4j.chapter1.proxy.aop;

import org.smart4j.chapter1.proxy.staticproxy.Hello;
import org.smart4j.chapter1.proxy.staticproxy.HelloImpl;
import org.springframework.aop.framework.ProxyFactory;

/**
 * Created by admin on 2018/8/28.
 */
public class Client {

  public static void main(String[] args) {
    ProxyFactory proxyFactory = new ProxyFactory();
    proxyFactory.setTarget(new HelloImpl());
    proxyFactory.addAdvice(new GreetingBeforeAdvice());
    proxyFactory.addAdvice(new GreetingAfterAdvice());
    proxyFactory.addAdvice(new GreetingAroundAdvice());

    Hello hello = (Hello) proxyFactory.getProxy();

    hello.say("spring aop");
  }

}
