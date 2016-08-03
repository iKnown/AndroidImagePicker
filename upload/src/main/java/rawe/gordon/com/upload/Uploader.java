package rawe.gordon.com.upload;

/**
 * Created by gordon on 8/2/16.
 */
public class Uploader {
    private UploadOptions options;

    private static class Holder {
        private static Uploader instance = new Uploader();
    }

    public static Uploader getInstance() {
        return Holder.instance;
    }

    public void handlerUploads(UploadOptions options, String... urls) {
//        Observable.from(urls).subscribeOn()
        new UploadOptions.Builder().setRetryTime(3).build();
    }
}
