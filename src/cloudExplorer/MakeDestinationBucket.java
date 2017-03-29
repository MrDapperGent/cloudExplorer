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

public class MakeDestinationBucket implements Runnable {

    NewJFrame mainFrame;
    public static String response = null;
    public static String region = null;
    public static String dest_bucket = null;
    BucketMigrationCLI migrate;
    String Home = System.getProperty("user.home");
    String config_file = (Home + File.separator + "s3Migrate.config");
    String active_folder = null;

    public MakeDestinationBucket(NewJFrame Frame) {
        mainFrame = Frame;
    }

    public void run() {
        try {
            final JButton startMigration = new JButton("Start Migration");
            final JButton close = new JButton("Stop/Close");
            final JLabel blank = new JLabel(" ");
            final JLabel blank2 = new JLabel(" ");
            final JLabel blank3 = new JLabel(" ");
            startMigration.setBackground(Color.white);
            startMigration.setForeground(Color.BLUE);
            startMigration.setFont(startMigration.getFont().deriveFont(14.0f));
            startMigration.setBorder(null);
            close.setBackground(Color.white);
            close.setBorder(null);
            close.setForeground(Color.BLUE);
            close.setFont(close.getFont().deriveFont(14.0f));
            close.setIcon(mainFrame.genericEngine);
            startMigration.setIcon(mainFrame.genericEngine);

            mainFrame.jPanel15.setVisible(false);

            startMigration.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    jTextArea1.setText("\nBucket migration has started. Please view this window for any errors.");
                    calibrate();
                    ReloadObjects object = new ReloadObjects(mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.bucket_item[mainFrame.active_bucket].getText(), mainFrame.cred.getEndpoint(), mainFrame);
                    object.run();
                    migrate = new BucketMigrationCLI(mainFrame.cred.access_key, mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), mainFrame.objectarray, false, mainFrame.snap_folder, false, false);
                    migrate.startc(mainFrame.cred.access_key, mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), mainFrame.objectarray, false, mainFrame.snap_folder, false, false);
                }
            });

            close.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    migrate.stop();
                    mainFrame.jPanel14.removeAll();
                    mainFrame.jPanel14.repaint();
                    mainFrame.jPanel14.revalidate();
                    mainFrame.jPanel14.validate();
                    mainFrame.drawBuckets();
                }
            });

            mainFrame.jPanel14.removeAll();
            mainFrame.jPanel14.setLayout(new BoxLayout(mainFrame.jPanel14, BoxLayout.Y_AXIS));
            mainFrame.jPanel14.add(blank);
            mainFrame.jPanel14.add(startMigration);
            mainFrame.jPanel14.add(close);
            mainFrame.jPanel14.repaint();
            mainFrame.jPanel14.revalidate();
            mainFrame.jPanel14.validate();
        } catch (Exception migration) {
            jTextArea1.append("\n" + migration.getMessage());
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
