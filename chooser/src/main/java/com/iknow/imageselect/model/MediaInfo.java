package com.iknow.imageselect.model;

import java.io.Serializable;

/**
 * @Author: J.Chou
 * @Email: who_know_me@163.com
 * @Created: 2016年04月06日 3:17 PM
 * @Description:
 */

public class MediaInfo implements Serializable,Cloneable {

  public long fileId;
  /** 图片显示的名称 */
  public String name = "";
  /** 作者名称 */
  public String author = "";
  /** 图片描述信息 */
  public String description = "";

  public String filePath;//图片所在文件夹的路径

  public String createTime;

  public String thumbPath;//缩略图路径

  public String fileName;//图片全路径,包含图片文件名的路径信息

  public int rotate;

  public String lat;

  public String lon;

  public int mediaType;

  public static MediaInfo obtain(String path){
     MediaInfo imageInfo = new MediaInfo();
     imageInfo.fileName = path;
    return imageInfo;
  }

  public static MediaInfo buildOneImage(String path){

    return null;
  }
}
