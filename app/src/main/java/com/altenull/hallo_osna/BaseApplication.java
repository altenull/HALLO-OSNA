package com.altenull.hallo_osna;


import android.app.Application;
import com.tsengvn.typekit.Typekit;


public class BaseApplication extends Application {
    public void onCreate() {
        super.onCreate();

        Typekit.getInstance().addNormal(Typekit.createFromAsset(this, "SeoulNamsanM.ttf")).addBold(Typekit.createFromAsset(this, "SeoulNamsanEB.ttf"));
    }
}