package com.dysen.mylibrary.utils.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

/**
 * Created by dy on 2016-10-09.
 */

public class WaterMark {

    public static Bitmap createBitmap(Bitmap src, Bitmap watermark){
        Bitmap newb = null;//创建一个保存水印的位图
        if(src == null){
            return null;
        }
        int w = src.getWidth();//原图片的宽
        int h = src.getHeight();//原图片的高
        int ww = watermark.getWidth();//水印图片的宽
        int wh = watermark.getHeight();//水印图片的高
        Log.v("wz",w+","+h+","+ww+","+wh);//日志文件中查看位图大小
        newb = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);//创建一个新的和src一样大小的位图
        Canvas cv = new Canvas(newb);//创建一个同等 大小的画布

        cv.drawBitmap(src, 0, 0, null);//从坐标0,0开始把src画入画布
        cv.drawBitmap(watermark, w-ww+5, h-wh+5, null);//在src中画入水印
        cv.save();//保存
        cv.restore();//存储
        return newb;//返回带水印的位图
    }

    public Bitmap getWaterMarkBitmap(Bitmap src){
        int w = src.getWidth();
        int h = src.getHeight();
        //压缩图片的bitmap
        float scale = (float) (1000000.0/(w*h));
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        Bitmap src1 = Bitmap.createBitmap(src, 0, 0, src.getWidth(),src.getHeight(), matrix, true);
        //释放掉原始位图
        src.recycle();
        Bitmap water = src;
        //加水印
        src1 = WaterMark.createBitmap(src1, water);

        return src1;
    }
}
