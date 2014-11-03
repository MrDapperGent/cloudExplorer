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
 * this program. If not, see <http://www.gnu.org/licenses/>.package
 * cloudExplorer
 *
 */
package cloudExplorer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class CLI {

    Delete delete;
    String[] object_array = null;

    Get get;
    String name = null;
    String arg1 = null;
    String operation = null;
    String destination = null;
    String delete_file = null;
    String get_file = null;
    String Home = System.getProperty("user.home");
    String s3_config_file = Home + File.separator + "s3.config";
    BucketClass bucketObject = new BucketClass();
    Acl objectacl = new Acl();
    File put_file;
    String build_file_location;
    Put put;
    String build_name = null;
    String[] saved_s3_configs = null;
    String secret_key = null;
    String access_key = null;
    String endpoint = null;
    String bucket = null;
    String region = null;

    void messageParser(String message) {
        System.out.print(message);
    }

    void mainmenu() {

        for (int i = 0; i != 20; i++) {
            messageParser("\n");
        }
        messageParser("\n------------------------------------------------");
        messageParser("\n           Cloud Explorer CLI: " + operation);
        messageParser("\n------------------------------------------------");

    }

    void loadS3credentials() {
        try {
            for (String what : saved_s3_configs) {
                if (what == null) {
                    messageParser("\nError: an S3 config was null");
                    System.exit(-1);
                }
            }

            access_key = saved_s3_configs[0];
            secret_key = saved_s3_configs[1];
            endpoint = saved_s3_configs[2] + ":" + saved_s3_configs[3];
            region = saved_s3_configs[4];
        } catch (Exception loadS3Credentials) {
        }
    }

    String loadConfig(String what) {
        String data = null;

        try {
            FileReader fr = new FileReader(what);
            BufferedReader bfr = new BufferedReader(fr);
            String read = null;

            while ((read = bfr.readLine()) != null) {
                data = data + read;
            }
        } catch (Exception loadConfig) {
        }
        String remove_null = data.replace("null", "");
        String remove_symbol = remove_null.replace("@", " ");
        return remove_symbol;
    }

    void start(String arg0, String arg1, String arg2) {
        operation = arg0;
        Put.terminal = true;
        Get.terminal = true;
        if (operation.contains("lsbuckets") || operation.contains("makebucket") || operation.contains("rmbucket")) {
            saved_s3_configs = loadConfig(this.s3_config_file).toString().split(" ");
            loadS3credentials();
            mainmenu();
            if (operation.contains("lsbuckets")) {
                listBuckets();
            }

            if (operation.contains("makebucket")) {
                bucket = arg1;
                makeBucket();
            }
            if (operation.contains("rmbucket")) {
                bucket = arg1;
                rmBucket();
            }

        } else {
            if (arg2 == null) {
                System.out.print("\n\n\nError, no bucket specified.\n\n\n");
            } else {
                bucket = arg2;
            }

            put_file = new File(arg1);
            get_file = arg1;
            delete_file = arg1;
            destination = arg1;
            mainmenu();

            try {
                File s3config = new File(s3_config_file);
                if (s3config.exists()) {
                } else {
                    messageParser("\nError: Build config file not found.");
                }

                saved_s3_configs = loadConfig(this.s3_config_file).toString().split(" ");
                loadS3credentials();

                new Thread(new Runnable() {
                    public void run() {
                        if (operation.contains("syncfroms3") || operation.contains("synctos3")) {

                            File check_destination = new File(destination);
                            if (check_destination.exists()) {

                                if (operation.contains("syncfroms3")) {
                                    syncFromS3();
                                }
                                if (operation.contains("synctos3")) {
                                    syncToS3();
                                }
                            } else {
                                System.out.print("\n\n\nError: origin/destination directory not found.\n\n\n");
                                System.exit(-1);
                            }
                        }
                        if (operation.contains("delete")) {
                            deleteFromS3();
                        }

                        if (operation.contains("ls")) {
                            ls();
                        }
                        if (operation.contains("put")) {
                            if (put_file.exists()) {
                                putTOs3(put_file);
                            } else {
                                messageParser("\nError: " + put_file.toString() + " does not exist");
                            }
                        }
                        if (operation.contains("get")) {
                            getFromS3();
                        }

                    }

                }).start();

            } catch (Exception Start) {
            }

        }
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

        File dir = new File(File.separator + what.substring(0, another_counter));
        dir.mkdirs();
        return what;
    }

    void syncToS3() {
        System.out.print("\n\nStarting sync from: " + destination + " to bucket: " + bucket + "\n");
        File dir = new File(destination);
        reloadObjects();
        String[] extensions = new String[]{" "};
        List<File> files = (List<File>) FileUtils.listFiles(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        for (File file_found : files) {
            int found = 0;
            for (int y = 1; y != object_array.length; y++) {
                if (object_array[y].contains(makeDirectory(file_found.getAbsolutePath().toString()))) {
                    found++;
                }
            }

            if (found == 0) {
                String object = makeDirectory(file_found.getAbsolutePath().toString());
                put = new Put(file_found.getAbsolutePath().toString(), access_key, secret_key, bucket, endpoint, object, false, false);
                put.run();
                found = 0;
            }
        }
        System.out.print("\nSync operation complete.\n\n\n");
    }

    void reloadObjects() {
        try {
            if (bucket != null) {
                String objectlist = bucketObject.listBucketContents(access_key, secret_key, bucket, endpoint);
                object_array = objectlist.split("@@");
            } else {
                System.out.print("\n\n\nError, no bucket specified.\n\n\n");
            }
        } catch (Exception reloadObjects) {
            System.out.print("\n\n\nError with loading objects:\n\n\n");
        }
    }

    void syncFromS3() {
        try {

            System.out.print("\n\nStarting sync from bucket: " + bucket + " to destination: " + destination + ".\n");
            reloadObjects();
            File[] fromS3File = new File[object_array.length];
            for (int i = 1; i != object_array.length; i++) {
                String new_object_name = convertObject(object_array[i], "download");
                fromS3File[i] = new File(destination + new_object_name);
                if (fromS3File[i].exists()) {
                } else {
                    makeDirectory(destination + File.separator + object_array[i]);
                    String object = makeDirectory(object_array[i]);
                    get = new Get(object_array[i], access_key, secret_key, bucket, endpoint, destination + File.separator + object, null);
                    get.run();
                }
            }
        } catch (Exception SyncLocal) {
        }
        System.out.print("\nSync operation complete.\n\n\n");
    }

    void ls() {
        try {
            System.out.print("\n\nLoading objects for Bucket: " + bucket + "........\n\n");
            String objectlist = bucketObject.listBucketContents(access_key, secret_key, bucket, endpoint);
            object_array = objectlist.split("@@");

            for (String obj : object_array) {
                if (obj.contains("null")) {
                } else {
                    System.out.print("\n" + obj);
                }
            }

            System.out.print("\n\n\nBucket listing operation Complete\n\n\n");

        } catch (Exception ls) {
            System.out.print("\n\nAn Error has occured while listing the objects or the bucket does not exist.\n\n\n");
            System.exit(-1);
        }
    }

    String convertObject(String what, String operation) {

        if (what.contains("/")) {
            what = what.replace("/", File.separator);
        }

        if (what.contains("\\")) {
            what = what.replace("\\", File.separator);
        }

        int count = 0;
        int slash_counter = 0;
        String out_file = null;
        int another_counter = 0;

        for (int y = 0; y != what.length(); y++) {
            if (what.substring(y, y + 1).contains(File.separator)) {
                slash_counter++;
                another_counter = y;
            }
        }

        for (int y = 0; y != what.length(); y++) {
            if (y == another_counter) {
                if (operation.contains("download")) {
                    if (what.contains(File.separator)) {
                        out_file = (what.substring(y, what.length()));
                    } else {
                        out_file = (what);
                    }
                } else {
                    out_file = (what.substring(y + 1, what.length()));
                }
            }
        }
        return out_file;
    }

    void getFromS3() {
        try {
            NewJFrame.perf = true;
            System.out.print("\n\nDownloading " + get_file + "........");
            String new_object_name = convertObject(get_file, "download");
            destination = Home + File.separator + new_object_name;
            get = new Get(get_file, access_key, secret_key, bucket, endpoint, destination, null);
            Get.debug = true;
            get.run();
            File check = new File(Home + File.separator + new_object_name);
            if (check.exists()) {
                System.out.print("\n\nGET operation Complete. File Saved to: " + destination + ". \n\n\n");
            } else {
                System.out.print("\n\nError: GET did not complete successfully.\n\n\n");
            }
        } catch (Exception send) {
            System.out.print("\n\nAn Error has occured while downloading the file.");
            System.exit(-1);
        }
    }

    void listBuckets() {
        try {
            System.out.print("\n\n\nListing Buckets:");
            BucketClass.terminal = true;
            System.out.print(bucketObject.listBuckets(access_key, secret_key, endpoint));
            System.out.print("\n\n\nBucket listing operation Complete\n\n\n");
        } catch (Exception listBuckets) {
            System.out.print("\n\n\nAn error has occurred while listing buckets.\n\n\n");
        }
    }

    void makeBucket() {
        try {
            BucketClass.terminal = true;
            System.out.print("\n\n\nCreating Bucket:" + bucket);
            bucketObject.makeBucket(access_key, secret_key, bucket, endpoint, region);
            System.out.printf("\n\n\nBucket creation operaton complete.\n\n\n");
        } catch (Exception makeBucket) {
            System.out.print("\n\n\nAn error has occurred with making a bucket.\n\n\n");
        }
    }

    void rmBucket() {
        try {
            BucketClass.terminal = true;
            System.out.print("\n\n\nDeleting Bucket:" + bucket);
            bucketObject.deleteBucket(access_key, secret_key, bucket, endpoint, region);
            System.out.print("\n\n\nBucket Delete operation Complete\n\n\n");

        } catch (Exception deleteBucket) {
            System.out.print("\n\n\nAn error has occurred with deleting a bucket.");
        }
    }

    void deleteFromS3() {
        try {
            System.out.print("\n\nDeleting: " + delete_file + "........");
            delete = new Delete(delete_file, access_key, secret_key, bucket, endpoint, null);
            Delete.debug = true;
            delete.startc(delete_file, access_key, secret_key, bucket, endpoint, null);
            System.out.print("\n\nDELETE operation Complete\n\n\n");
        } catch (Exception send) {
            System.out.print("\n\nAn Error has occured while deleting the file");
            System.exit(-1);
        }
    }

    void putTOs3(File dir) {
        try {
            NewJFrame.perf = true;
            System.out.print("\n\nUploading: " + put_file.getAbsolutePath().toString() + "........");
            put = new Put(put_file.getAbsolutePath().toString(), access_key, secret_key, bucket, endpoint, put_file.getName(), false, false);
            Put.debug = true;
            put.startc(put_file.getAbsolutePath().toString(), access_key, secret_key, bucket, endpoint, put_file.getName(), false, false);
            System.out.print("\n\nPUT operation Complete\n\n\n");
        } catch (Exception send) {
            System.out.print("\n\nAn Error has occured while uploading the file");
            System.exit(-1);
        }
    }
}
