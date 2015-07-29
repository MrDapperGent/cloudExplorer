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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class CLI {

    Delete delete;
    String[] object_array = null;
    String getOperationCount;
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
    double[] x;
    double[] y;
    double[] x_latency;
    double[] y_latency;
    double[] x_iops;
    double[] y_iops;
    String temp_file = (Home + File.separator + "object.tmp");
    String throughput_log = Home + File.separator + "throughput_results.csv";
    String latency_log = Home + File.separator + "latency_results.csv";
    String ops_log = Home + File.separator + "ops_results.csv";
    int threadcount;
    String getValue;
    Boolean performance_operation = true;
    public static Boolean mixed_mode = false;
    String win = "\\";
    String lin = "/";

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

    void deleteAll(String bucket) {
        System.out.print("\n\nDeleting all files in bucket: " + bucket);
        reloadObjects();
        for (int y = 1; y != object_array.length; y++) {
            if (object_array[y] != null) {
                arg1 = object_array[y];
                deleteFromS3();
            }
        }
        System.out.print("\n\nDelete everything operation complete.\n\n");
    }

    void createFolder(String folder
    ) {
        try {
            if (folder != null) {
                FileWriter frr = new FileWriter(temp_file, true);
                BufferedWriter bfrr = new BufferedWriter(frr);
                bfrr.write("This is a place holder file to simulate a folder for Cloud Explorer only. This file can be deleted.");
                bfrr.close();
                File temp = new File(temp_file);
                if (temp.exists()) {
                    System.out.print("\n\nAttempting to create folder: " + folder);
                    put = new Put(temp.getAbsolutePath().toString(), access_key, secret_key, bucket, endpoint, folder + File.separator, false, false);
                    Put.terminal = true;
                    put.run();
                    System.out.print("\nCreate folder operation complete\n\n");
                }

            }
        } catch (Exception createFolder) {
        }
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

    String loadConfig(String what
    ) {
        String data = null;

        try {
            FileReader fr = new FileReader(what);
            BufferedReader bfr = new BufferedReader(fr);
            String read = null;

            while ((read = bfr.readLine()) != null) {
                if (read.contains("@")) {
                    data = data + read;
                    break;
                }
            }
        } catch (Exception loadConfig) {
        }
        String remove_null = data.replace("null", "");
        String remove_symbol = remove_null.replace("@", " ");
        return remove_symbol;
    }

    void start(String arg0, final String arg1, final String arg2, final String arg3, final String arg4, final String arg5
    ) {
        operation = arg0;
        Put.terminal = true;
        Get.terminal = true;
        if (operation.contains("listbuckets") || operation.contains("makebucket") || operation.contains("rmbucket") || operation.contains("ls") || operation.contains("createfolder") || operation.contains("deleteall")) {
            saved_s3_configs = loadConfig(this.s3_config_file).toString().split(" ");
            loadS3credentials();
            mainmenu();
            bucket = arg1;

            if (operation.contains("ls")) {
                ls(0);
            }
            if (operation.contains("deleteall")) {
                deleteAll(arg1);
            }
            if (operation.contains("createfolder")) {
                createFolder(arg2);
            }
            if (operation.contains("listbuckets")) {
                listBuckets();
            }

            if (operation.contains("makebucket")) {
                makeBucket();
            }
            if (operation.contains("rmbucket")) {
                rmBucket();
            }

        } else {
            if (arg2 == null) {
                System.out.print("\n\n\nError, no bucket specified.\n\n\n");
                System.exit(-1);
            } else {
                bucket = arg2;
            }

            put_file = new File(arg1);
            get_file = arg1;

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
                        if (operation.contains("downloadtest") || operation.contains("uploadtest")) {
                            if (operation.contains("downloadtest")) {
                                performance_operation = false;
                            } else {
                                performance_operation = true;
                            }

                            bucket = arg1;
                            getValue = arg2;
                            threadcount = Integer.parseInt(arg3);
                            getOperationCount = arg4;
                            if (arg5 != null) {
                                if (arg5.contains("mixed")) {
                                    mixed_mode = true;
                                }
                            }
                            performance();
                        }

                        if (operation.contains("syncfroms3") || operation.contains("synctos3")) {

                            File check_destination = new File(destination);
                            if (check_destination.exists()) {

                                if (operation.contains("syncfroms3")) {
                                    syncFromS3(arg3);
                                }

                                if (operation.contains("synctos3")) {
                                    syncToS3(arg3);
                                }
                            } else {
                                System.out.print("\n\n\nError: origin/destination directory not found.\n\n\n");
                                System.exit(-1);
                            }
                        }
                        if (operation.contains("delete")) {
                            deleteFromS3();
                        }

                        if (operation.contains("put")) {
                            if (put_file.exists()) {
                                putTOs3(put_file);
                            } else {
                                messageParser("\nError: " + put_file.toString() + " does not exist");
                            }
                        }
                        if (operation.contains("search")) {
                            bucket = arg1;
                            get_file = arg2;
                            ls(1);
                        }

                        if (operation.contains("get")) {
                            destination = arg3;
                            getFromS3();
                        }

                    }

                }).start();

            } catch (Exception Start) {
            }

        }
    }

    String makeDirectory(String what
    ) {

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

    boolean modified_check(String remoteFile, String localFile, Boolean tos3) {
        boolean recopy = false;
        long milli;
        FileInputStream fis = null;
        String local_md5String = null;
        String remote_md5String = null;

        try {
            File check_localFile = new File(localFile);
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            Date remote = sdf.parse(bucketObject.getObjectInfo(remoteFile, access_key, secret_key, bucket, endpoint, "objectdate"));
            milli = check_localFile.lastModified();
            Date local = new Date(milli);
            fis = new FileInputStream(localFile);
            local_md5String = DigestUtils.md5Hex(fis);
            remote_md5String = bucketObject.getObjectInfo(remoteFile, access_key, secret_key, bucket, endpoint, "checkmd5");
            if (tos3) {
                if (local_md5String.contains(remote_md5String)) {
                } else {
                    if (local.after(remote)) {
                        recopy = true;
                    }
                }
            } else {
                if (local_md5String.contains(remote_md5String)) {
                } else {
                    if (remote.after(local)) {
                        recopy = true;
                    }
                }
            }
        } catch (Exception modifiedChecker) {
        }
        return recopy;
    }

    void syncToS3(String folder
    ) {
        if (folder != null) {
            System.out.print("\n\nStarting sync from: " + destination + " to bucket: " + bucket + " in folder: " + folder + " \n");
        } else {
            System.out.print("\n\nStarting sync from: " + destination + " to bucket: " + bucket + "\n");
        }

        File dir = new File(destination);

        reloadObjects();
        String[] extensions = new String[]{" "};
        List<File> files = (List<File>) FileUtils.listFiles(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        for (File file_found : files) {
            String object = makeDirectory(file_found.getAbsolutePath().toString());
            String[] cut = object.split(file_found.getName());
            String[] cut2 = null;
            object = object.replace(cut[0], "");
            if (cut[0].contains(win)) {
                cut2 = cut[0].split(Pattern.quote(win));
                object = cut2[cut2.length - 1] + win + object;
            } else {
                cut2 = cut[0].split(Pattern.quote(lin));
                object = cut2[cut2.length - 1] + lin + object;
            }

            int found = 0;
            for (int y = 1; y != object_array.length; y++) {
                if (object_array[y].contains(object)) {
                    if (!modified_check(object_array[y], file_found.getAbsolutePath(), true)) {
                        found++;
                    }
                }
            }

            if (found == 0) {

                if (folder != null) {
                    object = folder + File.separator + object;
                }
                put = new Put(file_found.getAbsolutePath().toString(), access_key, secret_key, bucket, endpoint, object, false, false);
                put.run();
                found = 0;
            }
        }

        System.out.print(
                "\nSync operation complete.\n\n\n");
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

    void syncFromS3(String folder) {
        try {
            if (folder != null) {
                System.out.print("\n\nStarting sync from Folder: " + folder + " on bucket: " + bucket + " to destination: " + destination + ".\n");
            } else {
                System.out.print("\n\nStarting sync from bucket: " + bucket + " to destination: " + destination + ".\n");
            }
            reloadObjects();
            File[] fromS3File = new File[object_array.length];
            for (int i = 1; i != object_array.length; i++) {
                if (object_array[i] != null) {
                    int found = 0;
                    String new_object_name = convertObject(object_array[i], "download");
                    fromS3File[i] = new File(destination + File.separator + object_array[i]);
                    if (fromS3File[i].exists()) {
                        if (!modified_check(object_array[i], fromS3File[i].getAbsolutePath(), false)) {
                            found++;
                        }
                    }
                    if (found == 0) {

                        try {
                            String[] cutit = null;
                            String transcoded_object = null;

                            if (object_array[i].contains(win) || (object_array[i].contains(lin))) {

                                if (object_array[i].contains(win)) {
                                    cutit = object_array[i].split(Pattern.quote(win));
                                    transcoded_object = cutit[1];
                                } else {
                                    cutit = object_array[i].split(Pattern.quote(lin));
                                    transcoded_object = cutit[1];
                                }
                            }

                            if (folder != null) {
                                if (object_array[i].contains(folder)) {
                                    File dir = new File(destination + File.separator + cutit[0]);
                                    dir.mkdirs();
                                    get = new Get(object_array[i], access_key, secret_key, bucket, endpoint, destination + File.separator + cutit[0] + File.separator + transcoded_object, null);
                                    System.out.print("\nDebug: " + object_array[i] + " " + access_key + " " + secret_key + " " + bucket + " " + endpoint + " " + destination + File.separator + cutit[0] + File.separator + transcoded_object);
                                    get.run();
                                }
                            } else {
                                if (object_array[i].contains(win) || (object_array[i].contains(lin))) {
                                    File dir = new File(destination + File.separator + cutit[0]);
                                    dir.mkdirs();
                                    get = new Get(object_array[i], access_key, secret_key, bucket, endpoint, destination + File.separator + cutit[0] + File.separator + cutit[1], null);
                                    get.run();
                                } else {
                                    get = new Get(object_array[i], access_key, secret_key, bucket, endpoint, destination + File.separator + object_array[i], null);
                                    get.run();
                                }
                            }
                        } catch (Exception foo) {
                        }
                        found = 0;
                    }
                }
            }
        } catch (Exception SyncLocal) {
        }
        System.out.print("\nSync operation complete.\n\n\n");
    }

    void ls(int op
    ) {
        try {
            int found = 0;

            if (op == 0) {
                System.out.print("\n\nLoading objects for Bucket: " + bucket + "........\n\n");
            } else {
                System.out.print("\n\nSearching for \"" + get_file + "\" in bucket \"" + bucket + "\"........\n\n");
            }
            String objectlist = bucketObject.listBucketContents(access_key, secret_key, bucket, endpoint);
            object_array = objectlist.split("@@");

            for (String obj : object_array) {
                if (obj.contains("null")) {
                } else {
                    if (op == 0) {
                        System.out.print("\n" + obj);
                        found++;
                    } else {
                        if (obj.contains(get_file)) {
                            System.out.print("\n" + obj);
                            found++;
                        }
                    }
                }
            }

            System.out.print("\n\n\nBucket listing operation Complete. Found: " + found + " file(s).\n\n\n");

        } catch (Exception ls) {
            System.out.print("\n\nAn Error has occured while listing the objects or the bucket does not exist.\n\n\n");
            System.exit(-1);
        }
    }

    String convertObject(String what, String operation
    ) {

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
            if (destination == null) {
                destination = Home + File.separator + new_object_name;
            } else {
                destination = destination + File.separator + new_object_name;
            }
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
            System.out.print("\n\n\nCreating Bucket \"" + bucket + "\".......");
            bucketObject.makeBucket(access_key, secret_key, bucket, endpoint, region);
            System.out.printf("\n\n\nBucket creation operaton complete.\n\n\n");
        } catch (Exception makeBucket) {
            System.out.print("\n\n\nAn error has occurred with making a bucket.\n\n\n");
        }
    }

    void rmBucket() {
        try {
            BucketClass.terminal = true;
            System.out.print("\n\n\nDeleting Bucket \"" + bucket + "\".......");
            bucketObject.deleteBucket(access_key, secret_key, bucket, endpoint, region);
            System.out.print("\n\n\nBucket Delete operation Complete\n\n\n");

        } catch (Exception deleteBucket) {
            System.out.print("\n\n\nAn error has occurred with deleting a bucket.");
        }
    }

    void deleteFromS3() {
        try {
            delete_file = arg1;
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

    void putTOs3(File dir
    ) {
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

    public void performance_logger(double time, double rate, String what) {
        try {
            FileWriter frr = new FileWriter(what, true);
            BufferedWriter bfrr = new BufferedWriter(frr);
            bfrr.write("\n" + time + "," + rate);
            bfrr.close();
        } catch (Exception perf_logger) {
        }
    }

    void performance() {
        System.out.print("\n\nStarting performance test.....\n\n\n");
        NewJFrame.perf = true;
        File tempFile = new File(temp_file);
        File throughputfile = new File(throughput_log);
        File opsfile = new File(ops_log);
        File latencyfile = new File(latency_log);

        int op_count = Integer.parseInt(getOperationCount);
        int file_size = Integer.parseInt(getValue);
        float num_threads = threadcount;

        if (tempFile.exists()) {
            tempFile.delete();
        }

        if (throughputfile.exists()) {
            throughputfile.delete();
        }
        if (latencyfile.exists()) {
            latencyfile.delete();
        }

        if (opsfile.exists()) {
            opsfile.delete();
        }

        if (file_size > 0 && num_threads > 0 && op_count > 0) {

            try {
                FileOutputStream s = new FileOutputStream(temp_file);
                byte[] buf = new byte[file_size * 1024];
                s.write(buf);
                s.flush();
                s.close();
            } catch (Exception add) {
            }

            if (tempFile.exists()) {

                try {
                    String upload = tempFile.getAbsolutePath();

                    if (!performance_operation || mixed_mode) {
                        put = new Put(upload, access_key, secret_key, bucket, endpoint, "performance_test_data", false, false);
                        put.startc(upload, access_key, secret_key, bucket, endpoint, "performance_test_data", false, false);
                    }

                    x = new double[op_count];
                    y = new double[op_count];
                    x_latency = new double[op_count];
                    y_latency = new double[op_count];
                    x_iops = new double[op_count];
                    y_iops = new double[op_count];

                    int counter = 0;
                    int display_counter = 0;

                    for (int z = 0; z != op_count; z++) {

                        if (mixed_mode) {
                            if (performance_operation) {
                                performance_operation = false;
                            } else {
                                performance_operation = true;
                            }
                        }

                        long t1 = System.currentTimeMillis();

                        for (int i = 0; i != num_threads; i++) {

                            if (performance_operation) {
                                put = new Put(upload, access_key, secret_key, bucket, endpoint, "performance_test_data_" + i + "_" + z, false, false);
                                put.startc(upload, access_key, secret_key, bucket, endpoint, "performance_test_data_" + i + "_" + z, false, false);
                            } else {
                                get = new Get("performance_test_data", access_key, secret_key, bucket, endpoint, temp_file + i, null);
                                get.startc("performance_test_data", access_key, secret_key, bucket, endpoint, temp_file + i, null);
                            }
                        }

                        double t2 = System.currentTimeMillis();
                        double diff = t2 - t1;
                        double total_time = diff / 1000;

                        double rate = (num_threads * file_size / total_time / 1024);
                        rate = Math.round(rate * 100);
                        rate = rate / 100;

                        double iops = (num_threads / total_time);
                        iops = Math.round(iops * 100);
                        iops = iops / 100;

                        if (performance_operation) {
                            System.out.print("\nPUT Operation: " + z + ". Time: " + total_time + " seconds." + " Average speed with " + num_threads + " thread(s) is: " + rate + " MB/s. OPS/s: " + iops);
                        } else {
                            System.out.print("\nGET Operation: " + z + ". Time: " + total_time + " seconds." + " Average speed with " + num_threads + " thread(s) is: " + rate + " MB/s. OPS/s: " + iops);
                        }
                        if (!mixed_mode) {
                            performance_logger(counter, rate, throughput_log);
                            performance_logger(counter, iops, ops_log);
                            performance_logger(counter, total_time, latency_log);
                        }

                        if (counter == 100) {
                            counter = 0;
                            x = new double[op_count];
                            y = new double[op_count];
                            x_latency = new double[op_count];
                            y_latency = new double[op_count];
                            x_iops = new double[op_count];
                            y_iops = new double[op_count];
                        }
                        y[counter] = (Double) rate;
                        x[counter] = counter;
                        y_latency[counter] = total_time;
                        x_latency[counter] = counter;
                        y_iops[counter] = iops;
                        x_iops[counter] = counter;
                        counter++;
                        display_counter++;
                    }

                } catch (Exception ex) {
                }

            } else {
                System.out.print("\n Please specifiy more than 0 threads.");
            }

            if (!mixed_mode) {
                System.out.print("\n\n\nResults saved in CSV format to: " + "\n" + throughput_log + "\n" + latency_log + "\n" + ops_log + "\n\n\n");
            }

            NewJFrame.perf = false;

        } else {
            System.out.print("\nError: Thread and Count values must be greater than 0. Object Size value must be 1024 or greater.");
        }
    }
}
