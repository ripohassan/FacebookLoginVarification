package com.example.ripo.fbloginvarification;

import android.app.Application;

import com.facebook.accountkit.AccountKit;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AccountKit.initialize(getApplicationContext());
    }
}
