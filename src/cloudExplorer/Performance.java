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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Performance implements Runnable {

    NewJFrame mainFrame;
    Put put;
    Performance putperformance;
    Boolean operation = true;
    PerformanceThread performancethread;

    public Performance(NewJFrame Frame, Boolean Aoperation) {
        mainFrame = Frame;
        operation = Aoperation;

    }

    public void run() {
        try {

            final JButton startPerformanceTest = new JButton("Start Test");
            final JButton abortPerformanceTest = new JButton("Abort");
            final JButton close = new JButton("Close");
            final JLabel fileSize = new JLabel("Object Size in KB: ");
            final JLabel threadCount = new JLabel("Thread Count:");
            final JLabel operationCount = new JLabel("Operation Count:");
            final JCheckBox graph = new JCheckBox("Graph");
            final JLabel blank = new JLabel(" ");
            final JLabel blank2 = new JLabel(" ");
            final JTextField getFileSize = new JTextField("1024");
            final JTextField getTheadCount = new JTextField("5");
            final JTextField getOperationCount = new JTextField("5");
            getFileSize.setMaximumSize(new Dimension(220, 20));
            getTheadCount.setMaximumSize(new Dimension(220, 20));
            getOperationCount.setMaximumSize(new Dimension(220, 20));
            graph.setBackground(Color.white);
            graph.setForeground(Color.blue);
            graph.setSelected(false);
            graph.setBorder(null);
            startPerformanceTest.setBackground(Color.white);
            startPerformanceTest.setForeground(Color.blue);
            abortPerformanceTest.setBackground(Color.white);
            abortPerformanceTest.setForeground(Color.blue);
            abortPerformanceTest.setBorder(null);
            startPerformanceTest.setBorder(null);

            close.setBackground(Color.white);
            close.setBorder(null);
            close.setForeground(Color.blue);

            close.setIcon(mainFrame.genericEngine);
            abortPerformanceTest.setIcon(mainFrame.genericEngine);
            startPerformanceTest.setIcon(mainFrame.genericEngine);

            mainFrame.jPanel15.setVisible(false);

            startPerformanceTest.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    Boolean graphData = false;

                    NewJFrame.jPanel11.removeAll();
                    NewJFrame.jPanel11.revalidate();
                    NewJFrame.jPanel11.repaint();

                    if (graph.isSelected()) {
                        graphData = true;
                    }
                    mainFrame.perf = true;
                    mainFrame.jTextArea1.append("\nStarting test. Please wait.");
                    mainFrame.calibrateTextArea();
                    int threadcount = Integer.parseInt(getTheadCount.getText());
                    String getValue = getFileSize.getText();
                    String operationCount = getOperationCount.getText();
                    performancethread = new PerformanceThread(threadcount, getValue, operationCount, mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), operation, graphData);
                    performancethread.startc(threadcount, getValue, operationCount, mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), operation, graphData);
                }
            });

            abortPerformanceTest.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    performancethread.stop();
                    mainFrame.jPanel14.removeAll();
                    mainFrame.jPanel14.repaint();
                    mainFrame.jPanel14.revalidate();
                    mainFrame.jPanel14.validate();
                    mainFrame.miniReload();
                }
            });

            close.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    mainFrame.jPanel14.removeAll();
                    mainFrame.jPanel14.repaint();
                    mainFrame.jPanel14.revalidate();
                    mainFrame.jPanel14.validate();
                    mainFrame.miniReload();
                }
            });

            mainFrame.jPanel14.removeAll();
            mainFrame.jPanel14.setLayout(new BoxLayout(mainFrame.jPanel14, BoxLayout.Y_AXIS));
            mainFrame.jPanel14.add(fileSize);
            mainFrame.jPanel14.add(getFileSize);
            mainFrame.jPanel14.add(threadCount);
            mainFrame.jPanel14.add(getTheadCount);
            mainFrame.jPanel14.add(operationCount);
            mainFrame.jPanel14.add(getOperationCount);
            mainFrame.jPanel14.add(blank2);
            mainFrame.jPanel14.add(graph);
            mainFrame.jPanel14.add(blank);
            mainFrame.jPanel14.add(startPerformanceTest);
            mainFrame.jPanel14.add(abortPerformanceTest);

            // mainFrame.jPanel14.add(close);
            mainFrame.jPanel14.repaint();
            mainFrame.jPanel14.revalidate();
            mainFrame.jPanel14.validate();

        } catch (Exception mp3player) {
            jTextArea1.append("\n" + mp3player.getMessage());
        }
        mainFrame.calibrateTextArea();

    }

    public void calibrate() {
        try {
            jTextArea1.setCaretPosition(jTextArea1.getLineStartOffset(jTextArea1.getLineCount() - 1));
        } catch (Exception e) {
        }
    }

    void startc(NewJFrame mainFrame, boolean Aoperation) {
        (new Thread(new Performance(mainFrame, Aoperation))).start();
    }
}
