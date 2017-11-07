## React Native OpenXC Library Integration
This library lets you use OpenXC platform within react-native android app


## Install
```sh
npm install --save react-native-openxc
```

RN > 0.47 or higher

## Manual Linking(Android)

* Add the following to `android/settings.gradle`:

    ```gradle
    ...
    include ':react-native-openxc'
    project(':react-native-openxc').projectDir = new File(settingsDir, '../node_modules/react-native-openxc/android')
    ```

* Add the following to `android/app/build.gradle`:
    ```gradle
    ...

    dependencies {
        ...
        compile project(':react-native-openxc')
    }
    ```
* Add the following to `android/app/src/main/java/**/MainApplication.java`:

    ```java
    package com.packagename;

      import com.ozgurchn.rnopenxc.RNOpenXcPackage;  // add this

    public class MainApplication extends Application implements ReactApplication {

        @Override
        protected List<ReactPackage> getPackages() {
            return Arrays.<ReactPackage>asList(
                new MainReactPackage(),
                new RNOpenXcPackage(MainActivity.activity)     //  add this line here
            );
        }
    }
    ```

* Add the following to `android/app/src/main/java/**/MainActivity.java`:

  ```java
  import android.app.Activity;  // <------ add here
  public class MainActivity extends ReactActivity {
    public static Activity activity;           // <------ add here
    ......
    @Override
    protected String getMainComponentName() {
        activity = this;           // <------ add here
        ......
    }
  }
  ```

* Add the following to `AndroidManifest.xml`:

    ```xml
    <service android:name="com.openxc.VehicleManager"/>
    ```

  This should go between <application> tags, like this:

    ```xml
    <application
        android:name=".MainApplication"
        android:allowBackup="true"
        android:label="@string/app_name"
        android:icon="@mipmap/ic_launcher"
        android:theme="@style/AppTheme">
        <activity
          android:name=".MainActivity"
          android:label="@string/app_name"
          android:configChanges="keyboard|keyboardHidden|orientation|screenSize"
          android:windowSoftInputMode="adjustResize">
          <intent-filter>
              <action android:name="android.intent.action.MAIN" />
              <category android:name="android.intent.category.LAUNCHER" />
          </intent-filter>
        </activity>
        ...
        <service android:name="com.openxc.VehicleManager"/>  // Add this line here
    </application>
    ```


* Simply you can use it like this:

    ```javascript
    import OpenXC from 'react-native-openxc'
    OpenXC.getIgnitionStatus((ignition) => {
      console.log('Ignition Status is:', ignition);
    })
    ```

* Available functions are;

  ```javascript
  OpenXC.getEngineSpeed((speed) => {
    console.log('Engine Speed is: ', speed);
  });

  OpenXC.getIgnitionStatus((ignition) => {
    console.log('Ignition Status is: ', ignition);
  });

  OpenXC.getBrakePedalStatus((brake) => {
    console.log('Brake Pedal Status is: ', brake);
  });
  ```
