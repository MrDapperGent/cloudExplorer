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
                    jTextArea1.setText("\nFile Name: " + mainFrame.object_item[i].getText());
                    jTextArea1.append("\nSize: " + mainFrame.bucket.getObjectInfo(mainFrame.object_item[i].getText(), mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.bucket_item[mainFrame.active_bucket].getText(), mainFrame.cred.getEndpoint(), "objectsize") + " Bytes");
                    jTextArea1.append("\nModified Date: " + mainFrame.bucket.getObjectInfo(mainFrame.object_item[i].getText(), mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.bucket_item[mainFrame.active_bucket].getText(), mainFrame.cred.getEndpoint(), "objectdate"));
                    break;
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
