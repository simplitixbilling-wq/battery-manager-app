# Battery Manager App - Android

A mobile battery management app that allows you to set custom charging thresholds for automatic battery optimization.

## 📱 Features

- **Custom Charging Thresholds**: Set start and stop charging percentages
- **Real-time Battery Monitoring**: Live battery level and charging status
- **Background Service**: Automatic monitoring every 30 seconds
- **Persistent Settings**: Saves your preferences using DataStore
- **Auto-start on Reboot**: Service restarts automatically after device restart
- **Battery Optimization Permission**: Requests proper permissions for reliable operation
- **Modern Material Design UI**

## 📦 Prerequisites

Before building, ensure you have:

1. **Java JDK 17+** - https://adoptium.net/
2. **Android Studio** (recommended) - https://developer.android.com/studio
3. **Android SDK** (comes with Android Studio)
4. **Minimum SDK Version**: API 24 (Android 7.0 Nougat)

## 🏗️ Building the APK

### Option 1: Android Studio (Recommended)

1. Open Android Studio
2. File → Open → Navigate to the `BatteryManagerApp` folder
3. Wait for Gradle sync to complete
4. Build → Build Bundle(s) / APK(s) → Build APK(s)
5. Find the APK at: `app/build/outputs/apk/debug/app-debug.apk`

### Option 2: Command Line (Gradle)

```bash
cd BatteryManagerApp

# If gradlew exists (gradle wrapper):
./gradlew assembleDebug
# or on Windows:
.\gradlew.bat assembleDebug
```

### Option 3: Manual Gradle Setup

```bash
# Install Gradle
winget install Gradle

# Build
gradle assembleDebug
```

### Option 4: Using Android SDK directly

```bash
# Set environment variables (adjust paths to your installations)
export ANDROID_HOME=/path/to/android/sdk
export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/build-tools/latest

# Build
cd BatteryManagerApp
./gradlew assembleDebug
```

## 📁 Project Structure

```
BatteryManagerApp/
├── app/
│   ├── src/main/java/com/example/batterymanager/
│   │   ├── MainActivity.kt          # Main UI and settings
│   │   ├── BatteryMonitorService.kt # Background service
│   │   └── BootReceiver.kt          # Handles reboot
│   ├── src/main/res/layout/
│   │   └── activity_main.xml        # UI layout
│   ├── src/main/res/values/
│   │   ├── strings.xml              # Text resources
│   │   ├── colors.xml               # Color palette
│   │   └── themes.xml               # App theme
│   └── src/main/res/drawable/
│       ├── ic_battery_24.xml        # Battery icon
│       ├── ic_settings_24.xml       # Settings icon
│       ├── ic_save_24.xml           # Save icon
│       └── ic_restore_24.xml        # Reset icon
├── build.gradle                     # Top-level config
├── settings.gradle                    # Project settings
└── README.md                        # This file
```

## ⚙️ Configuration

Default thresholds:
- **Start Charging**: 30%
- **Stop Charging**: 80%

Threshold ranges:
- Start: 10% - 75%
- Stop: Start + 5% to 95%

## 🔐 Permissions

The app requires:
- `RECEIVE_BOOT_COMPLETED` - Restart after reboot
- `FOREGROUND_SERVICE` - Run background service
- `BATTERY_STATS` - Monitor battery status
- `IGNORE_BATTERY_OPTIMIZATIONS` (granted by user) - Prevent battery killing

## ⚠️ Important Notes

### Android Restrictions

Due to Android's security model, **actual charging control** requires:
- OEM-specific APIs (Samsung, OnePlus, etc.)
- Root access
- Manufacturer custom ROMs

This app **monitors** battery levels and provides notifications/reminders. For actual charging control, you would need:

1. **ADB Commands** (for some devices):
```bash
# Enable charging (requires USB debugging enabled)
adb shell dumpsys battery set usb 1

# Disable charging
adb shell dumpsys battery set usb 0
```

2. **Root Access**:
```bash
# Direct charging control (requires root)
su -c "echo 1 > /sys/class/power_supply/battery/charging_enabled"
```

### Battery Optimization

For reliable background operation:
1. Go to Settings → Apps → Battery Manager
2. Tap "Battery" → "Battery optimization"
3. Set to "Don't optimize"

## 🧪 Testing

The app has been tested on:
- Android 7.0 (API 24) - Minimum SDK
- Android 12, 13, 14

## 📦 Dependencies

- Kotlin Coroutines
- AndroidX DataStore
- Material Design 3
- AndroidX Lifecycle

## 🎨 Customization

You can modify:
- Default threshold values in `MainActivity.kt`
- UI colors in `colors.xml`
- Icon styles in drawable resources
- Monitoring interval in `BatteryMonitorService.kt`

## 🤝 Contributing

Feel free to open issues or submit pull requests!

## 📄 License

This is a sample app for educational purposes.