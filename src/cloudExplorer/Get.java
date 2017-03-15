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

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import static cloudExplorer.NewJFrame.jTextArea1;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.s3.S3ClientOptions;

public class Get implements Runnable {

    String Home = System.getProperty("user.home");
    NewJFrame mainFrame;
    String what = null;
    String access_key = null;
    String bucket = null;
    String endpoint = null;
    String secret_key = null;
    String destination = null;
    String version = null;
    Thread get;
    public static Boolean debug = false;
    public static Boolean terminal = false;

    public void calibrate() {
        try {
            jTextArea1.setCaretPosition(jTextArea1.getLineStartOffset(jTextArea1.getLineCount() - 1));
        } catch (Exception e) {
        }
    }

    Get(String Awhat, String Aaccess_key, String Asecret_key, String Abucket, String Aendpoint, String Adestination, String Aversion) {
        what = Awhat;
        access_key = Aaccess_key;
        secret_key = Asecret_key;
        bucket = Abucket;
        endpoint = Aendpoint;
        destination = Adestination;
        version = Aversion;
    }

    void writeFile(InputStream is, String destination) {
        try {
            FileOutputStream fo = new FileOutputStream(destination);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = is.read(bytes)) != -1) {
                fo.write(bytes, 0, read);
            }
            fo.close();
        } catch (Exception add) {
            if (debug) {
                System.out.print("\n\n\n" + add.getMessage() + "\n\n\n");
            }
        }

    }

    public void run() {
        String message = null;
        AWSCredentials credentials = new BasicAWSCredentials(access_key, secret_key);
        File file = new File(what);
        AmazonS3 s3Client = new AmazonS3Client(credentials,
                new ClientConfiguration());
        s3Client.setS3ClientOptions(S3ClientOptions.builder().setPathStyleAccess(false).build());
        s3Client.setEndpoint(endpoint);

        try {
            long t1 = System.currentTimeMillis();
            S3Object s3object = s3Client.getObject(new GetObjectRequest(bucket, what, version));
            InputStream objectData = s3object.getObjectContent();
            this.writeFile(objectData, destination);
            long t2 = System.currentTimeMillis();
            long diff = t2 - t1;

            if (!mainFrame.perf) {
                if (terminal) {
                    System.out.print("\nDownloaded: " + what + " in " + diff / 1000 + " second(s).\n");
                } else {
                    mainFrame.jTextArea1.append("\nDownloaded: " + what + " in " + diff / 1000 + " second(s).");
                    mainFrame.calibrateTextArea();
                }
            }

        } catch (AmazonServiceException ase) {
            if (NewJFrame.gui) {
                mainFrame.jTextArea1.append("\n\nError Message:    " + ase.getMessage());
                mainFrame.jTextArea1.append("\nHTTP Status Code: " + ase.getStatusCode());
                mainFrame.jTextArea1.append("\nAWS Error Code:   " + ase.getErrorCode());
                mainFrame.jTextArea1.append("\nError Type:       " + ase.getErrorType());
                mainFrame.jTextArea1.append("\nRequest ID:       " + ase.getRequestId());
                calibrate();
            } else {
                System.out.print("\n\nError Message:    " + ase.getMessage());
                System.out.print("\nHTTP Status Code: " + ase.getStatusCode());
                System.out.print("\nAWS Error Code:   " + ase.getErrorCode());
                System.out.print("\nError Type:       " + ase.getErrorType());
                System.out.print("\nRequest ID:       " + ase.getRequestId());
            }
        } catch (Exception get) {
        }
        calibrate();
    }

    void startc(String Awhat, String Aaccess_key, String Asecret_key, String Abucket, String Aendpoint, String Adestination, String Aversion) {
        get = new Thread(new Get(Awhat, Aaccess_key, Asecret_key, Abucket, Aendpoint, Adestination, Aversion));
        get.start();
        if (NewJFrame.perf) {
            try {
                get.join();
                File delete_temp = new File(destination);
                if (delete_temp.exists()) {
                    delete_temp.delete();
                }
            } catch (Exception get) {
            }
        }

    }

    void stop() {
        get.stop();
        mainFrame.jTextArea1.setText("\nDownload completed or aborted.\n");
    }

}
