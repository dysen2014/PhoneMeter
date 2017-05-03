package com.dysen.myUtil.fileIo;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MyFileTools {

	static int i = 0;
	static File f;
	static File saveFile;
	static File BuildDir;
	static File sdCardDir;

	public static void main(String[] args) throws IOException {

		String str = "记录\n";
		String fileSrc = "D:\\hello.txt";

		// 得到 文件描述符
		File fd = myCreatFile(fileSrc);
		f = myCreatFile("D:\\i.txt");

		// 向文件里写数据
		myWrite(fd, str, true);

		// 从文件里读数据
		myRead(fd);
		System.out.println(myRead(fd));

	}

	public static File myCreatFile2(String fileName) {

		fileName = fileName + ".txt"; // 在文件名末尾加上.txt
		sdCardDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		// .getExternalStorageDirectory(); // 得到SD卡根目录
		BuildDir = new File(sdCardDir, "/data"); // 打开data目录，如不存在则生成
		System.out.println("创建成功" + BuildDir);
		if (BuildDir.exists() == false)
			BuildDir.mkdirs();
		saveFile = new File(BuildDir, fileName);
		System.out.println("success" + saveFile);
		return saveFile;
	}

	public static File myCreatFile(String fileSrc) {

		File fd = new File(fileSrc);
		try {
			fd.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return fd;
	}

	public static void myWrite(File fd, String str, boolean bl)
			throws IOException {

		OutputStream out = new FileOutputStream(fd, bl);// true 表示追加
		// FileOutputStream stream = new FileOutputStream(
		// f); // 打开文件输入流
		// OutputStreamWriter out = new OutputStreamWriter(
		// stream, "utf-8");

		byte[] b = str.getBytes();

		out.write(b);
		out.close();
	}

	public static String myRead(File fd) throws IOException {

		InputStream is = new FileInputStream(fd);

		byte[] b = new byte[is.available()];
		is.read(b);
		is.close();

		return new String(b);
	}

}
