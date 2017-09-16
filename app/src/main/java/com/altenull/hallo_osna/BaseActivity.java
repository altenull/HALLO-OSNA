package com.altenull.hallo_osna;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.animation.Animation;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tsengvn.typekit.TypekitContextWrapper;


public abstract class BaseActivity extends Activity {
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    protected Animation loadingAnimation;
    protected double imageScale;
    protected int stageWidth;
    protected int stageHeight;
    protected float density;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageLoaderConfiguration localImageLoaderConfiguration = new ImageLoaderConfiguration.Builder(getApplicationContext()).threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory().diskCacheFileNameGenerator(new Md5FileNameGenerator()).diskCacheSize(1024 * 1024 * 50).tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs().build();
        this.imageLoader.init(localImageLoaderConfiguration);

        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);

        this.stageWidth = localDisplayMetrics.widthPixels;
        this.stageHeight = localDisplayMetrics.heightPixels;
        this.density = localDisplayMetrics.density;
    }


    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }


    protected void showHelp() {
        startActivity(new Intent(BaseActivity.this, HelpActivity.class));
    }
}