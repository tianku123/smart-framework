package org.smart4j.chapter2.service;

import java.util.List;
import java.util.Map;
import org.smart4j.chapter2.model.Customer;
import org.smart4j.chapter2.util.DatabaseHelper;
import org.smart4j.framework.annotation.Transaction;

/**
 * Created by admin on 2018/8/21.
 */
public class CustomerService {

    /**
     * 获取客户列表
     */
    public List<Customer> getCustomerList(String name) {
        String sql = "select * from customer";
        return DatabaseHelper.queryEntityList(Customer.class, sql, new Object[]{name});
    }

    /**
     * 获取客户
     */
    public Customer getCustomer(long id) {
        String sql = "select * from customer where id=?";
        return DatabaseHelper.queryEntity(Customer.class, sql, new Object[]{id});
    }

    /**
     * 创建客户
     */
    @Transaction
    public boolean createCustomer(Map<String, Object> fieldMap) {
        return DatabaseHelper.insertEntity(Customer.class, fieldMap);
    }

    /**
     * 更新客户
     */
    @Transaction
    public boolean updateCustomer(long id, Map<String, Object> fieldMap) {
        return DatabaseHelper.updateEntity(Customer.class, id, fieldMap);
    }

    /**
     * 删除客户
     */
    @Transaction
    public boolean deleteCustomer(long id) {
        return DatabaseHelper.deleteEntity(Customer.class, id);
    }
}
