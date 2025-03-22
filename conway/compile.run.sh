#!/bin/bash
# build_and_run.sh: Compile and run the JAR

# Set the JAR file path
JAR="./testing/conway002.jar"

# Compile the JAR
./compile.sh "${JAR}"
if [ $? -ne 0 ]; then
    echo "Compilation failed. Exiting."
    exit 1
fi

# Run the JAR, passing along any additional arguments
./run.sh "${JAR}" "$@"

