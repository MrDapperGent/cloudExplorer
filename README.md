# License
This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.

A copy of the GNU GPL is located in `LICENSE.txt`.

# Description 
Cloud Explorer is a open-source S3 client. It works on Windows, Linux, and Mac.  It has a graphical and command line interface for each supported operating system.

# Features

* Search
* Performance testing
* Music player
* Transition buckets to Amazon Glacier  
* Amazon RRS
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

# Screenshots 

![Graph](https://www.linux-toys.com/7.png)
![Objects](https://www.linux-toys.com/8.png)

# How to run the program

To run the application, most users should be able to run the program by double clicking on the CloudExplorer.jar file. If not, there are two scripts included to assist:

Linux and OS X users can run it as follows:
```
./cloudExplorer
```
Windows users can run Cloud Explorer by double clicking on the cloudExplorer.bat or CloudExplorer.jar file.

For any OS, you can start the program with the Java command:
```
java -jar -Xms100m -Xmx500m ./CloudExplorer.jar
```
# Video demonstraton

https://www.youtube.com/watch?v=O1HVDYywZRY

# How to compile from source

http://youtu.be/54v3rIUh0h8

# Upgrading

Starting with v5.0, minor releases will no longer be published on Github. To upgrade, please use the updater located in the application. Everytime Cloud Explorer is ran, it will check for the latest version. To apply the update, click "Help -> Check for updates"


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
	$ java -jar -Xms100m -Xmx500m CloudExplorer.jar daemon
```

# Running Cloud Explorer in Docker
<br>
The following steps will explain how to run CloudExplorer from a Docker container. The template will install a CentOS 7 container with TWM and Xterm.
```
git clone https://github.com/rusher81572/cloudExplorer.git -b docker
cd cloudExplorer
docker build -t cloudexplorer .
docker run -d --net=host cloudexplorer 
```
If you want to use your existing S3 configuration file, copy s3.config from your home directory into the cloudExplorer directory. Modify DockerFile by adding the following before the CMD line:
```
ADD s3.config /root/
```
Save the file and repeat the "docker build" and "docker run" commands from earlier.

To connect to the container, you can use a VNC client such as vncviewer to connect to display #1.
```
	vncviewer localhost:1
```
After you login VNC with the default password of 123456, you should see the GUI.

If you want to use the Cloud Explorer CLI, start a terminal in the VNC session by right clicking on the desktop and choose "xterm". Finally, run the following command:
```
	java -jar /cloudExplorer/CloudExplorer.jar help
```	
** Please note that you will need to use the GUI to create the s3.config file before using the CLI if you did not import your existing s3.config file **

<br>
