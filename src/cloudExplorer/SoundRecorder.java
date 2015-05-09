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
import java.io.File;
import java.util.Random;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class SoundRecorder implements Runnable {

    NewJFrame mainFrame;
    public static String response = null;
    public static String region = null;
    Thread soundRecordThread;
    Random rand = new Random(System.currentTimeMillis());
    int random = rand.nextInt(256);
    File temp;

    public SoundRecorder(NewJFrame Frame) {
        mainFrame = Frame;
        temp = new File(mainFrame.temp_file);
    }

    public void run() {
        try {
            final JButton soundThread = new JButton("Record Sound");
            final JButton close = new JButton("Stop / Close");
            final JLabel blank = new JLabel(" ");
            final JLabel blank2 = new JLabel(" ");
            final JLabel blank3 = new JLabel(" ");
            final JTextField audioName = new JTextField("AudioRecording-" + random + ".wav");
            soundThread.setBackground(Color.white);
            soundThread.setForeground(Color.blue);
            soundThread.setBorder(null);
            close.setBackground(Color.white);
            close.setBorder(null);
            close.setForeground(Color.blue);
            audioName.setMaximumSize(new Dimension(325, 20));
            soundThread.setIcon(mainFrame.genericEngine);
            close.setIcon(mainFrame.genericEngine);
            jPanel15.setVisible(false);

            if (temp.exists()) {
                temp.delete();
            }

            soundThread.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    soundRecordThread = new Thread(new SoundRecorderThread(mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), mainFrame));
                    soundRecordThread.start();
                }
            });

            close.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        soundRecordThread.stop();
                    } catch (Exception stopThread) {
                    }

                    if (temp.exists()) {
                        Thread put = new Thread(new Put(mainFrame.temp_file, mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), audioName.getText(), false, false));
                        NewJFrame.jTextArea1.append("\nRecording has finished. Uploading file.");
                        mainFrame.calibrateTextArea();
                        put.start();
                    }
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
            mainFrame.jPanel14.add(audioName);
            mainFrame.jPanel14.add(blank);
            mainFrame.jPanel14.add(soundThread);
            mainFrame.jPanel14.add(blank2);
            mainFrame.jPanel14.add(close);
            mainFrame.jPanel14.repaint();
            mainFrame.jPanel14.revalidate();
            mainFrame.jPanel14.validate();

        } catch (Exception recordmessage) {
            jTextArea1.append("\n" + recordmessage.getMessage());
        }
        mainFrame.calibrateTextArea();
    }

    void startc() {
        (new Thread(new SoundRecorder(mainFrame))).start();
    }
}
