package com.iknow.imageselect.utils;

import android.text.TextUtils;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Author: J.Chou
 * Email: who_know_me@163.com
 * Created: 2016年04月06日 3:46 PM
 * Description:
 */

public class ImageFilePathUtil {
  public static final String DESTINATION_CACHE_FOLDER = android.os.Environment.getExternalStorageDirectory()
      .getPath() + "/Android/data/com.iknow.imageselect/";

    public static String getImgUrl(String url){

        if(TextUtils.isEmpty(url) || url.length() < 5){
            return "";
        }

        if (url.substring(0, 4).equalsIgnoreCase("http")) {
            String filePath = getLocalImagePath(url);
            File f = new File(filePath);
            if (f.exists()) {
                url = "file:///"+ filePath;
            }
        } else {
            String filePath2 = getLocalImagePath(url);
            File f = new File(filePath2);
            if (f.exists()) {
                url = "file:///"+ filePath2;
            } else{
                url = "file:///" + url;
            }

        }
        return url;
    }

  public static final String getCacheImagePath() {
    String path = DESTINATION_CACHE_FOLDER + "image/";
    initPath(path);

    createNoMediaScanFile(path);

    return path;
  }

  /**
   * 创建nomedia文件，防止图库扫描
   *
   * @param path
   */
  private static void createNoMediaScanFile(String path) {
    File noMediaFile = new File(path + "nomedia");
    if (!noMediaFile.exists()) {
      try {
        noMediaFile.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static final String getCacheDbPath() {
    String path = DESTINATION_CACHE_FOLDER + "data/";
    initPath(path);
    return path;
  }

  public static final String getImagePath(String url){

    if(TextUtils.isEmpty(url)){
      return "";
    }

    int index = url.lastIndexOf("?");

    if(index == -1){
      return url;
    }

    return url.substring(0, index);
  }

  public static final String getLocalFileUri(String filePath){
    File f = new File(filePath);
    return "file://"+f.getAbsolutePath();
  }

  public static final String getLocalImagePath(String url) {
    return getCacheImagePath() + getLocalImageFileName(url);
  }

  public static final String getLocalImageFileName(String url) {
    return getMD5(url.getBytes());
  }

  private static void initPath(String path) {
    File file = new File(path);
    if (!file.exists()) {
      file.mkdirs();
    }
  }

  public static String getMD5(byte[] source) {
    StringBuilder sb = new StringBuilder();
    java.security.MessageDigest md5 = null;
    try {
      md5 = java.security.MessageDigest.getInstance("MD5");
      md5.update(source);
    }
    catch (NoSuchAlgorithmException e) {
    }
    if (md5 != null) {
      for (byte b : md5.digest()) {
        sb.append(String.format("%02X", b));
      }
    }
    return sb.toString();
  }
}
