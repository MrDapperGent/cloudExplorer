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

public class ReloadBuckets implements Runnable {

    Thread reloadBuckets;
    NewJFrame mainFrame;
    BucketClass bucketObject = new BucketClass();
    String what = null;
    String access_key = null;
    String endpoint = null;
    String secret_key = null;
    String bucketlist = null;
    public String bucketlist() {
        return bucketlist;
    }

    public ReloadBuckets(String Aaccess_key, String Asecret_key, String Aendpoint, NewJFrame Frame) {
        access_key = Aaccess_key;
        secret_key = Asecret_key;
        endpoint = Aendpoint;
        mainFrame = Frame;
    }

    public void run() {
        bucketlist = bucketObject.listBuckets(access_key, secret_key, endpoint);
        mainFrame.bucketarray = bucketlist.split(" ");
        mainFrame.drawBuckets();
        mainFrame.jTabbedPane1.setSelectedIndex(1);
    }

    public void startc(String Aaccess_key, String Asecret_key, String Aendpoint, NewJFrame mainFrame) {
        reloadBuckets = new Thread(new ReloadBuckets(Aaccess_key, Asecret_key, Aendpoint, mainFrame));
        reloadBuckets.start();
    }

    public void stop() {
        reloadBuckets.stop();
    }
}
