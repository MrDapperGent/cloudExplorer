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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    Runnable syncengine;
    ExecutorService executor = Executors.newFixedThreadPool((int) 5);

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

    public void run() {
        try {
            File[] foo = new File[objectarray.length];
            int index = mainFrame.jList3.getSelectedIndex(); //get selected index
            for (int i = 1; i != objectarray.length; i++) {
                if (SyncFromS3.running) {
                    if (objectarray[i] != null) {
                        int found = 0;
                        foo[i] = new File(destination + File.separator + objectarray[i]);
                        if (index > -1) {
                            if (objectarray[i].contains(mainFrame.jList3.getSelectedValue().toString())) {
                                syncengine = new SyncEngine(objectarray[i], null, null, objectarray[i], bucket, access_key, secret_key, endpoint, null, null, null, false, destination);
                            }
                        } else {
                            syncengine = new SyncEngine(objectarray[i], null, null, objectarray[i], bucket, access_key, secret_key, endpoint, null, null, null, false, destination);

                        }
                        executor.execute(syncengine);
                        System.gc();
                    }
                }
            }
        } catch (Exception SyncLocal) {
            mainFrame.jTextArea1.append("\n" + SyncLocal.getMessage());
        }

        executor.shutdown();

        while (!executor.isTerminated()) {
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
