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
import java.net.URL;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import static cloudExplorer.NewJFrame.jTextArea1;
import javax.swing.JLabel;

public class BucketACL implements Runnable {

    NewJFrame mainFrame;
    public static String object_acl_change = null;

    public BucketACL(NewJFrame Frame) {
        mainFrame = Frame;

    }

    public void run() {
        try {

            URL music_url = null;

            final JCheckBox static_website = new JCheckBox("Static Website");
            final JCheckBox disable_website = new JCheckBox("Disable Static Website");
            final JCheckBox enable_versioning = new JCheckBox("Enable Versioning");
            final JCheckBox suspend_versioning = new JCheckBox("Suspend Versioning");
            final JCheckBox delete_bucket = new JCheckBox("Delete Bucket");
            final JLabel blank = new JLabel(" ");
            final JButton bucketACLbutton = new JButton("Commit");
            final JButton close = new JButton("Close");
            final JLabel blank_label = new JLabel(" ");

            static_website.setBackground(Color.white);
            static_website.setBorder(null);
            static_website.setForeground(Color.blue);

            enable_versioning.setBackground(Color.white);
            enable_versioning.setBorder(null);
            enable_versioning.setForeground(Color.blue);

            suspend_versioning.setBackground(Color.white);
            suspend_versioning.setBorder(null);
            suspend_versioning.setForeground(Color.blue);

            disable_website.setBackground(Color.white);
            disable_website.setBorder(null);
            disable_website.setForeground(Color.blue);

            delete_bucket.setBackground(Color.white);
            delete_bucket.setBorder(null);
            delete_bucket.setForeground(Color.blue);

            bucketACLbutton.setBackground(Color.white);
            bucketACLbutton.setBorder(null);
            bucketACLbutton.setForeground(Color.blue);

            close.setBackground(Color.white);
            close.setBorder(null);
            close.setForeground(Color.blue);

            bucketACLbutton.setIcon(mainFrame.genericEngine);
            close.setIcon(mainFrame.genericEngine);

            bucketACLbutton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {

                        if (enable_versioning.isSelected()) {
                            jTextArea1.append(mainFrame.bucket.controlVersioning(mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), mainFrame.cred.getRegion(), true));
                            mainFrame.reloadBuckets();
                        }

                        if (suspend_versioning.isSelected()) {
                            jTextArea1.append(mainFrame.bucket.controlVersioning(mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), mainFrame.cred.getRegion(), false));
                        }
                        if (delete_bucket.isSelected()) {
                            mainFrame.bucket_item[mainFrame.active_bucket].setSelected(false);
                            DeleteBucketThread delBucket = new DeleteBucketThread(mainFrame.cred.access_key, mainFrame.cred.secret_key, mainFrame.bucket_item[mainFrame.active_bucket].getText(), mainFrame.cred.end_point, mainFrame.cred.region, mainFrame);
                            delBucket.startc(mainFrame.cred.access_key, mainFrame.cred.secret_key, mainFrame.bucket_item[mainFrame.active_bucket].getText(), mainFrame.cred.end_point, mainFrame.cred.region, mainFrame);
                           
                        }

                        if (static_website.isSelected()) {
                            mainFrame.objectacl.setBUCKETwebsite(object_acl_change, mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.cred.getEndpoint(), mainFrame.cred.getBucket());
                            jTextArea1.append("\nWebsite access enabled.\n");

                        }

                        if (disable_website.isSelected()) {
                            mainFrame.objectacl.removeBUCKETwebsite(object_acl_change, mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.cred.getEndpoint(), mainFrame.cred.getBucket());
                            jTextArea1.append("\nBucket is no longer serving a website.\n");

                        }

                    } catch (Exception ObjectACL) {
                        jTextArea1.append("\n" + ObjectACL.getMessage() + "\n");
                    }

                    mainFrame.calibrateTextArea();
                    mainFrame.jPanel14.removeAll();
                    mainFrame.jPanel14.repaint();
                    mainFrame.jPanel14.revalidate();
                    mainFrame.jPanel14.validate();

                }
            });

            close.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    mainFrame.calibrateTextArea();
                    mainFrame.jPanel14.removeAll();
                    mainFrame.jPanel14.repaint();
                    mainFrame.jPanel14.revalidate();
                    mainFrame.jPanel14.validate();

                }
            });

            mainFrame.jPanel14.removeAll();
            mainFrame.jPanel14.setLayout(new BoxLayout(mainFrame.jPanel14, BoxLayout.Y_AXIS));
            mainFrame.jPanel14.add(static_website);
            mainFrame.jPanel14.add(disable_website);
            mainFrame.jPanel14.add(enable_versioning);
            mainFrame.jPanel14.add(suspend_versioning);
            mainFrame.jPanel14.add(delete_bucket);
            mainFrame.jPanel14.add(blank_label);
            mainFrame.jPanel14.add(bucketACLbutton);
            mainFrame.jPanel14.add(close);
            mainFrame.jPanel14.repaint();
            mainFrame.jPanel14.revalidate();
            mainFrame.jPanel14.validate();

        } catch (Exception mp3player) {
            jTextArea1.append("\n" + mp3player.getMessage());
        }
        mainFrame.calibrateTextArea();

    }

    void startc() {
        (new Thread(new BucketACL(mainFrame))).start();
    }
}
