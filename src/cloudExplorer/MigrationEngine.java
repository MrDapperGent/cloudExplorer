/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudExplorer;

import java.io.File;
import java.util.Random;
import java.util.UUID;

public class MigrationEngine implements Runnable {

    Put put;
    Get get;
    String remoteFile;
    String localFile;
    String bucket;
    String endpoint;
    String access_key;
    String secret_key;
    String object;
    String migration_bucket;
    String migration_endpoint;
    String migration_access_key;
    String migration_secret_key;
    BucketClass Bucket = new BucketClass();
    SyncToS3 synctos3;
    File file_found;
    String win = "\\";
    String lin = "/";
    File check_localFile;
    String Home = System.getProperty("user.home");
    String snapshot;

    MigrationEngine(String AremoteFile, String Abucket, String Aaccess_key, String Asecret_key, String Aendpoint, String Amigration_bucket, String Amigration_access_key, String Amigration_secret_key, String Amigration_endpoint, String Asnapshot) {
        remoteFile = AremoteFile;
        bucket = Abucket;
        access_key = Aaccess_key;
        secret_key = Asecret_key;
        endpoint = Aendpoint;
        migration_bucket = Amigration_bucket;
        migration_access_key = Amigration_access_key;
        migration_secret_key = Amigration_secret_key;
        migration_endpoint = Amigration_endpoint;
        snapshot = Asnapshot;
    }

    public String Transcode(String what) {
        String transcoded_object = null;
        if (what.contains(win) || (what.contains(lin))) {
            if (what.contains(win) && File.separator.contains(win)) {
                transcoded_object = what;
            }

            if (what.contains(lin) && File.separator.contains(lin)) {
                transcoded_object = what;
            }

            if (what.contains(lin) && File.separator.contains(win)) {
                transcoded_object = what.replace(lin, win);
            }

            if (what.contains(win) && File.separator.contains(lin)) {
                transcoded_object = what.replace(win, lin);
            }

        } else {
            transcoded_object = what;
        }
        return transcoded_object;
    }

    String makeDirectory(String what) {

        if (what.substring(0, 2).contains(":")) {
            what = what.substring(3, what.length());
        }

        if (what.substring(0, 1).contains("/")) {
            what = what.substring(1, what.length());
        }

        if (what.contains("/")) {
            what = what.replace("/", File.separator);
        }

        if (what.contains("\\")) {
            what = what.replace("\\", File.separator);
        }

        int slash_counter = 0;
        int another_counter = 0;

        for (int y = 0; y != what.length(); y++) {
            if (what.substring(y, y + 1).contains(File.separator)) {
                slash_counter++;
                another_counter = y;
            }
        }

        return what;
    }

    public void run() {

        boolean recopy = false;
        String snapFile_md5String = null;
        String origFile_md5String = null;
        String uuid = Home + File.separator + UUID.randomUUID().toString();

        try {
            snapFile_md5String = Bucket.getObjectInfo(remoteFile, migration_access_key, migration_secret_key, migration_bucket, migration_endpoint, "checkmd5");
            origFile_md5String = Bucket.getObjectInfo(remoteFile, access_key, secret_key, bucket, endpoint, "checkmd5");
            if (snapFile_md5String == null) {
                recopy = true;
            } else {
                if (snapFile_md5String.contains(origFile_md5String)) {
                } else {
                    recopy = true;
                }
            }
        } catch (Exception modifiedChecker) {
            System.out.print("\n\nError:" + modifiedChecker.getMessage() + "\n\n");
        }

        if (recopy) {
            get = new Get(remoteFile, access_key, secret_key, bucket, endpoint, uuid, null);
            get.run();
            if (snapshot != null) {
                System.out.print("\nSDebug 1:" + snapshot);
                put = new Put(uuid, migration_access_key, migration_secret_key, migration_bucket, migration_endpoint, snapshot, false, false, false);
            } else {
                System.out.print("\nSDebug 2:" + snapshot);
                put = new Put(uuid, migration_access_key, migration_secret_key, migration_bucket, migration_endpoint, remoteFile, false, false, false);
            }
            put.run();
            File delete = new File(uuid);
            delete.delete();
        }
    }

}
