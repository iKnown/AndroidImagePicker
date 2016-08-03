package rawe.gordon.com.upload;

/**
 * Created by gordon on 8/2/16.
 */
public class UploadOptions {

    public UploadOptions(Builder builder) {
        retryTime = builder.retryTime;
    }

    private int retryTime = 5;

    public static class Builder {

        public Builder setRetryTime(int retryTime) {
            this.retryTime = retryTime;
            return this;
        }

        private int retryTime = 5;

        public UploadOptions build() {
            return new UploadOptions(this);
        }
    }
}
