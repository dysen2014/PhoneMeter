package com.dysen.mylibrary.utils.util;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by dy on 2016-08-26.
 *
 */
public class ViewUtils {

    public ViewUtils(){
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void setText(Context context, String value, Object object){

        if (object instanceof TextView){
            ((TextView) object).setText(value);
        }else if (object instanceof Button){
            ((Button) object).setText(value);
        }else if (object instanceof EditText){
            ((EditText) object).setText(value);
        }else {

        }
    }

    public static String getText(Object object){

        if (object instanceof TextView){
            return  ((TextView) object).getText().toString();
        }else if (object instanceof Button){
            return  ((Button) object).getText().toString();
        }else if (object instanceof EditText){
            return  ((EditText) object).getText().toString();
        }else {

        }
        return "";
    }
}

