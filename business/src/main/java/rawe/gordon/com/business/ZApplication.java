package rawe.gordon.com.business;

import android.app.Application;

import com.iknow.imageselect.ImageSelectContextHolder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Author: Jason.Chou
 * Email: who_know_me@163.com
 * Created: 2016年04月26日 4:54 PM
 * Description:
 */
public class ZApplication extends Application {

    private static ZApplication mApplication;

    public static ZApplication getApplication() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
        ImageSelectContextHolder.getInstance().init(getApplicationContext());
    }
}
