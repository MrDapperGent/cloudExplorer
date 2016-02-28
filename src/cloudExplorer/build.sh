#!/bin/bash
BUILD_NAME="cloudExplorer-7.2"
SRC="$HOME/cloudExplorer"
ZIP="$SRC/$BUILD_NAME.zip"

cd $SRC
ant
rm -rf $SRC/$BUILD_NAME
cp -rf $SRC/dist $BUILD_NAME
rm -f *.zip
zip --exclude=*.git* -r $ZIP $BUILD_NAME
java -jar $SRC/$BUILD_NAME/CloudExplorer.jar help
rm -rf $SRC/$BUILD_NAME
ant clean



