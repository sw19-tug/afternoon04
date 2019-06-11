package at.tugraz.ist.swe;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class BitmapCache {

    private static int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    private static int cacheSize = maxMemory / 8;
    public static int rotation = 0;
    public static int oldRotation = 0;
    public final static int max_undo_steps = 10;
    public static int current_step = 0;
    public static boolean redo_overflow = false;
    public static int current_undo = 0;
    public static int oldBitmap = 0;

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
