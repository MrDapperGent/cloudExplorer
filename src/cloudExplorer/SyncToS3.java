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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class SyncToS3 implements Runnable {

    NewJFrame mainFrame;
    Runnable checkmetadata;
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
    Boolean infreq = false;
    Boolean encrypt = false;
    String Home = System.getProperty("user.home");
    String win = "\\";
    String lin = "/";
    ExecutorService executor = Executors.newFixedThreadPool((int) 5);

    SyncToS3(NewJFrame AmainFrame, File Alocation, String Aaccess_key, String Asecret_key, String Abucket, String Aendpoint, String[] Aobjectarray, Boolean Arrs, Boolean Aencrypt, Boolean Ainfreq) {
        objectarray = Aobjectarray;
        location = Alocation;
        access_key = Aaccess_key;
        secret_key = Asecret_key;
        bucket = Abucket;
        endpoint = Aendpoint;
        rrs = Arrs;
        infreq = Ainfreq;
        encrypt = Aencrypt;
        mainFrame = AmainFrame;
    }

    public void calibrate() {
        try {
            mainFrame.jTextArea1.setCaretPosition(mainFrame.jTextArea1.getLineStartOffset(mainFrame.jTextArea1.getLineCount() - 1));
        } catch (Exception e) {
        }
    }

    public void run() {
        int index = mainFrame.jList3.getSelectedIndex(); //get selected index

        List<File> files = (List<File>) FileUtils.listFiles(location, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        for (File file_found : files) {
            String clean_object_name[] = location.toString().split(Pattern.quote(File.separator));
            String object = file_found.getAbsolutePath().toString();

            object = object.replace(location.toString(), "");
            object = clean_object_name[clean_object_name.length - 1] + object;
            try {
                if (index != -1) {
                    object = mainFrame.jList3.getSelectedValue().toString() + object;
                }
            } catch (Exception indaex) {
            }

            checkmetadata = new MetaDataCheck(object, file_found.getAbsolutePath(), file_found, object, bucket, access_key, secret_key, endpoint, rrs, encrypt, infreq);
            executor.execute(checkmetadata);
        }
        executor.shutdown();

        while (!executor.isTerminated()) {
        }

        mainFrame.drawBuckets();
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

        if (what.contains(win)) {
            what = what.replace(win, File.separator);
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

    void startc(NewJFrame AmainFrame, File location, String Aaccess_key, String Asecret_key, String Abucket, String Aendpoint, String[] Aobjectarray, Boolean Arrs, Boolean Aencrypt, Boolean Ainfreq
    ) {
        if (SyncToS3.running) {
            syncToS3 = new Thread(new SyncToS3(AmainFrame, location, Aaccess_key, Asecret_key, Abucket, Aendpoint, Aobjectarray, Arrs, Aencrypt, Ainfreq));
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
