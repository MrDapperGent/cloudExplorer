This is a simple, free, and open-source S3 client made by me. It works on Windows, Linux, and Mac.  Cloud Explorer provides extras that are not found elsewhere in other S3 browsers like search for objects, music player, transition to Amazon Glacier, Amazon RRS, migrate buckets between S3 accounts, take screen shots to S3, simple text editor, and image viewer. Users can easily sync folders to their S3 account and upload and download files to their computer.



![Music Player](https://www.linux-toys.com/objects.png)
![Editor](https://linux-toys.com/editor-new.png)


[How to run the program]

To run the application, most users should be able to run the program by double clicking on the CloudExplorer.jar file. If not, there are two scripts included to assist:

Linux and OS X users can run it as follows:
./cloudExplorer

Windows users can run Cloud Explorer by double clicking on the cloudExplorer.bat or CloudExplorer.jar file.

For any OS, you can start the program with the Java command:

java -jar -Xms100m -Xmx500m ./CloudExplorer.jar


[How to migrate data between S3 accounts]

Load the destination account and click "Set as migration Account" under Settings.
Create the destination bucket on the destination account.
Load the origin S3 account and select the bucket to transfer to the new S3 account.
Under the "Tools" menu, select "Migrate bucket to another S3 account".
Type in the destination bucket name and click "Start Bucket Migration".
Wait for transfers to complete.


[Background Sync]

Background Sync allows Cloud Explorer to function like Dropbox. It will sync to and from the S3 server. Please note that no files will be deleted. 
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
