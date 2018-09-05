package org.smart4j.chapter1.service;

import org.junit.Test;
import org.smart4j.framework.annotation.Inject;

/**
 * Created by admin on 2018/8/27.
 */
public class HelloServiceTest{

    @Inject
    private HelloService helloService;

    @Test
    public void testHello() {
        helloService.hello(1);
    }
}
