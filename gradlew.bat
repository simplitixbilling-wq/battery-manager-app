#!/usr/bin/env bat
@REM *******************************************************
@REM * Gradle Kotlin DSL Starter Script
@REM * Adapted for Windows
@REM *******************************************************

@if defined DEBUG @echo on

@REM Attempt to set APP_HOME

@rem Resolve the script's known install dir
setlocal EnableDelayedExpansion
set "APP_HOME="

@rem Follow sym links if we have them
for %%f in ("%~f0") do (
    set "APP_HOME=%%~dpf.."
)

@rem Now we have the app dir, try to start Gradle
set "GRADLE_OPTS="
set "JAVA_OPTS="

@rem Find the Java executable
if defined JAVA_HOME (
    if exist "!JAVA_HOME!\bin\java.exe" (
        set "JAVA_EXE=!JAVA_HOME!\bin\java.exe"
    )
)

if not defined JAVA_EXE (
    for /f "tokens=*" %%i in ('where java 2^>nul') do (
        set "JAVA_EXE=%%i"
        if not defined JAVA_EXE goto :eof
    )
)

@rem Set the classpath
set "CLASSPATH=!APP_HOME!\gradle\wrapper\gradle-wrapper.jar"

@rem Execute Gradle
if defined JAVA_EXE (
    "!JAVA_EXE!" -classpath "!CLASSPATH!" org.gradle.wrapper.GradleWrapperMain %*
) else (
    echo ERROR: JAVA_HOME is not set.
    echo Please install Java 17 or set JAVA_HOME environment variable.
    exit /b 1
)