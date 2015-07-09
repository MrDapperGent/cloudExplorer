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
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;
import org.apache.commons.codec.digest.DigestUtils;

public class SyncFromS3 implements Runnable {

    NewJFrame mainFrame;
    String[] objectarray;
    String[] ObjectsConverted;
    String access_key = null;
    String bucket = null;
    String endpoint = null;
    String secret_key = null;
    String destination = null;
    public static Boolean running = false;
    Get get;
    Thread syncFromS3;
    String win = "\\";
    String lin = "/";

    SyncFromS3(NewJFrame AmainFrame, String[] Aobjectarray, String[] AObjectsConverted, String Aaccess_key, String Asecret_key, String Abucket, String Aendpoint, String Adestination) {
        objectarray = Aobjectarray;
        ObjectsConverted = AObjectsConverted;
        access_key = Aaccess_key;
        secret_key = Asecret_key;
        bucket = Abucket;
        endpoint = Aendpoint;
        destination = Adestination;
        mainFrame = AmainFrame;
    }

    public void calibrate() {
        try {
            jTextArea1.setCaretPosition(jTextArea1.getLineStartOffset(jTextArea1.getLineCount() - 1));
        } catch (Exception e) {
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

    boolean modified_check(String remoteFile, String localFile) {
        boolean recopy = false;
        long milli;
        String local_md5String = null;
        String remote_md5String = null;
        FileInputStream fis = null;

        try {
            File check_localFile = new File(localFile);
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            fis = new FileInputStream(localFile);
            local_md5String = DigestUtils.md5Hex(fis);
            remote_md5String = mainFrame.bucket.getObjectInfo(remoteFile, mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.bucket_item[mainFrame.active_bucket].getText(), mainFrame.cred.getEndpoint(), "checkmd5");
            Date remote = sdf.parse(mainFrame.bucket.getObjectInfo(remoteFile, mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.bucket_item[mainFrame.active_bucket].getText(), mainFrame.cred.getEndpoint(), "objectdate"));
            milli = check_localFile.lastModified();
            Date local = new Date(milli);

            if (local_md5String.contains(remote_md5String)) {
            } else {
                if (remote.after(local)) {
                    recopy = true;
                }
            }
        } catch (Exception modifiedChecker) {
        }
        return recopy;
    }

    public void run() {
        try {
            File[] foo = new File[objectarray.length];
            int index = mainFrame.jList3.getSelectedIndex(); //get selected index
            for (int i = 1; i != objectarray.length; i++) {

                if (objectarray[i] != null) {
                    int found = 0;
                    foo[i] = new File(destination + File.separator + objectarray[i]);

                    if (foo[i].exists()) {
                        if (!modified_check(objectarray[i], foo[i].getAbsolutePath())) {
                            calibrate();
                            found++;
                        }
                        calibrate();
                    }
                    if (found == 0) {

                        if (index > -1) {
                            if (objectarray[i].contains(mainFrame.jList3.getSelectedValue().toString())) {
                                makeDirectory(destination + File.separator + objectarray[i]);
                                String object = makeDirectory(objectarray[i]);
                            }
                        } else {
                            makeDirectory(destination + File.separator + objectarray[i]);
                            String object = makeDirectory(objectarray[i]);
                        }

                        if (SyncFromS3.running) {
                            try {
                                String transcoded_object = null;
                                if (index == -1) {
                                    String[] cutit = null;
                                    if (objectarray[i].contains(win) || (objectarray[i].contains(lin))) {
                                        if (objectarray[i].contains(win)) {
                                            cutit = objectarray[i].split(Pattern.quote(win));
                                            transcoded_object = cutit[1];
                                        } else {
                                            cutit = objectarray[i].split(Pattern.quote(lin));
                                            transcoded_object = cutit[1];
                                        }
                                        String object = makeDirectory(destination + File.separator + cutit[0]);
                                        get = new Get(objectarray[i], access_key, secret_key, bucket, endpoint, destination + File.separator + cutit[0] + File.separator + transcoded_object, null);
                                        get.run();
                                    } else {
                                        get = new Get(objectarray[i], access_key, secret_key, bucket, endpoint, destination + File.separator + objectarray[i], null);
                                        get.run();
                                    }
                                } else {
                                    if (objectarray[i].contains(mainFrame.jList3.getSelectedValue().toString())) {
                                        String[] cutit = null;
                                        if (objectarray[i].contains(win)) {
                                            cutit = objectarray[i].split(Pattern.quote(win));
                                            transcoded_object = cutit[1];
                                        } else {
                                            cutit = objectarray[i].split(lin);
                                            transcoded_object = cutit[1];
                                        }
                                        String object = makeDirectory(destination + File.separator + cutit[0]);
                                        get = new Get(objectarray[i], access_key, secret_key, bucket, endpoint, destination + File.separator + cutit[0] + File.separator + transcoded_object, null);
                                        get.run();
                                    }
                                }
                                found = 0;
                            } catch (Exception fo) {

                            }
                        }
                    }
                }
            }

        } catch (Exception SyncLocal) {
            mainFrame.jTextArea1.append("\n" + SyncLocal.getMessage());
        }

        mainFrame.drawBuckets();

        mainFrame.jTextArea1.append(
                "\nSync operation finished running. Please observe this window for any transfers that may still be running.");
        calibrate();
    }

    void startc(NewJFrame AmainFrame, String[] Aobjectarray, String[] AObjectsConverted, String Aaccess_key, String Asecret_key, String Abucket, String Aendpoint, String Adestination) {

        if (SyncFromS3.running) {
            syncFromS3 = new Thread(new SyncFromS3(AmainFrame, Aobjectarray, AObjectsConverted, Aaccess_key, Asecret_key, Abucket, Aendpoint, Adestination));
            syncFromS3.start();
        }
    }

    void stop() {
        SyncFromS3.running = false;
        syncFromS3.stop();
        syncFromS3.isInterrupted();
        mainFrame.jTextArea1.setText("\nAborted Download\n");
    }
}
