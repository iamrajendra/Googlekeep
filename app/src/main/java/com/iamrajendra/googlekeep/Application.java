package com.iamrajendra.googlekeep;


import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

public class Application  extends MultiDexApplication {
    private  static Application application;

    public static Application getApplication() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application =this;
        MultiDex.install(this);
    }
}
