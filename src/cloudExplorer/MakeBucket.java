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

import static cloudExplorer.NewJFrame.jPanel15;
import static cloudExplorer.NewJFrame.jTextArea1;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class MakeBucket implements Runnable {

    NewJFrame mainFrame;
    public static String response = null;

    public MakeBucket(NewJFrame Frame) {
        mainFrame = Frame;

    }

    public void run() {
        try {
            final JButton createBucket = new JButton("Create Bucket");
            final JButton close = new JButton("Close");
            final JLabel blank = new JLabel(" ");
            final JLabel blank2 = new JLabel(" ");
            final JLabel blank3 = new JLabel(" ");
            final JTextField bucketName = new JTextField();
            final JLabel name = new JLabel("Bucket Name:");
            bucketName.setMaximumSize(new Dimension(200, 20));
            name.setBackground(Color.WHITE);
            name.setForeground(Color.GRAY);
            name.setFont(name.getFont().deriveFont(14.0f));
            createBucket.setBackground(Color.white);
            createBucket.setForeground(Color.BLUE);
            createBucket.setFont(createBucket.getFont().deriveFont(14.0f));
            createBucket.setBorder(null);
            close.setBackground(Color.white);
            close.setBorder(null);
            close.setForeground(Color.BLUE);
            close.setFont(close.getFont().deriveFont(14.0f));

            createBucket.setIcon(mainFrame.genericEngine);
            close.setIcon(mainFrame.genericEngine);

            jPanel15.setVisible(false);
            createBucket.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (bucketName.getText().length() < 3) {
                        close.doClick();
                    } else {
                        MakeBucketThread bt = new MakeBucketThread(mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), bucketName.getText().toLowerCase(), mainFrame.cred.getEndpoint(), mainFrame);
                        bt.startc(mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), bucketName.getText().toLowerCase(), mainFrame.cred.getEndpoint(), mainFrame);
                        close.doClick();
                    }
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
            mainFrame.jPanel14.add(name);
            mainFrame.jPanel14.add(bucketName);
            mainFrame.jPanel14.add(blank3);
            mainFrame.jPanel14.add(blank);
            mainFrame.jPanel14.add(createBucket);
            mainFrame.jPanel14.add(close);
            mainFrame.jPanel14.repaint();
            mainFrame.jPanel14.revalidate();
            mainFrame.jPanel14.validate();

        } catch (Exception makebucket) {
            jTextArea1.append("\n" + makebucket.getMessage());
        }
        mainFrame.calibrateTextArea();

    }

    void startc() {
        (new Thread(new MakeBucket(mainFrame))).start();
    }
}
