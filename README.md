This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.

A copy of the GNU GPL is located in `LICENSE.txt`.

Cloud Explorer is a simple, free, and open-source S3 client made by me. It works on Windows, Linux, and Mac. Cloud Explorer provides extras that are not found elsewhere in other S3 browsers like search for objects, performance testing, music player, transition to Amazon Glacier, Amazon RRS, migrate buckets between S3 accounts, compress files piror to upload,take screen shots to S3, simple text editor, IRC client, share buckets with users, access shared buckets, and view images. Users can easily sync folders to their S3 account and upload and download files to their computer.


![Graph](https://www.linux-toys.com/performance5.png)
![Objects](https://www.linux-toys.com/objects5.png)

[How to run the program]

To run the application, most users should be able to run the program by double clicking on the CloudExplorer.jar file. If not, there are two scripts included to assist:

Linux and OS X users can run it as follows:
./cloudExplorer

Windows users can run Cloud Explorer by double clicking on the cloudExplorer.bat or CloudExplorer.jar file.

For any OS, you can start the program with the Java command:

java -jar -Xms100m -Xmx500m ./CloudExplorer.jar

[Video demonstraton]

[How to compile from source]

http://youtu.be/54v3rIUh0h8

[Upgrading]

Starting with v5.0, minor releases will no longer be published on Github. To upgrade, please use the updater located in the application. Everytime Cloud Explorer is ran, it will check for the latest version. To apply the update, click "Help -> Check for updates"


[How to migrate data between S3 accounts]

Load the destination account and click "Set as migration Account" under Settings.
Create the destination bucket on the destination account.
Load the origin S3 account and select the bucket to transfer to the new S3 account.
Under the "Tools" menu, select "Migrate bucket to another S3 account".
Type in the destination bucket name and click "Start Bucket Migration".
Wait for transfers to complete.


[Background Sync]

Background Sync allows Cloud Explorer to sync a directory in the background to S3 every 5 minutes. Sync will only upload files that do not exist on S3 and no files will be deleted. 
<br>
<br>
From the GUI: 
<br>
	Click on "Background Syncing"
<br>
	Click Configure
<br>
	Click on a destination Directory
<br>
	Click Save.
<br>
	Click on "Background Syncing"
<br>
	Click "Run".
<br>
	Syncing will occur every 5 minutes.

<br>
Starting background sync from the Command Line (Useful for running as a background process):

Create your config file from the GUI.
<br>
	$ java -jar -Xms100m -Xmx500m CloudExplorer.jar daemon

<br>
[Run Cloud Explorer from Docker]
<br>
<br>
This will allow you to run Cloud Explorer 4.2 from a Docker container using my public repository.

1. docker pull rusher81572/cloudexplorer
2. docker run -d --net=host rusher81572/cloudexplorer /opt/start.sh

To connect to the container, you can use a VNC client.

	Example: vncviewer localhost:3

After you login VNC, you should see the GUI.


The default password is 123456.


If you want to use the Cloud Explorer CLI instead of the GUI, please run the container as shown below:


	1. docker run -it --net=host cloudExplorer /bin/bash
	
		(You should be in the container now)


	2. java -jar /cloudExplorer-4.2/CloudExplorer.jar help


	** Please note that you will need to use the GUI to create the s3.config file before using the CLI **

	

<br>
