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
public class SyncEngine implements Runnable {

    Put put;
    Get get;
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
    Boolean ToS3;
    Boolean infreq;
    String destination;
    String win = "\\";
    String lin = "/";
    File check_localFile;

    SyncEngine(String AremoteFile, String AlocalFile, File Afile_found, String Aobject, String Abucket, String Aaccess_key, String Asecret_key, String Aendpoint, Boolean Arrs, Boolean Aencrypt, Boolean Ainfreq, Boolean AToS3, String Adestination) {
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
        ToS3 = AToS3;
        infreq = Ainfreq;
        destination = Adestination;
    }

    public String Transcode(String what) {
        String transcoded_object = null;
        if (what.contains(win) || (what.contains(lin))) {
            if (what.contains(win) && File.separator.contains(win)) {
                transcoded_object = what;
            }

            if (what.contains(lin) && File.separator.contains(lin)) {
                transcoded_object = what;
            }

            if (what.contains(lin) && File.separator.contains(win)) {
                transcoded_object = what.replace(lin, win);
            }

            if (what.contains(win) && File.separator.contains(lin)) {
                transcoded_object = what.replace(win, lin);
            }

        } else {
            transcoded_object = what;
        }
        return transcoded_object;
    }

    String makeDirectory(String what) {

        if (what.substring(0, 2).contains(":")) {
            what = what.substring(3, what.length());
        }

        if (what.substring(0, 1).contains("/")) {
            what = what.substring(1, what.length());
        }

        if (what.contains("/")) {
            what = what.replace("/", File.separator);
        }

        if (what.contains("\\")) {
            what = what.replace("\\", File.separator);
        }

        int slash_counter = 0;
        int another_counter = 0;

        for (int y = 0; y != what.length(); y++) {
            if (what.substring(y, y + 1).contains(File.separator)) {
                slash_counter++;
                another_counter = y;
            }
        }

        File dir = new File(File.separator + what.substring(0, another_counter));
        dir.mkdirs();
        return what;
    }

    public void run() {
        if (SyncManager.running) {
            Boolean recopy = false;
            long milli;
            FileInputStream fis = null;
            String local_md5String = null;
            String remote_md5String = null;
            try {
                if (!ToS3) {
                    localFile = destination + File.separator + Transcode(remoteFile);
                }
                check_localFile = new File(localFile);
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

                if ((!ToS3) && check_localFile.exists()) {
                    fis = new FileInputStream(localFile);
                    local_md5String = DigestUtils.md5Hex(fis);
                }

                if (ToS3) {
                    fis = new FileInputStream(localFile);
                    local_md5String = DigestUtils.md5Hex(fis);
                }

                remote_md5String = Bucket.getObjectInfo(remoteFile, access_key, secret_key, bucket, endpoint, "checkmd5");

                if ((remote_md5String == null) || (local_md5String == null)) {
                    recopy = true;
                } else {
                    Date remote = sdf.parse(Bucket.getObjectInfo(remoteFile, access_key, secret_key, bucket, endpoint, "objectdate"));
                    milli = check_localFile.lastModified();
                    Date local = new Date(milli);

                    if (local_md5String.contains(remote_md5String)) {
                    } else {
                        recopy = true;
                    }
                }
                if (recopy) {
                    if (ToS3) {
                        put = new Put(file_found.getAbsolutePath().toString(), access_key, secret_key, bucket, endpoint, object, rrs, encrypt, infreq);
                        put.run();
                    } else {
                        makeDirectory(destination + File.separator + remoteFile);
                        makeDirectory(remoteFile);
                        get = new Get(remoteFile, access_key, secret_key, bucket, endpoint, destination + File.separator + Transcode(remoteFile), null);
                        get.run();
                    }
                }
            } catch (Exception modifiedChecker) {
                System.out.print("\nError: " + modifiedChecker.getMessage());
            }
        }
    }
}
