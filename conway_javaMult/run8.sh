#!/bin/bash
# run.sh - A script to run conway.jar with JavaFX modules

# Define the jar file and JavaFX module path
JAVA_HOME="/opt/javafx-sdk/jdk8u442-full"
# MODULE_PATH="/opt/javafx-sdk/jdk8u442-full/lib/" # Note: Java 8 does not require the MODULE_PATH and MODULES settings as in Java 21.
# MODULES="javafx.controls"  # This is not needed for Java 8, but kept for consistency

JAR="./conway8.jar"



# Check if the JAR file exists
if [ ! -f "$JAR" ]; then
    echo "Error: $JAR not found in the current directory."
    exit 1
fi

# Run the jar with the specified module path and modules
$JAVA_HOME/bin/java  -jar "$JAR" "$@"
