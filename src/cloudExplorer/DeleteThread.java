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

public class DeleteThread implements Runnable {

    String message = null;
    NewJFrame mainFrame;
    String[] what = null;
    String[] verWhat = null;
    String access_key = null;
    String bucket = null;
    Delete delete;
    String endpoint = null;
    String secret_key = null;
    String destination = null;
    Thread del;
    String version = null;

    public void calibrate() {
        try {
            jTextArea1.setCaretPosition(jTextArea1.getLineStartOffset(jTextArea1.getLineCount() - 1));
        } catch (Exception e) {
        }
    }

    DeleteThread(NewJFrame AmainFrame, String[] Awhat, String[] AverWhat, String Aaccess_key, String Asecret_key, String Abucket, String Aendpoint, String Aversion) {
        what = Awhat;
        verWhat = AverWhat;
        access_key = Aaccess_key;
        secret_key = Asecret_key;
        bucket = Abucket;
        endpoint = Aendpoint;
        version = Aversion;
        mainFrame = AmainFrame;
    }

    public void run() {

        for (int i = 0; i != what.length; i++) {
            if (what[i] != null) {
                if (verWhat == null) {
                    delete = new Delete(what[i], access_key, secret_key, bucket, endpoint, null);
                    delete.startc(what[i], access_key, secret_key, bucket, endpoint, null);
                } else {
                    delete = new Delete(what[i], access_key, secret_key, bucket, endpoint, verWhat[i]);
                    delete.startc(what[i], access_key, secret_key, bucket, endpoint, verWhat[i]);
                }

            }
        }
        NewJFrame.deleting.setSelected(true);
    }

    void startc(NewJFrame AmainFrame, String[] Awhat, String[] AverWhat, String Aaccess_key, String Asecret_key, String Abucket, String Aendpoint, String Aversion) {
        del = new Thread(new DeleteThread(AmainFrame, Awhat, AverWhat, Aaccess_key, Asecret_key, Abucket, Aendpoint, Aversion));
        del.start();
        try {
            del.join();

        } catch (Exception Delete) {
        }
    }

    void stop() {
        del.stop();
        mainFrame.jTextArea1.setText("\nDownload compelted or aborted. Please click search to refresh the Object Explorer if needed.\n");
    }

}
