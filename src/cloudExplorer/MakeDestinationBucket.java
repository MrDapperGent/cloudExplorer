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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import static cloudExplorer.NewJFrame.jTextArea1;
import java.awt.Dimension;
import javax.swing.JCheckBox;

public class MakeDestinationBucket implements Runnable {

    NewJFrame mainFrame;
    public static String response = null;
    public static String region = null;
    public static String dest_bucket = null;
    BucketMigration migrate;

    public MakeDestinationBucket(NewJFrame Frame) {
        mainFrame = Frame;

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
            final JTextField bucketName = new JTextField();
            bucketName.setMaximumSize(new Dimension(250, 20));
            final JLabel name = new JLabel("Destination Bucket:");
            name.setBackground(Color.white);
            name.setForeground(Color.blue);
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
                    if (bucketName.getText().length() < 3) {
                        close.doClick();
                    } else {
                        jTextArea1.setText("\nBucket migration has started. Please view this window for any errors.");
                        calibrate();
                        ReloadObjects object = new ReloadObjects(mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.bucket_item[mainFrame.active_bucket].getText(), mainFrame.cred.getEndpoint(), mainFrame);
                        object.run();
                        if (deleteOrigin.isSelected()) {
                            migrate = new BucketMigration(mainFrame.cred.access_key, mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), mainFrame, bucketName.getText(), true);
                            migrate.startc(mainFrame.cred.access_key, mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), mainFrame, bucketName.getText(), true);
                        } else {
                            migrate = new BucketMigration(mainFrame.cred.access_key, mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), mainFrame, bucketName.getText(), false);
                            migrate.startc(mainFrame.cred.access_key, mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), mainFrame, bucketName.getText(), false);
                        }
                    }
                    //close.doClick();
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
                    mainFrame.miniReload();
                }
            });

            mainFrame.jPanel14.removeAll();
            mainFrame.jPanel14.setLayout(new BoxLayout(mainFrame.jPanel14, BoxLayout.Y_AXIS));
            mainFrame.jPanel14.add(name);
            mainFrame.jPanel14.add(bucketName);
            mainFrame.jPanel14.add(blank3);
            mainFrame.jPanel14.add(deleteOrigin);
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

    void startc() {
        (new Thread(new MakeDestinationBucket(mainFrame))).start();
    }
}
