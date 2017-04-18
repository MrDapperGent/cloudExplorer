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

import static cloudExplorer.NewJFrame.jTextArea1;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.BucketVersioningConfiguration;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.DeleteBucketRequest;
import com.amazonaws.services.s3.model.GetBucketLocationRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.SetBucketVersioningConfigurationRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import java.util.Date;

public class BucketClass {

    String objectlist = null;
    public static Boolean terminal = false;
    NewJFrame mainFrame;

    Boolean VersioningStatus(String access_key, String secret_key, String bucket, String endpoint, Boolean enable) {
        String message = null;
        boolean result = false;
        AWSCredentials credentials = new BasicAWSCredentials(access_key, secret_key);
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

    Boolean LifeCycleStatus(String access_key, String secret_key, String bucket, String endpoint, Boolean enable) {
        String message = null;
        boolean result = false;
        AWSCredentials credentials = new BasicAWSCredentials(access_key, secret_key);
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

    String controlVersioning(String access_key, String secret_key, String bucket, String endpoint, Boolean enable) {

        String message = null;
        AWSCredentials credentials = new BasicAWSCredentials(access_key, secret_key);
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
                new ClientConfiguration());

        if (endpoint.contains("amazonaws.com")) {
            s3Client.setEndpoint(endpoint);

            if (region.length() > 5) {
                if (region.contains("us-east-1")) {
                    s3Client.setEndpoint("https://s3.amazonaws.com");
                } else if (region.contains("us-west")) {
                    s3Client.setEndpoint("https://s3-" + region + ".amazonaws.com");
                } else if (region.contains("eu-west")) {
                    s3Client.setEndpoint("https://s3-" + region + ".amazonaws.com");
                } else if (region.contains("ap-")) {
                    s3Client.setEndpoint("https://s3-" + region + ".amazonaws.com");
                } else if (region.contains("sa-east-1")) {
                    s3Client.setEndpoint("https://s3-" + region + ".amazonaws.com");
                } else {
                    s3Client.setEndpoint("https://s3." + region + ".amazonaws.com");
                }
            }
        } else {
            s3Client.setS3ClientOptions(S3ClientOptions.builder().setPathStyleAccess(true).build());
            s3Client.setEndpoint(endpoint);
        }

        message = ("\nAttempting to create the bucket. Please view the Bucket list window for an update.");

        try {
            if (!(s3Client.doesBucketExist(bucket))) {
                s3Client.createBucket(new CreateBucketRequest(bucket));
            }
            String bucketLocation = s3Client.getBucketLocation(new GetBucketLocationRequest(bucket));
        } catch (AmazonServiceException ase) {
            if (ase.getMessage().contains("InvalidLocationConstraint")) {
                s3Client.createBucket(new CreateBucketRequest(bucket, region));
            } else {
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
        }
        return message;
    }

    String abortMPUploads(String access_key, String secret_key, String bucket, String endpoint) {
        String message = null;
        AWSCredentials credentials = new BasicAWSCredentials(access_key, secret_key);
        AmazonS3 s3Client = new AmazonS3Client(credentials,
                new ClientConfiguration());

        try {
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
            TransferManager tm = new TransferManager(s3Client);
            int month = 1000 * 60 * 60 * 24 * 30;
            Date oneWeekAgo = new Date(System.currentTimeMillis() - month);
            tm.abortMultipartUploads(bucket, oneWeekAgo);
            message = ("\nSent request to delete all the multi-part uploads in the past month");
        } catch (AmazonServiceException multipart) {
            if (NewJFrame.gui) {
                mainFrame.jTextArea1.append("\n\nError Message:    " + multipart.getMessage());
                mainFrame.jTextArea1.append("\nHTTP Status Code: " + multipart.getStatusCode());
                mainFrame.jTextArea1.append("\nAWS Error Code:   " + multipart.getErrorCode());
                mainFrame.jTextArea1.append("\nError Type:       " + multipart.getErrorType());
                mainFrame.jTextArea1.append("\nRequest ID:       " + multipart.getRequestId());
                calibrate();
            } else {
                System.out.print("\n\nError Message:    " + multipart.getMessage());
                System.out.print("\nHTTP Status Code: " + multipart.getStatusCode());
                System.out.print("\nAWS Error Code:   " + multipart.getErrorCode());
                System.out.print("\nError Type:       " + multipart.getErrorType());
                System.out.print("\nRequest ID:       " + multipart.getRequestId());
            }
        }
        if (message == null) {
            message = "Failed to list multi-part uploads.";
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
                new ClientConfiguration());
        s3Client.setS3ClientOptions(S3ClientOptions.builder().setPathStyleAccess(true).build());
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

    String listBucketContents(String access_key, String secret_key, String bucket, String endpoint) {
        objectlist = null;

        AWSCredentials credentials = new BasicAWSCredentials(access_key, secret_key);
        AmazonS3 s3Client = new AmazonS3Client(credentials,
                new ClientConfiguration());

        final ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucket).withDelimiter("");
        ListObjectsV2Result result;

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
            s3Client.setEndpoint(endpoint);
            s3Client.setS3ClientOptions(S3ClientOptions.builder().setPathStyleAccess(true).build());
        }

        try {
            do {
                result = s3Client.listObjectsV2(req);
                for (S3ObjectSummary objectSummary
                        : result.getObjectSummaries()) {
                    objectlist = objectlist + "@@" + objectSummary.getKey();
                }
                req.setContinuationToken(result.getNextContinuationToken());
            } while (result.isTruncated() == true);

        } catch (Exception listBucket) {
            if (listBucket.getMessage().contains("501")) {
                ListObjectsRequest listObjectsRequest = null;
                ObjectListing current = s3Client.listObjects((bucket));
                listObjectsRequest = new ListObjectsRequest().withBucketName(bucket);
                ObjectListing objectListing;
                do {
                    objectListing = s3Client.listObjects(listObjectsRequest);
                    for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                        objectlist = objectlist + "@@" + objectSummary.getKey();
                    }
                    listObjectsRequest.setMarker(objectListing.getNextMarker());
                } while (objectListing.isTruncated());
            } else {
                mainFrame.jTextArea1.append("\n" + listBucket.getMessage());
            }
        }

        String parse = null;
        if (objectlist != null) {
            parse = objectlist;
        } else {
            parse = "No objects_found.";
        }
        return parse;
    }

    String getObjectInfo(String key, String access_key,
            String secret_key, String bucket,
            String endpoint, String process
    ) {
        AWSCredentials credentials = new BasicAWSCredentials(access_key, secret_key);
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
        objectlist = null;

        try {
            ObjectListing current = s3Client.listObjects((bucket));

            ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucket);
            ObjectListing objectListing;
            do {
                objectListing = s3Client.listObjects(listObjectsRequest);

                for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                    if (process.contains("checkmd5")) {
                        if (objectSummary.getKey().contains(key)) {
                            objectlist = objectSummary.getETag();
                            break;
                        }
                    }
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
            //  mainFrame.jTextArea1.append("\n" + listBucket.getMessage());
        }

        return objectlist;
    }

    String deleteBucket(String access_key, String secret_key,
            String bucket, String endpoint
    ) {
        String message = null;
        AWSCredentials credentials = new BasicAWSCredentials(access_key, secret_key);
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
