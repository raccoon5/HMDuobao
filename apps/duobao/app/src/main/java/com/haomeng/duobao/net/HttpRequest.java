package com.haomeng.duobao.net;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.haomeng.duobao.R;
import com.haomeng.duobao.utils.MD5EncodeUtil;
import com.haomeng.duobao.utils.Utils;
import com.haomeng.duobao.utils.VolleyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HttpRequest {

	/**
	 * get请求
	 */
	public static void loadDataByGet(final Context context, String url, final Map<String, String> map, final Handler handler, boolean ifLoadCache, final int requestCode,
									 final ReceiveData receiveData) {
//		final Map<String,String> EncodeMap = new HashMap<>();
//		map = getSign(context, map);
		if (map != null) {
			url = url+"/?";
			Iterator<?> it = map.entrySet().iterator();
			while (it.hasNext()) {
				Entry<?, ?> entry = (Entry<?, ?>) it.next();
				if (entry.getValue() == null) {
					continue;
				}
//					EncodeMap.put(entry.getKey().toString(), URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
				url = url + entry.getKey() + "=" + entry.getValue().toString() + "&";
			}
		}

		System.out.println("===============requestUrl==="+url);
		if (Utils.hasNetwork(context)){
			StringRequest request = new StringRequest(url, new Response.Listener<String>() {

				@Override
				public void onResponse(String response) {
					System.out.println("=================response get====="+response.toString());
					parseData(context, response, handler, requestCode, receiveData);
				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					Message msg = new Message();
					msg.what = Utils.ERROR;
					msg.obj = "网络请求失败，请重试";
					sendMessage(handler, msg);
				}
			});
		if (ifLoadCache) {
//				request.setShouldCache(true);// 设置是否缓存
//				request.setCacheTime(1);// 单位:秒
			}
			VolleyUtils.getQueue(context).add(request);
		} else {
			if (!ifLoadCache) {
				showNetUnconnect(context, handler, requestCode);
				return;
			}
			if (VolleyUtils.getQueue(context).getCache().get(url) != null) {
	            String str = new String(VolleyUtils.getQueue(context).getCache().get(url).data);
	            parseData(context, str, handler, requestCode, receiveData);
			} else {
				showNetUnconnect(context, handler, requestCode);
			}
		}
	}

	/**
	 * post请求，参数含有Map
	 * @param context
	 * @param url
	 * @param map
	 * @param handler
	 * @param ifLoadCache 无网时是否加载缓存
	 * @param requestCode
	 * @param receiveData
	 */
	public static void loadDataByPostMap(final Context context, String url, final Map<String, String> map, final Handler handler,
										 boolean ifLoadCache, final int requestCode, final ReceiveData receiveData) {
		System.out.println("===============requestUrl==="+url);
		if (Utils.hasNetwork(context)){
			StringRequest request = new StringRequest(Request.Method.POST, url,
					new Response.Listener<String>() {

						@Override
						public void onResponse(String response) {
							System.out.println("===============response post====="+ System.currentTimeMillis());
							System.out.println(response.toString());
							parseData(context, response, handler, requestCode, receiveData);
						}
					}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					System.out.println("===============response error====="+ System.currentTimeMillis());
//						byte[] htmlBodyBytes = error.networkResponse.data;
					if (error.networkResponse==null) {
						System.out.println("error");
					} else {
						System.out.println(String.valueOf(error.networkResponse.statusCode));
					}
					Message msg = new Message();
					msg.what = Utils.ERROR;
					msg.arg1 = requestCode;
					msg.obj = context.getString(R.string.net_error);
					sendMessage(handler, msg);
				}
			}) {
				@Override
				protected Map<String, String> getParams() {
					System.out.println("===============request map==="+ System.currentTimeMillis()+map);
					return map;
				}
			};

			//设置60秒超时,DefaultRetryPolicy(超时时长毫秒，超时重试次数，超时重试时的超时系数（系数*超时时间为当次超时时间）)
			request.setRetryPolicy(new DefaultRetryPolicy(60000,
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			VolleyUtils.getQueue(context).add(request);
		} else { // 无网加载缓存
			showNetUnconnect(context, handler, requestCode);
		}

	}

	private static void parseData(final Context context, String response, Handler handler, int requestCode, ReceiveData receiveData) {
		if (!TextUtils.isEmpty(response)) {
			try {
				JSONObject json = new JSONObject(response);
				if (json != null) {
					String status = json.optString("status");
					if (!TextUtils.isEmpty(status) && status.equals("0")) {
						receiveData.receiveDate(response);
					} else {
						Message msg = new Message();
						msg.what = Utils.ERROR;
						msg.arg1 = requestCode;
						msg.obj = response;
						sendMessage(handler, msg);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Message msg = new Message();
			msg.what = Utils.ERROR;
			msg.arg1 = requestCode;
			msg.obj = response;
			sendMessage(handler, msg);
		}
	}

	public static void sendMessage(Handler handler, Message msg) {
		if (handler!=null) {
			handler.sendMessage(msg);
		}
	}

	public interface ReceiveData {
		public void receiveDate(String json);
	}

	public static void executeUpLoadHttpPost(Context context, String url,
											 Map<String, String> params, Handler mHandler,
											 Map<String, byte[]> files, ReceiveData data) {
		params = getSign(context, params);
		String result = "";
		String BOUNDARY = "ljhasdasd";
		String PREFIX = "--", LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";
		HttpURLConnection conn = null;
		try {
			URL uri = new URL(url);
			conn = (HttpURLConnection) uri.openConnection();
			conn.setDoInput(true);// 允许输入
			conn.setDoOutput(true);// 允许输出
			conn.setUseCaches(false); // 不允许使用缓存

			conn.setRequestMethod("POST");
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Charsert", "UTF-8");
			conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);

			// 首先组拼文本类型的参数

			StringBuilder sb = new StringBuilder();

			for (Entry<String, String> entry : params.entrySet()) {
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINEND);
				sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
				sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
				sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
				sb.append(LINEND);
				sb.append(entry.getValue());
				sb.append(LINEND);
			}

			DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
			outStream.write(sb.toString().getBytes());

			// 拼接文件数据

			if (files != null) {
				for (Entry<String, byte[]> file : files.entrySet()) {
					StringBuilder sbFile = new StringBuilder();
					sbFile.append(PREFIX);
					sbFile.append(BOUNDARY);
					sbFile.append(LINEND);
					sbFile.append("Content-Disposition: form-data; name=\"" + file.getKey() + "\";" + "filename=\""
							+ System.currentTimeMillis() + "\"" + LINEND);
					sbFile.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
					sbFile.append(LINEND);
					outStream.write(sbFile.toString().getBytes());
					outStream.write(file.getValue());
					outStream.write(LINEND.getBytes());
				}
			}

			// 请求结束标志
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
			outStream.write(end_data);
			outStream.flush();

			// 得到响应码
			int res = conn.getResponseCode();
			System.out.println("==============res======" + res);
			if (res == 200) {
				InputStream in = conn.getInputStream();
				StringBuilder sbResult = new StringBuilder();
				int ch;
				while ((ch = in.read()) != -1) {
					sbResult.append((char) ch);
				}
				result = sbResult.toString();
				System.out.println("==============result======" + result);
				outStream.close();
				conn.disconnect();
			} else {
				String msg = conn.getResponseMessage();
			}
		} catch (MalformedURLException e) {
			System.out.println("======================MalformedURLException == " + e.getMessage());
		} catch (IOException e) {
			System.out.println("==================IOException : " + e);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		if (!TextUtils.isEmpty(result)) {
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(result);
				String status = jsonObject.optString("status");
				if (status.equals("0")) {
					JSONObject datas = jsonObject.optJSONObject("data");
					if (datas!=null) {
						data.receiveDate(datas.optString("avatar"));
					}
				} else {
					Message msg = new Message();
					msg.what = Utils.ERROR;
					msg.obj = "上传失败";
					mHandler.sendMessage(msg);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Utils.ERROR;
				msg.obj = "上传失败";
				mHandler.sendMessage(msg);
			}
		} else {
			Message msg = new Message();
			msg.what = Utils.ERROR;
			msg.obj = "上传失败";
			mHandler.sendMessage(msg);
		}
	}

	private static void showNetUnconnect(final Context context, Handler handler, int code) {
		Message msg = new Message();
		msg.what = Utils.UNCONNECT;
		msg.arg1 = code;
		msg.obj = context.getString(R.string.net_error);
		sendMessage(handler, msg);
	}

	/**
	 * 生成签名
	 * @param map
	 * @return
	 */
	private static Map<String, String> getSign(Context context, Map<String, String> map) {
		map.put("timestamp", String.valueOf(System.currentTimeMillis()));
		map.put("sign", createLinkString(paraFilter(map)));
		return map;
	}

	/**
	 * 除去数组中的空值和签名参数
	 * @param sArray 签名参数组
	 * @return 去掉空值与签名参数后的新签名参数组
	 */
	public static Map<String, String> paraFilter(Map<String, String> sArray) {

		Map<String, String> result = new HashMap<String, String>();

		if (sArray == null || sArray.size() <= 0) {
			return result;
		}

		for (String key : sArray.keySet()) {
			String value = sArray.get(key);
			if (value == null || value.equals("") || key.equalsIgnoreCase("sign")) {
				continue;
			}
			result.put(key, value);
		}

		return result;
	}

	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * @param params 需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String createLinkString(Map<String, String> params) {

		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		String prestr = "";

		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);

			if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}

		prestr = MD5EncodeUtil.encryptData2MD5(MD5EncodeUtil.encryptData2MD5(prestr));

		return prestr;
	}
}
