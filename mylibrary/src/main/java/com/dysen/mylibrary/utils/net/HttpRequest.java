package com.dysen.mylibrary.utils.net;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.dysen.mylibrary.utils.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.dysen.mylibrary.utils.base.BaseActivity.TAG;

public class HttpRequest {
	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
//	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
//	public static String sendGet(String url) {
//		String result = "";
//		BufferedReader in = null;
//		try {
//			URL realUrl = new URL(url);
//			// 打开和URL之间的连接
//			URLConnection connection = realUrl.openConnection();
//			// 设置通用的请求属性
//			connection.setRequestProperty("accept", "*/*");
//			connection.setRequestProperty("connection", "Keep-Alive");
//			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//			// 建立实际的连接
//			connection.connect();
//			// 获取所有响应头字段
//			Map<String, List<String>> map = connection.getHeaderFields();
//			// 遍历所有的响应头字段
//			// for (String key : map.keySet()) {
//			// System.out.println(key + "--->" + map.get(key));
//			// }
//			// 定义 BufferedReader输入流来读取URL的响应
//			in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
//			String line;
//			while ((line = in.readLine()) != null) {
//				result += line;
//			}
//		} catch (Exception e) {
//			System.out.println("发送GET请求出现异常！" + e);
//			e.printStackTrace();
//		}
//		// 使用finally块来关闭输入流
//		finally {
//			try {
//				if (in != null) {
//					in.close();
//				}
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//		return result;
//	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			// 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
			// http正文内，因此需要设为true, 默认情况下是false;
			conn.setDoOutput(true);
			// 设置是否从httpUrlConnection读入，默认情况下是true;
			conn.setDoInput(true);
//			conn.setConnectTimeout(5000);
//			conn.setReadTimeout(5000);
			System.setProperty("sun.net.client.defaultConnectTimeout", "50000");
			System.setProperty("sun.net.client.defaultReadTimeout", "50000");

			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

//	public static String doGet(String url){
//		String result = "";
//
//		HttpGet httpGet = new HttpGet(url);
//		try {
//			HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
//			if (httpResponse.getStatusLine().getStatusCode == 200){
//
//			}else{
//
//			}
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return result;
//	}

	public static String volleyStringGet(RequestQueue requestQueue, String url){
		String result = "";

		StringRequest stringRequest = new StringRequest(url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d(TAG, response);
						System.out.println(response);
//						result = response;
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, error.getMessage(), error);
//						result = "";
					}
				}
		);

		requestQueue.add(stringRequest);
		return result;
	}

	public static void sendGetRequest(RequestQueue requestQueue, String url){


		//四个参数,第一个 url 其中第二个参数代表http方法，第三个和第四个分别是响应监听和响应错误监听

		JsonObjectRequest jsonRequest = new JsonObjectRequest(url, null,
				new Response.Listener<JSONObject>(){


					@Override
					public void onResponse(JSONObject response){
						//添加自己的响应逻辑，
						System.out.println(response+"===\n"+response.toString());
						parseResponse(response);
					}
				},
				new Response.ErrorListener(){
					@Override
					public void onErrorResponse(VolleyError error) {
						//错误处理
						LogUtils.i("Error Message:","Error is"+error);
					}
				});

		requestQueue.add(jsonRequest);
	}

	static List<Map<String, String>> list = new ArrayList<Map<String,String>>();
	private static List<Map<String, String>> parseResponse(JSONObject jsonObject) {
		list.clear();
		Iterator<String> it = jsonObject.keys();
		while (it.hasNext()) {
			String key = it.next();
			JSONObject obj = null;
			try {
					obj = jsonObject.getJSONObject(key);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			if (obj != null) {
				Iterator<String> objIt = obj.keys();
				while (objIt.hasNext()) {
					String objKey = objIt.next();
					String objValue;
					try {
						objValue = obj.getString(objKey);
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("title", objKey);
						map.put("content", objValue);
						list.add(map);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return list;
	}
}