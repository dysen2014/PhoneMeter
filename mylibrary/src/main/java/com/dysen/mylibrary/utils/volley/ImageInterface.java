package com.dysen.mylibrary.utils.volley;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.dysen.mylibrary.utils.util.ToastUtils;

/**
 * Created by dy on 2016-11-16.
 */

public abstract class ImageInterface {

    private Context context;
        public static Listener<Bitmap> bitmapListener;
        public static ErrorListener errorListener;

    public ImageInterface(Context context, Listener<Bitmap> bitmapListener, ErrorListener errorListener){

        this.context = context;
        this.errorListener = errorListener;
        this.bitmapListener = bitmapListener;
    }

        public abstract void onMySuccessBitmap(Bitmap bitmap);
        public abstract void onMyError(VolleyError error);

        public Listener<Bitmap> mListenerBitmap(){
            bitmapListener = new Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap bitmap) {

                    //弹出加载中
                    ToastUtils.showLong(context, "加载中", 1);

                    onMySuccessBitmap(bitmap);
                }
            };
            return bitmapListener;
        }

        public ErrorListener mErrorListener(){
            errorListener = new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    onMyError(error);
                    //提示请求失败
                    ToastUtils.showLong(context, "请求失败", 2);
                }
            };
            return errorListener;
        }
}
