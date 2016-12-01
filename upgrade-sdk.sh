#!/bin/bash
#
#This script will download the latest AWS SDK for Java and copy the libraries to the Cloud Explorer libs directory
#
function cleanup(){
  echo;echo "Cleaning up....."
  rm -rf aws*
  rm -rf sdk.zip
  rm -f libs/aws*
}

function copylibs(){
  echo;echo "Copying the libraries to Cloud Explorer....."
  export sdk_dir=`ls | grep aws-java-sdk`
  cp -f $sdk_dir/lib/$sdk_dir.jar libs/
  cp -f $sdk_dir/third-party/lib/commons* libs/
  cp -f $sdk_dir/third-party/lib/commons* libs/
  cp -f $sdk_dir/third-party/lib/http* libs/
  cp -f $sdk_dir/third-party/lib/jackson* libs/
  cp -f $sdk_dir/third-party/lib/joda* libs/
}

function getsdk(){
  echo;echo "Downloading the latest Java SDK....."
  curl https://sdk-for-java.amazonwebservices.com/latest/aws-java-sdk.zip  --output sdk.zip
  echo;echo "Extracting the SDK....."
  unzip -o -q sdk.zip
}

clear
cleanup
getsdk
copylibs
cleanup
echo;echo "Done.";echo;echo
