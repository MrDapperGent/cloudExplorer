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

import static cloudExplorer.NewJFrame.jPanel9;
import static cloudExplorer.NewJFrame.jTextArea1;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Update implements Runnable {

    NewJFrame mainFrame;
    public static String response = null;
    public static String region = null;
    public static String dest_bucket = null;
    BucketMigration migrate;

    public Update(NewJFrame Frame) {
        mainFrame = Frame;

    }

    public void run() {
        String new_version = null;
        String update_location = null;
        mainFrame.jPanel9.setVisible(true);
        try {
            mainFrame.jTextArea1.append("\nChecking for updates......");
            mainFrame.jTextArea1.append("\nVersion running: " + mainFrame.version);
            calibrate();
            URL update = new URL("https://linux-toys.com/versions.html");
            URLConnection yc = update.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.contains("vers=")) {
                    new_version = inputLine.replace("vers=", "");
                }
                if (inputLine.contains("loc=")) {
                    update_location = inputLine.replace("loc=", "");
                }
            }
            in.close();
            mainFrame.jTextArea1.append("\nLatest version is: " + new_version);
            mainFrame.jTextArea1.append("\nDownload URL: " + update_location);
            calibrate();
        } catch (Exception url) {
            mainFrame.jTextArea1.append("\nError: " + url.getMessage());
            calibrate();
        }
    }

    public void calibrate() {
        try {
            NewJFrame.jTextArea1.setCaretPosition(NewJFrame.jTextArea1.getLineStartOffset(NewJFrame.jTextArea1.getLineCount() - 1));
        } catch (Exception e) {
        }
    }

    void startc() {
        (new Thread(new Update(mainFrame))).start();
    }
}
