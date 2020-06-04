package com.example.contacts;

import android.app.Application;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import java.util.List;


public class appcls extends Application {


    public static final String APPLICATION_ID="5D51AC23-D0D4-01C9-FF91-AB15349F1B00";
    public static final String API_KEY="40FF0552-8BD5-4D4D-B186-3696569245A1";
    public static final String SERVICE_URL="https://api.backendless.com";
    public static BackendlessUser user;
    public static List<contacttable> cints;

    @Override
    public void onCreate() {
        super.onCreate();
        Backendless.setUrl(SERVICE_URL);
        Backendless.initApp(getApplicationContext(),
        APPLICATION_ID,API_KEY);

    }
}
