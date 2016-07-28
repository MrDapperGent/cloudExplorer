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
    Boolean gui = false;

    public Update(NewJFrame Frame, Boolean Acheck, Boolean Agui) {
        mainFrame = Frame;
        check = Acheck;
        gui = Agui;
    }

    public void update() {
        try {
            String path = null;
            if (gui) {
                path = NewJFrame.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            } else {
                path = CloudExplorer.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            }

            if (gui) {
                NewJFrame.jTextArea1.append("\nDownloading updated version........");
            } else {
                System.out.print("\n\nDownloading updated version........");
            }
            URL download = new URL(this.updateURL);
            ReadableByteChannel rbc = Channels.newChannel(download.openStream());
            FileOutputStream fos = new FileOutputStream(path);
            fos.getChannel().transferFrom(rbc, 0L, 9223372036854775807L);
        } catch (Exception update) {
            if (gui) {
                NewJFrame.jTextArea1.append("\nError: " + update.getMessage());
                calibrate();
            } else {
                System.out.print("\nError: " + update.getMessage());
            }
        }
        if (gui) {
            NewJFrame.jTextArea1.append("\nDownload complete. The new version will now launch.");
            NewJFrame.jTextArea1.append("\nIf the new version does not launch automatically, please restart Cloud Explorer.");
            calibrate();
            NewJFrame.jMenuItem23.doClick();
        } else {
            System.out.print("\nDownload complete. Please run Cloud Explorer again to use the updated version.\n\n");
          
        }
    }

    public void run() {
        String new_version = null;
        String alert_message = null;
        String update_location = null;
        double newver = 0.0D;
        double currentversion = Double.parseDouble(NewJFrame.release_version);
        Boolean alert = false;
        if (gui) {
            NewJFrame.jPanel9.setVisible(true);
        }
        try {
            if (gui) {
                NewJFrame.jTextArea1.append("\nChecking for update......");
                NewJFrame.jTextArea1.append("\nInstalled Version: " + NewJFrame.release_version);
                calibrate();
            } else {
                System.out.print("\n\nChecking for update......");
                System.out.print("\nInstalled Version: " + NewJFrame.release_version);
            }

            URL update = new URL("https://cloudexplorer.s3.amazonaws.com/" + NewJFrame.major + "/versions.html");
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

            if (gui) {
                NewJFrame.jTextArea1.append("\nLatest version is: " + new_version);
            } else {
                System.out.print("\nLatest version is: " + new_version);
            }

            if (newver > currentversion) {
                if (alert) {
                    if (gui) {
                        NewJFrame.jTextArea1.append("\n" + alert_message);
                    } else {
                        System.out.print("\n" + alert_message);
                    }
                }
                if (!check && !alert) {
                    update();
                }
            } else {
                if (gui) {
                    NewJFrame.jTextArea1.append("\nNo update available.");
                    calibrate();
                } else {
                    System.out.print("\n\nNo update available.");
                }
            }
        } catch (Exception url) {
            if (gui) {
                NewJFrame.jTextArea1.append("\nError: " + url.getMessage());
                calibrate();
            } else {
                System.out.print("\nError: " + url.getMessage());
            }
        }
    }

    public void calibrate() {
        try {
            if (gui) {
                NewJFrame.jTextArea1.setCaretPosition(NewJFrame.jTextArea1.getLineStartOffset(NewJFrame.jTextArea1.getLineCount() - 1));
            }
        } catch (Exception e) {
        }
    }

    void startc(Boolean Acheck, Boolean Agui) {
        new Thread(new Update(mainFrame, Acheck, Agui)).start();
    }
}
