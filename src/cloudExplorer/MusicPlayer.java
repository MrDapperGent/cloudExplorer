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

import jaco.mp3.player.MP3Player;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class MusicPlayer implements Runnable {

    NewJFrame mainFrame;
    AudioInputStream audioIn;
    Clip clip;
    Boolean wav = false;

    public MusicPlayer(NewJFrame Frame) {
        mainFrame = Frame;
    }

    public void run() {
        try {

            URL music_url = null;

            final MP3Player mp3 = new MP3Player();

            final JButton stopMusic = new JButton("Stop / Close");
            final JButton replayMusic = new JButton("Play/Replay");
            final JButton forwardMusic = new JButton("Forward");
            final JButton backwardMusic = new JButton("Backward");

            stopMusic.setBackground(Color.white);
            stopMusic.setForeground(Color.blue);
            stopMusic.setBorder(null);
            replayMusic.setBackground(Color.white);
            replayMusic.setBorder(null);
            replayMusic.setForeground(Color.blue);
            backwardMusic.setBackground(Color.white);
            backwardMusic.setBorder(null);
            backwardMusic.setForeground(Color.blue);
            forwardMusic.setBackground(Color.white);
            forwardMusic.setBorder(null);
            forwardMusic.setForeground(Color.blue);

            ImageIcon play = new ImageIcon(
                    getClass().getResource("Actions-media-playback-start-icon.png"));
            replayMusic.setIcon(play);

            ImageIcon stop = new ImageIcon(
                    getClass().getResource("Actions-process-stop-icon.png"));
            stopMusic.setIcon(stop);

            ImageIcon forward = new ImageIcon(
                    getClass().getResource("Actions-media-seek-forward-icon.png"));
            forwardMusic.setIcon(forward);

            ImageIcon rewind = new ImageIcon(
                    getClass().getResource("Actions-media-seek-backward-icon.png"));
            backwardMusic.setIcon(rewind);

            mainFrame.jPanel15.setVisible(false);
            forwardMusic.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    mp3.skipForward();
                }
            });

            backwardMusic.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    mp3.skipBackward();
                }
            });

            stopMusic.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (wav) {
                        clip.stop();
                    } else {
                        mp3.stop();
                    }
                    mainFrame.jButton17.setEnabled(true);
                    mainFrame.drawBuckets();
                }
            });
            replayMusic.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    mp3.stop();
                    mp3.play();
                    mainFrame.jButton17.setEnabled(false);
                }
            });

            int count = 0;
            for (int h = 1; h != mainFrame.previous_objectarray_length; h++) {
                if (mainFrame.object_item[h] != null) {
                    if (mainFrame.object_item[h].isSelected()) {
                        if (mainFrame.object_item[h].getText().toLowerCase().contains(".mp3")) {
                            String url = mainFrame.objectacl.setACLurl(mainFrame.object_item[h].getText(), mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.cred.getEndpoint(), mainFrame.cred.getBucket());
                            url = url.replace("Pre-Signed URL = ", "");
                            music_url = (new URL(url));
                            mp3.addToPlayList(music_url);
                            wav = false;
                            count++;
                        }

                        if (mainFrame.object_item[h].getText().toLowerCase().contains(".wav")) {
                            wav = true;
                            File temp_file = new File(mainFrame.temp_file);
                            NewJFrame.jTextArea1.append("\nDownloading file to play.");
                            mainFrame.jPanel9.setVisible(true);
                            Get get = new Get(mainFrame.object_item[h].getText(), mainFrame.cred.access_key, mainFrame.cred.secret_key, mainFrame.cred.bucket, mainFrame.cred.getEndpoint(), mainFrame.temp_file, null);
                            get.run();
                            audioIn = AudioSystem.getAudioInputStream(temp_file);
                            clip = AudioSystem.getClip();
                            clip.open(audioIn);
                            clip.start();
                            count++;
                        }

                        if (count == 1) {
                            mainFrame.jPanel14.removeAll();
                            mainFrame.jPanel14.setLayout(new BoxLayout(mainFrame.jPanel14, BoxLayout.Y_AXIS));
                            mainFrame.jPanel14.add(replayMusic);
                            if (!wav) {
                                mainFrame.jPanel14.add(forwardMusic);
                                mainFrame.jPanel14.add(backwardMusic);
                            }
                            mainFrame.jPanel14.add(stopMusic);
                            mainFrame.jPanel14.repaint();
                            mainFrame.jPanel14.revalidate();
                            mainFrame.jPanel14.validate();
                            mainFrame.jButton17.setEnabled(false);
                            if (wav) {
                            } else {
                                mp3.play();
                            }
                        }
                    }
                }
            }

        } catch (Exception mp3player) {
            NewJFrame.jTextArea1.append("\n" + mp3player.getMessage());
        }
        mainFrame.calibrateTextArea();

    }

    void startc() {
        (new Thread(new MusicPlayer(mainFrame))).start();
    }
}
