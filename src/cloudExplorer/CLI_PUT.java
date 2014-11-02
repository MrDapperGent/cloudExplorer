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

public class CLI_PUT {

    String name = null;
    String Home = System.getProperty("user.home");
    String s3_config_file = Home + File.separator + "s3.config";
    BucketClass Bucket = new BucketClass();
    Acl objectacl = new Acl();
    File put_file;
    String build_file_location;
    Put put;
    String build_name = null;
    String[] saved_s3_configs = null;
    String secret_key = null;
    String access_key = null;
    String endpoint = null;
    String bucket = null;
    String region = null;

    void messageParser(String message) {
        System.out.print(message);
    }

    void mainmenu() {

        for (int i = 0; i != 20; i++) {
            messageParser("\n");
        }
        messageParser("\n------------------------------------------------");
        messageParser("\n           Cloud Explorer PUT request.");
        messageParser("\n------------------------------------------------");

    }

    void loadS3credentials() {
        try {
            for (String what : saved_s3_configs) {
                if (what == null) {
                    messageParser("\nError: an S3 config was null");
                    System.exit(-1);
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

    void start(String Aput_file, String Abucket) {
        bucket = Abucket;
        put_file = new File(Aput_file);

        mainmenu();

        try {
            File s3config = new File(s3_config_file);
            if (s3config.exists()) {
            } else {
                messageParser("\nError: Build config file not found.");
            }

            saved_s3_configs = loadConfig(this.s3_config_file).toString().split(" ");
            loadS3credentials();

            if (put_file.exists()) {

                new Thread(new Runnable() {
                    public void run() {
                        putTOs3(put_file);
                    }
                }).start();

            } else {
                messageParser("\nError: " + put_file.toString() + " does not exist");
            }
        } catch (Exception Start) {
        }

    }

    void putTOs3(File dir) {
        try {
            NewJFrame.perf = true;
            System.out.print("\n\nUploading " + put_file.getAbsolutePath().toString() + "........");
            put = new Put(put_file.getAbsolutePath().toString(), access_key, secret_key, bucket, endpoint, put_file.getName(), false, false);
            Put.debug = true;
            put.startc(put_file.getAbsolutePath().toString(), access_key, secret_key, bucket, endpoint, put_file.getName(), false, false);
            System.out.print("\n\nPUT operation Complete\n\n\n");
        } catch (Exception send) {
            System.out.print("\n\nAn Error has occured while uploading the file");
            System.exit(-1);
        }
    }
}
