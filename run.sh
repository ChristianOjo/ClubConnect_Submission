#!/bin/bash

# --- Configuration ---
# Assuming the MySQL JDBC Connector JAR is in the current directory
JDBC_JAR="mysql-connector-j-8.0.33.jar"
SOURCE_DIR="src"
BUILD_DIR="build"
MAIN_CLASS="com.clubconnect.MainApp"

# --- Setup ---
echo "Setting up build environment..."
mkdir -p "$BUILD_DIR"

# --- Compilation ---
echo "Compiling Java source files..."
# Find all .java files and compile them, using the JDBC JAR in the classpath
find "$SOURCE_DIR" -name "*.java" > sources.txt
if [ ! -f "$JDBC_JAR" ]; then
    echo "WARNING: JDBC Connector JAR ($JDBC_JAR) not found. Compilation might fail if it's not in your system classpath."
    echo "Please download the MySQL JDBC Connector and place it in the project root."
    javac -d "$BUILD_DIR" @sources.txt
else
    javac -cp "$JDBC_JAR" -d "$BUILD_DIR" @sources.txt
fi

if [ $? -ne 0 ]; then
    echo "Compilation failed."
    rm sources.txt
    exit 1
fi

echo "Compilation successful."
rm sources.txt

# --- Execution ---
echo "Running the application..."
# Run the application, adding the build directory and JDBC JAR to the classpath
java -cp "$BUILD_DIR:$JDBC_JAR" "$MAIN_CLASS"

# --- Cleanup (Optional) ---
# echo "Cleaning up build directory..."
# rm -rf "$BUILD_DIR"
