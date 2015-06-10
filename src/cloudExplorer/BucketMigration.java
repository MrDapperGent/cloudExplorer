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
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

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
    String[] restoreArray = null;
    String Home = System.getProperty("user.home");
    String temp_file = (Home + File.separator + "object.tmp");
    String config_file = (Home + File.separator + "s3Migrate.config");
    Get get;
    Put put;
    Delete del;
    Boolean deleteOrigin = false;
    Boolean snapshot = false;
    BucketClass bucketObject = new BucketClass();
    Boolean restoreSnapshot = false;
    String active_folder = null;
    String objectlist = null;
    String win = "\\";
    String lin = "/";
    String sep = null;

    public void calibrate() {
        try {
            jTextArea1.setCaretPosition(jTextArea1.getLineStartOffset(jTextArea1.getLineCount() - 1));
        } catch (Exception e) {
        }
    }

    BucketMigration(String Aaccess_key, String Asecret_key, String Abucket, String Aendpoint, NewJFrame AmainFrame, Boolean AdeleteOrigin, Boolean Asnapshot, Boolean ArestoreSnapshot, String Aactive_folder) {
        access_key = Aaccess_key;
        secret_key = Asecret_key;
        bucket = Abucket;
        endpoint = Aendpoint;
        mainFrame = AmainFrame;
        deleteOrigin = AdeleteOrigin;
        snapshot = Asnapshot;
        restoreSnapshot = ArestoreSnapshot;
        active_folder = Aactive_folder;
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
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        return dt.format(date);
    }

    boolean modified_check(String snapFile, String origFile) {
        boolean recopy = false;
        String snapFile_md5String = null;
        String origFile_md5String = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            snapFile_md5String = mainFrame.bucket.getObjectInfo(snapFile, new_access_key, new_secret_key, new_bucket, new_endpoint, "checkmd5");
            Date snapFileDate = sdf.parse(mainFrame.bucket.getObjectInfo(snapFile, new_access_key, new_secret_key, new_bucket, new_endpoint, "objectdate"));
            origFile_md5String = mainFrame.bucket.getObjectInfo(origFile, access_key, secret_key, bucket, endpoint, "checkmd5");
            Date origFileDate = sdf.parse(mainFrame.bucket.getObjectInfo(origFile, access_key, secret_key, bucket, endpoint, "objectdate"));
            if (snapFile_md5String.contains(origFile_md5String) || snapFile_md5String.contains(origFile_md5String)) {
            } else {
                if ((origFileDate.after(snapFileDate) || snapFileDate.after(origFileDate))) {
                    recopy = true;
                }
            }
        } catch (Exception modifiedChecker) {
        }
        return recopy;
    }

    public void snapBack() {
        for (int i = 1; i != restoreArray.length; i++) {

            if (restoreArray[i] != null) {
                if (restoreArray[i].contains(active_folder)) {
                    String original_name = restoreArray[i].replaceAll(active_folder, "");
                    if (objectlist.contains(original_name)) {
                        if (modified_check(restoreArray[i], original_name)) {
                            get = new Get(restoreArray[i], new_access_key, new_secret_key, new_bucket, new_endpoint, temp_file, null);
                            get.run();
                            put = new Put(temp_file, access_key, secret_key, bucket, endpoint, original_name, false, false);
                            put.run();
                        }
                    } else {
                        get = new Get(restoreArray[i], new_access_key, new_secret_key, new_bucket, new_endpoint, temp_file, null);
                        get.run();
                        put = new Put(temp_file, access_key, secret_key, bucket, endpoint, original_name, false, false);
                        put.run();
                    }
                }
            }
        }
        jTextArea1.append("\nSnapshot restore operation complete.");
        calibrate();
    }

    public void migrate() {
        String date = date();
        for (int i = 1; i != mainFrame.objectarray.length; i++) {
            if (mainFrame.objectarray[i] != null) {
                if (mainFrame.objectarray[i].contains(win)) {
                    sep = win;
                } else {
                    sep = lin;
                }
                if (destinationBucketlist.contains("Snapshot-" + bucket + "-" + date + sep + mainFrame.objectarray[i])) {
                    if (snapshot) {
                        if (modified_check(mainFrame.objectarray[i], mainFrame.objectarray[i])) {
                            get = new Get(mainFrame.objectarray[i], access_key, secret_key, bucket, endpoint, temp_file, null);
                            get.run();
                            put = new Put(temp_file, new_access_key, new_secret_key, new_bucket, new_endpoint, "Snapshot-" + bucket + "-" + date + sep + mainFrame.objectarray[i], false, false);
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
                        put = new Put(temp_file, new_access_key, new_secret_key, new_bucket, new_endpoint, "Snapshot-" + bucket + "-" + date + sep + mainFrame.objectarray[i], false, false);
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

        if (snapshot) {
            jTextArea1.append("\nBucket snapshot complete.");
        } else {
            jTextArea1.append("\nBucket migration complete.");
        }
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
                    new_bucket = account_array[5];
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
        if (restoreSnapshot) {
            restoreArray = destinationBucketlist.split("@@");
            objectlist = bucketObject.listBucketContents(access_key, secret_key, bucket, endpoint);
        }
        System.gc();
    }

    public void run() {
        File config = new File(config_file);
        if (config.exists()) {
            loadDestinationAccount();
            if (!restoreSnapshot) {
                checkBucket();
            }
            if (restoreSnapshot) {
                scanDestination();
                snapBack();
            } else {
                if (bucketlist.contains(new_bucket)) {
                    scanDestination();
                    migrate();
                } else {
                    jTextArea1.append("\nError: Destination S3 account does not have the bucket: " + new_bucket + ".");
                    calibrate();
                }
            }

        } else {
            jTextArea1.append("\nError: Migration config file does not exist. Please create one under the Settings tab.");
            calibrate();
        }

    }

    void startc(String Aaccess_key, String Asecret_key, String Abucket, String Aendpoint, NewJFrame AmainFrame, Boolean AdeleteOrigin, Boolean Asnapshot, Boolean ArestoreSnapshot, String Aactive_folder) {
        bucketMigration = new Thread(new BucketMigration(Aaccess_key, Asecret_key, Abucket, Aendpoint, AmainFrame, AdeleteOrigin, Asnapshot, ArestoreSnapshot, Aactive_folder));
        bucketMigration.start();
    }

    void stop() {
        mainFrame.jTextArea1.append("\nMigration aborted.\n");
        calibrate();
        bucketMigration.stop();
    }

}
