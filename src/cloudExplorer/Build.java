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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Build {

    NewJFrame mainFrame;
    String name = null;
    String Home = System.getProperty("user.home");
    String s3_config_file = Home + File.separator + "s3.config";
    BucketClass Bucket = new BucketClass();
    Acl objectacl = new Acl();
    File build_file;
    String build_file_location;
    Put put;
    String build_name = null;

    String[] saved_s3_configs = null;
    String[] saved_directory_to_sync = null;
    File dirToSync = new File("");
    String secret_key = null;
    String access_key = null;
    String endpoint = null;
    String bucket = null;
    String region = null;
    boolean gui = false;

    void messageParser(String message) {
        System.out.print(message);
    }

    void mainmenu() {
        if (!gui) {
            for (int i = 0; i != 20; i++) {
                messageParser("\n");
            }
            messageParser("\n------------------------------------------------");
            messageParser("\n           Cloudian Explorer Build mode.");
            messageParser("\n------------------------------------------------");
        } else {
            messageParser("\nBuild Sync mode is running.....");
        }
    }

    void loadS3credentials() {
        try {
            for (String what : saved_s3_configs) {
                if (what == null) {
                    messageParser("\nError: an S3 config was null");
                    if (!gui) {
                        System.exit(-1);
                    }
                }
            }

            access_key = saved_s3_configs[0];
            secret_key = saved_s3_configs[1];
            endpoint = saved_s3_configs[2] + ":" + saved_s3_configs[3];
            region = saved_s3_configs[4];
        } catch (Exception loadS3Credentials) {
        }
    }

    String loadConfig(String what) {
        String data = null;

        try {
            FileReader fr = new FileReader(what);
            BufferedReader bfr = new BufferedReader(fr);
            String read = null;

            while ((read = bfr.readLine()) != null) {
                data = data + read;
            }
        } catch (Exception loadConfig) {
        }
        String remove_null = data.replace("null", "");
        String remove_symbol = remove_null.replace("@", " ");
        return remove_symbol;
    }

    void start(String Aname, String Abuild_file) {
        build_name = Aname;
        build_file = new File(Abuild_file);

        mainmenu();

        try {
            File s3config = new File(s3_config_file);
            if (s3config.exists()) {
            } else {
                messageParser("\nError: Build config file not found.");
            }

            saved_s3_configs = loadConfig(this.s3_config_file).toString().split(" ");
            loadS3credentials();

            if (build_file.exists()) {

                new Thread(new Runnable() {
                    public void run() {
                        putTOs3(build_file);
                    }
                }).start();

            } else {
                messageParser("\nError: " + build_file.toString() + " does not exist");
            }
        } catch (Exception Start) {
        }

    }

    void putTOs3(File dir) {
        NewJFrame.perf = true;
        System.out.print("\n\nUploading " + build_file.getAbsolutePath().toString() + "........");
        put = new Put(build_file.getAbsolutePath().toString(), access_key, secret_key, bucket, endpoint, build_name, false, false);
        put.startc(build_file.getAbsolutePath().toString(), access_key, secret_key, bucket, endpoint, build_name, false, false);
        String url = objectacl.setACLurl(build_name, access_key, secret_key, endpoint, bucket);
        url = url.replace("Pre-Signed URL = ", "");
        System.out.print("\n\n" + url);
    }
}
