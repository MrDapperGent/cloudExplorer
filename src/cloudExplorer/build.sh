#!/bin/bash
BUILD_NAME="cloudExplorer-3.5testing"
HOME="/Users/philliptribble"
#HOME="/home/ptribble"
SRC="$HOME/cloudExplorer"
JAR="CloudExplorer.jar"
JARHOME="$HOME/NetBeansProjects/CloudExplorer"
JARLOC="$JARHOME/dist"
README="$SRC/src/cloudExplorer/Release_Notes.txt"
WINDOWS="$SRC/src/cloudExplorer/cloudExplorer"
LINUX="$SRC/src/cloudExplorer/cloudExplorer.bat"
LOC="$HOME"
LOCBUILD="$LOC/$BUILD_NAME/"
ZIP="$LOC/$BUILD_NAME.zip"

rm -rf $LOC/$BUILD_NAME
mkdir $LOC/$BUILD_NAME
cd $SRC
git pull
cd $JARHOME
ant
rm -f $LOCBUILD/$JAR
rm -f $ZIP
rm -rf $LOCBUILD/home
cp -rf $JARLOC/* $LOCBUILD
cp -f $README $LOCBUILD
cp -f $WINDOWS $LOCBUILD
cp -f $LINUX $LOCBUILD
chmod +x $LOCBUILD/cloudExplorer
cd $LOC
zip -r $ZIP $BUILD_NAME
cd $LOCBUILD
java -jar $LOCBUILD/CloudExplorer.jar build $BUILD_NAME $ZIP
echo;echo;echo ;ls $LOCBUILD;echo
./cloudExplorer
