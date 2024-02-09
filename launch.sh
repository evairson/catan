#!/bin/bash
## This is a simple bash script to compile all files and run the Main file.

## How to run the program
# Path: launch.sh

echo Which platform are you on ?
echo "(0) - Windows"
echo "(1) - Linux"
read platform

if [ $platform != "0" -a $platform != "1" ]
then
    echo "Error : Invalid argument supplied. Should be 0 (Windows) or 1 (Linux)"
    exit 1
fi 


echo "Starting..."
find -name "*.java" > files.txt
echo "Compiling..."
javac  -d src/classFiles @files.txt

if [ $platform -eq 0 ] 
then
    CLASSPATH=".;src/classFiles/;src/classFiles/*;src/classFiles/*/*;src/classFiles/*/*/*;src/classFiles/*/*/*/*;src/classFiles/*/*/*/*/*"
elif [ $platform -eq 1 ] 
then 
    CLASSPATH=".:src/classFiles/:src/classFiles/*:src/classFiles/*/*:src/classFiles/*/*/*:src/classFiles/*/*/*/*:src/classFiles/*/*/*/*/*"
else 
    echo "Please enter 0 (Windows start) or 1 (Linux start)"
    exit 1
fi

echo "Running..."
java -cp $CLASSPATH start.Main

echo "Cleaning up..."
DELCLASSPATH="src/classFiles/*"
rm -r $DELCLASSPATH
rm files.txt
#For Windows
#read -p "Press any key to continue" x



