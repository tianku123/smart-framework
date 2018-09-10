package org.smart4j.chapter1.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.smart4j.chapter1.model.Customer;
import org.smart4j.chapter1.service.CustomerService;
import org.smart4j.framework.annotation.Action;
import org.smart4j.framework.annotation.Controller;
import org.smart4j.framework.annotation.Inject;
import org.smart4j.framework.bean.Data;
import org.smart4j.framework.bean.FileParam;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.bean.View;

/**
 * Created by admin on 2018/9/8.
 */
@Controller
public class CustomerController {
  @Inject
  private CustomerService customerService;


  @Action("get:/list")
  public Data list(Param param) {
    String name = param.getString("name");
    List<Customer> list = customerService.getCustomerList(name);
    return new Data(list);
  }

  @Action("get:/goAddCustomerJsp")
  public View goAddCustomerJsp() {
    return new View("addCustomer.jsp", new HashMap<String, Object>());
  }

  @Action("post:/customer_create")
  public Data customer_create(Param param) {
    Map<String, Object> fieldMap = param.getFieldMap();
    FileParam fileParam = param.getFile("photo");
    boolean b = customerService.createCustomer(fieldMap, fileParam);
    return new Data(b);
  }


}
