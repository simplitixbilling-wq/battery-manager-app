# ⚡ Quick Start Guide

## 📦 Project Summary

I've created a complete Android Battery Manager app with:

| Feature | Status |
|---------|--------|
| Threshold Settings UI | ✅ Complete |
| Battery Status Display | ✅ Complete |
| Background Service | ✅ Complete |
| DataStore Persistence | ✅ Complete |
| Boot Receiver | ✅ Complete |
| Battery Optimization Permission | ✅ Complete |
| Material Design 3 UI | ✅ Complete |
| Vector Drawables | ✅ Complete |

## 🚀 Fastest Way to Get the APK

### Option 1: Android Studio (Recommended)

1. **Open Android Studio**
2. **File → Open**
3. **Navigate to:** `C:\Users\Chandana\FreeLLM\BatteryManagerApp`
4. **Wait for sync** (may take 1-2 minutes)
5. **Build → Build Bundle(s) / APK(s) → Build APK(s)**
6. **Click "Locate"** when build completes

**APK will be at:**
```
app/build/outputs/apk/debug/app-debug.apk
```

### Option 2: Install JDK & Build

**Step 1: Install OpenJDK 17**
```powershell
# PowerShell as Administrator
winget install Microsoft.OpenJDK.17
```

**Step 2: Set Environment Variables**
```cmd
set JAVA_HOME=C:\Program Files\Java\jdk-17
set ANDROID_HOME=C:\Users\Chandana\AppData\Local\Android\Sdk
set PATH=%PATH%;%ANDROID_HOME%\platform-tools;%ANDROID_HOME%\build-tools\36.1.0
```

**Step 3: Build**
```cmd
cd C:\Users\Chandana\FreeLLM\BatteryManagerApp
gradlew.bat assembleDebug
```

### Option 3: Use Pre-installed JDK (if available)

Some systems have Java installed in:
- `C:\Program Files\Android\Android Studio\jre` (Android Studio bundled JDK)
- `C:\Program Files\Java` (Manual JDK installation)
- Check with: `where java` in command prompt

## 📱 How the App Works

1. **User opens app** → Sees current battery level and charging status
2. **Adjust sliders** → Set start (e.g., 30%) and stop (e.g., 80%) thresholds
3. **Tap "Save"** → Settings saved, background service started
4. **Service runs** → Monitors battery every 30 seconds
5. **Notifications** → Shows current status in notification shade
6. **Auto-start** → After device reboot, service restarts automatically

**⚠️ Important:** Due to Android restrictions, this app monitors battery levels. For actual charging control, you would need:
- Root access
- OEM-specific APIs (Samsung, OnePlus, etc.)
- Custom ROM

## 🎨 Customization (Easy!)

- **Change default thresholds:** Edit `startThreshold = 30` in `MainActivity.kt`
- **Change colors:** Edit `colors.xml`
- **Change monitoring interval:** Edit `Thread.sleep()` in `BatteryMonitorService.kt`

## ❓ Need Help?

The `BUILD_APK.bat` script contains automated build instructions. Just double-click it.

For detailed troubleshooting, check `BUILD_INSTRUCTIONS.md`