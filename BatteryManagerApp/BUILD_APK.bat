@echo off
REM Battery Manager APK Build Script
REM Auto-detects your Android SDK and builds the APK

echo ==================================
echo   Battery Manager App - APK Builder
echo ==================================
echo.

REM Set Android SDK path
set "ANDROID_SDK=C:\Users\Chandana\AppData\Local\Android\Sdk"
if not exist "%ANDROID_SDK%" (
    echo ERROR: Android SDK not found at %ANDROID_SDK%
    echo Please install Android Studio or set ANDROID_HOME environment variable
    pause
    exit /b 1
)

REM Check for JDK
set "JAVA_HOME=C:\Program Files\Java\jdk-17"
if not exist "%JAVA_HOME%" (
    REM Try alternative locations
    for %%J in (
        "C:\Program Files\Android\Android Studio\jre"
        "C:\Program Files\Java"
    ) do (
        if exist "%%J\bin\java.exe" (
            set "JAVA_HOME=%%J"
            goto :java_found
        )
    )
    echo WARNING: JDK not found. Please install OpenJDK 17+
    echo Download from: https://adoptium.net/
    echo Or install Android Studio first
    echo.
    goto :manual_build
)

:java_found
echo Using Java at: %JAVA_HOME%
echo Using Android SDK at: %ANDROID_SDK%
echo.

REM Set PATH
set "PATH=%JAVA_HOME%\bin;%ANDROID_SDK%\platform-tools;%ANDROID_SDK%\build-tools\36.1.0;%PATH%"

REM Navigate to project
cd /d "%~dp0"
echo Building APK...
echo.

REM Build with Gradle wrapper
if exist "gradlew.bat" (
    echo Running Gradle wrapper...
    call gradlew.bat assembleDebug
    goto :build_success
)

:build_success
echo.
echo ==================================
echo BUILD SUCCESSFUL!
echo ==================================
echo.
echo APK Location:
echo   %~dp0app\build\outputs\apk\debug\app-debug.apk
echo.

if exist "%~dp0app\build\outputs\apk\debug\app-debug.apk" (
    echo Installing on device...
    %ANDROID_SDK%\platform-tools\adb.exe install -r "%~dp0app\build\outputs\apk\debug\app-debug.apk"
) else (
    echo To install on device:
    echo   adb install app\build\outputs\apk\debug\app-debug.apk
)
pause
exit /b 0

:manual_build
echo.
echo ==================================
echo Manual Build Instructions
echo ==================================
echo.
echo Option 1 - Android Studio (EASIEST):
echo   1. Open Android Studio
echo   2. File ^> Open ^> Select this folder
echo   3. Build ^> Build APK(s)
echo.
echo Option 2 - Command Line:
echo   set JAVA_HOME=C:\Program Files\Java\jdk-17
echo   set ANDROID_HOME=C:\Users\Chandana\AppData\Local\Android\Sdk
echo   cd %ANDROID_SDK%\..
echo   gradlew.bat assembleDebug
echo.
echo Option 3 - Install JDK:
echo   winget install Microsoft.OpenJDK.17
echo.
pause