package com.haomeng.duobao.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {
	public static final int ERROR = -100;
	public static final int UNCONNECT = -102;

	/**
	 * dip转为 px
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * px 转为 dip
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 判断是否显示引导页 true显示 false不显示
	 */
	public static boolean isFirst(Context context) {
		SharedPreferences share = context.getSharedPreferences("VERSION", 0);
		int versioncodeAgo = share.getInt("VERSIONCODE", 0); //上次安装存储在本地的版本号
		int versioncode = getVersionCode(context); //当前版本号
		if (versioncode>versioncodeAgo) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 存储安装APP时的版本号
	 */
	public static void setVersionAgo(Context context) {
		SharedPreferences share = context.getSharedPreferences("VERSION", 0);
		Editor editor = share.edit();
		editor.putInt("VERSIONCODE", getVersionCode(context));
		editor.commit();
	}

	/**
	 * 获取当前版本号
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context)//获取版本号(内部识别号)
	{
		try {
			PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pi.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 判断是否有网络
	 *
	 * @param context
	 * @return
	 */
	public static boolean hasNetwork(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			return true;
		}
		return false;
	}
}
