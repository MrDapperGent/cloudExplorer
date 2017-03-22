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
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.Bucket;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
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

    public void calibrate() {
        try {
            jTextArea1.setCaretPosition(jTextArea1.getLineStartOffset(jTextArea1.getLineCount() - 1));
        } catch (Exception e) {
        }
    }

    BucketMigration(String Aaccess_key, String Asecret_key, String Abucket, String Aendpoint, NewJFrame AmainFrame, Boolean Asnapshot, Boolean ArestoreSnapshot, String Aactive_folder, Boolean Adeltas) {
        access_key = Aaccess_key;
        secret_key = Asecret_key;
        bucket = Abucket;
        endpoint = Aendpoint;
        mainFrame = AmainFrame;
        snapshot = Asnapshot;
        restoreSnapshot = ArestoreSnapshot;
        active_folder = Aactive_folder;
        deltas = Adeltas;
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
            if (NewJFrame.gui) {
                jTextArea1.append(loadConfig.getMessage());
            } else {
                System.out.print(loadConfig.getMessage());
            }
        }
        String remove_null = data.replace("null", "");
        return remove_null;

    }

    String date(String format) {
        Date date = new Date();
        SimpleDateFormat dt = new SimpleDateFormat(format);
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
            if (NewJFrame.gui) {
                jTextArea1.append(modifiedChecker.getMessage());
            } else {
                System.out.print(modifiedChecker.getMessage());
            }
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
                            put = new Put(temp_file, access_key, secret_key, bucket, endpoint, original_name, false, false, false);
                            put.run();
                        }
                    } else {
                        get = new Get(restoreArray[i], new_access_key, new_secret_key, new_bucket, new_endpoint, temp_file, null);
                        get.run();
                        put = new Put(temp_file, access_key, secret_key, bucket, endpoint, original_name, false, false, false);
                        put.run();
                    }
                }
            }
        }
        jTextArea1.append("\nSnapshot restore operation complete.");
        calibrate();
    }

    public void migrate() {
        String date = date("yyyy-MM-dd");
        for (int i = 1; i != mainFrame.objectarray.length; i++) {
            if (mainFrame.objectarray[i] != null) {
                if (mainFrame.objectarray[i].contains(win)) {
                    sep = win;
                } else {
                    sep = lin;
                }

                String search = null;
                if (snapshot) {
                    search = "Snapshot-" + bucket + "-" + date + sep + mainFrame.objectarray[i];
                } else {
                    search = mainFrame.objectarray[i];
                }

                if (mainFrame.objectarray[i].contains("Snapshot-Changes-")) {
                } else {
                    if (destinationBucketlist.contains(search)) {

                        if (modified_check(search, mainFrame.objectarray[i])) {
                            if (snapshot) {
                                get = new Get(mainFrame.objectarray[i], access_key, secret_key, bucket, endpoint, temp_file, null);
                                get.run();
                                if (deltas) {
                                    change_folder = mainFrame.snap_folder.replace("Snapshot-", "Snapshot-Changes-");
                                    put = new Put(temp_file, new_access_key, new_secret_key, new_bucket, new_endpoint, change_folder + mainFrame.objectarray[i], false, false, false);
                                } else {
                                    put = new Put(temp_file, new_access_key, new_secret_key, new_bucket, new_endpoint, "Snapshot-" + bucket + "-" + date + sep + mainFrame.objectarray[i], false, false, false);
                                }
                                put.run();
                            } else {
                                get = new Get(mainFrame.objectarray[i], access_key, secret_key, bucket, endpoint, temp_file, null);
                                get.run();
                                put = new Put(temp_file, new_access_key, new_secret_key, new_bucket, new_endpoint, mainFrame.objectarray[i], false, false, false);
                                put.run();
                            }
                        }
                    } else {
                        get = new Get(mainFrame.objectarray[i], access_key, secret_key, bucket, endpoint, temp_file, null);
                        get.run();
                        if (snapshot) {
                            if (deltas) {
                                change_folder = mainFrame.snap_folder.replace("Snapshot-", "Snapshot-Changes-");
                                put = new Put(temp_file, new_access_key, new_secret_key, new_bucket, new_endpoint, change_folder + mainFrame.objectarray[i], false, false, false);
                            } else {
                                put = new Put(temp_file, new_access_key, new_secret_key, new_bucket, new_endpoint, "Snapshot-" + bucket + "-" + date + sep + mainFrame.objectarray[i], false, false, false);
                            }
                        } else {
                            put = new Put(temp_file, new_access_key, new_secret_key, new_bucket, new_endpoint, mainFrame.objectarray[i], false, false, false);
                        }
                        put.run();
                    }
                }
            }
        }

        if (snapshot) {
            if (NewJFrame.gui) {
                jTextArea1.append("\nBucket snapshot complete.");
            } else {
                System.out.print("\nBucket snapshot complete.");
            }
        } else {
            if (NewJFrame.gui) {
                jTextArea1.append("\nBucket migration complete.");
            } else {
                System.out.print("\n\nBucket migration complete.");
            }
        }

        mainFrame.drawBuckets();

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
            if (NewJFrame.gui) {
                jTextArea1.append(loadconfig.getMessage());
            } else {
                System.out.print(loadconfig.getMessage());
            }
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
            if (NewJFrame.gui) {
                mainFrame.jTextArea1.append("\n\nAn error has occurred in listBucket.");
                mainFrame.jTextArea1.append("\n\nError Message:    " + listBucket.getMessage());
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
        if (config.exists() || !NewJFrame.gui) {
            if (NewJFrame.gui) {
                loadDestinationAccount();
            }

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
                    if (NewJFrame.gui) {
                        jTextArea1.append("\nError: Destination S3 account does not have the bucket: " + new_bucket + ".");
                        calibrate();
                    } else {
                        System.out.print("\nError: Destination S3 account does not have the bucket: " + new_bucket + ".");
                    }

                }
            }

        } else {
            if (NewJFrame.gui) {
                jTextArea1.append("\nError: Migration config file does not exist. Please create one under the Settings tab.");
                calibrate();
            } else {
                System.out.print("\nError: Migration config file does not exist. Please create one in the GUI under the Settings tab.\n\n");
            }

        }

    }

    void startc(String Aaccess_key, String Asecret_key, String Abucket, String Aendpoint, NewJFrame AmainFrame, Boolean Asnapshot, Boolean ArestoreSnapshot, String Aactive_folder, Boolean Adeltas) {
        bucketMigration = new Thread(new BucketMigration(Aaccess_key, Asecret_key, Abucket, Aendpoint, AmainFrame, Asnapshot, ArestoreSnapshot, Aactive_folder, Adeltas));
        bucketMigration.start();
    }

    void stop() {
        mainFrame.jTextArea1.append("\nMigration aborted.\n");
        calibrate();
        bucketMigration.stop();
    }

}
