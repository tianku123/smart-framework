package org.smart4j.chapter2.controller;

import java.util.List;
import org.smart4j.chapter2.model.Customer;
import org.smart4j.chapter2.service.CustomerService;
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
public class CustomerController {

  @Inject
  private CustomerService customerService;

  @Action("get:/hello")
  public Data hello(Param param) {
    String name = param.getString("name");
    List<Customer> list = customerService.getCustomerList(name);
    return new Data(list);
  }

}
