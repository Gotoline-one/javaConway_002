#!/bin/bash
# compile_and_package.sh: Compile Java sources and package them into a JAR.

# Enable recursive globbing for ** patterns
shopt -s globstar

JAVA_HOME="/opt/javafx-sdk/jdk8u442-full"

# Variables (modify as needed)
# JAVAFX_LIB="/opt/javafx-sdk/jdk8u442-full/jre/lib/ext/jfxrt.jar" 
# Java 8 does not require the JavaFX_LIB and MODULES settings as in later jdks
SRC_DIR="src/main/java"
TARGET_DIR="target"
MANIFEST_FILE="manifest.txt"
# Set a default jar name; you can override this by passing a different name as the first argument.
JAR_NAME="${1:-conway8.jar}"

# Check if the Java home directory exists
if [ ! -d "$JAVA_HOME" ]; then
    echo "Java home directory $JAVA_HOME does not exist. Please check your installation."
    exit 1
fi
# Check if the source directory exists
if [ ! -d "$SRC_DIR" ]; then
    echo "Source directory $SRC_DIR does not exist. Please check your source path."
    exit 1
fi
# Check if the manifest file exists
if [ ! -f "$MANIFEST_FILE" ]; then
    echo "Manifest file $MANIFEST_FILE does not exist. Please check your manifest file."
    exit 1
fi
# Check if the target directory exists and remove it to start fresh
if [ -d "$TARGET_DIR" ]; then
    echo "Removing existing target directory $TARGET_DIR..."
    rm -rf "$TARGET_DIR"
fi
# Ensure the target directory is clean before starting the build
echo "Preparing to compile Java source files..."

# Create the target directory if it doesn't exist
mkdir -p "$TARGET_DIR"

echo "Compiling Java source files from $SRC_DIR into $TARGET_DIR..."
$JAVA_HOME/bin/javac -d "$TARGET_DIR" "$SRC_DIR"/com/conway/**/*.java
if [ $? -ne 0 ]; then
    echo "Compilation failed."
    exit 1
fi

echo "Packaging the JAR file as $JAR_NAME..."
$JAVA_HOME/bin/jar cfm "$JAR_NAME" "$MANIFEST_FILE" -C "$TARGET_DIR" .
if [ $? -ne 0 ]; then
    echo "JAR packaging failed."
    exit 1
fi

echo "Build successful! JAR file created: $JAR_NAME"

