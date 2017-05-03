package com.dysen.mylibrary.utils.volley;

import android.content.Context;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.dysen.mylibrary.utils.util.ToastUtils;

/**
 * Created by dy on 2016-11-15.
 */

public abstract class VolleyInterface {

    public Context context;
    public static Listener<String> listener;
    public static ErrorListener errorListener;

    public VolleyInterface(Context context, Listener<String> listener, ErrorListener errorListener){

        this.context = context;
        this.listener = listener;
        this.errorListener = errorListener;
    }

    public abstract void onMySuccess(String s);
    public abstract void onMyError(VolleyError error);

    public Listener<String> mListener(){
        listener = new Listener<String>() {
            @Override
            public void onResponse(String s) {

                //弹出加载中
                ToastUtils.showLong(context, "加载中", 1);

                onMySuccess(s);
            }
        };
        return listener;
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
