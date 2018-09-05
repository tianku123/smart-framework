package org.smart4j.chapter2.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.smart4j.chapter2.model.Customer;

import java.util.List;

/**
 * Created by admin on 2018/8/21.
 */
public class CustomerServiceTest {

    private final CustomerService customerService;

    public CustomerServiceTest() {
        customerService = new CustomerService();
    }

    @Before
    public void init() {
        // TODO 初始化数据库
    }


    /**
     * 获取客户列表
     */
    @Test
    public void getCustomerList(String name) {
        List<Customer> customerList = customerService.getCustomerList(null);
        Assert.assertNotNull(customerList);
    }

    /**
     * 获取客户
     */
    public void getCustomer(long id) {
        Customer customer = customerService.getCustomer(1);
        Assert.assertNotNull(customer);
    }
}
