#!/bin/sh
# Indicate the path of the java compiler to use
export JAVA_HOME=/usr/csshare/pkgs/jdk1.7.0_17
export PATH=$JAVA_HOME/bin:$PATH

# Export classpath with the postgressql driver
export CLASSPATH=$CLASSPATH:/home/csmajs/tlian020/VirtualBox\ VMs/CS166-Database-Project-Phase-3/java/lib/pg73jdbc3.jar

# compile the java program
javac -d "$PWD" /home/csmajs/tlian020/VirtualBox\ VMs/CS166-Database-Project-Phase-3/java/src/Retail.java

#run the java program
#Use your database name, port number and login

java Retail $USER"_DB" 8192 $USER

