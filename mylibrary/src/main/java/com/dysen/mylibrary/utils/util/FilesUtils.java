package com.dysen.mylibrary.utils.util;

import android.os.Environment;

import com.dysen.mylibrary.utils.MyDateUtils;

import java.io.File;
import java.util.Date;

/**
 * Created by dy on 2016-10-08.
 */

public class FilesUtils {

    public static String setDirName(String dirName) {
        String fileName = MyDateUtils.formatDate(new Date(), "yyyy-MM-dd_HHmmss");

        File file = new File(getSDPath() + "/"+dirName);
        if (!makeDir(file))
            return "";
        String path = file.toString();

        return path;
    }

    public static String setDirName(String dir, String dirName) {
        String fileName = MyDateUtils.formatDate(new Date(), "yyyy-MM-dd_HHmmss");

        File file = new File(dir + "/"+dirName);

        String path = file.toString();

        return path;
    }

    public static boolean makeDir(File dir) {
        if (!dir.getParentFile().exists()) {
            makeDir(dir.getParentFile());
        }
        return  dir.mkdir();
    }

    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        String dir = sdDir.toString();
        return dir;
    }
}
