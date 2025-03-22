#!/bin/bash
# run.sh - A script to run conway.jar with JavaFX modules

# Define the jar file and JavaFX module path
JAR="./conway8.jar"
JAVA_HOME="/opt/javafx-sdk/jdk8u442-full"


# Check if the JAR file exists
if [ ! -f "$JAR" ]; then
    echo "Error: $JAR not found in the current directory."
    exit 1
fi

# Run the jar with the specified module path and modules
$JAVA_HOME/bin/java  -jar "$JAR" "$@"

