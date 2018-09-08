package org.smart4j.framework.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.helper.ConfigHelper;

/**
 * 增删改查操作工具类
 */
public final class DatabaseHelper {

  private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);

  private static final ThreadLocal<Connection> CONNECTION_THREAD_LOCAL;

  private static final QueryRunner QUERY_RUNNER;

  private static final BasicDataSource DATA_SOURCE;

  static {
    CONNECTION_THREAD_LOCAL = new ThreadLocal<Connection>();

    QUERY_RUNNER = new QueryRunner();
    String driver = ConfigHelper.getJdbcDriver();
    String url = ConfigHelper.getJdbcUrl();
    String username = ConfigHelper.getJdbcUsername();
    String password = ConfigHelper.getJdbcPassword();

    DATA_SOURCE = new BasicDataSource();
    DATA_SOURCE.setDriverClassName(driver);
    DATA_SOURCE.setUrl(url);
    DATA_SOURCE.setUsername(username);
    DATA_SOURCE.setPassword(password);
  }

  /**
   * 获取数据库连接
   */
  public static Connection getConnection() {
    Connection conn = CONNECTION_THREAD_LOCAL.get();
    if (conn == null) {
      try {
        conn = DATA_SOURCE.getConnection();
      } catch (SQLException e) {
        LOGGER.error("get connection failure", e);
        throw new RuntimeException(e);
      } finally {
        CONNECTION_THREAD_LOCAL.set(conn);
      }
    }
    return conn;
  }

  /**
   * 关闭数据库连接
   */
  public static void closeConnection() {
    Connection conn = CONNECTION_THREAD_LOCAL.get();
    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        LOGGER.error("close connection failure", e);
        throw new RuntimeException(e);
      } finally {
        CONNECTION_THREAD_LOCAL.remove();
      }
    }
  }

  /**
   * 查询实体列表
   */
  public static <T> List<T> queryEntityList(Class<T> entityClass, String sql) {
    return queryEntityList(entityClass, sql, null);
  }

  public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
    List<T> entityList;
    try {
      Connection conn = getConnection();
      if (params == null) {
        entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>(entityClass));
      } else {
        entityList = QUERY_RUNNER
            .query(conn, sql, new BeanListHandler<T>(entityClass), params);
      }
    } catch (SQLException e) {
      LOGGER.error("query entity list failure", e);
      throw new RuntimeException((e));
    }
    return entityList;
  }

  /**
   * 查询实体
   */
  public static <T> T queryEntity(Class<T> entityClass, String sql, Object... params) {
    T entity;
    try {
      Connection conn = getConnection();
      if (params == null) {
        entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<T>(entityClass));
      } else {
        entity = QUERY_RUNNER
            .query(conn, sql, new BeanHandler<T>(entityClass), params);
      }
    } catch (SQLException e) {
      LOGGER.error("query entity list failure", e);
      throw new RuntimeException((e));
    }
    return entity;
  }

  /**
   * 执行查询语句，返回List<Map<String, Object>>
   */
  public static List<Map<String, Object>> executeQuery(String sql, Object... params) {
    List<Map<String, Object>> result;
    try {
      Connection conn = getConnection();
      result = QUERY_RUNNER.query(conn, sql, new MapListHandler(), params);
    } catch (Exception e) {
      LOGGER.error("execute query failure", e);
      throw new RuntimeException(e);
    }
    return result;
  }

  /**
   * 执行更新语句（包括update、insert、delete）
   */
  public static int executeUpdate(String sql, Object... params) {
    int rows = 0;
    try {
      Connection conn = getConnection();
      rows = QUERY_RUNNER.update(conn, sql, params);
    } catch (SQLException e) {
      LOGGER.error("execute update failure", e);
    }
    return rows;
  }

  /**
   * 插入实体
   */
  public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> fieldMap) {
    if (CollectionUtil.isEmpty(fieldMap)) {
      LOGGER.error("can not insert entity: fieldMap is empty");
      return false;
    }
    StringBuilder sql = new StringBuilder("INSERT INTO ");
    sql.append(getTableName(entityClass));
    StringBuilder columns = new StringBuilder("(");
    StringBuilder values = new StringBuilder("(");
    for (String fieldName : fieldMap.keySet()) {
      columns.append(fieldName).append(", ");
      values.append("?, ");
    }
    columns.replace(columns.lastIndexOf(", "), columns.length(), ")");
    values.replace(values.lastIndexOf(", "), values.length(), ")");
    sql.append(columns).append(" values ").append(values);

    Object[] params = fieldMap.values().toArray();
    return executeUpdate(sql.toString(), params) == 1;
  }

  /**
   * 更新实体
   */
  public static <T> boolean updateEntity(Class<T> entityClass, long id,
      Map<String, Object> fieldMap) {
    if (CollectionUtil.isEmpty(fieldMap)) {
      LOGGER.error("can not update entity: fieldMap is empty");
      return false;
    }
    StringBuilder sql = new StringBuilder("UPDATE ");
    sql.append(getTableName(entityClass));
    sql.append(" SET ");
    StringBuilder columns = new StringBuilder("");
    for (String fieldName : fieldMap.keySet()) {
      columns.append(fieldName).append("=?, ");
    }
    sql.append(columns.substring(0, columns.lastIndexOf(", "))).append(" WHERE id=?");

    List<Object> paramList = new ArrayList<Object>();
    paramList.addAll(fieldMap.values());
    paramList.add(id);
    Object[] params = paramList.toArray();
    return executeUpdate(sql.toString(), params) == 1;
  }

  /**
   * 删除实体
   */
  public static <T> boolean deleteEntity(Class<T> entityClass, long id) {
    StringBuilder sql = new StringBuilder("DELETE FROM ");
    sql.append(getTableName(entityClass)).append(" WHERE id=?");
    return executeUpdate(sql.toString(), id) == 1;
  }

  /**
   * 执行SQL文件
   */
  public static void executeSqlFile(String filePath) {
    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    try {
      String sql;
      while ((sql = reader.readLine()) != null) {
        executeUpdate(sql);
      }
    } catch (Exception e) {
      LOGGER.error("execute sql file failure", e);
    }
  }

  private static <T> String getTableName(Class<T> entityClass) {
    return entityClass.getSimpleName();
  }

  /**
   * 开启事务
   * 默认是自动提交事务的，所以需要将自动提交属性设置为false。开启事务完毕后，将Connection对象放入
   * 本地线程变量中。当事务提交或回滚后，需要移除本地线程变量中的Connection对象。
   */
  public static void beginTransaction() {
    Connection conn = getConnection();
    if (conn != null) {
      try {
        conn.setAutoCommit(false);
      } catch (SQLException e) {
        LOGGER.error("begin transaction failure", e);
        throw new RuntimeException(e);
      } finally {
        CONNECTION_THREAD_LOCAL.set(conn);
      }
    }
  }

  /**
   * 提交事务
   */
  public static void commitTransaction() {
    Connection conn = getConnection();
    if (conn != null) {
      try {
        conn.commit();
        conn.close();
      } catch (SQLException e) {
        LOGGER.error("commit transaction failure", e);
        throw new RuntimeException(e);
      } finally {
        CONNECTION_THREAD_LOCAL.remove();
      }
    }
  }

  /**
   * 回滚事务
   */
  public static void rollbackTransaction() {
    Connection conn = getConnection();
    if (conn != null) {
      try {
        conn.rollback();
        conn.close();
      } catch (SQLException e) {
        LOGGER.error("rollback transaction failure", e);
        throw new RuntimeException(e);
      } finally {
        CONNECTION_THREAD_LOCAL.remove();
      }
    }
  }
}
