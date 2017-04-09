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
import java.io.File;
import static cloudExplorer.NewJFrame.jTextArea1;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetBucketLocationRequest;
import com.amazonaws.services.s3.model.GetObjectMetadataRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.RestoreObjectRequest;

public class RestoreObject implements Runnable {

    NewJFrame mainFrame;
    String what = null;
    String access_key = null;
    String bucket = null;
    String endpoint = null;
    String secret_key = null;
    Thread restoreobject;

    public void calibrate() {
        try {
            jTextArea1.setCaretPosition(jTextArea1.getLineStartOffset(jTextArea1.getLineCount() - 1));
        } catch (Exception e) {
        }
    }

    RestoreObject(String Awhat, String Aaccess_key, String Asecret_key, String Abucket, String Aendpoint) {
        what = Awhat;
        access_key = Aaccess_key;
        secret_key = Asecret_key;
        bucket = Abucket;
        endpoint = Aendpoint;

    }

    public void run() {
        String message = null;
        AWSCredentials credentials = new BasicAWSCredentials(access_key, secret_key);
        File file = new File(what);
        AmazonS3 s3Client = new AmazonS3Client(credentials,
                new ClientConfiguration());
        if (endpoint.contains("amazonaws.com")) {
            String aws_endpoint = s3Client.getBucketLocation(new GetBucketLocationRequest(bucket));
            if (aws_endpoint.contains("US")) {
                s3Client.setEndpoint("https://s3.amazonaws.com");
            } else if (aws_endpoint.contains("us-west")) {
                s3Client.setEndpoint("https://s3-" + aws_endpoint + ".amazonaws.com");
            } else if (aws_endpoint.contains("eu-west")) {
                s3Client.setEndpoint("https://s3-" + aws_endpoint + ".amazonaws.com");
            } else if (aws_endpoint.contains("ap-")) {
                s3Client.setEndpoint("https://s3-" + aws_endpoint + ".amazonaws.com");
            } else if (aws_endpoint.contains("sa-east-1")) {
                s3Client.setEndpoint("https://s3-" + aws_endpoint + ".amazonaws.com");
            } else {
                s3Client.setEndpoint("https://s3." + aws_endpoint + ".amazonaws.com");
            }
        } else {
            s3Client.setS3ClientOptions(S3ClientOptions.builder().setPathStyleAccess(true).build());
            s3Client.setEndpoint(endpoint);
        }

        try {
            RestoreObjectRequest requestRestore = new RestoreObjectRequest(bucket, what, 2);
            s3Client.restoreObject(requestRestore);

            GetObjectMetadataRequest requestCheck = new GetObjectMetadataRequest(bucket, what);
            ObjectMetadata response = s3Client.getObjectMetadata(requestCheck);

            Boolean restoreFlag = response.getOngoingRestore();
            mainFrame.jTextArea1.append("\nRestoration in progress. Please try to access the file again in a few hours.");
            calibrate();
        } catch (AmazonS3Exception amazonS3Exception) {
            mainFrame.jTextArea1.append("\nAn Amazon S3 error occurred. Exception: %s" + amazonS3Exception.toString());
            calibrate();
        } catch (Exception ex) {
            mainFrame.jTextArea1.append("\nException: %s" + ex.toString());
            calibrate();
        }

        calibrate();
    }

    void startc(String Awhat, String Aaccess_key, String Asecret_key, String Abucket, String Aendpoint) {
        restoreobject = new Thread(new RestoreObject(Awhat, Aaccess_key, Asecret_key, Abucket, Aendpoint));
        restoreobject.start();
    }

}
