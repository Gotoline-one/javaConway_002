# Compare JavaConway_002's  JavaFX implementation between Java/JavaFX 8 and Java/JavaFX 21 

_**WORK IN PROGRESS**_


## Setup 
This is expected to be run on a Linux type system to take advantage of the time command

because of this the compile and runtimes are handled with shell scripts so to not distrub host system


## How To Compile

### Setup _compile8.sh_
  - If a different jdk is wanted to be used for 8, replace JAVA_HOME, see bleow:
  - NOTE: **if another java8 verison is used ensure _jfxrt.jar_ is included**
  
```bash
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
```
### Compile conway8.jar

expected results:

```bash
peter@thinkbook:conway_javaMult$ ./compile8.sh 
Removing existing target directory target...
Preparing to compile Java source files...
Compiling Java source files from src/main/java into target...
Packaging the JAR file as conway8.jar...
Build successful! JAR file created: conway8.jar

```


### Setup compile21.sh
  - After java8 the JDKs for Java JavaFX are separate, in order to compile a jar correctly other variables are needed
  - NOTE: JAVA_HOME and JAVAFX_LIB are different and held in different places on the system.
  - The zips from wherever the JDKs are downloaded from can be unziped anywhere accessable and pointed to here
```bash
# Enable recursive globbing for ** patterns
shopt -s globstar

JAVA_HOME="/usr/lib/jvm/jdk-21.0.6+7/"  # Adjust this path to your JavaFX SDK installation

# Variables (modify as needed)
JAVAFX_LIB="/opt/javafx-sdk/javafx-sdk-21.0.6/lib"
MODULES="javafx.controls"
SRC_DIR="src/main/java"
TARGET_DIR="target"
MANIFEST_FILE="manifest.txt"
# Set a default jar name; you can override this by passing a different name as the first argument.
JAR_NAME="${1:-conway21.jar}"
```
### Compile conway21.jar

expected results:

```bash
peter@thinkbook:conway_javaMult$ ./compile21.sh 
Removing existing target directory target...
Preparing to compile Java source files...
Compiling Java source files from src/main/java into target...
Packaging the JAR file as conway21.jar...
Build successful! JAR file created: conway21.jar
```



