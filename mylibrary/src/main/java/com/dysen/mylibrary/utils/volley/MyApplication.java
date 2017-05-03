package com.dysen.mylibrary.utils.volley;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by dy on 2016-11-15.
 */

public class MyApplication extends Application {

    public static RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();

        requestQueue = Volley.newRequestQueue(this);
    }

    public static RequestQueue getHttpQueues(){
        return requestQueue;
    }
}
