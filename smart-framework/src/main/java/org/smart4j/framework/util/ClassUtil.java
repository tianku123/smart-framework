package org.smart4j.framework.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类操作工具类
 * @author rh
 * @since 1.0.0
 */
public final class ClassUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);

  /**
   * 获取类加载器
   */
  public static ClassLoader getClassLoader() {
    return Thread.currentThread().getContextClassLoader();
  }

  /**
   * 加载类,为了提高加载类的性能，可将loadClass方法的isInitialized参数设置为false
   */
  public static Class<?> loadClass(String className, boolean isInitialized) {
    Class<?> cls;
    try {
      cls = Class.forName(className, isInitialized, getClassLoader());
    } catch (ClassNotFoundException e) {
      LOGGER.error("load class failure", e);
      throw new RuntimeException(e);
    }
    return cls;
  }
  public static Class<?> loadClass(String className) {
    Class<?> cls;
    try {
      cls = Class.forName(className);
    } catch (ClassNotFoundException e) {
      LOGGER.error("load class failure", e);
      throw new RuntimeException(e);
    }
    return cls;
  }

  /**
   * 获取指定包下的所有类
   * 我们需要根据包名并将其转换为文件路径，读取class文件和jar包，获取指定的类名去加载类
   */
  public static Set<Class<?>> getClassSet(String packgeName) {
    Set<Class<?>> classSet = new HashSet<Class<?>>();
    try{
      Enumeration<URL> urls = getClassLoader().getResources(packgeName.replace(".", "/"));
      while (urls.hasMoreElements()) {
        URL url = urls.nextElement();
        if (url != null) {
          String protocol = url.getProtocol();
          if (protocol.equals("file")) {
            String packagePath = url.getPath().replaceAll("%20", "");
            addClass(classSet, packagePath, packgeName);
          } else if (protocol.equals("jar")) {
            JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
            if (jarURLConnection != null) {
              JarFile jarFile = jarURLConnection.getJarFile();
              if (jarFile != null) {
                Enumeration<JarEntry> jarEntries = jarFile.entries();
                while(jarEntries.hasMoreElements()) {
                  JarEntry jarEntry = jarEntries.nextElement();
                  String jarEntryName = jarEntry.getName();
                  if (jarEntryName.endsWith(".class")) {
                    String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                    doAddClass(classSet, className);
                  }
                }
              }
            }
          }
        }
      }
    } catch (IOException e) {
      LOGGER.error("get class set failure", e);
      throw new RuntimeException(e);
    }
    return classSet;
  }

  private static void doAddClass(Set<Class<?>> classSet, String className) {
    Class<?> cls = loadClass(className, false);
    classSet.add(cls);
  }

  private static void addClass(Set<Class<?>> classSet, String packagePath, String packgeName) {
    File[] files = new File(packagePath).listFiles(new FileFilter() {
      public boolean accept(File file) {
        return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
      }
    });
    for (File file : files) {
      String fileName = file.getName();
      if (file.isFile()) {
        String className = fileName.substring(0, fileName.lastIndexOf("."));
        if (StringUtil.isNotEmpty(packgeName)) {
          className = packgeName + "." + className;
        }
        doAddClass(classSet, className);
      } else {
        String subPackagePath = fileName;
        if (StringUtil.isNotEmpty(packagePath)) {
          subPackagePath = packagePath + "/" + subPackagePath;
        }
        String subPackageName = fileName;
        if (StringUtil.isNotEmpty(packgeName)) {
          subPackageName = packgeName + "." + subPackageName;
        }
        addClass(classSet, subPackagePath, subPackageName);
      }
    }
  }
}
