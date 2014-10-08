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

import static cloudExplorer.NewJFrame.jTextArea1;
import java.io.File;

public class GetThread implements Runnable {

    String message = null;
    String[] what = null;
    String access_key = null;
    String bucket = null;
    Get get;
    String endpoint = null;
    String secret_key = null;
    String destination = null;
    Thread getthread;
    String version = null;
    File File_Destination;

    public void calibrate() {
        try {
            jTextArea1.setCaretPosition(jTextArea1.getLineStartOffset(jTextArea1.getLineCount() - 1));
        } catch (Exception e) {
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

    GetThread(String[] Awhat, String Aaccess_key, String Asecret_key, String Abucket, String Aendpoint, String Aversion, File AFile_Destination) {
        what = Awhat;
        access_key = Aaccess_key;
        secret_key = Asecret_key;
        bucket = Abucket;
        endpoint = Aendpoint;
        version = Aversion;
        File_Destination = AFile_Destination;
    }

    public void run() {

        for (int i = 0; i != what.length; i++) {
            if (what[i] != null) {
                String new_object_name = convertObject(what[i], "download");
                get = new Get(what[i], access_key, secret_key, bucket, endpoint, File_Destination.toString() + File.separator + new_object_name, version);
                get.run();
            }
        }

    }

    void startc(String[] Awhat, String Aaccess_key, String Asecret_key, String Abucket, String Aendpoint, String Aversion, File AFile_Destination) {
        getthread = new Thread(new GetThread(Awhat, Aaccess_key, Asecret_key, Abucket, Aendpoint, Aversion, AFile_Destination));
        getthread.start();
        try {
            getthread.join();
        } catch (Exception getThread) {
        }
    }

    void stop() {
        getthread.stop();
        NewJFrame.jTextArea1.setText("\nDownload compelted or aborted. Please click search to refresh the Object Explorer if needed.\n");
    }

}
