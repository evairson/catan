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
if [ $platform -eq 0 ] 
then
  find -name "*.java" > files.txt
  sed -i "/\b\(test\|Test\)\b/d" files.txt
  echo "Compiling..."
  javac  -d src/classFiles @files.txt
  CLASSPATH=".;src/classFiles/;src/classFiles/*;src/classFiles/*/*;src/classFiles/*/*/*;src/classFiles/*/*/*/*;src/classFiles/*/*/*/*/*;src/main/resources"
  echo "Running..."
  java -cp $CLASSPATH start.Main

  echo "Cleaning up..."
  DELCLASSPATH="src/classFiles/*"
  rm -r $DELCLASSPATH
  rm files.txt
  #For Windows
  #read -p "Press any key to continue" x
elif [ $platform -eq 1 ] 
then
  # Répertoire de sortie des classes compilées
  OUTPUT_DIR="bin"
  SRC_DIR="src/main/java"
  RESOURCES_DIR="src/main/resources"
  LIB_DIR="lib"

  # Trouver et compiler tous les fichiers Java
  echo "Compiling Java sources..."
  find $SRC_DIR -name "*.java" | xargs javac -classpath "$LIB_DIR/*" -d $OUTPUT_DIR

  # Copier les ressources dans le répertoire de sortie
  echo "Copying resources..."
  cp -r $RESOURCES_DIR/* $OUTPUT_DIR

  # Exécuter l'application
  echo "Running the application..."
  java -classpath "$OUTPUT_DIR:$LIB_DIR/*" start.Main
else 
    echo "Please enter 0 (Windows start) or 1 (Linux start)"
    exit 1
fi