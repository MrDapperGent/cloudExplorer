package cloudExplorer;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Update
        implements Runnable {

    NewJFrame mainFrame;
    String updateURL = null;
    Boolean check = false;

    public Update(NewJFrame Frame, Boolean Acheck) {
        mainFrame = Frame;
        check = Acheck;
    }

    public void update() {
        try {
            String path = NewJFrame.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            NewJFrame.jTextArea1.append("\nDownloading updated version........");
            URL download = new URL(this.updateURL);
            ReadableByteChannel rbc = Channels.newChannel(download.openStream());
            FileOutputStream fos = new FileOutputStream(path);
            fos.getChannel().transferFrom(rbc, 0L, 9223372036854775807L);
        } catch (Exception update) {
            NewJFrame.jTextArea1.append("\nError: " + update.getMessage());
            calibrate();
        }
        NewJFrame.jTextArea1.append("\nDownload complete. The new version will now launch.");
        NewJFrame.jTextArea1.append("\nIf the new version does not launch automatically, please restart Cloud Explorer.");
        calibrate();
        NewJFrame.jMenuItem23.doClick();
    }

    public void run() {
        String new_version = null;
        String alert_message = null;
        String update_location = null;
        double newver = 0.0D;
        double currentversion = Double.parseDouble(this.mainFrame.release_version);
        Boolean alert = false;

        NewJFrame.jPanel9.setVisible(true);
        try {
            NewJFrame.jTextArea1.append("\nChecking for update......");
            NewJFrame.jTextArea1.append("\nInstalled Version: " + this.mainFrame.release_version);
            calibrate();
            URL update = new URL("https://www.linux-toys.com/" + this.mainFrame.major + "/versions.html");
            URLConnection yc = update.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(yc
                    .getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.contains("vers=")) {
                    new_version = inputLine.replace("vers=", "");
                    newver = Double.parseDouble(new_version);
                }
                if (inputLine.contains("loc=")) {
                    update_location = inputLine.replace("loc=", "");
                }
                if (inputLine.contains("update=")) {
                    updateURL = inputLine.replace("update=", "");
                }
                if (inputLine.contains("alert=")) {
                    alert_message = inputLine.replace("alert=", "");
                    alert = true;
                }
            }
            in.close();

            NewJFrame.jTextArea1.append("\nLatest version is: " + new_version);
            if (!check) {
                NewJFrame.jTextArea1.append("\nRelease URL: " + update_location);
            }
            if (!check) {
                if (!alert) {
                    if (newver > currentversion) {
                        update();
                    } else {
                        NewJFrame.jTextArea1.append("\nNo update available.");
                    }
                } else {
                    NewJFrame.jTextArea1.append("\n" + alert_message);
                }
            }
            calibrate();
        } catch (Exception url) {
            NewJFrame.jTextArea1.append("\nError: " + url.getMessage());
            calibrate();
        }
    }

    public void calibrate() {
        try {
            NewJFrame.jTextArea1.setCaretPosition(NewJFrame.jTextArea1.getLineStartOffset(NewJFrame.jTextArea1.getLineCount() - 1));
        } catch (Exception e) {
        }
    }

    void startc(Boolean Acheck) {
        new Thread(new Update(mainFrame, Acheck)).start();
    }
}
