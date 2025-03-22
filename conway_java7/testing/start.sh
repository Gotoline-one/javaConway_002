#!/bin/bash
# run.sh - A script to run conway.jar with JavaFX modules

# Define the jar file and JavaFX module path
JAR="conway.jar"
MODULE_PATH="/opt/javafx-sdk/javafx-sdk-21.0.6/lib"
MODULES="javafx.controls"

JAR_NAME="${1:-$JAR}"

# Check if the JAR file exists
if [ ! -f "$JAR_NAME" ]; then
    echo "Error: $JAR_NAME not found in the current directory."
    exit 1
fi

# Run the jar with the specified module path and modules
java --module-path "$MODULE_PATH" --add-modules "$MODULES" -jar "$JAR_NAME" 

