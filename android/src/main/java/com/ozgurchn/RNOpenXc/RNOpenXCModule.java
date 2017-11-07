package com.ozgurchn.RNOpenXc;

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

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.openxc.units.Boolean;
import com.facebook.react.bridge.Callback;

import javax.annotation.Nonnull;

public class RNOpenXCModule extends ReactContextBaseJavaModule {
    private static final String TAG = "StarterActivity";
    private VehicleManager mVehicleManager;
    private TextView mEngineSpeedView;
    private Activity activity;
    Double mEngineStatus = 0.0;
    Timer timer = new Timer();


    public RNOpenXCModule(ReactApplicationContext reactContext, Activity activity) {
        super(reactContext);
        this.activity = activity;
        System.out.println("created");
        if(mVehicleManager == null) {
            System.out.println("created2");
            Intent intent = new Intent(activity, VehicleManager.class);

            activity.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public String getName() {
        return "openXC";
    }

    /*@ReactMethod
    public void checkEngineStatus() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                getEngineStatus();
            }
        }, 500);
    } */

   /* @ReactMethod
    public void alert() {
        setTimerExample();
    } */

    @ReactMethod
    void getActivityName(@Nonnull Callback callback) {
        Activity activity = getCurrentActivity();
        if (activity != null) {
            callback.invoke(getEngineStatus());
        }
    }

    protected Activity getActivity() {
        return activity;
    }

    EngineSpeed.Listener mSpeedListener = new EngineSpeed.Listener() {
        @Override
        public void receive(Measurement measurement) {
            System.out.println("created5");
            // When we receive a new EngineSpeed value from the car, we want to
            // update the UI to display the new value. First we cast the generic
            // Measurement back to the type we know it to be, an EngineSpeed.
            final EngineSpeed speed = (EngineSpeed) measurement;
            System.out.println("created6");
            System.out.println(speed.getValue().doubleValue());
            // In order to modify the UI, we have to make sure the code is
            // running on the "UI thread" - Google around for this, it's an
            // important concept in Android.
            setEngineStatus(speed.getValue().doubleValue());
           /* getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    // Finally, we've got a new value and we're running on the
                    // UI thread - we set the text of the EngineSpeed view to
                    // the latest value
                    mEngineSpeedView.setText("Engine speed (RPM): "
                            + speed.getValue().doubleValue());
                }
            }); */
        }
    };

    /*private void setTimerExample() {
        timer.schedule(doTimerBro, 0, 5000);
    }
    TimerTask doTimerBro = new TimerTask() {
        int i = 0;
        @Override
        public void run() {
            if(i==0) {
                setEngineStatus(true);
            } else if(i==2) {
                setEngineStatus(false);
            } else if(i==5) {
                setEngineStatus(true);
                timer.cancel();
            }
            i++;
        }
    }; */

    private Double getEngineStatus() {
        return this.mEngineStatus;
    }

    private void setEngineStatus(Double value) {
        this.mEngineStatus = value;
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        // Called when the connection with the VehicleManager service is
        // established, i.e. bound.
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            System.out.println("created3");
            Log.i(TAG, "Bound to VehicleManager");
            // When the VehicleManager starts up, we store a reference to it
            // here in "mVehicleManager" so we can call functions on it
            // elsewhere in our code.
            mVehicleManager = ((VehicleManager.VehicleBinder) service)
                    .getService();
            System.out.println("created3");
            System.out.println(mVehicleManager);
            // We want to receive updates whenever the EngineSpeed changes. We
            // have an EngineSpeed.Listener (see above, mSpeedListener) and here
            // we request that the VehicleManager call its receive() method
            // whenever the EngineSpeed changes
            mVehicleManager.addListener(EngineSpeed.class, mSpeedListener);
            System.out.println("created4");
        }

        // Called when the connection with the service disconnects unexpectedly
        public void onServiceDisconnected(ComponentName className) {
            Log.w(TAG, "VehicleManager Service  disconnected unexpectedly");
            mVehicleManager = null;
            setEngineStatus(333.0);
        }
    };
}
