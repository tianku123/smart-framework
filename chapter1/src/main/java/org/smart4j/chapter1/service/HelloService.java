package org.smart4j.chapter1.service;

import org.smart4j.framework.annotation.Service;

/**
 * Created by admin on 2018/8/27.
 */
@Service
public class HelloService {

  public Object hello(long type) {
    if (type == 1) {
      return "aa";
    } else {
      return "bb";
    }
  }
}
