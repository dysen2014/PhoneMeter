package com.dysen.mylibrary.utils.volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.dysen.mylibrary.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dy on 2016-11-15.
 */

public class VolleyRequest {

    public static StringRequest stringRequest;

    public static void RequestGet(Context context, String url, String tag, VolleyInterface volleyInterface){

        MyApplication.getHttpQueues().cancelAll(tag);
        stringRequest = new StringRequest(Request.Method.GET, url, volleyInterface.mListener(), volleyInterface.mErrorListener());
        stringRequest.setTag(tag);
        MyApplication.getHttpQueues().add(stringRequest);
        MyApplication.getHttpQueues().start();
    }

    public static void RequestPost(Context context, String url, String tag, final HashMap<String, String> params, VolleyInterface volleyInterface){

        MyApplication.getHttpQueues().cancelAll(tag);
        stringRequest = new StringRequest(Request.Method.POST, url, volleyInterface.mListener(), volleyInterface.mErrorListener()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return params;
            }
        };
        stringRequest.setTag(tag);
        MyApplication.getHttpQueues().add(stringRequest);
        MyApplication.getHttpQueues().start();
    }

    /**
     * 利用Volley ImageRequest异步加载图片
     */
    public static void imgRequest(String url, ImageInterface imageInterface) {

        ImageRequest imageRequest = new ImageRequest(url, imageInterface.mListenerBitmap(), 0, 0, Bitmap.Config.RGB_565, imageInterface.mErrorListener());

        MyApplication.getHttpQueues().add(imageRequest);
    }

    /**
     * 利用Volley异步加载图片
     *
     * 注意方法参数:
     * getImageListener(ImageView view, int defaultImageResId, int errorImageResId)
     * 第一个参数:显示图片的ImageView
     * 第二个参数:默认显示的图片资源
     * 第三个参数:加载错误时显示的图片资源
     */
    public static void loadImageByVolley(String url, ImageView mImageView){

        ImageLoader imageLoader = new ImageLoader(MyApplication.getHttpQueues(), new BitmapCache());
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(mImageView, R.drawable.icon_back, R.drawable.ic_back);
        imageLoader.get(url, listener);
    }

    /**
     * 利用NetworkImageView显示网络图片
     */
    public static void showImageByNetworkImageView(String url, NetworkImageView mNetworkImageView){

        ImageLoader imageLoader = new ImageLoader(MyApplication.getHttpQueues(), new BitmapCache());
        mNetworkImageView.setTag("url");
        mNetworkImageView.setImageUrl(url,imageLoader);
    }
}
