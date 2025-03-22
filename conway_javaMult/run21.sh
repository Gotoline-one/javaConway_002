#!/bin/bash
# run.sh - A script to run conway.jar with JavaFX modules

# Define the jar file and JavaFX module path
JAVA_HOME="/usr/lib/jvm/jdk-21.0.6+7"  # Adjust this path if necessary
MODULE_PATH="/opt/javafx-sdk/javafx-sdk-21.0.6/lib"
MODULES="javafx.controls"

JAR="./conway21.jar"



# Check if the JAR file exists
if [ ! -f "$JAR" ]; then
    echo "Error: $JAR not found in the current directory."
    exit 1
fi

# Run the jar with the specified module path and modules
$JAVA_HOME/bin/java --module-path "$MODULE_PATH" --add-modules "$MODULES" -jar "$JAR" "$@"
