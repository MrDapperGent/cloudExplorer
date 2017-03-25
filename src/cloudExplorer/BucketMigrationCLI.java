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
    String new_region = null;
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
    String active_folder = null;
    String objectlist = null;
    String win = "\\";
    String lin = "/";
    String sep = null;
    Boolean deltas = false;
    String change_folder = null;
    String[] object_array;
    Runnable migrationengine;
    ExecutorService executor = Executors.newFixedThreadPool((int) 5);

    BucketMigrationCLI(String Aaccess_key, String Asecret_key, String Abucket, String Aendpoint, String[] Aobject_array, Boolean Asnapshot) {
        access_key = Aaccess_key;
        secret_key = Asecret_key;
        bucket = Abucket;
        endpoint = Aendpoint;
        object_array = Aobject_array;
        snapshot = Asnapshot;
    }

    String date(String format) {
        Date date = new Date();
        SimpleDateFormat dt = new SimpleDateFormat(format);
        return dt.format(date);
    }

    /**
     *
     * public void snapBack() { for (int i = 1; i != restoreArray.length; i++) {
     *
     * if (restoreArray[i] != null) { if
     * (restoreArray[i].contains(active_folder)) { String original_name =
     * restoreArray[i].replaceAll(active_folder, ""); if
     * (objectlist.contains(original_name)) { if
     * (modified_check(restoreArray[i], original_name)) { get = new
     * Get(restoreArray[i], new_access_key, new_secret_key, new_bucket,
     * new_endpoint, temp_file, null); get.run(); put = new Put(temp_file,
     * access_key, secret_key, bucket, endpoint, original_name, false, false,
     * false); put.run(); } } else { get = new Get(restoreArray[i],
     * new_access_key, new_secret_key, new_bucket, new_endpoint, temp_file,
     * null); get.run(); put = new Put(temp_file, access_key, secret_key,
     * bucket, endpoint, original_name, false, false, false); put.run(); } } } }
     * System.out.print("\nSnapshot restore operation complete."); }
     *
     */
    public void migrate() {
        String date = date("yyyy-MM-dd");
        for (int i = 1; i != object_array.length; i++) {
            if (object_array[i] != null) {
                if (object_array[i].contains(win)) {
                    sep = win;
                } else {
                    sep = lin;
                }

                String search = null;
                if (snapshot) {
                    search = "Snapshot-" + bucket + "-" + date + sep + object_array;
                } else {
                    search = object_array[i];
                }

                if (object_array[i].contains("Snapshot-Changes-")) {
                } else {
                    if (snapshot) {
                        get = new Get(object_array[i], access_key, secret_key, bucket, endpoint, temp_file, null);
                        get.run();
                        if (deltas) {
                            //change_folder = mainFrame.snap_folder.replace("Snapshot-", "Snapshot-Changes-");
                            put = new Put(temp_file, new_access_key, new_secret_key, new_bucket, new_endpoint, change_folder + object_array[i], false, false, false);
                        } else {
                            put = new Put(temp_file, new_access_key, new_secret_key, new_bucket, new_endpoint, "Snapshot-" + bucket + "-" + date + sep + object_array[i], false, false, false);
                        }
                        put.run();
                    } else {
                        migrationengine = new MigrationEngine(object_array[i], bucket, access_key, secret_key, endpoint, new_bucket, new_access_key, new_secret_key, new_endpoint);
                        executor.execute(migrationengine);
                    }
                }
            }
            System.gc();
        }
        executor.shutdown();

        while (!executor.isTerminated()) {
        }
        if (snapshot) {
            System.out.print("\nBucket snapshot complete.\n\n");
        } else {
            System.out.print("\n\nBucket migration complete.\n\n");
        }

    }

    String listBuckets(String access_key, String secret_key, String endpoint) {

        AWSCredentials credentials = new BasicAWSCredentials(access_key, secret_key);
        AmazonS3 s3Client = new AmazonS3Client(credentials,
                new ClientConfiguration());
        s3Client.setS3ClientOptions(S3ClientOptions.builder().setPathStyleAccess(false).build());
        s3Client.setEndpoint(endpoint);
        String[] array = new String[10];
        String bucketlist = null;

        try {
            for (Bucket bucket : s3Client.listBuckets()) {
                bucketlist = bucketlist + " " + bucket.getName();
            }
        } catch (Exception listBucket) {
            System.out.print("\n\nAn error has occurred in listBucket.");
            System.out.print("\n\nError Message:    " + listBucket.getMessage());
        }
        String parse = null;

        if (bucketlist != null) {
            parse = bucketlist.replace("null", "");

        } else {
            parse = "no_bucket_found";
        }

        return parse;
    }

    void checkBucket() {
        ReloadBuckets buckets = new ReloadBuckets(new_access_key, new_secret_key, new_endpoint, null);
        bucketlist = listBuckets(new_access_key, new_secret_key, new_endpoint);
    }

    void scanDestination() {

        BucketClass bucketObject = new BucketClass();
        destinationBucketlist = bucketObject.listBucketContents(new_access_key, new_secret_key, new_bucket, new_endpoint);
        if (restoreSnapshot) {
            restoreArray = destinationBucketlist.split("@@");
            objectlist = bucketObject.listBucketContents(access_key, secret_key, bucket, endpoint);
        }
        System.gc();
    }

    public void run() {
        if (!restoreSnapshot) {
            checkBucket();
        }
        if (restoreSnapshot) {
            //          scanDestination();
//            snapBack();
        } else {
            if (bucketlist.contains(new_bucket)) {
                scanDestination();
                migrate();
            } else {
                System.out.print("\nError: Destination S3 account does not have the bucket: " + new_bucket + ".");
            }
        }

    }

    void startc(String Aaccess_key, String Asecret_key, String Abucket, String Aendpoint, String[] Aobject_array, Boolean Asnapshot) {
        bucketMigration = new Thread(new BucketMigrationCLI(Aaccess_key, Asecret_key, Abucket, Aendpoint, Aobject_array, Asnapshot));
        bucketMigration.start();
    }

}
