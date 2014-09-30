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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import static cloudExplorer.NewJFrame.jTextArea1;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;

public class GetPerformance implements Runnable {

    NewJFrame mainFrame;
    Put put;
    Get get;
    GetPerformance getperformance;
    String Home = System.getProperty("user.home");
    String output_log = Home + File.separator + "performance_results.csv";

    public GetPerformance(NewJFrame Frame) {
        mainFrame = Frame;

    }

    public void performance_logger(float time, float rate) {
        try {
            FileWriter frr = new FileWriter(output_log, true);
            BufferedWriter bfrr = new BufferedWriter(frr);
            bfrr.write("\n" + time + "," + rate);
            bfrr.close();
        } catch (Exception perf_logger) {
        }
    }

    public void run() {
        try {

            final JButton startPerformanceTest = new JButton("Start Test");
            //final JButton abortPerformanceTest = new JButton("Abort");
            final JButton close = new JButton("Close");
            final JLabel fileSize = new JLabel("File Size in KB: ");
            final JLabel threadCount = new JLabel("Thread Count:");
            final JLabel operationCount = new JLabel("Operation Count:");
            final JLabel blank = new JLabel(" ");;
            final JTextField getFileSize = new JTextField();
            final JTextField getTheadCount = new JTextField("5");
            final JTextField getOperationCount = new JTextField("5");
            getFileSize.setText("1024");

            startPerformanceTest.setBackground(Color.white);
            startPerformanceTest.setForeground(Color.blue);
            //abortPerformanceTest.setBackground(Color.white);
            //abortPerformanceTest.setForeground(Color.blue);
            //abortPerformanceTest.setBorder(null);
            startPerformanceTest.setBorder(null);

            close.setBackground(Color.white);
            close.setBorder(null);
            close.setForeground(Color.blue);

            close.setIcon(mainFrame.genericEngine);
            //abortPerformanceTest.setIcon(mainFrame.genericEngine);
            startPerformanceTest.setIcon(mainFrame.genericEngine);

            startPerformanceTest.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    File file = new File(mainFrame.temp_file);
                    File check_results = new File(mainFrame.Home + File.separator + "perf.txt");
                    File outputlog = new File(output_log);

                    if (file.exists()) {
                        file.delete();
                    }

                    if (outputlog.exists()) {
                        outputlog.delete();
                    }

                    if (check_results.exists()) {
                        check_results.delete();
                    }

                    int file_size = 0;
                    float num_threads = num_threads = Integer.parseInt(getTheadCount.getText());

                    if (num_threads > 0) {

                        try {
                            mainFrame.jTextArea1.append("\nCreating creating temp file....");
                            mainFrame.calibrateTextArea();
                            String getValue = getFileSize.getText();
                            file_size = Integer.parseInt(getValue);
                            FileOutputStream s = new FileOutputStream(mainFrame.temp_file);
                            byte[] buf = new byte[file_size * 1024];
                            s.write(buf);
                            s.flush();
                            s.close();
                        } catch (Exception add) {
                        }

                        File tempFile = new File(mainFrame.temp_file);

                        if (tempFile.exists()) {

                            String upload = file.getAbsolutePath();
                            mainFrame.calibrateTextArea();

                            int op_count = Integer.parseInt(getOperationCount.getText());

                            mainFrame.put = new Put(upload, mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), "test_download", false, false);
                            mainFrame.put.startc(upload, mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), "test_download", false, false);

                            for (int z = 0; z != op_count; z++) {

                                for (int i = 0; i != num_threads; i++) {
                                    get = new Get(upload, mainFrame.cred.access_key, mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), mainFrame.temp_file + i, null);
                                    get.startc(upload, mainFrame.cred.access_key, mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), mainFrame.temp_file + i, null);
                                }

                                long total_time = 0;

                                if (check_results.exists()) {
                                    try {
                                        FileReader frr = new FileReader(check_results);
                                        BufferedReader bfrr = new BufferedReader(frr);
                                        String read = null;
                                        while ((read = bfrr.readLine()) != null) {
                                            if (read.length() > 0) {
                                                int translate = Integer.parseInt(read);
                                                total_time = total_time + translate;
                                            }
                                        }
                                    } catch (Exception ex) {
                                    }

                                }

                                for (int i = 0; i != num_threads; i++) {
                                    File delete = new File(mainFrame.temp_file + i);
                                    if (delete.exists()) {
                                        delete.delete();
                                    }
                                }

                                float float_file_size = file_size;
                                float rate = (num_threads * float_file_size / total_time / 1024);
                                mainFrame.jTextArea1.append("Operation: " + z + ". Time:" + total_time + " seconds." + " Average speed with " + num_threads + " threads is: " + rate + " MB/s");
                                performance_logger(total_time, rate);
                                mainFrame.calibrateTextArea();
                            }
                        }
                    } else {
                        mainFrame.jTextArea1.append("\n Please specifiy more than 0 threads.");
                        mainFrame.calibrateTextArea();
                    }

                    if (check_results.exists()) {
                        check_results.delete();
                    }

                    mainFrame.jTextArea1.append("Results saved in CSV format to: " + output_log);
                    mainFrame.calibrateTextArea();
                    NewJFrame.perf = false;
                }
            });

            /**
             * abortPerformanceTest.addActionListener(new ActionListener() {
             *
             * public void actionPerformed(ActionEvent e) {
             *
             * }
             * });
             *
             */
            close.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    mainFrame.jPanel14.removeAll();
                    mainFrame.jPanel14.repaint();
                    mainFrame.jPanel14.revalidate();
                    mainFrame.jPanel14.validate();
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
            mainFrame.jPanel14.add(blank);
            mainFrame.jPanel14.add(startPerformanceTest);
            // mainFrame.jPanel14.add(abortPerformanceTest);
            mainFrame.jPanel14.add(close);
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

    void startc() {
        (new Thread(new GetPerformance(mainFrame))).start();
    }
}
