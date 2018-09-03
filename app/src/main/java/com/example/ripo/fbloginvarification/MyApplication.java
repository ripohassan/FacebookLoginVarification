package com.example.ripo.fbloginvarification;

import android.app.Application;
import android.util.Log;

import com.facebook.accountkit.AccountKit;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AccountKit.initialize(getApplicationContext(), new AccountKit.InitializeCallback() {
            @Override
            public void onInitialized() {
                Log.d("AccountTesting","onInitialied");
            }
        });
    }
}
