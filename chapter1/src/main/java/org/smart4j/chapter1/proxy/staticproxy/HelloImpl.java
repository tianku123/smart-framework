package org.smart4j.chapter1.proxy.staticproxy;

/**
 * Created by admin on 2018/8/28.
 */
public class HelloImpl implements Hello{

  @Override
  public void say(String name) {
    System.out.println("Hello! " + name);
  }

  public void goodMorning(String name) {
    System.out.println("Good Morning! " + name);
  }

  public void goodNight(String name) {
    System.out.println("Good Night! " + name);
  }
}
