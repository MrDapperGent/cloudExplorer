package cloudExplorer;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3VersionSummary;
import com.amazonaws.services.s3.model.VersionListing;
import java.util.List;

public class Versioning {

    NewJFrame mainFrame;
    Delete del;
    public static Boolean delete = false;

    public Versioning(NewJFrame Frame) {
        mainFrame = Frame;
    }

    void getVersions(String key, String access_key, String secret_key, String bucket, String endpoint) {
        String results = null;
        try {
            mainFrame.jTextArea1.append("\nPlease wait, loading versions.");
            mainFrame.calibrateTextArea();
            AWSCredentials credentials = new BasicAWSCredentials(access_key, secret_key);
            AmazonS3 s3Client = new AmazonS3Client(credentials);
            s3Client.setEndpoint(endpoint);

            VersionListing vListing;
            if (key == null) {
                vListing = s3Client.listVersions(bucket, null);
            } else {
                vListing = s3Client.listVersions(bucket, key);
            }

            List<S3VersionSummary> summary;

            if (Versioning.delete) {
                do {
                    summary = vListing.getVersionSummaries();
                    for (S3VersionSummary object : summary) {
                        del = new Delete(object.getKey(), mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), object.getVersionId());
                        del.startc(object.getKey(), mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), object.getVersionId());
                        System.gc();
                        long t1 = System.currentTimeMillis();
                        long diff = 0;
                        while (diff < 500) {
                            long t2 = System.currentTimeMillis();
                            diff = t2 - t1;
                        }
                    }
                } while (vListing.isTruncated());
            } else {

                summary = vListing.getVersionSummaries();
                for (S3VersionSummary object : summary) {
                    mainFrame.versioning_date.add(object.getLastModified().toString());
                    mainFrame.versioning_id.add(object.getVersionId());
                    mainFrame.versioning_name.add(object.getKey());
                    System.gc();
                }

            }
            mainFrame.jTextArea1.append("\n\nDone gathering Versions.");
            mainFrame.calibrateTextArea();
        } catch (Exception getVersions) {

        }
        if (Versioning.delete) {
            Versioning.delete = false;
        }

    }

}
