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

public class AddExternalBucket implements Runnable {

    NewJFrame mainFrame;
    public static String response = null;
    public static String region = null;

    public AddExternalBucket(NewJFrame Frame) {
        mainFrame = Frame;

    }

    public void run() {
        try {
            final JButton createBucket = new JButton("Add Bucket");
            final JButton close = new JButton("Close");
            final JLabel blank = new JLabel(" ");
            final JLabel blank2 = new JLabel(" ");
            final JLabel blank3 = new JLabel(" ");
            final JTextField bucketName = new JTextField();
            final JTextField regionName = new JTextField(mainFrame.cred.getRegion());
            final JLabel name = new JLabel("Bucket Name:");
            final JLabel region_name = new JLabel("Region Name:");
            bucketName.setMaximumSize(new Dimension(200, 20));
            regionName.setMaximumSize(new Dimension(200, 20));
            name.setBackground(Color.WHITE);
            name.setForeground(Color.GRAY);
            region_name.setBackground(Color.white);
            region_name.setForeground(Color.GREEN);
            createBucket.setBackground(Color.white);
            createBucket.setForeground(Color.GREEN);
            createBucket.setBorder(null);
            close.setBackground(Color.white);
            close.setBorder(null);
            close.setForeground(Color.GREEN);

            createBucket.setIcon(mainFrame.genericEngine);
            close.setIcon(mainFrame.genericEngine);

            jPanel15.setVisible(false);
            createBucket.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (bucketName.getText().length() < 3) {
                        close.doClick();
                    } else {
                        Boolean proceed = true;
                        String[] backup = new String[mainFrame.bucketarray.length + 1];
                        for (int i = 0; i != mainFrame.bucketarray.length; i++) {
                            backup[i] = mainFrame.bucketarray[i];
                        }
                        backup[backup.length - 1] = bucketName.getText();
                        mainFrame.bucketarray = backup;
                        proceed = false;
                        mainFrame.drawBuckets();
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
            //mainFrame.jPanel14.add(blank3);
            // mainFrame.jPanel14.add(region_name);
            // mainFrame.jPanel14.add(regionName);
            mainFrame.jPanel14.add(blank);
            mainFrame.jPanel14.add(createBucket);
            mainFrame.jPanel14.add(blank2);
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
        (new Thread(new AddExternalBucket(mainFrame))).start();
    }
}
