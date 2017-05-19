package sysu.lwt.volley;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by 12136 on 2017/5/19.
 */

public class MyImageCache implements ImageLoader.ImageCache{

    private LruCache mCache;

    public MyImageCache() {
        int MAX_SIZE = 10 * 1024 * 1024;
        mCache = new LruCache<String, Bitmap>(MAX_SIZE) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {
        return (Bitmap) mCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mCache.put(url, bitmap);
    }
}
