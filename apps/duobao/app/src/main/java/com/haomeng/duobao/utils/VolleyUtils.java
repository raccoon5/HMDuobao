package com.haomeng.duobao.utils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Volley操作的工具类
 */
public class VolleyUtils {

	private static RequestQueue mQueue;

	/**
	 * 以单例模式获取Volley的请求对列
	 * 
	 * @param context
	 *            上下文对象
	 * @return RequestQueue对象
	 */
	public static RequestQueue getQueue(Context context) {
		if (mQueue == null) {
			mQueue = Volley.newRequestQueue(context);
		}
		return mQueue;
	}
}
