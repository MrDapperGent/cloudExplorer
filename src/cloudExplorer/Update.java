package cloudExplorer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

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
        String path = NewJFrame.class.getProtectionDomain().getCodeSource().getLocation().toString();
        path = path.replace("CloudExplorer.jar", "");
        path = path.replace("file:", "");

        try {
            message("\nDownloading updated version........");
            URL download = new URL(this.updateURL);
            ReadableByteChannel rbc = Channels.newChannel(download.openStream());
            FileOutputStream fos = new FileOutputStream(path + File.separator + "update.zip");
            fos.getChannel().transferFrom(rbc, 0L, 9223372036854775807L);
        } catch (Exception update) {
            message("\nError Downloading Zip: " + update.getMessage());
        }

        if (gui) {
            NewJFrame.jTextArea1.append("\nDownload complete. Trying to Extract file.");
            calibrate();
            File check_zip = new File(path + "update.zip");
            if (check_zip.exists()) {
                Zip zip = new Zip(path + "update.zip", path, "unzip");
                zip.decompress();
                message("\nExtraction Complete. Trying to copy files.");
                File scan_location = new File(path + File.separator + "cloudExplorer");
                List<File> files = (List<File>) FileUtils.listFiles(scan_location, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
                for (File file_found : files) {
                    String dest = null;
                    String source = (file_found.getAbsolutePath());
                    try {
                        InputStream is = null;
                        OutputStream os = null;
                        if (file_found.getAbsolutePath().contains("lib")) {
                            dest = path + "lib" + File.separator + file_found.getName();
                        } else {
                            dest = path + file_found.getName();
                        }

                        try {
                            is = new FileInputStream(source);
                            os = new FileOutputStream(dest);
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = is.read(buffer)) > 0) {
                                os.write(buffer, 0, length);
                            }
                        } finally {
                            is.close();
                            os.close();
                        }
                    } catch (Exception copy) {
                        message("\nCopy file error occurred:" + copy.getMessage());
                    }
                }
                message("\nFile copy complete. Deleteing old upgrade directory.");
                try {
                    FileUtils.deleteDirectory(new File(scan_location.toString()));
                    check_zip.delete();
                } catch (IOException ex) {
                    Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
                }
                message("\nUpgrade complete. Please restart Cloud Explorer.");
                //      NewJFrame.jMenuItem23.doClick();
            } else {
                message("\nError: Zip file not found!");
            }
        }
    }

    public void message(String what) {
        if (gui) {
            NewJFrame.jTextArea1.append(what);
            calibrate();
        } else {
            System.out.print(what);
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
            message("\nChecking for update......");
            message("\nInstalled Version: " + NewJFrame.release_version);

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
            message("\nLatest version is: " + new_version);

            if (newver > currentversion) {
                if (alert) {
                    message("\n" + alert_message);
                }
                if (!check && !alert) {
                    update();
                }
            } else {
                message("\nNo update available.");
            }
        } catch (Exception url) {
            message("\nError: " + url.getMessage());
        }
    }

    public void calibrate() {
        try {
            NewJFrame.jTextArea1.setCaretPosition(NewJFrame.jTextArea1.getLineStartOffset(NewJFrame.jTextArea1.getLineCount() - 1));
        } catch (Exception e) {
        }
    }

    void startc(Boolean Acheck, Boolean Agui) {
        new Thread(new Update(mainFrame, Acheck, Agui)).start();
    }
}
