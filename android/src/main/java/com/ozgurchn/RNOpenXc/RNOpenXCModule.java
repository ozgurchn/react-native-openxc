package com.ozgurchn.rnopenxc;

/**
 * Created by ozgur.cihan on 7.11.2017.
 */

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;

//import com.openxcplatform.openxcstarter.R;
import com.openxc.VehicleManager;
import com.openxc.measurements.Measurement;
import com.openxc.measurements.EngineSpeed;
import com.openxc.measurements.IgnitionStatus;
import com.openxc.measurements.BrakePedalStatus;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.openxc.units.Boolean;
import com.facebook.react.bridge.Callback;

import javax.annotation.Nonnull;

public class RNOpenXcModule extends ReactContextBaseJavaModule {
    private static final String TAG = "StarterActivity";
    private VehicleManager mVehicleManager;
    private TextView mEngineSpeedView;
    private Activity activity;
    Double mEngineStatus = 0.0;
    String mIgnitionStatus = "";
    Boolean mBrakePedalStatus;

    public RNOpenXcModule(ReactApplicationContext reactContext, Activity activity) {
        super(reactContext);
        this.activity = activity;
        if(mVehicleManager == null) {
          Intent intent = new Intent(activity, VehicleManager.class);
          activity.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public String getName() {
        return "openXC";
    }

    @ReactMethod
    void getEngineSpeed(@Nonnull Callback callback) {
        Activity activity = getCurrentActivity();
        if (activity != null) {
            callback.invoke(EngineStatus());
        }
    }

    @ReactMethod
    void getIgnitionStatus(@Nonnull Callback callback) {
        Activity activity = getCurrentActivity();
        if (activity != null) {
            callback.invoke(IgnitionValue());
        }
    }

    @ReactMethod
    void getBrakePedalStatus(@Nonnull Callback callback) {
        Activity activity = getCurrentActivity();
        if (activity != null) {
            callback.invoke(BrakePedalValue());
        }
    }

    protected Activity getActivity() {
        return activity;
    }

    EngineSpeed.Listener mSpeedListener = new EngineSpeed.Listener() {
        @Override
        public void receive(Measurement measurement) {
            final EngineSpeed speed = (EngineSpeed) measurement;
            setEngineStatus(speed.getValue().doubleValue());
        }
    };
    IgnitionStatus.Listener mIgnitionListener = new IgnitionStatus.Listener() {
        @Override
        public void receive(Measurement measurement) {
            final IgnitionStatus ignition = (IgnitionStatus) measurement;
            setIgnitionStatus(ignition.getValue().getSerializedValue());
        }
    };

    BrakePedalStatus.Listener mBrakePedalListener = new BrakePedalStatus.Listener() {
        @Override
        public void receive(Measurement measurement) {
            final BrakePedalStatus brake = (BrakePedalStatus) measurement;
            setBrakePedalStatus(brake.getValue());
        }
    };

    private Double EngineStatus() {
        return this.mEngineStatus;
    }

    private void setEngineStatus(Double value) {
        this.mEngineStatus = value;
    }

    private String IgnitionValue() {
        return this.mIgnitionStatus;
    }

    private void setIgnitionStatus(String value) {
        this.mIgnitionStatus = value;
    }

    private Boolean BrakePedalValue() {
        return this.mBrakePedalStatus;
    }

    private void setBrakePedalStatus(Boolean value) {
        this.mBrakePedalStatus = value;
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            Log.i(TAG, "Bound to VehicleManager");

            mVehicleManager = ((VehicleManager.VehicleBinder) service)
                    .getService();

            mVehicleManager.addListener(EngineSpeed.class, mSpeedListener);
            mVehicleManager.addListener(IgnitionStatus.class, mIgnitionListener);
            mVehicleManager.addListener(BrakePedalStatus.class, mBrakePedalListener);

        }

        // Called when the connection with the service disconnects unexpectedly
        public void onServiceDisconnected(ComponentName className) {
            Log.w(TAG, "VehicleManager Service  disconnected unexpectedly");
            mVehicleManager = null;
        }
    };
}
