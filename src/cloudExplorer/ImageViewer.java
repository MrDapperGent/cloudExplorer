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
import java.awt.Dimension;
import java.io.File;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import static cloudExplorer.NewJFrame.jTextArea1;

public class ImageViewer implements Runnable {

    NewJFrame mainFrame;

    public ImageViewer(NewJFrame Frame) {
        mainFrame = Frame;

    }

    public void run() {
        try {

            String temp_file = (mainFrame.Home + File.separator + "object.tmp");

            JLabel image[] = new JLabel[mainFrame.objectarray.length];
            ImageIcon[] photo = new ImageIcon[mainFrame.objectarray.length];
            JFrame image_frame = new JFrame();
            image_frame.setTitle("Cloud Explorer Image Viewer");
            image_frame.setIconImage(new ImageIcon(getClass()
                    .getResource("cloud.jpg")).getImage());
            JScrollBar bar = new JScrollBar(JScrollBar.VERTICAL);
            JPanel image_panel = new JPanel();
            image_panel.setBackground(Color.white);
            JScrollPane scrolling_pane = new JScrollPane(image_panel);
            image_frame.setSize(new Dimension(2000, 1000));
            image_frame.add(scrolling_pane);

            for (int i = 1; i != mainFrame.previous_objectarray_length; i++) {
                if (mainFrame.object_item[i].isSelected()) {
                    image_panel.setLayout(new BoxLayout(image_panel, BoxLayout.PAGE_AXIS));
                    String new_object_name = mainFrame.convertObject(mainFrame.object_item[i].getText(), "download");
                    jTextArea1.setText("\nPlease wait, the image is loading.");
                    mainFrame.calibrateTextArea();
                    mainFrame.get = new Get(mainFrame.object_item[i].getText(), mainFrame.cred.access_key, mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), temp_file + i, null);
                    mainFrame.get.run();
                    photo[i] = new ImageIcon(temp_file + i);
                    image[i] = new JLabel(photo[i]);
                    image_panel.add(image[i]);
                    image_frame.repaint();
                    image_frame.revalidate();
                    image_frame.validate();
                    mainFrame.deleteFle(temp_file + i);
                    image_frame.setVisible(true);
                    image_frame.setAlwaysOnTop(true);
                    mainFrame.object_item[i].setSelected(false);
                    break;
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
