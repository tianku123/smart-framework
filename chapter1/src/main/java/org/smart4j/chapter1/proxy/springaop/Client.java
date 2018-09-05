package org.smart4j.chapter1.proxy.springaop;

import org.smart4j.chapter1.proxy.staticproxy.Hello;
import org.smart4j.chapter1.proxy.staticproxy.HelloImpl;
import org.springframework.aop.framework.ProxyFactory;

/**
 * 类增强，接口动态实现
 */
public class Client {

  public static void main(String[] args) {
    ProxyFactory factory = new ProxyFactory();
    factory.addInterface(Apology.class);
    factory.setTarget(new HelloImpl());
    factory.addAdvice(new HelloIntroAdvice());
    factory.setProxyTargetClass(true);
    Hello hello = (Hello) factory.getProxy();
    hello.say("Jack");
    /**
     * 这里引入增强给我们带来的特性，也就是“接口动态实现”功能
     */
    Apology apology = (Apology)factory.getProxy();
    apology.saySorry("Jack");
  }
}
