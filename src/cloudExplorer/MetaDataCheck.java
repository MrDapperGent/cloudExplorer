/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudExplorer;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author rusher81572
 */
public class MetaDataCheck implements Runnable {

    Put put;
    String remoteFile;
    String localFile;
    String bucket;
    String endpoint;
    String access_key;
    String secret_key;
    String object;
    BucketClass Bucket = new BucketClass();
    SyncToS3 synctos3;
    File file_found;
    Boolean rrs;
    Boolean encrypt;
    Boolean infreq;

    MetaDataCheck(String AremoteFile, String AlocalFile, File Afile_found, String Aobject, String Abucket, String Aaccess_key, String Asecret_key, String Aendpoint, Boolean Arrs, Boolean Aencrypt, Boolean Ainfreq) {
        remoteFile = AremoteFile;
        localFile = AlocalFile;
        bucket = Abucket;
        access_key = Aaccess_key;
        secret_key = Asecret_key;
        endpoint = Aendpoint;
        file_found = Afile_found;
        object = Aobject;
        rrs = Arrs;
        encrypt = Aencrypt;
        infreq = Ainfreq;
    }

    public void run() {
        Boolean upload = false;
        long milli;
        FileInputStream fis = null;
        String local_md5String = null;
        String remote_md5String = null;
        try {
            File check_localFile = new File(localFile);
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            fis = new FileInputStream(localFile);
            local_md5String = DigestUtils.md5Hex(fis);
            remote_md5String = Bucket.getObjectInfo(remoteFile, access_key, secret_key, bucket, endpoint, "checkmd5");
            if (remote_md5String == null) {
                upload = true;
            } else {
                Date remote = sdf.parse(Bucket.getObjectInfo(remoteFile, access_key, secret_key, bucket, endpoint, "objectdate"));
                milli = check_localFile.lastModified();
                Date local = new Date(milli);
                if (local_md5String.contains(remote_md5String)) {
                } else {
                    if (local.after(remote)) {
                        upload = true;
                    }
                }

            }
            if (upload) {
                put = new Put(file_found.getAbsolutePath().toString(), access_key, secret_key, bucket, endpoint, object, rrs, encrypt, infreq);
                put.run();
            }
        } catch (Exception modifiedChecker) {
            System.out.print("\nError: " + modifiedChecker.getMessage());
        }
    }

}
