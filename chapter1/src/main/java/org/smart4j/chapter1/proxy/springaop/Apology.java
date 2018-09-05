package org.smart4j.chapter1.proxy.springaop;

/**
 *  以上提到的都是对方法的增强，那能否对类进行增强呢？
 *  用AOP 对方法的增强交Weaving（织入），而对类的增强叫 Introduction（引入）。
 *  Introduction Advice就是对类的功能增强，它也是 Spring AOP 提供的最后一种增强。
 */
public interface Apology {

  void saySorry(String name);
}
