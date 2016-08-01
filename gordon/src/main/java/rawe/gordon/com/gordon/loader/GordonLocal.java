package rawe.gordon.com.gordon.loader;

import android.widget.ImageView;

import rawe.gordon.com.gordon.loader.local.BitmapWorkerTask;

/**
 * Created by gordon on 16/8/1.
 */
public class GordonLocal {


    public static class Holder {
        public static GordonLocal instance = new GordonLocal();
    }

    public static GordonLocal getInstance() {
        return Holder.instance;
    }


    public void loadBitmap(String filePath, ImageView imageView) {
        BitmapWorkerTask task = new BitmapWorkerTask(imageView);
        task.execute(filePath);
    }

}
