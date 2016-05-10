package com.iknow.imageselect.presenter;

import com.iknow.imageselect.model.MediaInfo;
import java.util.ArrayList;

/**
 * @Author: J.Chou
 * @Email: who_know_me@163.com
 * @Created: 2016年04月06日 6:37 PM
 * @Description:
 */

public interface IImageChoosePresenter {
  void doCancel();
  String takePhotos();
  void doSendAction();
  void doPreview(ArrayList<MediaInfo> hasCheckedImages);
}
