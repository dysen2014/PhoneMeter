package com.dysen.mylibrary.utils.volley;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;


/**
 * Created by dy on 2016-11-16.
 */

public class BitmapCache implements ImageLoader.ImageCache {

    LruCache<String, Bitmap> lruCache;
    int max = 10 *1024 *1024; //10M

    public BitmapCache(){
        lruCache = new LruCache<String, Bitmap>(max){
            @Override
            protected int sizeOf(String key, Bitmap value){

                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    @Override
    public void putBitmap(String key, Bitmap value) {
        lruCache.put(key, value);
    }

    @Override
    public Bitmap getBitmap(String key) {
        return lruCache.get(key);
    }
}
