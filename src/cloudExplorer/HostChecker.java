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

public class HostChecker implements Runnable {

    public static String host;
    NewJFrame mainFrame;

    public HostChecker(String Host, NewJFrame Frame) {
        host = Host;
        mainFrame = Frame;

    }

    public void run() {

        mainFrame.reloadBuckets();

    }

    void startc() {
        mainFrame.jPanel9.setVisible(true);
        mainFrame.jTextArea1.append("\nLoading configuration: " + mainFrame.jTextField3.getText());
        mainFrame.calibrateTextArea();
        (new Thread(new HostChecker(host, mainFrame))).start();
    }
}
