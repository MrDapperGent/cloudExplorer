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

public class Snapshot implements Runnable {

    NewJFrame mainFrame;
    public static String response = null;
    public static String region = null;
    public static String dest_bucket = null;
    BucketMigrationCLI migrate;
    String Home = System.getProperty("user.home");
    String config_file = (Home + File.separator + "s3Migrate.config");
    String active_folder = null;
    public Boolean deltas = false;

    public Snapshot(NewJFrame Frame, String Aactive_folder) {
        mainFrame = Frame;
        active_folder = Aactive_folder;
    }

    public void run() {
        try {
            final JButton createSnapshot = new JButton("Create Snapshot");
            final JButton restoreSnapshot = new JButton("Restore Snapshot");
            final JButton abortMigration = new JButton("Abort");
            final JButton close = new JButton("Close");
            final JCheckBox sync_deltas = new JCheckBox("Sync Only Changes");
            final JLabel blank = new JLabel(" ");
            final JLabel blank2 = new JLabel(" ");
            final JLabel blank3 = new JLabel(" ");
            final JLabel blank4 = new JLabel(" ");
            restoreSnapshot.setBackground(Color.white);
            restoreSnapshot.setForeground(Color.BLUE);
            restoreSnapshot.setFont(restoreSnapshot.getFont().deriveFont(14.0f));
            sync_deltas.setBackground(Color.white);
            sync_deltas.setForeground(Color.BLUE);
            sync_deltas.setFont(sync_deltas.getFont().deriveFont(14.0f));
            createSnapshot.setBackground(Color.white);
            createSnapshot.setForeground(Color.BLUE);
            createSnapshot.setFont(createSnapshot.getFont().deriveFont(14.0f));

            abortMigration.setBackground(Color.white);
            abortMigration.setForeground(Color.BLUE);
            createSnapshot.setBorder(null);
            restoreSnapshot.setBorder(null);
            abortMigration.setBorder(null);
            close.setBackground(Color.white);
            close.setBorder(null);
            close.setFont(close.getFont().deriveFont(14.0f));
            close.setForeground(Color.BLUE);
            close.setIcon(mainFrame.genericEngine);
            abortMigration.setIcon(mainFrame.genericEngine);
            restoreSnapshot.setIcon(mainFrame.genericEngine);
            createSnapshot.setIcon(mainFrame.genericEngine);

            mainFrame.jPanel15.setVisible(false);

            createSnapshot.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (sync_deltas.isSelected()) {
                        deltas = true;
                    }
                    jTextArea1.setText("\nBucket snapshot has started. Please view this window for any errors.");
                    ReloadObjects object = new ReloadObjects(mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.bucket_item[mainFrame.active_bucket].getText(), mainFrame.cred.getEndpoint(), mainFrame);
                    object.run();
                    migrate = new BucketMigrationCLI(mainFrame.cred.access_key, mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), mainFrame.objectarray, true, mainFrame.snap_folder, deltas);
                    migrate.startc(mainFrame.cred.access_key, mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), mainFrame.objectarray, true, mainFrame.snap_folder, deltas);
                }
            });

            restoreSnapshot.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    jTextArea1.setText("\nBucket snapshot has started. Please view this window for any errors.");
                    calibrate();
                    if (active_folder != null) {
                        ReloadObjects object = new ReloadObjects(mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.bucket_item[mainFrame.active_bucket].getText(), mainFrame.cred.getEndpoint(), mainFrame);
                        object.run();
                        migrate = new BucketMigrationCLI(mainFrame.cred.access_key, mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), mainFrame.objectarray, true, active_folder, deltas);
                        migrate.startc(mainFrame.cred.access_key, mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), mainFrame.objectarray, true, active_folder, deltas);
                    } else {
                        jTextArea1.setText("\nError: You did not select an active folder to restore from.");
                        calibrate();
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
                    mainFrame.drawBuckets();
                }
            });

            mainFrame.jPanel14.removeAll();
            mainFrame.jPanel14.setLayout(new BoxLayout(mainFrame.jPanel14, BoxLayout.Y_AXIS));
            mainFrame.jPanel14.add(blank3);
            mainFrame.jPanel14.add(createSnapshot);
            mainFrame.jPanel14.add(restoreSnapshot);
            mainFrame.jPanel14.add(sync_deltas);
            mainFrame.jPanel14.add(blank4);
            mainFrame.jPanel14.add(close);
            mainFrame.jPanel14.repaint();
            mainFrame.jPanel14.revalidate();
            mainFrame.jPanel14.validate();

        } catch (Exception snapshot) {
            jTextArea1.append("\n" + snapshot.getMessage());
        }
        mainFrame.calibrateTextArea();

    }

    public void calibrate() {
        try {
            jTextArea1.setCaretPosition(jTextArea1.getLineStartOffset(jTextArea1.getLineCount() - 1));
        } catch (Exception e) {
        }
    }

    void startc(String Aactive_folder) {
        (new Thread(new Snapshot(mainFrame, Aactive_folder))).start();
    }
}
