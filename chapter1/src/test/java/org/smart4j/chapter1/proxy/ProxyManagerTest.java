package org.smart4j.chapter1.proxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.proxy.AspectProxy;
import org.smart4j.framework.proxy.Proxy;
import org.smart4j.framework.proxy.ProxyManager;

/**
 * Created by admin on 2018/9/8.
 */
public class ProxyManagerTest {

  @Test
  public void testProxyManager() {
    List<Proxy> proxyList = new ArrayList<>();
    proxyList.add(new BeginAspect());
    proxyList.add(new BeforeAspect());
    proxyList.add(new AfterAspect());
    Hello hello = ProxyManager.createProxy(Hello.class, proxyList);
    hello.hello();
  }
}

class Hello {
  public void hello() {
    System.out.println("hello");
  }
}

class BeginAspect extends AspectProxy {

  @Override
  public void begin() {
    System.out.println("========= begin ========");
  }
  @Override
  public void before(Class<?> cls, Method method, Object[] params) {
    System.out.println("========= BeginAspect before ========");
  }
}
class BeforeAspect extends AspectProxy {

  private final Logger LOGGER = LoggerFactory.getLogger(
      org.smart4j.framework.proxy.ControllerAspect.class);
  @Override
  public void before(Class<?> cls, Method method, Object[] params) {
    LOGGER.debug("------------ before ------------");
  }

}
class AfterAspect extends AspectProxy {

  private final Logger LOGGER = LoggerFactory.getLogger(
      org.smart4j.framework.proxy.ControllerAspect.class);
  @Override
  public void after(Class<?> cls, Method method, Object[] params, Object result) {
    LOGGER.debug("----------- after -----------");
  }

}