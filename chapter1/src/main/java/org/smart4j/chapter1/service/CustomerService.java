package org.smart4j.chapter1.service;

import java.util.List;
import java.util.Map;
import org.smart4j.chapter1.model.Customer;
import org.smart4j.framework.annotation.Service;
import org.smart4j.framework.annotation.Transaction;
import org.smart4j.framework.util.DatabaseHelper;
import org.smart4j.framework.util.StringUtil;

/**
 * Created by admin on 2018/8/21.
 */
@Service
public class CustomerService {

    /**
     * 获取客户列表
     */
    public List<Customer> getCustomerList(String name) {
        String sql = "select * from customer";
        Object[] params = null;
        if (StringUtil.isNotEmpty(name)) {
            sql += " where name like ?";
            params = new Object[]{"%" + name + "%"};
        }
        return DatabaseHelper.queryEntityList(Customer.class, sql, params);
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
        boolean b =  DatabaseHelper.insertEntity(Customer.class, fieldMap);
        int a = 1/0;
        return b;
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
