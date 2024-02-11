@echo off
setlocal enabledelayedexpansion

:: This is a simple Batch script to compile all files and run the Main file.

:: How to run the program
:: Path: launch.bat

echo Which platform are you on ?
echo (0) - Windows
echo (1) - Linux
set /p platform=Enter platform (0 for Windows, 1 for Linux): 

if not "%platform%"=="0" if not "%platform%"=="1" (
    echo "Error: Invalid argument supplied. Should be 0 (Windows) or 1 (Linux)"
    pause
    exit /b 1
)

::find all java file paths and put them in a file
echo Starting...
dir /s /b *.java > files.txt
:: Compile the program
echo Compiling...
javac -d src/classFiles @files.txt

:: Set the classpath
if "%platform%"=="0" (
    set "CLASSPATH=.;src/classFiles/;src/classFiles/*;src/classFiles/*/*;src/classFiles/*/*/*;src/classFiles/*/*/*/*"
) else if "%platform%"=="1" (
    set "CLASSPATH=.:src/classFiles/:src/classFiles/*:src/classFiles/*/*:src/classFiles/*/*/*:src/classFiles/*/*/*/*"
) else (
    echo "Please enter 0 (Windows start) or 1 (Linux start)"
    pause
    exit /b 1
)

:: Run the program
echo Running... 
java -cp %CLASSPATH% start.Main
echo Cleaning up...

:: Clean up
echo Cleaning up...
set DELCLASSPATH="src\classFiles\*"
del /q /s !DELCLASSPATH! >nul
del files.txt >nul
pause
exit /b 0