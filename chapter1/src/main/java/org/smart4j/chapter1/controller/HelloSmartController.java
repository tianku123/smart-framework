package org.smart4j.chapter1.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.smart4j.chapter1.model.Customer;
import org.smart4j.chapter1.service.CustomerService;
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

  @Inject
  private CustomerService customerService;


  @Action("get:/hello")
  public Data hello(Param param) {
    long type = param.getLong("type");
    Object obj = helloService.hello(type);
    return new Data(obj);
  }

  @Action("get:/list")
  public Data list(Param param) {
    String name = param.getString("name");
    List<Customer> list = customerService.getCustomerList(name);
    return new Data(list);
  }

  @Action("get:/save")
  public Data save(Param param) {
    String name = param.getString("name");
    Map<String, Object> fieldMap = new HashMap<>();
    fieldMap.put("name", name);
    boolean b = customerService.createCustomer(fieldMap);
    return new Data(b);
  }

}
