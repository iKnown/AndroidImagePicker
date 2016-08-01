package rawe.gordon.com.business;

import android.app.Application;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.iknow.imageselect.ImageSelectContextHolder;
import com.iknow.imageselect.configs.ImagePipelineConfigFactory;

/**
 * Author: Jason.Chou
 * Email: who_know_me@163.com
 * Created: 2016年04月26日 4:54 PM
 * Description:
 */
public class ZApplication extends Application{

  private static ZApplication mApplication;
  public static ZApplication getApplication() {
    return mApplication;
  }

  @Override public void onCreate() {
    super.onCreate();
    mApplication = this;
    Fresco.initialize(this, ImagePipelineConfigFactory.getImagePipelineConfig(this));
    ImageSelectContextHolder.getInstance().init(getApplicationContext());
  }
}
