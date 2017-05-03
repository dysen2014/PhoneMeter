package com.dysen.myUtil.excel;

import android.content.Context;

import com.dysen.myUtil.ToastDemo;
import com.dysen.table.tExcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcelUtils {
	public static WritableFont arial14font = null;

	public static WritableCellFormat arial14format = null;
	public static WritableFont arial10font = null;
	public static WritableCellFormat arial10format = null;
	public static WritableFont arial12font = null;
	public static WritableCellFormat arial12format = null;

	public final static String UTF8_ENCODING = "UTF-8";
	public final static String GBK_ENCODING = "GBK";

	public static void format() {
		try {
			arial14font = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD);
			arial14font.setColour(jxl.format.Colour.LIGHT_BLUE);
			arial14format = new WritableCellFormat(arial14font);
			arial14format.setAlignment(jxl.format.Alignment.CENTRE);
			arial14format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
			arial14format.setBackground(jxl.format.Colour.VERY_LIGHT_YELLOW);
			arial10font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
			arial10format = new WritableCellFormat(arial10font);
			arial10format.setAlignment(jxl.format.Alignment.CENTRE);
			arial10format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
			arial10format.setBackground(jxl.format.Colour.LIGHT_BLUE);
			arial12font = new WritableFont(WritableFont.ARIAL, 12);
			arial12format = new WritableCellFormat(arial12font);
			arial12format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
		}
		catch (WriteException e) {

			e.printStackTrace();
		}
	}

	public static void initExcel(String fileName, String[] colName) {
		format();
		WritableWorkbook workbook = null;
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			workbook = Workbook.createWorkbook(file);
			WritableSheet sheet = workbook.createSheet("潜江抄表流水", 0);
//			sheet.addCell((WritableCell) new Label(0, 0, fileName, arial14format));
			sheet.addCell(new Label(5, 0, "抄表流水", arial10format));
			for (int col = 0; col < colName.length; col++) {
				sheet.addCell(new Label(col, 1, colName[col], arial10format));
			}
			workbook.write();
		}
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> void writeObjListToExcel(List<tExcel> objList, String fileName, Context c) {
		if (objList != null && objList.size() > 0) {
			WritableWorkbook writebook = null;
			InputStream in = null;
			try {
				WorkbookSettings setEncode = new WorkbookSettings();
				setEncode.setEncoding(UTF8_ENCODING);
				in = new FileInputStream(new File(fileName));
				Workbook workbook = Workbook.getWorkbook(in);
				writebook = Workbook.createWorkbook(new File(fileName), workbook);
				WritableSheet sheet = writebook.getSheet(0);
				sheet.getSettings().setPassword("123"); // 设置xls的密码
				sheet.getSettings().setDefaultColumnWidth(15); // 设置列的默认宽度10 ,2cm左右
				sheet.setColumnView(7, 300);// 设置第8列宽度，6cm左右
				sheet.setColumnView(4, 300);

				for (int i = 0; i < objList.size(); i++) {

					sheet.addCell(new Label(0, i+2, objList.get(i).getMeterId(), arial12format));
					sheet.addCell(new Label(1, i+2, objList.get(i).getAmrId(), arial12format));
					sheet.addCell(new Label(2, i+2, objList.get(i).getUserName(), arial12format));
					sheet.addCell(new Label(3, i+2, objList.get(i).getMonth(), arial12format));
					sheet.addCell(new Label(4, i+2, objList.get(i).getReadTime(), arial12format));
					sheet.addCell(new Label(5, i+2, ""+objList.get(i).getReadStart(), arial12format));
					sheet.addCell(new Label(6, i+2, ""+objList.get(i).getReadEnd(), arial12format));
					sheet.addCell(new Label(7, i+2, objList.get(i).getAddr(), arial12format));
					sheet.addCell(new Label(8, i+2, objList.get(i).getReadType(), arial12format));
					sheet.addCell(new Label(9, i+2, objList.get(i).getReadName(), arial12format));
					sheet.addCell(new Label(10, i+2, objList.get(i).getReadNumber(), arial12format));
					sheet.addCell(new Label(11, i+2, objList.get(i).getReadInfo(), arial12format));
				}
				writebook.write();
				ToastDemo.myHint(c, "导出数据成功", 1);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				if (writebook != null) {
					try {
						writebook.close();
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (in != null) {
					try {
						in.close();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}else {
			ToastDemo.myHint(c, "没有需要导出的数据", 1);
		}
	}

	public static Object getValueByRef(Class cls, String fieldName) {
		Object value = null;
		fieldName = fieldName.replaceFirst(fieldName.substring(0, 1), fieldName.substring(0, 1).toUpperCase());
		String getMethodName = "get" + fieldName;
		try {
			Method method = cls.getMethod(getMethodName);
			value = method.invoke(cls);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
}
