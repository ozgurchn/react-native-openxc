package com.ozgurchn.rnopenxc;

/**
 * Created by ozgur.cihan on 7.11.2017.
 */
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.app.Activity;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

public class RNOpenXcPackage implements ReactPackage {
    private Activity activity;

    public RNOpenXcPackage(Activity activity) {
        super();
        this.activity = activity;
    }

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        return Arrays.<NativeModule>asList(
                new RNOpenXcModule(reactContext, activity)
        );
    }

    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Arrays.<ViewManager>asList();
    }
}
