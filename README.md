# rnTVLauncher

rnTVLauncher is a custom launcher application specifically designed for Android TV. This launcher leverages React Native (React Native TVOS) and native modules to provide a seamless and integrated experience. The application requests accessibility settings to make it the default launcher and intercepts calls to ensure it remains the default launcher. Additionally, the app automatically starts when the TV is powered on.

## Features

- Custom launcher for Android TV
- Requests accessibility settings to set as the default launcher
- Intercepts calls to maintain default launcher status
- Automatically starts when the TV is powered on
- Displays a grid of installed applications
- Allows launching of installed applications


## Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/Sammy970/React-native-launcher.git
    cd rnTVLauncher
    ```

2. Install dependencies:
    ```sh
    npm install
    ```

3. Set up the Android project:
    ```sh
    cd android
    ./gradlew clean
    cd ..
    ```

4. Start the Metro bundler:
    ```sh
    npm start
    ```

5. Run the application on Android TV:
    ```sh
    npm run android
    ```

## Usage

1. When the application is launched for the first time, it will request accessibility settings to set itself as the default launcher.
2. Once accessibility settings are enabled, the launcher will intercept calls to ensure it remains the default launcher.
3. The launcher will automatically start when the TV is powered on.
4. The main screen displays a grid of installed applications. You can navigate through the applications and launch them by pressing the select button on the remote.

## Code Overview

### `App.tsx`

This is the main React Native component that renders the grid of installed applications. It uses the `AppListModule` native module to fetch the list of installed applications and allows launching them.

### `android/app/src/main/AndroidManifest.xml`

This file contains the Android manifest configuration, including permissions, features, and components such as activities, receivers, and services.

### `android/app/src/main/java/com/rntvlauncher/AppListModule.kt`

This Kotlin file defines the `AppListModule` native module, which provides methods to fetch the list of installed applications and launch them.

### `android/app/src/main/java/com/rntvlauncher/AppListPackage.kt`

This Kotlin file defines the `AppListPackage` class, which registers the `AppListModule` native module.

### `android/app/src/main/java/com/rntvlauncher/BootReceiver.kt`

This Kotlin file defines the `BootReceiver` class, which ensures the launcher starts automatically when the TV is powered on.

### `android/app/src/main/java/com/rntvlauncher/HomeButtonService.kt`

This Kotlin file defines the `HomeButtonService` class, which intercepts calls to maintain the launcher as the default launcher.

### `android/app/src/main/java/com/rntvlauncher/MainApplication.kt`

This Kotlin file defines the `MainApplication` class, which initializes the React Native application and registers the native modules.

### `android/app/src/main/res/xml/accessibility_service_config.xml`

This XML file defines the configuration for the accessibility service.

## Contributing

Contributions are welcome! Please open an issue or submit a pull request if you have any improvements or bug fixes.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
