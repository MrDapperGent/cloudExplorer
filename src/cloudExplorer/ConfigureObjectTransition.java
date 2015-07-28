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

import static cloudExplorer.NewJFrame.jTextArea1;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ConfigureObjectTransition implements Runnable {

    NewJFrame mainFrame;
    public static String object_acl_change = null;

    public ConfigureObjectTransition(NewJFrame Frame) {
        mainFrame = Frame;

    }

    public void run() {
        try {
            final JLabel label = new JLabel("Expire after # days:");
            final JLabel prefix = new JLabel("Prefix (Optional):");
            final JLabel blank = new JLabel(" ");
            final JTextField days = new JTextField("");
            final JTextField prefix_field = new JTextField("");
            final JButton commitTransition = new JButton("Commit");
            final JButton disableRules = new JButton("Disable Rules");
            final JCheckBox glacier = new JCheckBox("Tier to Glacier");

            days.setMaximumSize(new Dimension(300, 20));
            prefix_field.setMaximumSize(new Dimension(300, 20));

            days.setBackground(Color.white);
            days.setForeground(Color.BLUE);

            glacier.setBackground(Color.white);
            glacier.setForeground(Color.GRAY);

            label.setBackground(Color.white);
            label.setForeground(Color.GRAY);

            prefix.setBackground(Color.white);
            prefix.setForeground(Color.GRAY);

            commitTransition.setBackground(Color.white);
            commitTransition.setBorder(null);
            commitTransition.setForeground(Color.BLUE);

            disableRules.setBackground(Color.white);
            disableRules.setBorder(null);
            disableRules.setForeground(Color.BLUE);

            disableRules.setIcon(mainFrame.genericEngine);
            commitTransition.setIcon(mainFrame.genericEngine);
            mainFrame.jPanel15.setVisible(false);

            commitTransition.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        if (days.getText().contains("# of days") || days.getText().isEmpty()) {
                        } else {
                            if (glacier.isSelected()) {
                                BucketTransitionGlacier bucketTransition = new BucketTransitionGlacier(mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), days.getText(), prefix_field.getText(), false);
                                bucketTransition.startc(mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), days.getText(), prefix_field.getText(), false);

                            } else {
                                BucketTransition bucketTransition = new BucketTransition(mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), days.getText(), prefix_field.getText(), false);
                                bucketTransition.startc(mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), days.getText(), prefix_field.getText(), false);
                            }

                        }

                    } catch (Exception Transition) {
                        jTextArea1.append("\n" + Transition.getMessage() + "\n");
                    }

                    mainFrame.drawBuckets();
                    mainFrame.calibrateTextArea();
                    mainFrame.jPanel14.removeAll();
                    mainFrame.jPanel14.repaint();
                    mainFrame.jPanel14.revalidate();
                    mainFrame.jPanel14.validate();

                }
            });

            disableRules.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        BucketTransition bucketTransition = new BucketTransition(mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), days.getText(), prefix_field.getText(), true);
                        bucketTransition.startc(mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), days.getText(), prefix_field.getText(), true);

                    } catch (Exception Transition) {
                        jTextArea1.append("\n" + Transition.getMessage() + "\n");
                    }

                    mainFrame.calibrateTextArea();
                    mainFrame.jPanel14.removeAll();
                    mainFrame.jPanel14.repaint();
                    mainFrame.jPanel14.revalidate();
                    mainFrame.jPanel14.validate();
                    mainFrame.drawBuckets();

                }
            });

            mainFrame.jPanel14.removeAll();
            mainFrame.jPanel14.setLayout(new BoxLayout(mainFrame.jPanel14, BoxLayout.Y_AXIS));
            mainFrame.jPanel14.add(label);
            mainFrame.jPanel14.add(days);
            mainFrame.jPanel14.add(prefix);
            mainFrame.jPanel14.add(prefix_field);
            mainFrame.jPanel14.add(blank);
            mainFrame.jPanel14.add(glacier);
            mainFrame.jPanel14.add(commitTransition);
            mainFrame.jPanel14.add(disableRules);
            mainFrame.jPanel14.repaint();
            mainFrame.jPanel14.revalidate();
            mainFrame.jPanel14.validate();

        } catch (Exception mp3player) {
            jTextArea1.append("\n" + mp3player.getMessage());
        }
        mainFrame.calibrateTextArea();

    }

    void startc() {
        (new Thread(new ConfigureObjectTransition(mainFrame))).start();
    }
}
