package org.smart4j.framework.helper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.bean.FileParam;
import org.smart4j.framework.bean.FormParam;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.util.CollectionUtil;
import org.smart4j.framework.util.FileUtil;
import org.smart4j.framework.util.StreamUtil;
import org.smart4j.framework.util.StringUtil;

/**
 * 文件上传助手类
 */
public final class UploadHelper {

  private static final Logger LOGGER = LoggerFactory.getLogger(UploadHelper.class);

  /**
   * Apache Commons FileUpload 提供的 Servlet 文件上传对象
   */
  private static ServletFileUpload servletFileUpload;

  /**
   * 初始化
   */
  public static void init(ServletContext servletContext) {
    /**
     * 每一个servlet上下文都需要一个临时存储目录。Servlet容器必须为每一个servlet上下文提供一个私有的临时目录，
     * 并且使它可以通过javax.servlet.context.tempdir上下文属性可用。这些属性关联的对象必须是java.io.File类型。
     *
     * 这项需求认可了很多servlet引擎实现中提供的常见便利。容器不需要在servlet重启时维持临时目录的内容，
     * 但是需要确保一个servlet上下文的临时目录的内容对于该servlet容器上运行的其他web应用的servlet上下文不可见。
     */
    File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
    servletFileUpload = new ServletFileUpload(
        new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, repository));
    int uploadLimit = ConfigHelper.getAppUploadLimit();
    if (uploadLimit != 0) {
      servletFileUpload.setFileSizeMax(uploadLimit * 1024 * 1024);
    }
  }

  /**
   * 判断请求是否为 multipart 类型
   */
  public static boolean isMultipart(HttpServletRequest request) {
    return ServletFileUpload.isMultipartContent(request);
  }

  /**
   * 创建请求对象
   */
  public static Param createParam(HttpServletRequest request) throws IOException {
    List<FormParam> formParamList = new ArrayList<FormParam>();
    List<FileParam> fileParamList = new ArrayList<FileParam>();
    try {
      Map<String, List<FileItem>> fileItemListMap = servletFileUpload.parseParameterMap(request);
      if (CollectionUtil.isNotEmpty(fileItemListMap)) {
        for (Map.Entry<String, List<FileItem>> entry : fileItemListMap.entrySet()) {
          String fieldName = entry.getKey();
          List<FileItem> fileItemList = entry.getValue();
          if (CollectionUtil.isNotEmpty(fileItemList)) {
            for (FileItem fileItem : fileItemList) {
              if (fileItem.isFormField()) {
                String fieldValue = fileItem.getString("UTF-8");
                formParamList.add(new FormParam(fieldName, fieldValue));
              } else {
                String fileName = FileUtil
                    .getRealFileName(new String(fileItem.getName().getBytes(), "UTF-8"));
                if (StringUtil.isNotEmpty(fieldName)) {
                  long fileSize = fileItem.getSize();
                  String contentType = fileItem.getContentType();
                  InputStream inputStream = fileItem.getInputStream();
                  fileParamList
                      .add(new FileParam(fieldName, fileName, fileSize, contentType, inputStream));
                }
              }
            }
          }
        }
      }
    } catch (FileUploadException e) {
      LOGGER.error("create param failure", e);
      throw new RuntimeException(e);
    }
    return new Param(formParamList, fileParamList);
  }

  /**
   * 上传文件
   */
  public static void uploadFile(String basePath, FileParam fileParam) {
    try {
      if (fileParam != null) {
        String filePath = basePath + fileParam.getFileName();
        FileUtil.createFile(filePath);
        InputStream inputStream = new BufferedInputStream(fileParam.getInputStream());
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
        StreamUtil.copyStream(inputStream, outputStream);
      }
    } catch (Exception e) {
      LOGGER.error("upload file failure", e);
      throw new RuntimeException(e);
    }
  }

  /**
   * 批量上传文件
   */
  public static void uploadFile(String basePath, List<FileParam> fileParamList) {
    try {
      if (CollectionUtil.isNotEmpty(fileParamList)) {
        for (FileParam fileParam : fileParamList) {
          uploadFile(basePath, fileParam);
        }
      }
    } catch (Exception e) {
      LOGGER.error("upload file failure", e);
      throw new RuntimeException(e);
    }
  }
}
