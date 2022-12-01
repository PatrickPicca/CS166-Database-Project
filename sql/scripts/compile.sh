#!/bin/sh
# Indicate the path of the java compiler to use
export JAVA_HOME=/usr/csshare/pkgs/jdk1.7.0_17
export PATH=$JAVA_HOME/bin:$PATH

# Export classpath with the postgressql driver
export CLASSPATH=$CLASSPATH:$(git rev-parse --show-toplevel)/java/lib/pg73jdbc3.jar

# compile the java program
#javac -d "$(git rev-parse --show-toplevel)/java/classes" "$(git rev-parse --show-toplevel)/java/src/Retail.java"
javac -d "$PWD" "$(git rev-parse --show-toplevel)/java/src/Retail.java"
#run the java program
#Use your database name, port number and login
cd "$(git rev-parse --show-toplevel)/java/classes"
java Retail $USER"_DB" 8192 $USER
cd "$(git rev-parse --show-toplevel)/sql/scripts"
