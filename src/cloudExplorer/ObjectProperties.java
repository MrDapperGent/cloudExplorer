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

import static cloudExplorer.NewJFrame.jTextArea1;

public class ObjectProperties implements Runnable {

    NewJFrame mainFrame;

    public ObjectProperties(NewJFrame Frame) {
        mainFrame = Frame;

    }

    public void run() {
        try {
            for (int i = 1; i != mainFrame.previous_objectarray_length; i++) {

                if (mainFrame.object_item[i].isSelected()) {
                    mainFrame.object_item[i].setText(mainFrame.object_item[i].getText() + "   File Size: " + mainFrame.bucket.getObjectInfo(mainFrame.object_item[i].getText(), mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.bucket_item[mainFrame.active_bucket].getText(), mainFrame.cred.getEndpoint(), "objectsize") + " Bytes"
                            + "   Modified Date: " + mainFrame.bucket.getObjectInfo(mainFrame.object_item[i].getText(), mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.bucket_item[mainFrame.active_bucket].getText(), mainFrame.cred.getEndpoint(), "objectdate"));
                    mainFrame.jButton1.setEnabled(false);
                    mainFrame.jButton3.setEnabled(false);
                    mainFrame.jButton4.setEnabled(false);
                    mainFrame.jButton7.setEnabled(false);
                    mainFrame.jButton12.setEnabled(false);
                    mainFrame.jButton13.setEnabled(false);
                    mainFrame.jButton17.setEnabled(false);
                    mainFrame.jButton18.setEnabled(false);
                    mainFrame.jButton19.setEnabled(false);
                }
            }
        } catch (Exception ObjectACL) {
            jTextArea1.append("\n" + ObjectACL.getMessage() + "\n");
            mainFrame.calibrateTextArea();
        }
    }

    void startc() {
        (new Thread(new ObjectProperties(mainFrame))).start();
    }
}
