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

import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import static cloudExplorer.NewJFrame.jTextArea1;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ImageViewer implements Runnable {

    NewJFrame mainFrame;
    JLabel label;

    public ImageViewer(NewJFrame Frame) {
        mainFrame = Frame;
    }

    public void run() {
        try {

            String temp_file = (mainFrame.Home + File.separator + "object.tmp");

            for (int i = 1; i != mainFrame.previous_objectarray_length; i++) {
                if (mainFrame.object_item[i] != null) {
                    if (mainFrame.object_item[i].isSelected()) {
                        String new_object_name = mainFrame.convertObject(mainFrame.object_item[i].getText(), "download");
                        jTextArea1.setText("\n\nPlease wait, the image is loading.");
                        mainFrame.calibrateTextArea();
                        mainFrame.get = new Get(mainFrame.object_item[i].getText(), mainFrame.cred.access_key, mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), temp_file, null);
                        mainFrame.get.run();
                        BufferedImage imagee = ImageIO.read(new File(temp_file));
                        label = new JLabel(new ImageIcon(imagee));
                        label.revalidate();
                        label.validate();
                        label.repaint();
                        mainFrame.jPanel11.removeAll();
                        mainFrame.jPanel11.add(label);
                        mainFrame.jPanel11.repaint();
                        mainFrame.jPanel11.revalidate();
                        mainFrame.jPanel11.validate();
                        mainFrame.deleteFle(temp_file);
                        mainFrame.object_item[i].setSelected(false);
                        break;
                    }
                }
            }

            mainFrame.dialog.setVisible(false);
        } catch (Exception imageLoading) {
            jTextArea1.append("\n" + imageLoading.getMessage());
        }
        mainFrame.calibrateTextArea();

    }

    void startc() {
        (new Thread(new ImageViewer(mainFrame))).start();
    }
}
