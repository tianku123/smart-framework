package org.smart4j.chapter2.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by admin on 2018/9/11.
 */
public class HelloShiro {

  private static final Logger LOGGER = LoggerFactory.getLogger(HelloShiro.class);

  public static void main(String[] args) {
    /**
     * Shiro 的调用流程
     *      通过Subject 调用 SecurityManager，通过SecurityManager调用 Realm。这个 Realm就是提供用户信息的数据源。
     *      下面的例子在 shiro.ini 中配置的用户信息就是一种Realm，在Shiro中叫 IniRealm。
     *      除此之外， Shiro 还提供了其他几种 Realm：PropertiesRealm、JdbcRealm，JndiLdapRealm、ActiveDirectoryRealm等；
     *      也可以定制Realm来满足业务需求。
     * 不难发现，SecurityManager 才是Shiro的真正的核心，只需通过 Subject就可以操作SecurityManager，尤其是在Web应用中，
     * 甚至都可以忘记 SecurityManager的存在。
     */
    // 初始化
    Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
    SecurityManager securityManager = factory.getInstance();
    SecurityUtils.setSecurityManager(securityManager);

    // 获取当前用户
    Subject subject = SecurityUtils.getSubject();

    // 登录
    UsernamePasswordToken token = new UsernamePasswordToken("shiro", "123456");
    try {
      subject.login(token);
    } catch (AuthenticationException e) {
      LOGGER.info("登录失败!");
      return;
    }
    LOGGER.info("登录成功！Hello " + subject.getPrincipal());

    // 注销
    subject.logout();
  }
}
