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
import static cloudExplorer.NewJFrame.jTextArea1;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class BucketACL implements Runnable {

    NewJFrame mainFrame;
    public static String object_acl_change = null;

    public BucketACL(NewJFrame Frame) {
        mainFrame = Frame;

    }

    public void refresh() {
        mainFrame.calibrateTextArea();
        //mainFrame.jPanel14.removeAll();
        mainFrame.jPanel14.repaint();
        mainFrame.jPanel14.revalidate();
        mainFrame.jPanel14.validate();
        mainFrame.jPanel9.setVisible(true);
    }

    public void run() {
        try {

            URL music_url = null;

            final JRadioButton static_website = new JRadioButton("Static Website");
            final JRadioButton refresh_bucket = new JRadioButton("Refresh Bucket");
            final JRadioButton disable_website = new JRadioButton("Disable Static Website");
            final JRadioButton enable_versioning = new JRadioButton("Enable Versioning");
            final JRadioButton suspend_versioning = new JRadioButton("Suspend Versioning");
            final JRadioButton delete_bucket = new JRadioButton("Delete Bucket");
            final JRadioButton share_button = new JRadioButton("Share bucket");
            final JLabel blank = new JLabel(" ");
            final JLabel blank_label = new JLabel(" ");

            refresh_bucket.setBackground(Color.white);
            refresh_bucket.setBorder(null);
            refresh_bucket.setForeground(Color.blue);

            share_button.setBackground(Color.white);
            share_button.setBorder(null);
            share_button.setForeground(Color.blue);

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

            enable_versioning.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (enable_versioning.isSelected()) {
                        jTextArea1.append(mainFrame.bucket.controlVersioning(mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), mainFrame.cred.getRegion(), true));
                        mainFrame.miniReload();
                        mainFrame.jPanel15.setVisible(false);
                        enable_versioning.setSelected(false);
                        refresh();
                    }
                }
            });

            refresh_bucket.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jTextArea1.append("\nRefreshing Bucket....");
                    NewJFrame.jButton6.doClick();
                }
            });

            share_button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    mainFrame.jPanel14.removeAll();
                    mainFrame.jPanel15.setVisible(false);
                    final JLabel canon_label = new JLabel("Canonical ID:");
                    final JLabel canon_blank = new JLabel(" ");
                    final JLabel canon_blank2 = new JLabel(" ");
                    final JButton canon_button = new JButton("Commit");
                    final JRadioButton ro = new JRadioButton("Read Only");
                    final JRadioButton full = new JRadioButton("Full Access");
                    final JRadioButton remove = new JRadioButton("Remove All Access");
                    final JTextField canon_field = new JTextField("");

                    canon_label.setBackground(Color.white);
                    canon_label.setBorder(null);
                    canon_label.setForeground(Color.blue);

                    canon_button.setBackground(Color.white);
                    canon_button.setBorder(null);
                    canon_button.setForeground(Color.blue);

                    ro.setBackground(Color.white);
                    ro.setBorder(null);
                    ro.setForeground(Color.blue);

                    full.setBackground(Color.white);
                    full.setBorder(null);
                    full.setForeground(Color.blue);

                    remove.setBackground(Color.white);
                    remove.setBorder(null);
                    remove.setForeground(Color.blue);

                    mainFrame.jPanel14.add(canon_label);
                    mainFrame.jPanel14.add(canon_field);
                    mainFrame.jPanel14.add(canon_blank2);
                    mainFrame.jPanel14.add(ro);
                    mainFrame.jPanel14.add(full);
                    mainFrame.jPanel14.add(remove);
                    mainFrame.jPanel14.add(canon_blank);
                    mainFrame.jPanel14.add(canon_button);
                    mainFrame.jPanel14.repaint();
                    mainFrame.jPanel14.revalidate();
                    mainFrame.jPanel14.validate();

                    canon_button.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            int operation = 0;
                            boolean proceed = false;

                            Acl change = new Acl();

                            if (full.isSelected()) {
                                operation = 1;
                                proceed = true;
                            }

                            if (ro.isSelected()) {
                                operation = 0;
                                proceed = true;
                            }

                            if (remove.isSelected()) {
                                operation = 3;
                                proceed = true;
                            }
                            mainFrame.jPanel9.setVisible(true);

                            if (proceed) {
                                change.setAccess(canon_field.getText(), operation, mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.cred.getEndpoint(), mainFrame.cred.getBucket());
                                mainFrame.jTextArea1.append("\nSent request to modify ACL. Please obeserve this window for any errors.");
                            }
                            mainFrame.reloadBuckets();
                            refresh();
                        }
                    });

                }
            });

            suspend_versioning.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (suspend_versioning.isSelected()) {
                        jTextArea1.append(mainFrame.bucket.controlVersioning(mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), mainFrame.cred.getRegion(), false));
                        suspend_versioning.setSelected(false);
                        refresh();
                    }
                }
            });

            disable_website.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (disable_website.isSelected()) {
                        mainFrame.objectacl.removeBUCKETwebsite(object_acl_change, mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.cred.getEndpoint(), mainFrame.cred.getBucket());
                        jTextArea1.append("\nBucket is no longer serving a website.\n");
                        disable_website.setSelected(false);
                        refresh();
                    }
                }
            });

            delete_bucket.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (delete_bucket.isSelected()) {
                        mainFrame.bucket_item[mainFrame.active_bucket].setSelected(false);
                        DeleteBucketThread delBucket = new DeleteBucketThread(mainFrame.cred.access_key, mainFrame.cred.secret_key, mainFrame.bucket_item[mainFrame.active_bucket].getText(), mainFrame.cred.end_point, mainFrame.cred.region, mainFrame);
                        delBucket.startc(mainFrame.cred.access_key, mainFrame.cred.secret_key, mainFrame.bucket_item[mainFrame.active_bucket].getText(), mainFrame.cred.end_point, mainFrame.cred.region, mainFrame);
                        delete_bucket.setSelected(false);
                        mainFrame.jPanel15.setVisible(false);
                        refresh();
                    }
                }
            });

            static_website.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (static_website.isSelected()) {
                        mainFrame.objectacl.setBUCKETwebsite(object_acl_change, mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.cred.getEndpoint(), mainFrame.cred.getBucket());
                        jTextArea1.append("\nWebsite access enabled.\n");
                        static_website.setSelected(false);
                        refresh();
                    }
                }
            });

            mainFrame.jPanel14.removeAll();
            mainFrame.jPanel14.setLayout(new BoxLayout(mainFrame.jPanel14, BoxLayout.Y_AXIS));
            mainFrame.jPanel14.add(refresh_bucket);
            mainFrame.jPanel14.add(static_website);
            mainFrame.jPanel14.add(disable_website);
            mainFrame.jPanel14.add(enable_versioning);
            mainFrame.jPanel14.add(suspend_versioning);
            mainFrame.jPanel14.add(delete_bucket);
            mainFrame.jPanel14.add(blank_label);
            mainFrame.jPanel14.add(share_button);
            mainFrame.jPanel14.add(blank_label);
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
