package rawe.gordon.com.gordon.loader;

import android.widget.ImageView;

import rawe.gordon.com.gordon.loader.local.BitmapWorkerTask;

/**
 * Created by gordon on 16/8/1.
 */
public class GordonLocal {
    public static String TAG = GordonLocal.class.getCanonicalName();

    public static class Holder {
        public static GordonLocal instance = new GordonLocal();
    }

    public static GordonLocal getInstance() {
        return Holder.instance;
    }


    public void loadBitmap(String filePath, ImageView imageView) {
        if (imageView.getTag() == null) {
            BitmapWorkerTask task = new BitmapWorkerTask(imageView);
            imageView.setTag(task);
            task.execute(filePath);
            return;
        }
        BitmapWorkerTask task = (BitmapWorkerTask) imageView.getTag();
        task.cancel(false);
        task = new BitmapWorkerTask(imageView);
        imageView.setTag(task);
        task.execute(filePath);
    }

}
