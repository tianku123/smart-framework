package org.smart4j.chapter1.controller;

import org.smart4j.chapter1.service.HelloService;
import org.smart4j.framework.annotation.Action;
import org.smart4j.framework.annotation.Aspect;
import org.smart4j.framework.annotation.Controller;
import org.smart4j.framework.annotation.Inject;
import org.smart4j.framework.bean.Data;
import org.smart4j.framework.bean.Param;

/**
 *
 */
@Controller
@Aspect(value = Controller.class)
public class HelloSmartController {

  @Inject
  private HelloService helloService;

  @Action("get:/hello")
  public Data hello(Param param) {
    long type = param.getLong("type");
    Object obj = helloService.hello(type);
    return new Data(obj);
  }

}
