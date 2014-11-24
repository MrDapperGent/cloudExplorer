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

public class DeleteBucketThread implements Runnable {

    NewJFrame mainFrame;
    public static String response = null;
    public static String region = null;
    public static String access_key = null;
    public static String secret_key = null;
    public static String endpoint = null;

    public String bucket = null;

    public DeleteBucketThread(String Aaccess_key, String Asecret_key, String Abucket, String Aendpoint, String Aregion, NewJFrame AmainFrame) {
        access_key = Aaccess_key;
        secret_key = Asecret_key;
        bucket = Abucket;
        region = Aregion;
        endpoint = Aendpoint;
        mainFrame = AmainFrame;
    }

    public void run() {
        try {
            BucketClass deletebucket = new BucketClass();
            mainFrame.jTextArea1.append("\n" + deletebucket.deleteBucket(access_key, secret_key, bucket, endpoint, region));
            mainFrame.active_bucket = 0;
            mainFrame.reloadBuckets();
        } catch (Exception makebucket) {
            jTextArea1.append("\n" + makebucket.getMessage());
        }
    }

    void startc(String Aaccess_key, String Asecret_key, String Abucket, String Aendpoint, String Aregion, NewJFrame AmainFrame) {
        (new Thread(new DeleteBucketThread(Aaccess_key, Asecret_key, Abucket, Aendpoint, Aregion, AmainFrame))).start();
    }
}
