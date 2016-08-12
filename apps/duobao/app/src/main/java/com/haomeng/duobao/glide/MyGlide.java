package com.haomeng.duobao.glide;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.haomeng.duobao.R;

/**
 * Created by wuhao on 2016/4/8.
 */
public class MyGlide {

    /**
     * 加载图片
     *
     * @param context
     * @param url
     * @param imgv
     */
    public static void load(Context context, String url, ImageView imgv) {
        if (context == null) {
            return;
        }

        Glide.with(context).load(url).placeholder(R.mipmap.ic_launcher).into(imgv);
    }

    public static void load(Activity activity, String url, ImageView imgv) {
        if (activity == null || activity.isFinishing()) {
            return;
        }

        Glide.with(activity).load(url).placeholder(R.mipmap.ic_launcher).into(imgv);
    }

    public static void load(Fragment fragment, String url, ImageView imgv) {
        if (fragment == null || fragment.isDetached()) {
            return;
        }

        Glide.with(fragment).load(url).placeholder(R.mipmap.ic_launcher).into(imgv);
    }

    /**
     * 图片验证码不做任何缓存处理
     *
     * @param context
     * @param url
     * @param imgv
     */
    public static void loadNoCache(Context context, String url, ImageView imgv) {
        if (context == null) {
            return;
        }

        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).placeholder(R.mipmap.ic_launcher).into(imgv);
    }
}
