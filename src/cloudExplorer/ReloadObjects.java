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

public class ReloadObjects implements Runnable {

    Thread reloadObjects;
    NewJFrame mainFrame;
    BucketClass bucketObject = new BucketClass();
    String what = null;
    String access_key = null;
    String bucket = null;
    String endpoint = null;
    String ObjectKey = null;
    String secret_key = null;
    String objectlist = null;

    public ReloadObjects(String Aaccess_key, String Asecret_key, String Abucket, String Aendpoint, NewJFrame Frame) {
        access_key = Aaccess_key;
        secret_key = Asecret_key;
        bucket = Abucket;
        endpoint = Aendpoint;
        mainFrame = Frame;
    }

    public void run() {

        objectlist = bucketObject.listBucketContents(access_key, secret_key, bucket, endpoint);
        mainFrame.objectarray = objectlist.split("@@");
        mainFrame.previous_objectarray_length = mainFrame.objectarray.length;
        System.gc();
    }

    public void startc(String Aaccess_key, String Asecret_key, String Abucket, String Aendpoint) {
        reloadObjects = new Thread(new ReloadObjects(Aaccess_key, Asecret_key, Abucket, Aendpoint, mainFrame));
        reloadObjects.start();
    }

    public void stop() {
        reloadObjects.stop();
    }
}
