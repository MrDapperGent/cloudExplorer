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

public class Abort implements Runnable {

    Thread abort;
    NewJFrame mainFrame;
    BucketClass bucketObject = new BucketClass();
    String what = null;
    String access_key = null;
    String bucket = null;
    String endpoint = null;
    String ObjectKey = null;
    String secret_key = null;
    String objectlist = null;
    SyncToS3 SyncToS3;
    SyncFromS3 SyncFromS3;
    String operation = null;

    public String objectlist() {
        return objectlist;
    }

    public Abort(String what) {
        operation = what;
    }

    public void run() {

        if (operation.contains("To")) {
            SyncToS3.running = false;
            SyncToS3.stop();
        }
        if (operation.contains("From")) {
            SyncFromS3.running = false;
            SyncFromS3.stop();
        }
    }

    public void startc(String operation) {
        abort = new Thread(new Abort(operation));
        abort.start();
    }

    public void stop() {
        abort.stop();
    }
}
