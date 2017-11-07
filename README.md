## React Native OpenXC Library Integration
This project serves as a boilerplate to create custom React Native native modules that can later be installed through NPM and easily be used in production.


## Install

  npm install --save react-native-openxc

  RN > 0.47 or higher

## Manuel Linking

Link the library:
    * Add the following to `android/settings.gradle`:
        ```
        include ':react-native-openxc'
        project(':react-native-openxc').projectDir = new File(settingsDir, '../node_modules/react-native-openxc/android')
        ```

    * Add the following to `android/app/build.gradle`:
        ```xml
        ...

        dependencies {
            ...
            compile project(':react-native-openxc')
        }
        ```
    * Add the following to `android/app/src/main/java/**/MainApplication.java`:
        ```java
        package com.projectname;

        import com.ozgurchn.rnopenxc.RNOpenXcPackage;  // add this for react-native-android-library-boilerplate

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
 Now you can use it like:

    ```javascript
    import OpenXC from 'react-native-openxc'
    OpenXC.getIgnitionStatus((ignition) => {
      console.log('Ignition Status', ignition);
    })
    ```
