@echo off
echo Setting up Java environment for Android build...

REM Try to find Java in common locations
set "JAVA_HOME="

REM Check if Java is in PATH
java -version >nul 2>&1
if %errorlevel% equ 0 (
    echo Java found in PATH
    goto :build
)

REM Check common Java installation paths
if exist "C:\Program Files\Java\jdk-17" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-17"
    echo Found Java 17 at %JAVA_HOME%
) else if exist "C:\Program Files\Java\jdk-11" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-11"
    echo Found Java 11 at %JAVA_HOME%
) else if exist "C:\Program Files\Eclipse Adoptium\jdk-17" (
    set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17"
    echo Found Java 17 at %JAVA_HOME%
) else if exist "C:\Program Files\Eclipse Adoptium\jdk-11" (
    set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-11"
    echo Found Java 11 at %JAVA_HOME%
) else (
    echo Java not found in common locations
    echo Please install Java 11 or 17 and set JAVA_HOME
    pause
    exit /b 1
)

REM Add Java to PATH
set "PATH=%JAVA_HOME%\bin;%PATH%"

:build
echo.
echo Cleaning project...
call gradlew clean

echo.
echo Building project...
call gradlew build --stacktrace

echo.
echo Build completed!
pause 