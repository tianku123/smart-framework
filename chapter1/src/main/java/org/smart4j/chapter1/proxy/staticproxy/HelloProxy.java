package org.smart4j.chapter1.proxy.staticproxy;

/**
 * Created by admin on 2018/8/28.
 */
public class HelloProxy implements Hello {

  private Hello hello;

  public HelloProxy() {
    hello = new HelloImpl();
  }

  @Override
  public void say(String name) {
    before();
    hello.say(name);
    after();
  }

  private void before() {
    System.out.println("Before");
  }

  private void after() {
    System.out.println("After");
  }

  public static void main(String[] args) {
    Hello hello = new HelloProxy();
    hello.say("Jack");
  }
}
