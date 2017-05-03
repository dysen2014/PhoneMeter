package com.dysen.mylibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by dysen_000 on 2016-07-01 10:12.
 * Email：dysen@outlook.com
 * Info：
 */
public class MyTextView extends TextView {

    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public MyTextView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean isFocused() {
        // TODO Auto-generated method stub
        return true;
    }


    public void setText(TextView v, String text) {

        v.setText(text+"");
    }

    public String getText(TextView v) {

        return v.getText().toString().trim();
    }
}

