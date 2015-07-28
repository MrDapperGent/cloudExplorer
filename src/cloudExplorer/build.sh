#!/bin/bash
BUILD_NAME="cloudExplorer-6.0"
SRC="$HOME/cloudExplorer"
JAR="CloudExplorer.jar"
JARLOC="$SRC/dist"
README="$SRC/src/cloudExplorer/Release_Notes.txt"
WINDOWS="$SRC/src/cloudExplorer/cloudExplorer"
LINUX="$SRC/src/cloudExplorer/cloudExplorer.bat"
LOCBUILD="$HOME/$BUILD_NAME/"
ZIP="$HOME/$BUILD_NAME.zip"
BUCKET="cloudexplorer"

rm -rf $HOME/$BUILD_NAME
ant -f $SRC/build.xml
rm -f $ZIP
rm -rf $LOCBUILD/home
cp -rf $JARLOC $LOCBUILD
cp -f $README $LOCBUILD
cp -f $WINDOWS $LOCBUILD
cp -f $LINUX $LOCBUILD
chmod +x $LOCBUILD/cloudExplorer
cd $HOME
zip -r $ZIP $BUILD_NAME
cd $LOCBUILD
java -jar $LOCBUILD/CloudExplorer.jar build $BUILD_NAME.zip $ZIP $BUCKET
echo;echo;echo ;ls $LOCBUILD;echo
./cloudExplorer
