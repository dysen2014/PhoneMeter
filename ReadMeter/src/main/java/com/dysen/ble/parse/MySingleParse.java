package com.dysen.ble.parse;

import android.util.Log;

import com.dysen.myUtil.IsNumeric;
import com.dysen.myUtil.MyDateUtils;
import com.dysen.myUtil.MyStringConversion;
import com.dysen.myUtil.MyUtils;
import com.dysen.mylibrary.utils.util.LogUtils;
import com.dysen.table.tLoraMeter;
import com.dysen.table.tReadMeter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author dysen
 * @version 2015-1-17 下午2:07:13
 */
public class MySingleParse {

	static String funcNum;
	static String[] sRate = new String[]{"300", "600", "1200", "2400", "4800", "9600", "14400", "19200", "38.4K", "57600", "80K", "125K", "250K", "521K"
			, "768K", "1M", "1.5M", "2M", "Other"};


	public static String getPkgHeager(String str, String fomat) {

		Pattern p=Pattern.compile(fomat);
		Matcher m=p.matcher(str);
		while(m.find()){
			System.out.println(m.group());
			return m.group();
		}
		return "";
	}

	public static List<String> searchReadData(String s) {

		// 获取3c开头的下标
		List<Integer> intList = MyUtils.seachString(s, "3C");
		List<String> strList = new ArrayList<String>();

		LogUtils.i("intList="+intList);
		// 截取3c字符串集合
		if (intList != null && intList.size() > 0 && s.length() >= 26) {
			for (int i : intList) {

				if (s.length() >= i + 26){

					funcNum = s.substring(i + 16, i + 18);
					Log.i("dysen", intList.size()+"---i="+i+"\n funcNum="+funcNum);
					if (funcNum.equals("F4")){//设无线配置
						if (i+36 <= s.length()) {
							strList.add(s.substring(i, i + 36));
						}
					}else if (funcNum.equals("F9")){//唤醒
						if (i+36 <= s.length()) {
							strList.add(s.substring(i, i + 36));
						}
					}else if (funcNum.equals("22") || funcNum.equals("EA")) {//抄表

						if (i + 82 <= s.length()) {
							// 截取22判断是否为单抄
							strList.add(s.substring(i, i + 82));
						}
					}else if(funcNum.equals("24")){// lora 抄表
						if ((i + 56 <= s.length())){
							strList.add(s.substring( i + 56));
						}
					}else if (funcNum.equals("F5")){
						if (i+52 <= s.length()) {//读无线配置
							// 截取功能号判断
							strList.add(s.substring(i, i + 52));
						}
					}else if (funcNum.equals("42")){
						if (i+60 <= s.length()){
							strList.add(s.substring(i, i+60));
						}
					}else if (funcNum.equals("43")){
						if (i+28 <= s.length()){
							strList.add(s.substring(i, i+28));
						}
					}
				}
			}
			return strList;
		}else {
			Log.i("dysen", "pakg len:"+s.length()+"\n协议接收不完整1：");
			if (s.equals("B5F11F")){
				strList.add(s.substring(0));
				return strList;
			}else {}
		}
		return null;
	}

	public static List<tReadMeter> singleParse(String s) {

		// 获取3c开头的下标
		List<Integer> intList = MyUtils.seachString(s, getPkgHeager(s, "3C"));
		List<String> strList = new ArrayList<String>();
		// 截取3c字符串集合
		if (intList != null && intList.size() > 0) {
			for (int i : intList) {
//				Log.i("dysen", i+"pakg len:"+s.length());
				if (s.length() > i+26){

					funcNum = s.substring(i + 16, i + 18);
					// System.out.println(funcNum + "----");
//					Log.i("dysen", "funcNum id:"+funcNum);

						if (funcNum.equals("F4")){//设无线配置
							if (i+36 <= s.length()) {
								strList.add(s.substring(i, i + 36));
							}
						}else if (funcNum.equals("F9")){//唤醒
							if (i+36 <= s.length()) {
								strList.add(s.substring(i, i + 36));
							}
						}else if (funcNum.equals("22") || funcNum.equals("EA")) {//抄表

							if (i + 82 <= s.length()) {
								// 截取22判断是否为单抄
								strList.add(s.substring(i, i + 82));
							}
						}else if(funcNum.equals("24")){// lora 抄表
							if ((i + 56 <= s.length())){

								strList.add(s.substring(i, i + 56));
							}
						}else if (funcNum.equals("F5")){
							if (i+52 <= s.length()) {//读无线配置
								// 截取功能号判断
								strList.add(s.substring(i, i + 52));
							}
						}else if (funcNum.equals("42")){
							if (i+60 <= s.length()){
								strList.add(s.substring(i, i+60));
							}
						}else if (funcNum.equals("43")){
							if (i+28 <= s.length()){
								strList.add(s.substring(i, i+28));
							}
						}
				}else{
					Log.i("dysen", "pakg len:"+s.length()+"\n协议接收不完整2：");
				}
			}
			return  str2tReadMeter(strList, funcNum);
		}
		return null;
	}

	public static List<tReadMeter> str2tReadMeter(List<String> strList, String funcNum) {

		List<tReadMeter> ms = new ArrayList<tReadMeter>();
		tReadMeter m = new tReadMeter();

		if (funcNum.equals("22") || funcNum.equals("EA")){
			// 开始解析
			if (strList != null && strList.size() > 0) {
				for (String s1 : strList) {
//					Log.i("dysen", "&&&&&&&&&&&&&&&&&&&&&&&&&&解析："+s1);
					// 效验和
					String sum = MyUtils.HexSUM(s1.substring(2, 80));
					// System.out.println(sum);
					if (s1.substring(80).equals(sum)) {
						// System.out.println(s1.substring(80).equals(sum));
						// 截取每部分
						String netNo = s1.substring(4, 8);// 网号
						long meterSn = Long.valueOf(s1.substring(8, 16), 16);// 表号
						// short version = Short.valueOf(s1.substring(28, 30), 16);
						long meterEnd = Long.valueOf(s1.substring(30, 38), 16);// 读数
						long scaling_factor = Long.valueOf(s1.substring(38, 40),
								16) % 16;// 比例
						long meterSty = Long.valueOf(s1.substring(38, 40), 16) / 16;// 表类型
						long meterVoltage = Long
								.valueOf(s1.substring(40, 42), 16);// 电压
						long meterStat1 = Long.valueOf(s1.substring(42, 44), 16);// 表状态1
						long meterStat2 = Long.valueOf(s1.substring(44, 46), 16);// 表状态2
						long meterPrepaid = Long
								.valueOf(s1.substring(46, 54), 16);// 预付费
						long meterStar = Long.valueOf(s1.substring(54, 62), 16);// 底数
						long meterTemperature = Long.valueOf(
								s1.substring(62, 64), 16);// 温度
						// 将表号网号高低位调换,返回十六进制
						netNo = MyUtils.HexHightLowConvert(
								Long.valueOf(netNo, 16)).substring(0, 4);
						// System.out.println(netNo + "------");
						String strSn = Long.valueOf(
								MyUtils.HexHightLowConvert(meterSn), 16).toString();// 已转int
						meterEnd = Long.valueOf(
								MyUtils.HexHightLowConvert(meterEnd), 16);// 已转int
						meterStar = Long.valueOf(
								MyUtils.HexHightLowConvert(meterStar), 16);
						// 添加到MeterDataFlow

						m.setNetId(Short.valueOf(netNo, 16));// 网号
						m.setMeterId(strSn);// 表号
						m.setLong_meterEnd(meterEnd);// 读数
						m.setDscaling_factor(MyParseTools
								.sfactorInfo(scaling_factor));// 比例
						m.setMeterSty(MyParseTools.meterStyInfo(meterSty));// 表类型
						m.setMeterVoltage1((meterVoltage % 8 + 30) / 10);// 电压1
						m.setMeterVoltage2((meterVoltage / 8 + 30) / 10);// 电压2
						m.setMeterStat1(MyParseTools.meterStat1Info(meterStat1));// 表状态1
						m.setMeterStat2(MyParseTools.meterStat2Info(meterStat2));// 表状态2
						m.setMeterPrepaid(meterPrepaid);// 预付费
						m.setMeterStar(meterStar);// 底数
						m.setMeterTemperature(meterTemperature / 2 - 20);// 温度
						m.setSrcString("");// 原二进制码
						m.setMeterDate(MyDateUtils.formatDate(new Date(), "yy-MM-dd HH:mm:ss"));
						m.setMeterDate2(new Date());
						ms.add(m);
					}
				}
				return ms;
			}else {
				Log.i("dysen", "查表数据有误！！！");
			}
		}else if (funcNum.equals("F4")){

			for (String s1 : strList) {
				// 效验和
				String sum = MyUtils.HexSUM(s1.substring(2, 34));
				if (s1.substring(34).equals(sum)) {
					m.setSrcString("配置设置成功");
					ms.add(m);
				}
			}
			return ms;
		}else if (funcNum.equals("F5")){
			for (String s1 : strList) {
				// 效验和
				String sum = MyUtils.HexSUM(s1.substring(2, 50));
				// System.out.println(sum);
				if (s1.substring(50).equals(sum)) {

					String rate = s1.substring(24, 26);//速率
					String capacity = s1.substring(26, 28);//功率
					String frequency = s1.substring(32, 36);//频率
					long lRate = Long.valueOf(
							MyStringConversion.myInverseConver(rate, 2), 16);

					m.setRate(sRate[(int)lRate]);
					m.setCapacity(Long.valueOf(
							MyStringConversion.myInverseConver(capacity, 2), 16));
					m.setFrequency(Long.valueOf(
							MyStringConversion.myInverseConver(frequency, 4), 16));
					ms.add(m);
				}
			}
			return ms;
		}else if (funcNum.equals("F9")){

			for (String s1 : strList) {
				// 效验和
				String sum = MyUtils.HexSUM(s1.substring(2, 34));
				if (s1.substring(34).equals(sum)) {
					m.setSrcString("唤醒成功");
					ms.add(m);
				}
			}
			return ms;
		}else if (funcNum.equals("24")){// lora 抄表
			for (String s1 : strList) {
				// 效验和
				String sum = MyUtils.HexSUM(s1.substring(2, 54));
				if (s1.substring(54).equals(sum)) {

					// 截取每部分
					long netNo = Long.valueOf(s1.substring(4, 8));// 网号
					long meterSn = Long.valueOf(s1.substring(8, 16), 16);// 表号
					long remainingVolume = Long.valueOf(s1.substring(42, 46), 16);//剩余气量

//					LogUtils.i(meterSn +""+remainingVolume);

					m.setMeterId(Long.valueOf(MyUtils.HexHightLowConvert(meterSn), 16)+"号");
					LogUtils.i(MyUtils.HexHightLowConvert(remainingVolume)+"********"+remainingVolume);
					m.setRemainingVolume(((float)Long.valueOf(MyUtils.HexHightLowConvert(remainingVolume))/100000)+"m³");
					ms.add(m);
				}
			}
			return ms;
		}else if (funcNum.equals("42")){//lora 读表信息

			for (String s1 : strList) {
				// 效验和
				String sum = MyUtils.HexSUM(s1.substring(2, 58));
				if (s1.substring(58).equals(sum)) {

					// 截取每部分
					String lSn = s1.substring(24, 32);
					String lNetId = s1.substring(32, 36);
					String lMeterId = s1.substring(36, 44);
					String lCornetId = s1.substring(44, 48);
					String lAreaId = s1.substring(48, 56);
					String lRepeater = s1.substring(56, 58);

					System.out.println("sn="+lSn+"netId="+lNetId+"meterId="+lMeterId+"cornetId="+lCornetId+"areaId="+lAreaId+"repeater="+lRepeater);

					m.setlSn(Long.valueOf(MyStringConversion.myInverseConver(lSn, 8), 16)+"");
					m.setlCornetId(Long.valueOf(MyStringConversion.myInverseConver(lCornetId, 4), 16)+"");
					m.setlNetId(Long.valueOf(MyStringConversion.myInverseConver(lNetId, 4), 16)+"");
					m.setlMeterId(Long.valueOf(MyStringConversion.myInverseConver(lMeterId, 8), 16)+"");
					m.setlAreaId(Long.valueOf(MyStringConversion.myInverseConver(lAreaId, 8), 16)+"");
					m.setlRepeater(Long.valueOf(MyStringConversion.myInverseConver(lRepeater, 2), 16)+"");
					ms.add(m);
				}
			}
			return ms;
		}else if (funcNum.equals("43")){
			for (String s1 : strList) {
				// 效验和
				String sum = MyUtils.HexSUM(s1.substring(2, 26));
				if (s1.substring(26).equals(sum)) {
					// 截取每部分
					String setSate = s1.substring(24, 26);
					m.setSetState(Long.valueOf(MyStringConversion.myInverseConver(setSate, 2), 16)+"");
					ms.add(m);
				}
			}
			return ms;
		}
//		else if (funcNum.equals("F9")){
//
//			for (String s1 : strList) {
//		// 效验和
//		String sum = MyUtils.HexSUM(s1.substring(2, 80));
//		if (s1.substring(50).equals(sum)) {
//
//		}
//			}
//			return ms;
//		}
		return null;
	} 

	public static List<tLoraMeter> str2LoraMeter(List<String> strList, String funcNum){

		List<tLoraMeter> listLoraMeter = new ArrayList<tLoraMeter>();
		tLoraMeter loraMeter = new tLoraMeter();

		if(funcNum.equals("24")){// lora 抄表

			// 开始解析
			if (strList != null && strList.size() > 0) {
				for (String s1 : strList) {
//					Log.i("dysen", "&&&&&&&&&&&&&&&&&&&&&&&&&&解析："+s1);
					// 效验和
					String sum = MyUtils.HexSUM(s1.substring(2, 54));
					// System.out.println(sum);
					if (s1.substring(54).equals(sum)) {
						// System.out.println(s1.substring(80).equals(sum));
						// 截取每部分
						long netNo = Long.valueOf(s1.substring(4, 8));// 网号
						long meterSn = Long.valueOf(s1.substring(8, 16));// 表号
						long remainingVolume = Long.valueOf(s1.substring(42, 46));//剩余气量

						loraMeter.setNetId(MyUtils.HexHightLowConvert(netNo));
						loraMeter.setMeterId(MyUtils.HexHightLowConvert(meterSn));
						loraMeter.setRemainingVolume(MyUtils.HexHightLowConvert(remainingVolume));

						listLoraMeter.add(loraMeter);
					}
				}
				return listLoraMeter;
			}
		}
		return null;
	}

	public static String mySingleParseStr(String name, String str, int id) {

		String strShow = "";
		String strHexShow = "";
		List<tReadMeter> ms = singleParse(str);
		if (ms != null && ms.size() > 0) {

			for (tReadMeter m : ms) {

				if(name.equals("BleWirelessDebugActivity") && (id == 0 || id == 3)){

					strShow = m.getSrcString();
					strHexShow +=
//						strShow +
							"\n"
							+m.getSrcString();
				} else if(name.equals("LoraReadMeterActivity") && id == 1){

					strHexShow +=  "表号："
							+ m.getMeterId()
							+ "\t\t\t剩余气量："
							+ m.getRemainingVolume()
					+"\n";
				}else if(name.equals("LoraReadMeterActivity") && id == 2){
					strHexShow +=  "SN："
							+ m.getlSn()
							+"网号："
							+ m.getlNetId()
							+"表号："
							+ m.getlMeterId()
							+"短号："
							+ m.getlCornetId()
							+"地域号："
							+ m.getlAreaId()
							+"中继属性："
							+ (IsNumeric.isNumeric(m.getlRepeater()) ? (Integer.parseInt(m.getlRepeater()) == 1 ? "有中继属性" : "无中继属性") : m.getlRepeater());

				}else if(name.equals("LoraReadMeterActivity") && id == 3){
					strHexShow +=  "设表信息状态：\t"+(m.getSetState().equals("1")?"设置成功":"设置失败");
				}else if (name.equals("BleReadMeterActivity") && id == 1 || id == 2) {

					strShow = m.getSrcString();
					strHexShow +=
//						strShow +
							"\n"
									+ "表号："
									+ m.getMeterId()
									+ "   读数："
									+ m.getLong_meterEnd()
									+ "   比例："
									+ m.getDscaling_factor()// sfactor.get(scaling_factor)
									+ "   表类型：" + m.getMeterSty() + "   电压1："
									+ m.getMeterVoltage1() + "V" + "   电压2："
									+ m.getMeterVoltage2() + "V" + "   表状态1："
									+ m.getMeterStat1() + "   表状态2：" + m.getMeterStat2()
									+ "   预付费：" + m.getMeterPrepaid() + "   底数："
									+ m.getMeterStar() + "   温度：" + m.getMeterTemperature()
									+ "°C" + "   时间：" + m.getMeterDate() + "   保留：" + "" + "\n";
				}else if(name.equals("BleWirelessDebugActivity") && id == 4){//{"唤醒", "抄表(读单元)", "基站抄表","设无线配置", "读无线配置", "单元开阀", "单元关阀", "设SN", "读SN", "设网号"}
					strHexShow +=
//							strShow
					"\n" + "频率：" +m.getFrequency() + ",	功率：" + m.getCapacity() + ",	速率：" + m.getRate();
				}else {

				}
			}
			return strHexShow;
		}
		return "";
	}
}
