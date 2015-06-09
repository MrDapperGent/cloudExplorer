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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import static cloudExplorer.NewJFrame.jTextArea1;
import java.io.File;
import javax.swing.JCheckBox;

public class MakeDestinationBucket implements Runnable {

    NewJFrame mainFrame;
    public static String response = null;
    public static String region = null;
    public static String dest_bucket = null;
    BucketMigration migrate;
    Boolean snapshot = false;
    Boolean restoreSnapshot = false;
    String Home = System.getProperty("user.home");
    String config_file = (Home + File.separator + "s3Migrate.config");
    String active_folder = null;

    public MakeDestinationBucket(NewJFrame Frame, Boolean Asnapshot, Boolean ArestoreSnapshot, String Aactive_folder) {
        mainFrame = Frame;
        snapshot = Asnapshot;
        restoreSnapshot = ArestoreSnapshot;
        active_folder = Aactive_folder;
    }

    public void run() {
        try {
            final JButton startMigration = new JButton("Start Migration");
            final JButton abortMigration = new JButton("Abort");
            final JCheckBox deleteOrigin = new JCheckBox("Delete Origin");
            final JButton close = new JButton("Close");
            final JLabel blank = new JLabel(" ");
            final JLabel blank2 = new JLabel(" ");
            final JLabel blank3 = new JLabel(" ");
            deleteOrigin.setBackground(Color.white);
            deleteOrigin.setForeground(Color.blue);
            startMigration.setBackground(Color.white);
            startMigration.setForeground(Color.blue);
            abortMigration.setBackground(Color.white);
            abortMigration.setForeground(Color.blue);
            startMigration.setBorder(null);
            abortMigration.setBorder(null);
            close.setBackground(Color.white);
            close.setBorder(null);
            close.setForeground(Color.blue);
            close.setIcon(mainFrame.genericEngine);
            abortMigration.setIcon(mainFrame.genericEngine);
            startMigration.setIcon(mainFrame.genericEngine);

            mainFrame.jPanel15.setVisible(false);

            startMigration.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    if (snapshot) {
                        jTextArea1.setText("\nBucket snapshot has started. Please view this window for any errors.");
                    } else {
                        jTextArea1.setText("\nBucket migration has started. Please view this window for any errors.");
                    }
                    calibrate();
                    ReloadObjects object = new ReloadObjects(mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.bucket_item[mainFrame.active_bucket].getText(), mainFrame.cred.getEndpoint(), mainFrame);
                    object.run();
                    if (deleteOrigin.isSelected()) {
                        migrate = new BucketMigration(mainFrame.cred.access_key, mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), mainFrame, true, false, false, null);
                        migrate.startc(mainFrame.cred.access_key, mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), mainFrame, true, false, false, null);
                    } else if (snapshot) {
                        if (!restoreSnapshot) {
                            migrate = new BucketMigration(mainFrame.cred.access_key, mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), mainFrame, false, true, false, null);
                            migrate.startc(mainFrame.cred.access_key, mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), mainFrame, false, true, false, null);
                        } else {
                            migrate = new BucketMigration(mainFrame.cred.access_key, mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), mainFrame, false, true, true, active_folder);
                            migrate.startc(mainFrame.cred.access_key, mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), mainFrame, false, true, true, active_folder);
                        }
                    } else {
                        migrate = new BucketMigration(mainFrame.cred.access_key, mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), mainFrame, false, false, false, null);
                        migrate.startc(mainFrame.cred.access_key, mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), mainFrame, false, false, false, null);
                    }
                }
            });

            abortMigration.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    migrate.stop();
                }
            });

            close.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    mainFrame.jPanel14.removeAll();
                    mainFrame.jPanel14.repaint();
                    mainFrame.jPanel14.revalidate();
                    mainFrame.jPanel14.validate();
                    mainFrame.reloadBuckets();
                }
            });

            mainFrame.jPanel14.removeAll();
            mainFrame.jPanel14.setLayout(new BoxLayout(mainFrame.jPanel14, BoxLayout.Y_AXIS));
            mainFrame.jPanel14.add(blank3);

            if ((snapshot)) {
                startMigration.setText("Create Snapshot");
            }
            if (!snapshot) {
                mainFrame.jPanel14.add(deleteOrigin);
            }

            if ((snapshot) && (restoreSnapshot)) {
                startMigration.setText("Restore Snapshot");
            }

            mainFrame.jPanel14.add(blank);
            mainFrame.jPanel14.add(startMigration);
            mainFrame.jPanel14.add(abortMigration);
            mainFrame.jPanel14.add(close);
            mainFrame.jPanel14.repaint();
            mainFrame.jPanel14.revalidate();
            mainFrame.jPanel14.validate();

        } catch (Exception mp3player) {
            jTextArea1.append("\n" + mp3player.getMessage());
        }
        mainFrame.calibrateTextArea();

    }

    public void calibrate() {
        try {
            jTextArea1.setCaretPosition(jTextArea1.getLineStartOffset(jTextArea1.getLineCount() - 1));
        } catch (Exception e) {
        }
    }

    void startc(boolean Asnapshot, boolean ArestoreSnapshot, String Aactive_folder) {
        (new Thread(new MakeDestinationBucket(mainFrame, Asnapshot, restoreSnapshot, Aactive_folder))).start();
    }
}
