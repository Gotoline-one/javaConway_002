@echo off
REM compile_and_package.bat: Compile Java sources and package them into a JAR.

REM --- Configuration (modify as needed) ---
set "JAVA_HOME=C:\java\sdk\jdk-21.0.5"            			REM Adjust this path to your Java installation (remove trailing backslash if present)
set "JAVAFX_LIB=C:\java\javafx\javafx-sdk-21.0.6\lib"    	REM Adjust to point to your JavaFX SDK lib directory
set "MODULES=javafx.controls"
set "SRC_DIR=src\main\java"
set "TARGET_DIR=target"
set "MANIFEST_FILE=manifest.txt"

REM Set a default JAR name; you can override this by passing a different name as the first argument.
if "%~1"=="" (
    set "JAR_NAME=conway21.jar"
) else (
    set "JAR_NAME=%~1"
)
REM --- Check prerequisites ---
if not exist "%JAVA_HOME%" (
    echo Java home directory "%JAVA_HOME%" does not exist. Please check your installation.
    exit /b 1
)

if not exist "%SRC_DIR%" (
    echo Source directory "%SRC_DIR%" does not exist. Please check your source path.
    exit /b 1
)

if not exist "%MANIFEST_FILE%" (
    echo Manifest file "%MANIFEST_FILE%" does not exist. Please check your manifest file.
    exit /b 1
)

REM --- Prepare build directory ---
if exist "%TARGET_DIR%" (
    echo Removing existing target directory "%TARGET_DIR%"...
    rmdir /s /q "%TARGET_DIR%"
)
echo Preparing to compile Java source files...
mkdir "%TARGET_DIR%"

REM --- Compile Java sources ---
echo Compiling Java source files from "%SRC_DIR%\com\conway" into "%TARGET_DIR%"...
REM Enable delayed variable expansion for accumulating file list.
setlocal enabledelayedexpansion
set "sourceFiles="

REM Recursively collect all .java files under SRC_DIR\com\conway
for /R "%SRC_DIR%\com\conway" %%f in (*.java) do (
    set "sourceFiles=!sourceFiles! "%%f""
)

"%JAVA_HOME%\bin\javac" --module-path "%JAVAFX_LIB%" --add-modules %MODULES% -d "%TARGET_DIR%" !sourceFiles!
if errorlevel 1 (
    echo Compilation failed.
    exit /b 1
)
endlocal

REM --- Package the JAR ---
echo Packaging the JAR file as "%JAR_NAME%"...
"%JAVA_HOME%\bin\jar" cfm "%JAR_NAME%" "%MANIFEST_FILE%" -C "%TARGET_DIR%" .
if errorlevel 1 (
    echo JAR packaging failed.
    exit /b 1
)

echo Build successful! JAR file created: %JAR_NAME%
