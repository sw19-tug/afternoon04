package at.tugraz.ist.swe;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class BitmapCache {

    private static int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    private static int cacheSize = maxMemory / 8;

    public BitmapCache()
    {

    }

    public static LruCache<String, Bitmap> mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
        @Override
        protected int sizeOf(String key, Bitmap bitmap) {
            // The cache size will be measured in kilobytes rather than
            // number of items.
            return bitmap.getByteCount() / 1024;
        }
    };



}
