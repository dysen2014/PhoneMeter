package com.dysen.mylibrary.utils.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by dy on 2016-08-27.
 */
public class DialogUtils {

    private  Context context;
    int imgId;
    final String items_single[]={"男","女"};
    final String items[]={"篮球","足球","排球"};
    String mConfirm, mIgnore, mCancel, mTitle, mMsg;

    public DialogUtils(Context context){

        this.context = context;
    }

    public void dialog(){

        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setPositiveButton(mConfirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                // 退出
                dialog.dismiss();
            }
        });
        alert.setNegativeButton(mCancel, new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.create().show();
    }

    public void dialog1(){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);  //先得到构造器
        builder.setTitle(mTitle); //设置标题
        builder.setMessage("是否确认"+ mMsg +"?"); //设置内容
//        builder.setIcon(imgId);//设置图标，图片id即可
        builder.setPositiveButton(mConfirm, new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //关闭dialog
                Toast.makeText(context, mConfirm + which, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(mCancel, new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(context, mCancel + which, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNeutralButton(mIgnore, new DialogInterface.OnClickListener() {//设置忽略按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(context, mIgnore + which, Toast.LENGTH_SHORT).show();
            }
        });
        //参数都设置完成了，创建并显示出来
        builder.create().show();
    }
    public void dialog1_1(){
        //先new出一个监听器，设置好监听
        DialogInterface.OnClickListener dialogOnclicListener=new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case Dialog.BUTTON_POSITIVE:
                        Toast.makeText(context, mConfirm + which, Toast.LENGTH_SHORT).show();
                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        Toast.makeText(context, mCancel + which, Toast.LENGTH_SHORT).show();
                        break;
                    case Dialog.BUTTON_NEUTRAL:
                        Toast.makeText(context, mIgnore + which, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        //dialog参数设置
        AlertDialog.Builder builder=new AlertDialog.Builder(context);  //先得到构造器
        builder.setTitle(mTitle); //设置标题
        builder.setMessage("是否确认"+ mMsg +"?"); //设置内容
//        builder.setIcon(imgId);//设置图标，图片id即可
        builder.setPositiveButton(mConfirm,dialogOnclicListener);
        builder.setNegativeButton(mCancel, dialogOnclicListener);
        builder.setNeutralButton(mIgnore, dialogOnclicListener);
        builder.create().show();
    }
    public void dialog2() {
        
        //dialog参数设置
        AlertDialog.Builder builder=new AlertDialog.Builder(context);  //先得到构造器
        builder.setTitle(mTitle); //设置标题
        //builder.setMessage("是否确认"+ mMsg +"?"); //设置内容
//        builder.setIcon(imgId);//设置图标，图片id即可
        //设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
        builder.setItems(items,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(context, items[which], Toast.LENGTH_SHORT).show();

            }
        });
        builder.setPositiveButton(mConfirm,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(context, mConfirm, Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }
    public void dialog3(){
        
        AlertDialog.Builder builder=new AlertDialog.Builder(context);  //先得到构造器
        builder.setTitle(mTitle); //设置标题
//        builder.setIcon(imgId);//设置图标，图片id即可
        builder.setSingleChoiceItems(items_single,0,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.dismiss();
                Toast.makeText(context, items_single[which], Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton(mConfirm,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(context, mConfirm, Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }
    public void dialog4(){
        
        final boolean selected[]={true,false,true};
        AlertDialog.Builder builder=new AlertDialog.Builder(context);  //先得到构造器
        builder.setTitle(mTitle); //设置标题
//        builder.setIcon(imgId);//设置图标，图片id即可
        builder.setMultiChoiceItems(items,selected,new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // dialog.dismiss();
                Toast.makeText(context, items[which]+isChecked, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton(mConfirm,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(context, mConfirm, Toast.LENGTH_SHORT).show();
                //android会自动根据你选择的改变selected数组的值。
                for (int i=0;i<selected.length;i++){
                    Log.e("hongliang",""+selected[i]);
                }
            }
        });
        builder.create().show();
    }
}
