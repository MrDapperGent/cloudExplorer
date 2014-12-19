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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.JRadioButton;

public class Credentials {

    String access_key = null;
    String secret_key = null;
    String end_point = null;
    String bucket = null;
    String region = null;
    String Home = System.getProperty("user.home");
    String userid = null;
    String groupid = null;
    String OS = System.getProperty("os.name");
    String config_file = (Home + File.separator + "s3.config");
    NewJFrame mainFrame;

    public String getRegion() {
        return region;
    }

    public String getBucket() {
        return bucket;
    }

    public String getGROUPID() {
        return groupid;
    }

    public String getUSERID() {
        return userid;
    }

    public String getEndpoint() {
        return end_point;
    }

    public String getAccess_key() {
        return access_key;
    }

    public String getSecret_key() {
        return secret_key;
    }

    void setAccess_key(String what) {
        access_key = what;
    }

    void setSecret_key(String what) {
        secret_key = what;
    }

    void setEndpoint(String what) {
        end_point = what;
    }

    void setBucket(String what) {
        bucket = what;
    }

    void setRegion(String what) {
        region = what;
    }

    void setGROUPID(String what) {
        groupid = what;
    }

    void setUSERID(String what) {
        userid = what;
    }

    String loadConfig() {
        String data = null;

        config_file = (Home + File.separator + "s3.config");

        try {
            FileReader fr = new FileReader(config_file);
            BufferedReader bfr = new BufferedReader(fr);
            String read = null;
            int h = 0;
            while ((read = bfr.readLine()) != null) {
                data = data + read;
                mainFrame.account_item[h] = new JRadioButton();
                mainFrame.account_item[h].setText(read);
                mainFrame.jPanel21.add(mainFrame.account_item[h]);
                mainFrame.jPanel21.repaint();
                mainFrame.jPanel21.revalidate();
                mainFrame.jPanel21.validate();
                h++;
            }
        } catch (Exception loadConfig) {
        }
        String remove_null = data.replace("null", "");
        String remove_symbol = remove_null.replace("@", " ");
        return remove_symbol;

    }

    String writeConfig(String access_key, String secret_key, String host, String port, String region, String name) {
        try {
            config_file = (Home + File.separator + "s3.config");
            FileWriter fr = new FileWriter(config_file, true);
            BufferedWriter bfr = new BufferedWriter(fr);
            if (name == null) {
                bfr.write("\n" + access_key + "@" + secret_key + "@" + host + "@" + port + "@" + region);
            } else {
                bfr.write("\n" + access_key + "@" + secret_key + "@" + host + "@" + port + "@" + region + "@" + name);
            }

            bfr.close();
        } catch (Exception writeConfig) {
        }
        return "\nSaved Config";
    }

    String writeMigrateConfig(String access_key, String secret_key, String host, String port, String region) {
        try {
            config_file = (Home + File.separator + "s3Migrate.config");
            FileWriter fr = new FileWriter(config_file);
            BufferedWriter bfr = new BufferedWriter(fr);
            bfr.write(access_key + "@" + secret_key + "@" + host + "@" + port + "@" + region);
            bfr.close();
        } catch (Exception writeConfig) {
        }
        return ("\nSaved current account as the migration account.");
    }
}
