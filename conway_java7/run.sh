#!/bin/bash
# run.sh - A script to run conway.jar with JavaFX modules

# Define the jar file and JavaFX module path
JAR="./testing/conway002.jar"
MODULE_PATH="/opt/javafx-sdk/javafx-sdk-21.0.6/lib"
MODULES="javafx.controls"


# Check if the JAR file exists
if [ ! -f "$JAR" ]; then
    echo "Error: $JAR not found in the current directory."
    exit 1
fi

# Run the jar with the specified module path and modules
java --module-path "$MODULE_PATH" --add-modules "$MODULES" -jar "$JAR" "$@"

