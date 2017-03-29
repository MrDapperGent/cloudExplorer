/**
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package cloudExplorer;

import java.io.File;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.Bucket;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BucketMigrationCLI implements Runnable {

    String what = null;
    String access_key = null;
    String bucket = null;
    BucketClass bucketinfo = new BucketClass();
    String endpoint = null;
    String secret_key = null;
    String destination = null;
    String version = null;
    String bucketlist = null;
    String new_bucket = null;
    String new_access_key = null;
    String new_secret_key = null;
    String new_endpoint = null;
    Thread bucketMigration;
    String destinationBucketlist = null;
    String[] restoreArray = null;
    String Home = System.getProperty("user.home");
    String temp_file = (Home + File.separator + "object.tmp");
    String config_file = (Home + File.separator + "s3Migrate.config");
    Get get;
    Put put;
    Delete del;
    Boolean snapshot = false;
    BucketClass bucketObject = new BucketClass();
    Boolean restoreSnapshot = false;
    String win = "\\";
    String lin = "/";
    String objectlist = null;
    String sep = null;
    Boolean deltas = false;
    String change_folder = null;
    String[] object_array;
    Runnable migrationengine;
    ExecutorService executor = Executors.newFixedThreadPool((int) 5);
    String snapfolder;

    BucketMigrationCLI(String Aaccess_key, String Asecret_key, String Abucket, String Aendpoint, String[] Aobject_array, Boolean Asnapshot, String Asnapfolder, Boolean Adeltas, Boolean ArestoreSnapshot) {
        access_key = Aaccess_key;
        secret_key = Asecret_key;
        bucket = Abucket;
        endpoint = Aendpoint;
        object_array = Aobject_array;
        snapshot = Asnapshot;
        snapfolder = Asnapfolder;
        deltas = Adeltas;
        restoreSnapshot = ArestoreSnapshot;
    }

    public void calibrate() {
        try {
            NewJFrame.jTextArea1.setCaretPosition(NewJFrame.jTextArea1.getLineStartOffset(NewJFrame.jTextArea1.getLineCount() - 1));
        } catch (Exception e) {
        }
    }

    String date(String format) {
        Date date = new Date();
        SimpleDateFormat dt = new SimpleDateFormat(format);
        return dt.format(date);
    }

    public void migrate() {
        String date = date("yyyy-MM-dd");

        for (int i = 1; i != object_array.length; i++) {
            if (object_array[i] != null) {

                String snapshot_data = null;

                if (object_array[i].contains(win)) {
                    sep = win;
                } else {
                    sep = lin;
                }

                if (object_array[i].contains("Snapshot-Changes-")) {
                } else {
                    if (snapshot) {
                        if (deltas) {
                            change_folder = snapfolder.replace("Snapshot-", "Snapshot-Changes-");
                            snapshot_data = change_folder + object_array[i];
                        } else {
                            snapshot_data = "Snapshot-" + bucket + "-" + date + sep + object_array[i];
                        }
                    }
                    if (restoreSnapshot) {
                        migrationengine = new MigrationEngine(object_array[i], new_bucket, new_access_key, new_secret_key, new_endpoint, bucket, access_key, secret_key, endpoint, null, snapfolder);
                    } else {
                        migrationengine = new MigrationEngine(object_array[i], bucket, access_key, secret_key, endpoint, new_bucket, new_access_key, new_secret_key, new_endpoint, snapshot_data, null);
                    }
                    executor.execute(migrationengine);
                    System.gc();
                }
            }
        }
        executor.shutdown();

        while (!executor.isTerminated()) {
        }
        if (snapshot) {
            if (NewJFrame.gui) {
                NewJFrame.jTextArea1.append("\nBucket snapshot complete.\n\n");
                calibrate();
            } else {
                System.out.print("\nBucket snapshot complete.\n\n");
            }
        } else {
            if (NewJFrame.gui) {
                NewJFrame.jTextArea1.append("\n\nBucket migration complete.\n\n");
                calibrate();
            } else {
                System.out.print("\n\nBucket migration complete.\n\n");
            }
        }

    }

    String listBuckets(String access_key, String secret_key, String endpoint) {

        AWSCredentials credentials = new BasicAWSCredentials(access_key, secret_key);
        AmazonS3 s3Client = new AmazonS3Client(credentials,
                new ClientConfiguration());
        if (!endpoint.contains("amazonaws.com")) {
            s3Client.setS3ClientOptions(S3ClientOptions.builder().setPathStyleAccess(true).build());
        }
        s3Client.setEndpoint(endpoint);
        String[] array = new String[10];
        String bucketlist = null;

        try {
            for (Bucket bucket : s3Client.listBuckets()) {
                bucketlist = bucketlist + " " + bucket.getName();
            }
        } catch (Exception listBucket) {
            if (NewJFrame.gui) {
                NewJFrame.jTextArea1.append("\n\nAn error has occurred in listBucket.");
                NewJFrame.jTextArea1.append("\n\nError Message:    " + listBucket.getMessage());
            } else {
                System.out.print("\n\nAn error has occurred in listBucket.");
                System.out.print("\n\nError Message:    " + listBucket.getMessage());
            }

        }
        String parse = null;

        if (bucketlist != null) {
            parse = bucketlist.replace("null", "");

        } else {
            parse = "no_bucket_found";
        }

        return parse;
    }

    String loadMigrationConfig() {
        String data = null;

        try {
            FileReader fr = new FileReader(config_file);
            BufferedReader bfr = new BufferedReader(fr);
            String read = null;

            while ((read = bfr.readLine()) != null) {
                if (read.length() > 1) {
                    if (read.contains("@")) {
                        data = data + read;
                    }
                }
            }
        } catch (Exception loadConfig) {
        }
        String remove_null = data.replace("null", "");
        return remove_null;

    }

    void loadDestinationAccount() {
        String account = loadMigrationConfig();
        String[] account_array = new String[6];
        try {
            account_array = account.split("@");
            for (int i = 0; i != 6; i++) {
                if (account_array[i] != null) {
                    new_access_key = account_array[0];
                    new_secret_key = account_array[1];
                    new_endpoint = account_array[2] + ":" + account_array[3];
                    new_bucket = account_array[5];
                }
            }

        } catch (Exception loadconfig) {
        }
    }

    void checkBucket() {
        ReloadBuckets buckets = new ReloadBuckets(new_access_key, new_secret_key, new_endpoint, null);
        bucketlist = listBuckets(new_access_key, new_secret_key, new_endpoint);
    }

    void scanDestination() {
        BucketClass bucketObject = new BucketClass();
        destinationBucketlist = bucketObject.listBucketContents(new_access_key, new_secret_key, new_bucket, new_endpoint);
        System.gc();
    }

    public void run() {
        if (NewJFrame.gui) {
            loadDestinationAccount();
        }

        checkBucket();

        if (restoreSnapshot) {
            objectlist = bucketObject.listBucketContents(new_access_key, new_secret_key, new_bucket, new_endpoint);
            object_array = objectlist.split("@@");
        }
        if (bucketlist.contains(new_bucket)) {
            scanDestination();
            migrate();
        } else {
            if (NewJFrame.gui) {
                NewJFrame.jTextArea1.append("\nError: Destination S3 account does not have the bucket: " + new_bucket + ".");
            } else {
                System.out.print("\nError: Destination S3 account does not have the bucket: " + new_bucket + ".");
            }
        }
    }

    void startc(String Aaccess_key, String Asecret_key, String Abucket, String Aendpoint, String[] Aobject_array, Boolean Asnapshot, String Asnapfolder, Boolean Adeltas, Boolean ArestoreSnapshot) {
        bucketMigration = new Thread(new BucketMigrationCLI(Aaccess_key, Asecret_key, Abucket, Aendpoint, Aobject_array, Asnapshot, Asnapfolder, Adeltas, ArestoreSnapshot));
        bucketMigration.start();
    }

    void stop() {
        bucketMigration.stop();
        bucketMigration.isInterrupted();
    }
}
