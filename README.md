# Dinthi MultiView (Android)

A starter Android app with four independent WebView panels. Each panel can load YouTube, Instagram, Facebook, or Google Search / a basic website landing page.

## Requirements
- Android Studio (current stable)
- Android SDK Platform 35
- JDK 17 (Android Studio's bundled JDK is suitable)
- Internet connection

## Open and build
1. Extract `DinthiMultiView.zip`.
2. In Android Studio choose **Open** and select the `DinthiMultiView` folder (the one containing `settings.gradle`).
3. Allow Gradle sync and install the SDK components if Android Studio asks.
4. Connect an Android phone with USB debugging enabled, or create an emulator.
5. Choose **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
6. Android Studio shows a notification; click **locate** to find the debug APK.
7. To install from terminal after a successful build: `adb install app/build/outputs/apk/debug/app-debug.apk`.

## Project tree
```
DinthiMultiView/
├── settings.gradle
├── build.gradle
├── gradle.properties
├── README.md
└── app/
    ├── build.gradle
    └── src/main/
        ├── AndroidManifest.xml
        └── java/com/dinthi/multiview/MainActivity.java
```

## Notes and limitations
- The four panels are real, separate Android WebViews; each can be changed using its dropdown.
- The initial layout is YouTube, Instagram, Facebook, and Google Search / website.
- Instagram, Facebook, or YouTube may restrict login, playback, popups, or some features inside embedded WebViews. Use the arrow button to open the current page in a normal browser or compatible official app.
- YouTube embeds have their own official player/API requirements. This starter opens the normal mobile website; it does not scrape, download, or circumvent restrictions.
- Four pages can consume significant memory, battery and mobile data. Some phones may reload pages under memory pressure.
- For security, JavaScript is enabled for compatibility, but no JavaScript-to-native bridge is exposed. Non-HTTPS/non-HTTP schemes are not loaded in the embedded panel.
- This is a source project, not a prebuilt APK. Build it in Android Studio to generate the APK.
