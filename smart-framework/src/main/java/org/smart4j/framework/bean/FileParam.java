package org.smart4j.framework.bean;

import java.io.InputStream;

/**
 * 封装上传文件参数
 */
public class FileParam {

  private String fieldName;
  private String fileName;
  private long fileSize;
  private String contentType;
  private InputStream inputStream;

  public FileParam(String fieldName, String fileName, long fileSize, String contentType,
      InputStream inputStream) {
    // 文件表单的字段名
    this.fieldName = fieldName;
    // 上传文件的文件名
    this.fileName = fileName;
    // 文件大小
    this.fileSize = fileSize;
    // 上传文件的Content-Type，可判断文件类型
    this.contentType = contentType;
    // 上传文件的字节输入流
    this.inputStream = inputStream;
  }

  public String getFieldName() {
    return fieldName;
  }

  public String getFileName() {
    return fileName;
  }

  public long getFileSize() {
    return fileSize;
  }

  public String getContentType() {
    return contentType;
  }

  public InputStream getInputStream() {
    return inputStream;
  }
}
