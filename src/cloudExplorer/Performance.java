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

import static cloudExplorer.NewJFrame.jTextArea1;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class Performance implements Runnable {
    
    NewJFrame mainFrame;
    Put put;
    Performance putperformance;
    Boolean operation = true;
    PerformanceThread performancethread;
    JList jlist_perf = mainFrame.jList2;
    JScrollPane pane_perf = new JScrollPane();
    JPanel jlist_panel = new JPanel();
    JLabel jlist_label = new JLabel("Destination Folder:");
    final JButton startPerformanceTest = new JButton("Start Test");
    final JButton abortPerformanceTest = new JButton("Close / Abort");
    final JButton close = new JButton("Close");
    JLabel title = new JLabel("Performance Testing    ");
    final JLabel fileSize = new JLabel("Object Size in KB: ");
    final JLabel threadCount = new JLabel("Thread Count:");
    final JLabel operationCount = new JLabel("Operation Count:");
    final JCheckBox latency_graph = new JCheckBox("Graph Latency ");
    final JCheckBox ops_graph = new JCheckBox("Graph OP/s ");
    final JCheckBox overwrite_put = new JCheckBox("Overwrite PUTS");
    final JCheckBox throughput_graph = new JCheckBox("Graph Throughput");
    final JLabel blank = new JLabel(" ");
    final JLabel blank2 = new JLabel(" ");
    final JLabel blank3 = new JLabel(" ");
    final JLabel blank4 = new JLabel(" ");
    final JTextField getFileSize = new JTextField("1024");
    final JTextField getTheadCount = new JTextField("5");
    final JTextField getOperationCount = new JTextField("5");
    Performance performance;
    Boolean graph_latency = false;
    Boolean graph_throughput = false;
    Boolean graph_ops = false;
    int num_graphs = 0;
    Boolean mixed = false;
    Boolean overwrite = false;
    Boolean mixed_traffic = false;
    
    public Performance(NewJFrame Frame, Boolean Aoperation, Boolean Amixed) {
        mainFrame = Frame;
        operation = Aoperation;
        mixed_traffic = Amixed;
    }
    
    public void run() {
        try {
            
            getFileSize.setMaximumSize(new Dimension(220, 20));
            getTheadCount.setMaximumSize(new Dimension(220, 20));
            getOperationCount.setMaximumSize(new Dimension(220, 20));
            latency_graph.setBackground(Color.white);
            latency_graph.setForeground(Color.blue);
            latency_graph.setSelected(false);
            latency_graph.setBorder(null);
            ops_graph.setBackground(Color.white);
            ops_graph.setForeground(Color.blue);
            ops_graph.setSelected(false);
            ops_graph.setBorder(null);
            throughput_graph.setBackground(Color.white);
            throughput_graph.setForeground(Color.blue);
            throughput_graph.setSelected(false);
            throughput_graph.setBorder(null);
            title.setForeground(Color.RED);
            overwrite_put.setBackground(Color.white);
            overwrite_put.setForeground(Color.blue);
            overwrite_put.setSelected(false);
            overwrite_put.setBorder(null);
            
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
            
            jlist_perf.setModel(new javax.swing.AbstractListModel() {
                
                public int getSize() {
                    return mainFrame.folders2.length;
                }
                
                public Object getElementAt(int i) {
                    return mainFrame.folders2[i];
                }
            });
            pane_perf = new JScrollPane(jlist_perf);
            startPerformanceTest.addActionListener(new ActionListener() {
                
                public void actionPerformed(ActionEvent e) {
                    startPerformanceTest.setVisible(false);
                    Boolean graphData = false;
                    
                    NewJFrame.jPanel11.removeAll();
                    NewJFrame.jPanel11.revalidate();
                    NewJFrame.jPanel11.repaint();
                    
                    num_graphs = 0;
                    
                    if (overwrite_put.isSelected()) {
                        overwrite = true;
                    } else {
                        overwrite = false;
                    }
                    if (latency_graph.isSelected() || throughput_graph.isSelected() || ops_graph.isSelected()) {
                        graphData = true;
                        
                        if (latency_graph.isSelected()) {
                            graph_latency = true;
                            num_graphs++;
                            
                        }
                        if (throughput_graph.isSelected()) {
                            graph_throughput = true;
                            num_graphs++;
                        }
                        if (ops_graph.isSelected()) {
                            graph_ops = true;
                            num_graphs++;
                        }
                    }
                    mainFrame.perf = true;
                    mainFrame.jPanel9.setVisible(true);
                    mainFrame.jTextArea1.append("\nStarting test. Please wait.");
                    mainFrame.calibrateTextArea();
                    int threadcount = Integer.parseInt(getTheadCount.getText());
                    String getValue = getFileSize.getText();
                    String operationCount = getOperationCount.getText();
                    int index = jlist_perf.getSelectedIndex(); //get selected index
                    String folder = null;
                    try {
                        if (index != -1) {
                            folder = jlist_perf.getSelectedValue().toString();
                        }
                    } catch (Exception indaex) {
                    }
                    performancethread = new PerformanceThread(startPerformanceTest, threadcount, getValue, operationCount, mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), operation, graphData, graph_throughput, graph_latency, graph_ops, num_graphs, mixed_traffic, overwrite, folder);
                    performancethread.startc(startPerformanceTest, threadcount, getValue, operationCount, mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), operation, graphData, graph_throughput, graph_latency, graph_ops, num_graphs, mixed_traffic, overwrite, folder);
                    graph_ops = false;
                    graph_throughput = false;
                    graph_latency = false;
                    num_graphs = 0;
                }
            });
            
            abortPerformanceTest.addActionListener(new ActionListener() {
                
                public void actionPerformed(ActionEvent e) {
                    try {
                        performancethread.stop();
                    } catch (Exception stopPerf) {
                    }
                    mainFrame.jPanel14.removeAll();
                    mainFrame.jPanel14.repaint();
                    mainFrame.jPanel14.revalidate();
                    mainFrame.jPanel14.validate();
                    graph_ops = false;
                    graph_throughput = false;
                    graph_latency = false;
                    num_graphs = 0;
                    mainFrame.reloadBuckets();
                }
            });
            
            mainFrame.jPanel14.removeAll();
            mainFrame.jPanel14.setLayout(new BoxLayout(mainFrame.jPanel14, BoxLayout.Y_AXIS));
            mainFrame.jPanel14.add(title);
            mainFrame.jPanel14.add(blank4);
            mainFrame.jPanel14.add(fileSize);
            mainFrame.jPanel14.add(getFileSize);
            mainFrame.jPanel14.add(threadCount);
            mainFrame.jPanel14.add(getTheadCount);
            mainFrame.jPanel14.add(operationCount);
            mainFrame.jPanel14.add(getOperationCount);
            mainFrame.jPanel14.add(blank2);
            pane_perf.setPreferredSize(new Dimension(75, 50));
            jlist_perf.setPreferredSize(new Dimension(75, 50));
            mainFrame.jPanel14.add(jlist_label);
            mainFrame.jPanel14.add(pane_perf);
            mainFrame.jPanel14.add(blank3);
            if (!mixed_traffic) {
                mainFrame.jPanel14.add(throughput_graph);
                mainFrame.jPanel14.add(ops_graph);
                mainFrame.jPanel14.add(latency_graph);
            }
            if (operation) {
                mainFrame.jPanel14.add(overwrite_put);
            }
            mainFrame.jPanel14.add(blank);
            mainFrame.jPanel14.add(startPerformanceTest);
            mainFrame.jPanel14.add(abortPerformanceTest);
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
    
    void startc(NewJFrame mainFrame, boolean Aoperation, boolean Amixed) {
        (new Thread(new Performance(mainFrame, Aoperation, Amixed))).start();
    }
}
