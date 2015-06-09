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
import static cloudExplorer.NewJFrame.jTextArea1;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import org.apache.commons.codec.digest.DigestUtils;

public class BucketMigration implements Runnable {

    NewJFrame mainFrame;
    String what = null;
    String access_key = null;
    String bucket = null;
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
    String Home = System.getProperty("user.home");
    String temp_file = (Home + File.separator + "object.tmp");
    String config_file = (Home + File.separator + "s3Migrate.config");
    Get get;
    Put put;
    Delete del;
    Boolean deleteOrigin = false;
    Boolean snapshot = false;
    BucketClass bucketObject = new BucketClass();

    public void calibrate() {
        try {
            jTextArea1.setCaretPosition(jTextArea1.getLineStartOffset(jTextArea1.getLineCount() - 1));
        } catch (Exception e) {
        }
    }

    BucketMigration(String Aaccess_key, String Asecret_key, String Abucket, String Aendpoint, NewJFrame AmainFrame, String Anew_bucket, Boolean AdeleteOrigin, Boolean Asnapshot) {
        access_key = Aaccess_key;
        secret_key = Asecret_key;
        bucket = Abucket;
        endpoint = Aendpoint;
        mainFrame = AmainFrame;
        new_bucket = Anew_bucket;
        deleteOrigin = AdeleteOrigin;
        snapshot = Asnapshot;
    }

    String loadMigrationConfig() {
        String data = null;

        try {
            FileReader fr = new FileReader(config_file);
            BufferedReader bfr = new BufferedReader(fr);
            String read = null;

            while ((read = bfr.readLine()) != null) {
                if (read.contains("=")) {
                    byte[] str = Base64.getDecoder().decode(read);
                    data = data + new String(str, "utf-8");
                } else {
                    data = data + read;
                }
            }
        } catch (Exception loadConfig) {
        }
        String remove_null = data.replace("null", "");
        return remove_null;

    }

    String date() {
        Date date = new Date();
        return date.toString();
    }

    boolean modified_check(String remoteFile, String localFile, Boolean tos3) {
        boolean recopy = false;
        long milli;
        FileInputStream fis = null;

        try {
            File check_localFile = new File(localFile);
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            Date remote = sdf.parse(bucketObject.getObjectInfo(remoteFile, access_key, secret_key, bucket, endpoint, "objectdate"));
            milli = check_localFile.lastModified();
            Date local = new Date(milli);
            Date local_md5String = sdf.parse(bucketObject.getObjectInfo(remoteFile, access_key, secret_key, bucket, endpoint, "objectdate"));
            Date remote_md5String = sdf.parse(bucketObject.getObjectInfo(remoteFile, new_access_key, new_secret_key, new_bucket, new_endpoint, "objectdate"));

            if (local.after(remote)) {
                recopy = true;
            }
        } catch (Exception modifiedChecker) {
        }
        return recopy;
    }

    public void migrate() {
        String date = date();
        for (int i = 1; i != mainFrame.objectarray.length; i++) {
            if (mainFrame.objectarray[i] != null) {
                if (destinationBucketlist.contains("Snapshot-" + bucket + "-" + date + File.separator + mainFrame.objectarray[i])) {
                    if (snapshot) {
                        if (modified_check(mainFrame.objectarray[i], mainFrame.objectarray[i], true)) {
                            get = new Get(mainFrame.objectarray[i], access_key, secret_key, bucket, endpoint, temp_file, null);
                            get.run();
                            put = new Put(temp_file, new_access_key, new_secret_key, new_bucket, new_endpoint, "Snapshot-" + bucket + "-" + date + File.separator + mainFrame.objectarray[i], false, false);
                            put.run();
                        }
                    } else if (deleteOrigin) {
                        del = new Delete(mainFrame.objectarray[i], access_key, secret_key, bucket, endpoint, null);
                        del.run();
                    } else {
                        mainFrame.jTextArea1.append("\nSkipping: " + mainFrame.objectarray[i] + " because it exists on the destination bucket already.");
                        calibrate();
                    }

                } else {
                    get = new Get(mainFrame.objectarray[i], access_key, secret_key, bucket, endpoint, temp_file, null);
                    get.run();
                    if (snapshot) {
                        put = new Put(temp_file, new_access_key, new_secret_key, new_bucket, new_endpoint, "Snapshot-" + bucket + "-" + date + File.separator + mainFrame.objectarray[i], false, false);

                    } else {
                        put = new Put(temp_file, new_access_key, new_secret_key, new_bucket, new_endpoint, mainFrame.objectarray[i], false, false);
                    }
                    put.run();

                    if (deleteOrigin) {
                        scanDestination();
                        if (destinationBucketlist.contains(mainFrame.objectarray[i])) {
                            del = new Delete(mainFrame.objectarray[i], access_key, secret_key, bucket, endpoint, null);
                            del.run();
                        }
                    }

                }
            }
        }

        jTextArea1.append(
                "\nBucket migration complete.");
        mainFrame.reloadBuckets();

        calibrate();

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
                    new_region = account_array[4];
                }
            }

        } catch (Exception loadconfig) {
        }
    }

    String listBuckets(String access_key, String secret_key, String endpoint) {

        AWSCredentials credentials = new BasicAWSCredentials(access_key, secret_key);
        AmazonS3 s3Client = new AmazonS3Client(credentials,
                new ClientConfiguration().withSignerOverride("S3SignerType"));
        s3Client.setEndpoint(endpoint);

        String[] array = new String[10];

        String bucketlist = null;

        try {

            for (Bucket bucket : s3Client.listBuckets()) {
                bucketlist = bucketlist + " " + bucket.getName();
            }

        } catch (Exception listBucket) {
            mainFrame.jTextArea1.append("\n\nAn error has occurred in listBucket.");
            mainFrame.jTextArea1.append("\n\nError Message:    " + listBucket.getMessage());
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
        System.gc();
    }

    public void run() {
        File config = new File(config_file);
        if (config.exists()) {
            loadDestinationAccount();
            checkBucket();
            if (bucketlist.contains(new_bucket)) {
                scanDestination();
                migrate();
            } else {
                jTextArea1.append("\nError: Destination S3 account does not have the bucket: " + new_bucket + ".");
                calibrate();
            }

        } else {
            jTextArea1.append("\nError: Migration config file does not exist. Please create one under the Settings tab.");
            calibrate();
        }

    }

    void startc(String Aaccess_key, String Asecret_key, String Abucket, String Aendpoint, NewJFrame AmainFrame, String Anew_bucket, Boolean AdeleteOrigin, Boolean Asnapshot) {
        bucketMigration = new Thread(new BucketMigration(Aaccess_key, Asecret_key, Abucket, Aendpoint, AmainFrame, Anew_bucket, AdeleteOrigin, Asnapshot));
        bucketMigration.start();
    }

    void stop() {
        mainFrame.jTextArea1.append("\nMigration aborted.\n");
        calibrate();
        bucketMigration.stop();
    }

}
