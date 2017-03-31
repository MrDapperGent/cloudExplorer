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
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class BackgroundSync implements Runnable {

    String what = null;
    String version = null;
    String bucketlist = null;
    Thread bucketSync;
    String destinationBucketlist = null;
    String[] restoreArray = null;
    String Home = System.getProperty("user.home");
    String temp_file = (Home + File.separator + "object.tmp");
    String config_file = (Home + File.separator + "s3config.sync");
    String win = "\\";
    String lin = "/";
    String objectlist = null;
    String sep = null;
    String[] object_array;
    String directory;
    Runnable syncengine;
    String access_key = null;
    String secret_key = null;
    String endpoint = null;
    String bucket = null;
    ExecutorService executor = Executors.newFixedThreadPool((int) 5);
    ExecutorService executor2 = Executors.newFixedThreadPool((int) 5);

    BackgroundSync() {

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

    public void sync() {
        reloadObjects();
        String[] transcode = null;
        String transcoded_directory = null;

        if (directory.contains(win) || directory.contains(lin)) {
            if (directory.contains(win)) {
                sep = win;
                transcode = directory.split(Pattern.quote("\\"));
            }

            if (directory.contains(lin)) {
                sep = lin;
                transcode = directory.split(Pattern.quote("/"));
            }

            transcoded_directory = directory.replace(sep + transcode[transcode.length - 1], "");
        }

        if (NewJFrame.gui) {
            NewJFrame.jTextArea1.append("\nStarting sync from: " + directory + " to bucket: " + bucket);
            calibrate();
        } else {
            System.out.print("\nStarting sync from: " + directory + " to bucket: " + bucket);
        }

        try {
            File dir = new File(directory);
            List<File> files = (List<File>) FileUtils.listFiles(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
            for (File file_found : files) {
                String clean_object_name[] = directory.split(Pattern.quote(File.separator));
                String object = file_found.getAbsolutePath().toString();
                object = object.replace(directory, "");
                object = clean_object_name[clean_object_name.length - 1] + object;
                syncengine = new SyncEngine(object, file_found.getAbsolutePath(), file_found, object, bucket, access_key, secret_key, endpoint, false, false, false, true, null);
                executor2.execute(syncengine);
            }

            executor2.shutdown();
            while (!executor2.isTerminated()) {
            }
            reloadObjects();
        } catch (Exception e) {
            if (NewJFrame.gui) {
                NewJFrame.jTextArea1.append("\nError:" + e.getMessage());
                calibrate();
            } else {
                System.out.print("\nError:" + e.getMessage());
            }
        }

        if (NewJFrame.gui) {
            NewJFrame.jTextArea1.append("\nStarting sync from bucket: " + bucket + " to: " + transcoded_directory);
            calibrate();
        } else {
            System.out.print("\nStarting sync from bucket: " + bucket + " to: " + transcoded_directory);
        }

        try {
            File[] foo = new File[object_array.length];
            for (int i = 1; i != object_array.length; i++) {
                if (object_array[i] != null) {
                    int found = 0;
                    foo[i] = new File(transcoded_directory + File.separator + object_array[i]);
                    syncengine = new SyncEngine(object_array[i], null, null, object_array[i], bucket, access_key, secret_key, endpoint, null, null, null, false, transcoded_directory);
                    executor.execute(syncengine);
                }
            }
            executor.shutdown();
            while (!executor.isTerminated()) {
            }

            if (NewJFrame.gui) {
                NewJFrame.jTextArea1.append("\n\nBackground Sync complete.\n\n");
                calibrate();
            } else {
                System.out.print("\n\nBackground Sync complete.\n\n");
            }
            Thread.sleep(300000);
        } catch (Exception sync) {
            if (NewJFrame.gui) {
                NewJFrame.jTextArea1.append("\nError:" + sync.getMessage());
                calibrate();
            } else {
                System.out.print("\nError:" + sync.getMessage());
            }
        }
    }

    String loadSyncConfig() {
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

    void loadAccount() {
        String account = loadSyncConfig();
        String[] account_array = new String[6];
        try {
            account_array = account.split("@");
            for (int i = 0; i != 6; i++) {
                if (account_array[i] != null) {
                    access_key = account_array[0];
                    secret_key = account_array[1];
                    endpoint = account_array[2];
                    bucket = account_array[4];
                    directory = account_array[3];
                }
            }

        } catch (Exception loadconfig) {
        }
    }

    void reloadObjects() {
        BucketClass bucketObject = new BucketClass();
        String objectlist = bucketObject.listBucketContents(access_key, secret_key, bucket, endpoint);
        object_array = objectlist.split("@@");
        System.gc();
    }

    public void run() {
        if ((System.getenv("ACCESS_KEY") == null) || System.getenv("SECRET_KEY") == null || System.getenv("ENDPOINT") == null || System.getenv("DIRECTORY") == null || System.getenv("BUCKET") == null) {
            loadAccount();
        } else {
            access_key = System.getenv("ACCESS_KEY");
            secret_key = System.getenv("SECRET_KEY");
            endpoint = System.getenv("ENDPOINT");
            directory = System.getenv("DIRECTORY");
            bucket = System.getenv("BUCKET");
        }
        while (true) {
            sync();
            executor = Executors.newFixedThreadPool((int) 5);
            executor2 = Executors.newFixedThreadPool((int) 5);
        }
    }

    void startc() {
        bucketSync = new Thread(new BackgroundSync());
        bucketSync.start();
    }

    void stop() {
        bucketSync.stop();
        bucketSync.isInterrupted();
        NewJFrame.jTextArea1.setText("\nBackground Sync Cancelled.\n");
        calibrate();
    }
}
