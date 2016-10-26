# Description 
Cloud Explorer is a open-source S3 client. It works on Windows, Linux, and Mac.  It has a graphical and command line interface for each supported operating system. If you have a feature suggestion or find a bug, please open an issue. 
[![Codewake](https://www.codewake.com/badges/ask_question.svg)](https://www.codewake.com/p/cloud-explorer)

![Graph](http://i.imgur.com/aw5iKZf.png)

# Features

* Search
* Performance testing
* Music player
* Transition buckets to Amazon Glacier  
* Amazon RRS and Infrequently Accessed storage tiers
* Migrate buckets between S3 accounts
* Compress files prior to upload
* Take screen shots to S3
* Simple text editor
* IRC client
* Share buckets with users
* Access shared buckets
* View images
* Sync folders
* Graph CSV files and save them to a bucket
* Record audio messages and save them to a bucket
* Create snapshots of buckets

# System Requirements

* Java 8
* S3 credentials

# Downloading

For the latest stable release, click [Download.](https://cloudexplorer.s3.amazonaws.com/8/cloudExplorer-8.zip)

For the latest development release, please download cloudExplorer-dev.zip from [Amazon S3.](https://cloudexplorer.s3.amazonaws.com:443/cloudExplorer-dev.zip) [![Build Status](https://travis-ci.org/rusher81572/cloudExplorer.svg?branch=master)](https://travis-ci.org/rusher81572/cloudExplorer)

# License
This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.

A copy of the GNU GPL is located in `LICENSE.txt`.

# How to run the program

To run the application, most users should be able to run the program by double clicking on the CloudExplorer.jar file. 

If not, you can start the program with the Java command:
```
java -jar CloudExplorer.jar
```
# Video demonstraton

https://www.youtube.com/watch?v=O1HVDYywZRY

# How to compile from source

If you want to make changes to the GUI, here is a video demonstration on how to compile Cloud Explorer with NetBeans: http://youtu.be/54v3rIUh0h8

## 1. Install and Configure Java
``` 
apt-get update
apt-get -y install git ant openjdk-8-jdk-headless
```
## 2. Clone this repository and run ant to compile the project
```
git clone https://github.com/rusher81572/cloudExplorer.git
cd cloudExplorer
ant
```
## 3. Run Cloud Explorer
```
Double click on dist/CloudExplorer.jar 
or....
java -jar dist/CloudExplorer.jar help
```

# Upgrading

To upgrade, please use the updater located in the application. Everytime Cloud Explorer is ran, it will check for the latest version. To apply the update, click "Help -> Check for updates". Starting with version 8, you will be able to upgrade to newer releases with major library changes without manually downloading the newer release from this site. Since there is no upgrade path from version 7 to 8, you will need to download the version 8 release manually. 

# How to migrate data between S3 accounts
* Load the destination account and create or select a bucket to migrate to.
* From the menu "Snapshots and Migration", click "Set Migration/Snapshot Account and Bucket".
* Switch to the origin account and select the bucket to migrate.
* Under the "Snapshots and Migration", select "Migrate Bucket to Another S3 Account".
* Click Start Migration

# How to create a bucket snapshot
* Load the destination account and create or select a bucket to store the snapshot.
* From the menu "Snapshots and Migration", click "Set Migration/Snapshot Account and Bucket".
* Switch to the origin account and select the bucket to snap.
* Under the "Snapshots and Migration", select "Create or Restore a Bucket Snapshot".
* Click Create Snapshot

# Background Sync

Background Sync allows Cloud Explorer to sync a directory in the background to S3 every 5 minutes. Sync will only upload files that do not exist on S3 and no files will be deleted. 
<br>
<br>
From the GUI: 
* Click on "Background Syncing"
* Click Configure
* Click Save.
* Click on "Background Syncing"
* Click "Run".
* Syncing will occur every 5 minutes.

Starting background sync from the Command Line (Useful for running as a background process):

Create your config file from the GUI.
```
	$ java -jar CloudExplorer.jar daemon
```

# Running Cloud Explorer in Docker

The following steps will explain how to run Cloud Explorer from a Docker container. The template will install an Ubuntu container using the FVWM window manager. 

## Creating the container

First, clone this repo:
```
git clone https://github.com/rusher81572/cloudExplorer.git 
cd cloudExplorer
```

## Build Cloud Explorer 
```
ant
```

##
If you want to use your existing S3 configuration file, copy s3.config from your home directory into the cloudExplorer directory. Modify DockerFile by adding the following before the CMD line:
```
ADD s3.config /root/
```

Build the container
```
docker build -t cloudexplorer .
```

## Running the container - CLI


To use enviornment variables to store S3 account information when using the Cloud Explorer CLI instead of uploading an s3.config file:
```
docker run -it --net=host -e ACCESS_KEY='****' -e SECRET_KEY='***' -e ENDPOINT='https://s3.amazonaws.com:443' -e REGION='default' cloudexplorer bash
java -jar /CloudExplorer.jar help
```

If you want to use the Cloud Explorer CLI from a VNC session, skip to "Using the CLI in VNC".

## Running the container - GUI

Run the container:
```
docker run -d --net=host cloudexplorer 
````

To access to the container, use a VNC client such as vncviewer to connect to display #1.
```
vncviewer docker_host_ip:1
```
The default password for VNC is 123456. You should see the Cloud Explorer GUI after authentication.

## Using the CLI in VNC 

If you want to use the Cloud Explorer CLI, start a terminal in the VNC session by right clicking on the desktop and choose "xterm". Finally, run the following command:
```
java -jar /CloudExplorer.jar help
```	
