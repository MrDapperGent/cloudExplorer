#!/bin/bash
BUILD_NAME="cloudExplorer-6.1"
SRC="$HOME/cloudExplorer"
README="$SRC/src/cloudExplorer/Release_Notes.txt"
ZIP="$SRC/$BUILD_NAME.zip"

cd $SRC
ant
rm -rf $SRC/$BUILD_NAME
cp -rf $SRC/dist $BUILD_NAME
cp -f $README $SRC/$BUILD_NAME
cp -rf $SRC/libs $SRC/$BUILD_NAME/
rm -f *.zip
zip --exclude=*.git* -r $ZIP $BUILD_NAME
java -jar $SRC/$BUILD_NAME/CloudExplorer.jar help
rm -rf $SRC/$BUILD_NAME
ant clean



