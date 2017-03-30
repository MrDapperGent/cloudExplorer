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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.TimeUnit;

public class Daemon {

    NewJFrame mainFrame;
    String Home = System.getProperty("user.home");
    String sync_config_file = Home + File.separator + "s3config.sync";
    String s3_config_file = Home + File.separator + "s3.config";
    String[] saved_s3_configs = null;
    String[] sync_config = null;
    File dirToSync = new File("");
    String bucket = null;
    boolean gui = false;
    CLI cli = new CLI();

    void messageParser(String message) {
        if (gui) {
            mainFrame.jTextArea1.append(message);
        } else {
            System.out.print(message);
        }
    }

    void mainmenu() {
        if (!gui) {
            for (int i = 0; i != 20; i++) {
                messageParser("\n");
            }
            messageParser("\n------------------------------------------------");
            messageParser("\n   Cloud Explorer is running in Daemon mode.");
            messageParser("\n------------------------------------------------");
        } else {
            messageParser("\nBackground Sync mode is running.....");
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

    void start() {

        sync_config_file = (Home + File.separator + "s3config.sync");

        mainmenu();
        try {
            
            if (!gui) {
                messageParser("\n\nCloud Explorer will sync the directory listed in the config file:\n\n" + sync_config_file + " to S3 every 5 minutes.");
            }

            File syncconfig = new File(sync_config_file);
            if (syncconfig.exists()) {
            } else {
                messageParser("\nError: Sync config file not found.");
                if (!gui) {
                    System.exit(-1);
                }
            }

            sync_config = loadConfig(sync_config_file).toString().split("@");
            System.out.print("\nDebug:" + sync_config);
            bucket = sync_config[4];
            dirToSync = new File(sync_config[3]);

            File syncDIR = new File(sync_config[3]);
            if (syncDIR.exists()) {
                System.out.print("\nHere");
                cli.bucket = bucket;
                cli.access_key = sync_config[0];
                cli.secret_key = sync_config[1];
                cli.endpoint = sync_config[2];
                cli.destination = dirToSync.toString();

                new Thread(new Runnable() {
                    public void run() {
                        try {
                            String parse_dir[] = dirToSync.toString().split(File.separator);
                            String final_dir = dirToSync.toString().replace(File.separator + parse_dir[parse_dir.length - 1], "");

                            cli.destination = dirToSync.toString();
                            cli.syncToS3(null);

                            cli.destination = final_dir;
                            cli.syncFromS3(null);

                            //    Thread.sleep(TimeUnit.MINUTES.toMillis(5));
                            if (gui) {
                                mainFrame.jTextArea1.setText("");
                            }
                            run();

                        } catch (Exception e) {
                            System.out.print("\nError:" + e.getMessage());
                        }
                    }
                }).start();

            } else {
                messageParser("\nError: " + syncDIR.toString() + " does not exist");
            }
        } catch (Exception Start) {
        }
    }
}
