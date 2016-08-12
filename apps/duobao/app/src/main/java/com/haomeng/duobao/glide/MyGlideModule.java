package com.haomeng.duobao.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.volley.VolleyGlideModule;

/**
 * Created by wuhao on 2016/2/26.
 */
public class MyGlideModule extends VolleyGlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);
        // Apply options to the builder here.
        //Android6.0版本以上如果没有开启存储权限是不能够写入本地的，应该使用glide默认存储
//        File file = new File(Utils.imgCache);
//        if (!file.exists()) {
//            file.mkdir();
//        }
//        DiskCache dlw = DiskLruCacheWrapper.get(file.getAbsoluteFile(), 250 * 1024 * 1024);
//        builder.setDiskCache(dlw);
    }
    @Override
    public void registerComponents(Context context, Glide glide) {
        super.registerComponents(context, glide);
        // register ModelLoaders here.
    }
}
