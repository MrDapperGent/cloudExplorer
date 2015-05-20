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
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import static cloudExplorer.NewJFrame.jTextArea1;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SyncToS3 implements Runnable {

    NewJFrame mainFrame;
    String[] objectarray;
    String[] ObjectsConverted;
    String access_key = null;
    String bucket = null;
    String endpoint = null;
    String secret_key = null;
    String destination = null;
    public static Boolean running = false;
    File location;
    Get get;
    Thread syncToS3;
    Put put;
    Boolean rrs = false;
    Boolean encrypt = false;
    String Home = System.getProperty("user.home");
    String win = "\\";
    String lin = "/";

    SyncToS3(NewJFrame AmainFrame, File Alocation, String Aaccess_key, String Asecret_key, String Abucket, String Aendpoint, String[] Aobjectarray, Boolean Arrs, Boolean Aencrypt) {
        objectarray = Aobjectarray;
        location = Alocation;
        access_key = Aaccess_key;
        secret_key = Asecret_key;
        bucket = Abucket;
        endpoint = Aendpoint;
        rrs = Arrs;
        encrypt = Aencrypt;
        mainFrame = AmainFrame;
    }

    public void calibrate() {
        try {
            jTextArea1.setCaretPosition(jTextArea1.getLineStartOffset(jTextArea1.getLineCount() - 1));
        } catch (Exception e) {
        }
    }

    boolean modified_check(String remoteFile, String localFile) {
        boolean recopy = false;
        long milli;
        try {
            File check_localFile = new File(localFile);
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            Date remote = sdf.parse(mainFrame.bucket.getObjectInfo(remoteFile, mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.bucket_item[mainFrame.active_bucket].getText(), mainFrame.cred.getEndpoint(), "objectdate"));
            milli = check_localFile.lastModified();
            Date local = new Date(milli);

            if (local.after(remote)) {
                recopy = true;
            }
        } catch (Exception modifiedChecker) {
        }
        return recopy;
    }

    public void run() {
        String[] extensions = new String[]{" "};
        List<File> files = (List<File>) FileUtils.listFiles(location, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        for (File file_found : files) {
            int found = 0;
            String object = makeDirectory(file_found.getAbsolutePath().toString());
            String[] cut = object.split(file_found.getName());
            String[] cut2 = null;
            object = object.replace(cut[0], "");
            if (cut[0].contains(win)) {
                cut2 = cut[0].split(win);
                object = cut2[cut2.length - 1] + win + object;
            } else {
                cut2 = cut[0].split(lin);
                object = cut2[cut2.length - 1] + lin + object;
            }

            for (int y = 1; y != objectarray.length; y++) {
                if (objectarray[y].contains(object)) {
                    if (!modified_check(objectarray[y], file_found.getAbsolutePath())) {
                        calibrate();
                        found++;
                    }
                }
            }

            if (found == 0) {

                if (SyncToS3.running) {
                    int index = mainFrame.jList3.getSelectedIndex(); //get selected index

                    try {
                        if (index != -1) {
                            object = mainFrame.jList3.getSelectedValue().toString() + object;
                        }
                    } catch (Exception indaex) {

                    }
                    put = new Put(file_found.getAbsolutePath().toString(), access_key, secret_key, bucket, endpoint, object, rrs, encrypt);
                    put.run();
                }
                found = 0;
            }
        }
        mainFrame.reloadBuckets();
        mainFrame.jTextArea1.append("\nSync operation finished running. Please observe this window for any transfers that may still be running.");
        calibrate();
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

        File dir = new File(what.substring(0, another_counter));
        dir.mkdirs();
        return what;
    }

    void startc(NewJFrame AmainFrame, File location, String Aaccess_key, String Asecret_key, String Abucket, String Aendpoint, String[] Aobjectarray, Boolean Arrs, Boolean Aencrypt
    ) {
        if (SyncToS3.running) {
            syncToS3 = new Thread(new SyncToS3(AmainFrame, location, Aaccess_key, Asecret_key, Abucket, Aendpoint, Aobjectarray, Arrs, Aencrypt));
            syncToS3.start();

        }
    }

    void stop() {
        SyncToS3.running = false;
        syncToS3.stop();
        syncToS3.isInterrupted();
        mainFrame.jTextArea1.setText("\nUpload complete or aborted.\n");
    }

}
