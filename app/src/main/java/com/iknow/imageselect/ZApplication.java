package com.iknow.imageselect;

import android.app.Application;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * @Author: Jason.Chou
 * @Email: who_know_me@163.com
 * @Created: 2016年04月26日 4:54 PM
 * @Description:
 */
public class ZApplication extends Application{

  // ===========================================================
  // Constants
  // ===========================================================

  // ===========================================================
  // Fields
  // ===========================================================
  private static ZApplication mApplication;
  // ===========================================================
  // Constructors
  // ===========================================================

  // ===========================================================
  // Getter & Setter
  // ===========================================================
  public static ZApplication getApplication() {
    return mApplication;
  }
  // ===========================================================
  // Methods for/from SuperClass/Interfaces
  // ===========================================================

  @Override public void onCreate() {
    super.onCreate();
    mApplication = this;
    Fresco.initialize(this,ImagePipelineConfigFactory.getImagePipelineConfig(this));
  }

  // ===========================================================
  // Methods
  // ===========================================================

  // ===========================================================
  // Inner and Anonymous Classes
  // ===========================================================
}
