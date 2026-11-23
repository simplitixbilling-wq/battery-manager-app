# How to Build the Battery Manager APK

## Quick Build Steps (Recommended)

### Method 1: Using Android Studio (Fastest)

1. **Open Android Studio**
2. Click "Open an existing Android Studio Project"
3. Navigate to: `C:\Users\Chandana\FreeLLM\BatteryManagerApp`
4. Wait for Gradle sync (may take 1-2 minutes)
5. Go to **Build → Build Bundle(s) / APK(s) → Build APK(s)**
6. After build completes, click "Locate" to find your APK

**APK Location:**
```
C:\Users\Chandana\FreeLLM\BatteryManagerApp\app\build\outputs\apk\debug\app-debug.apk
```

### Method 2: Using Command Line (if Android SDK is set up)

1. **Set Environment Variables** (in Command Prompt or PowerShell):
```cmd
set ANDROID_HOME=C:\Users\Chandana\AppData\Local\Android\Sdk
set PATH=%PATH%;%ANDROID_HOME%\platform-tools;%ANDROID_HOME%\build-tools\36.1.0
```

2. **Build the APK**:
```cmd
cd C:\Users\Chandana\FreeLLM\BatteryManagerApp
gradlew.bat assembleDebug
```

Or on newer systems with Gradle:
```cmd
gradle assembleDebug
```

### Method 3: Download Gradle and Build

1. **Download Gradle**:
   - Visit: https://gradle.org/download/
   - Download "Gradle Binary" (or use Chocolatey: `choco install gradle`)

2. **Add to PATH** and run:
```cmd
cd C:\Users\Chandana\FreeLLM\BatteryManagerApp
gradle assembleDebug
```

## If You Need Java

The build system requires **Java JDK 17+**. You can:

1. **Download OpenJDK** from: https://adoptium.net/
2. **Install with Chocolatey** (if you have it):
```cmd
choco install openjdk17
```
3. **Install with Winget**:
```cmd
winget install Microsoft.OpenJDK.17
```

## Verifying the Build

After successful build, the APK will be at:
```
app\build\outputs\apk\debug\app-debug.apk
```

To install on your device:
```cmd
adb install app\build\outputs\apk\debug\app-debug.apk
```

## Troubleshooting

### Common Issues:

1. **"Gradle Sync Failed"**
   - Ensure you have stable internet connection
   - Check Android Studio settings (Gradle → Settings)
   - File → Sync Project with Gradle Files

2. **"Java Not Found"**
   - Set JAVA_HOME environment variable
   - Example: `set JAVA_HOME=C:\Program Files\Java\jdk-17`

3. **"SDK Not Found"**
   - In Android Studio: Tools → SDK Manager
   - Install Android SDK Platform 34 or higher

4. **"Build Failed"**
   - Check internet connection
   - Clean project: Build → Clean Project
   - Rebuild: Build → Rebuild Project

## Quick Test Without Building

You can test the app concept using the provided **BUILD_APK.bat** script that will guide you through the process.

Just double-click `BUILD_APK.bat` in the project folder.