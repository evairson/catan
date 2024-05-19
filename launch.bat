@echo off

REM Trouver tous les fichiers .java et les lister dans files.txt
dir /S /B *.java > files.txt

REM Supprimer les lignes contenant 'test' ou 'Test'
powershell -Command "(Get-Content files.txt) -notmatch '\b(test|Test)\b' | Set-Content files.txt"

echo Compiling...
REM Compiler les fichiers .java listés dans files.txt et placer les .class dans src\classFiles
javac -d src\classFiles @files.txt

REM Définir le CLASSPATH
set CLASSPATH=.;src\classFiles\;src\classFiles\*;src\classFiles\*\*;src\classFiles\*\*\*;src\classFiles\*\*\*\*;src\classFiles\*\*\*\*\*;src\main\resources

echo Running...
REM Exécuter la classe principale start.Main
java -cp %CLASSPATH% start.Main

echo Cleaning up...
REM Supprimer les fichiers compilés et files.txt
rmdir /S /Q src\classFiles
del files.txt
