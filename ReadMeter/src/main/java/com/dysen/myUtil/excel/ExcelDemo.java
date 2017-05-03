package com.dysen.myUtil.excel;

import android.content.Context;
import android.os.Environment;

import com.dysen.mylibrary.utils.MyDateUtils;
import com.dysen.table.tExcel;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created by dy on 2016-09-28.
 */

public class ExcelDemo {

    String[] t = {"表号", "电子表号", "用户名", "月份",	"抄表时间", "起码", "止码", "地址", "操作类型",	 "操作员姓名", "操作员编号", "备注"};
    private File file;

    /**
     *
     * @param dirName 目录名
     */
    public String initData(String dirName) {
        String fileName = MyDateUtils.formatDate(new Date(), "yyyy-MM-dd_HHmmss");

        file = new File(getSDPath() + "/"+dirName);
        makeDir(file);
        String path = getSDPath() + "/"+dirName + "/"+ fileName +".xls";
        ExcelUtils.initExcel(file.toString() + "/"+ fileName +".xls", t);

        return path;
    }

    public void export2Excel(List<tExcel> listM, String dirName, Context context){

        ExcelUtils.writeObjListToExcel(listM, initData(dirName), context);
    }

    public static void makeDir(File dir) {
        if (!dir.getParentFile().exists()) {
            makeDir(dir.getParentFile());
        }
        dir.mkdir();
    }

    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        String dir = sdDir.toString();
        return dir;
    }
}
