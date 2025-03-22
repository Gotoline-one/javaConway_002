#!/bin/bash
# compile_and_package.sh: Compile Java sources and package them into a JAR.

# Enable recursive globbing for ** patterns
shopt -s globstar

# Variables (modify as needed)
JAVAFX_LIB="/opt/javafx-sdk/javafx-sdk-21.0.6/lib"
MODULES="javafx.controls"
SRC_DIR="src/main/java"
TARGET_DIR="target"
MANIFEST_FILE="manifest.txt"
# Set a default jar name; you can override this by passing a different name as the first argument.
JAR_NAME="${1:-conway.jar}"

# Create the target directory if it doesn't exist
mkdir -p "$TARGET_DIR"

echo "Compiling Java source files from $SRC_DIR into $TARGET_DIR..."
javac --module-path "$JAVAFX_LIB" --add-modules "$MODULES" -d "$TARGET_DIR" "$SRC_DIR"/com/conway/**/*.java
if [ $? -ne 0 ]; then
    echo "Compilation failed."
    exit 1
fi

echo "Packaging the JAR file as $JAR_NAME..."
jar cfm "$JAR_NAME" "$MANIFEST_FILE" -C "$TARGET_DIR" .
if [ $? -ne 0 ]; then
    echo "JAR packaging failed."
    exit 1
fi

echo "Build successful! JAR file created: $JAR_NAME"

