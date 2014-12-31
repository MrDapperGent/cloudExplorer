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
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.BucketVersioningConfiguration;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.DeleteBucketRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.SetBucketVersioningConfigurationRequest;

public class BucketClass {

    String objectlist = null;
    public static Boolean terminal = false;
    NewJFrame mainFrame;

    Boolean VersioningStatus(String access_key, String secret_key, String bucket, String endpoint, String region, Boolean enable) {
        String message = null;
        boolean result = false;
        AWSCredentials credentials = new BasicAWSCredentials(access_key, secret_key);
        AmazonS3 s3Client = new AmazonS3Client(credentials,
                new ClientConfiguration().withSignerOverride("S3SignerType"));
        s3Client.setEndpoint(endpoint);
        try {
            message = s3Client.getBucketVersioningConfiguration(bucket).getStatus().toString();
            if (message.contains("Enabled") || message.contains("Suspended")) {
                result = true;
            } else {
                result = false;
            }
        } catch (Exception versioning) {
        }

        return result;
    }

    Boolean LifeCycleStatus(String access_key, String secret_key, String bucket, String endpoint, String region, Boolean enable) {
        String message = null;
        boolean result = false;
        AWSCredentials credentials = new BasicAWSCredentials(access_key, secret_key);
        AmazonS3 s3Client = new AmazonS3Client(credentials,
                new ClientConfiguration().withSignerOverride("S3SignerType"));
        s3Client.setEndpoint(endpoint);
        try {
            message = s3Client.getBucketLifecycleConfiguration(bucket).getRules().toString();
            if (message == null) {
                result = false;
            } else {
                result = true;
            }
        } catch (Exception lifecyclestatus) {
        }

        return result;
    }

    String controlVersioning(String access_key, String secret_key, String bucket, String endpoint, String region, Boolean enable) {

        String message = null;
        AWSCredentials credentials = new BasicAWSCredentials(access_key, secret_key);
        AmazonS3 s3Client = new AmazonS3Client(credentials,
                new ClientConfiguration().withSignerOverride("S3SignerType"));
        s3Client.setEndpoint(endpoint);
        try {
            SetBucketVersioningConfigurationRequest request;
            if (enable) {
                request = new SetBucketVersioningConfigurationRequest(bucket, new BucketVersioningConfiguration(BucketVersioningConfiguration.ENABLED));
            } else {
                request = new SetBucketVersioningConfigurationRequest(bucket, new BucketVersioningConfiguration(BucketVersioningConfiguration.SUSPENDED));
            }
            s3Client.setBucketVersioningConfiguration(request);
            message = "\nBucket Versioning is:" + request.getVersioningConfiguration().getStatus();
        } catch (Exception versioning) {
            message = "\n" + versioning.getMessage();
        }
        if (message == null) {
            message = "\nVersioning failed.";
        }
        return message;

    }

    String makeBucket(String access_key, String secret_key, String bucket, String endpoint, String region) {
        String message = null;
        AWSCredentials credentials = new BasicAWSCredentials(access_key, secret_key);
        AmazonS3 s3Client = new AmazonS3Client(credentials,
                new ClientConfiguration().withSignerOverride("S3SignerType"));
        s3Client.setEndpoint(endpoint);
        try {
            if (endpoint.contains("amazon")) {
                s3Client.createBucket(new CreateBucketRequest(bucket));

            } else {
                s3Client.createBucket(new CreateBucketRequest(bucket, region));
            }

            message = ("\nAttempting to create the bucket. Please view the Bucket list window for an update.");
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
        }
        if (message == null) {
            message = "Failed to create bucket.";
        }
        return message;

    }

    public void calibrate() {
        try {
            jTextArea1.setCaretPosition(jTextArea1.getLineStartOffset(jTextArea1.getLineCount() - 1));
        } catch (Exception e) {
        }
    }

    String listBuckets(String access_key, String secret_key, String endpoint) {

        AWSCredentials credentials = new BasicAWSCredentials(access_key, secret_key);
        AmazonS3 s3Client = new AmazonS3Client(credentials,
                new ClientConfiguration().withSignerOverride("S3SignerType"));
        s3Client.setEndpoint(endpoint);
        String[] array = new String[10];

        String bucketlist = null;

        int i = 0;
        try {

            for (Bucket bucket : s3Client.listBuckets()) {
                bucketlist = bucketlist + " " + bucket.getName();
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

        } catch (Exception lsbuckets) {

            if (lsbuckets.getMessage().contains("peer not authenticated") || lsbuckets.getMessage().contains("hostname in certificate didn't match")) {
                if (NewJFrame.gui) {
                    mainFrame.jTextArea1.append("\nError: This program does not support non-trusted SSL certificates\n\nor your SSL certificates are incorrect.");
                } else {
                    System.out.print("\n\nError: This program does not support non-trusted SSL certificates\n\nor your SSL certificates are not correctly installed.");
                }
            }
        }
        String parse = null;

        if (bucketlist != null) {
            parse = bucketlist.replace("null", "");

        } else {
            parse = "no_bucket_found";
        }

        return parse;
    }

    String listBucketContents(String access_key, String secret_key, String bucket, String endpoint
    ) {
        objectlist = null;

        AWSCredentials credentials = new BasicAWSCredentials(access_key, secret_key);
        AmazonS3 s3Client = new AmazonS3Client(credentials,
                new ClientConfiguration().withSignerOverride("S3SignerType"));
        s3Client.setEndpoint(endpoint);

        try {
            ObjectListing current = s3Client.listObjects((bucket));

            ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucket);
            ObjectListing objectListing;
            do {
                objectListing = s3Client.listObjects(listObjectsRequest);
                for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                    objectlist = objectlist + "@@" + objectSummary.getKey();
                }
                listObjectsRequest.setMarker(objectListing.getNextMarker());
            } while (objectListing.isTruncated());

        } catch (Exception listBucket) {
            mainFrame.jTextArea1.append("\n" + listBucket.getMessage());
        }

        String parse = null;
        if (objectlist != null) {
            parse = objectlist;
        } else {
            parse = "No objects_found.";
        }
        return parse;
    }

    String getObjectInfo(String key, String access_key, String secret_key, String bucket, String endpoint, String process
    ) {
        AWSCredentials credentials = new BasicAWSCredentials(access_key, secret_key);
        AmazonS3 s3Client = new AmazonS3Client(credentials,
                new ClientConfiguration().withSignerOverride("S3SignerType"));
        s3Client.setEndpoint(endpoint);
        objectlist = null;

        try {
            ObjectListing current = s3Client.listObjects((bucket));

            ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucket);
            ObjectListing objectListing;
            do {
                objectListing = s3Client.listObjects(listObjectsRequest);

                for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {

                    if (process.contains("objectsize")) {
                        if (objectSummary.getKey().contains(key)) {
                            objectlist = String.valueOf(objectSummary.getSize());
                            break;
                        }
                    }

                    if (process.contains("objectdate")) {
                        if (objectSummary.getKey().contains(key)) {
                            objectlist = String.valueOf(objectSummary.getLastModified());
                            break;
                        }

                    }
                }
                listObjectsRequest.setMarker(objectListing.getNextMarker());
            } while (objectListing.isTruncated());

        } catch (Exception listBucket) {
            mainFrame.jTextArea1.append("\n" + listBucket.getMessage());
        }

        return objectlist;
    }

    String deleteBucket(String access_key, String secret_key, String bucket, String endpoint, String region
    ) {
        String message = null;
        AWSCredentials credentials = new BasicAWSCredentials(access_key, secret_key);
        AmazonS3 s3Client = new AmazonS3Client(credentials,
                new ClientConfiguration().withSignerOverride("S3SignerType"));
        s3Client.setEndpoint(endpoint);
        message = ("\nDeleting bucket: " + bucket);
        try {
            s3Client.deleteBucket(new DeleteBucketRequest(bucket));
        } catch (Exception Delete) {
            if (terminal) {
                System.out.print("\n\n\n" + Delete.getMessage() + "\n\n\n");
            } else {
                message = message + "\n" + Delete.getMessage();
            }
        }

        if (message == null) {
            message = "\nDelete operation failed.";
        }

        return message;
    }
}
