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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

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

    String writeConfig(String access_key, String secret_key, String host, String port, String region, String name) {
        try {
            config_file = (Home + File.separator + "s3.config");
            FileWriter fr = new FileWriter(config_file, true);
            BufferedWriter bfr = new BufferedWriter(fr);
            String str = null;
            
            if (region.length() < 1) {
                region = "defaultAWS";
            }
            if (name == null) {
                str = ("\n" + access_key + "@" + secret_key + "@" + host + "@" + port + "@" + region);
            } else {
                str = ("\n" + access_key + "@" + secret_key + "@" + host + "@" + port + "@" + region + "@" + name);
            }
            bfr.write(str);
            bfr.close();
        } catch (Exception writeConfig) {
        }
        return "\nSaved Config";
    }

    String writeMigrateConfig(String access_key, String secret_key, String host, String port, String region, String bucket) {
        try {
            config_file = (Home + File.separator + "s3Migrate.config");
            FileWriter fr = new FileWriter(config_file);
            BufferedWriter bfr = new BufferedWriter(fr);
            String str = (access_key + "@" + secret_key + "@" + host + "@" + port + "@" + region + "@" + bucket);
            bfr.write(str);
            bfr.close();
        } catch (Exception writeConfig) {
        }
        return ("\nSaved current account as the migration account.");
    }
}
