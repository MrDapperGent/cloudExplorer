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
import com.googlecode.charts4j.AxisLabelsFactory;
import com.googlecode.charts4j.Color;
import com.googlecode.charts4j.Data;
import com.googlecode.charts4j.GCharts;
import com.googlecode.charts4j.Plot;
import com.googlecode.charts4j.Plots;
import com.googlecode.charts4j.XYLineChart;
import java.awt.GridLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.net.URL;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class PerformanceThread implements Runnable {

    String Home = System.getProperty("user.home");
    NewJFrame mainFrame;
    String output_log = Home + File.separator + "performance_results.csv";
    int threadcount;
    String getValue;
    String getOperationCount;
    Put put;
    Thread performancethread;
    String temp_file = (Home + File.separator + "object.tmp");
    String what = null;
    String access_key = null;
    String secret_key = null;
    String bucket = null;
    String endpoint = null;
    Boolean operation = true;
    Boolean graphdata = true;
    double[] x;
    double[] y;
    double[] x_latency;
    double[] y_latency;
    Get get;
    JLabel label;

    public void performance_logger(double time, double rate) {
        try {
            FileWriter frr = new FileWriter(output_log, true);
            BufferedWriter bfrr = new BufferedWriter(frr);
            bfrr.write("\n" + time + "," + rate);
            bfrr.close();
        } catch (Exception perf_logger) {
        }
    }

    public void calibrate() {
        try {
            jTextArea1.setCaretPosition(jTextArea1.getLineStartOffset(jTextArea1.getLineCount() - 1));
        } catch (Exception e) {
        }
    }

    PerformanceThread(int Athreadcount, String AgetValue, String AgetOperationCount, String Aaccess_key, String Asecret_key, String Abucket, String Aendpoint, Boolean Aoperation, Boolean Agraphdata) {
        threadcount = Athreadcount;
        getValue = AgetValue;
        getOperationCount = AgetOperationCount;
        access_key = Aaccess_key;
        secret_key = Asecret_key;
        bucket = Abucket;
        endpoint = Aendpoint;
        operation = Aoperation;
        graphdata = Agraphdata;
    }

    public void run() {

        File tempFile = new File(temp_file);
        File outputlog = new File(output_log);
        int op_count = Integer.parseInt(getOperationCount);
        int file_size = Integer.parseInt(getValue);
        float num_threads = threadcount;

        if (tempFile.exists()) {
            tempFile.delete();
        }

        if (outputlog.exists()) {
            outputlog.delete();
        }

        if (file_size > 0 && num_threads > 0 && op_count > 0) {

            try {
                FileOutputStream s = new FileOutputStream(temp_file);
                byte[] buf = new byte[file_size * 1024];
                s.write(buf);
                s.flush();
                s.close();
            } catch (Exception add) {
            }

            if (tempFile.exists()) {

                try {
                    String upload = tempFile.getAbsolutePath();
                    calibrate();

                    if (!operation) {
                        put = new Put(upload, access_key, secret_key, bucket, endpoint, "performance_test_data", false, false);
                        put.startc(upload, access_key, secret_key, bucket, endpoint, "performance_test_data", false, false);
                    }

                    x = new double[op_count];
                    y = new double[op_count];
                    x_latency = new double[op_count];
                    y_latency = new double[op_count];

                    int counter = 0;

                    for (int z = 0; z != op_count; z++) {
                        long t1 = System.currentTimeMillis();

                        for (int i = 0; i != num_threads; i++) {
                            if (operation) {
                                put = new Put(upload, access_key, secret_key, bucket, endpoint, "performance_test_data_" + i + "_" + z, false, false);
                                put.startc(upload, access_key, secret_key, bucket, endpoint, "performance_test_data_" + i + "_" + z, false, false);
                            } else {
                                get = new Get("performance_test_data", access_key, secret_key, bucket, endpoint, temp_file + i, null);
                                get.startc("performance_test_data", access_key, secret_key, bucket, endpoint, temp_file + i, null);
                            }
                        }

                        double t2 = System.currentTimeMillis();
                        double diff = t2 - t1;
                        double total_time = diff / 1000;

                        double rate = (num_threads * file_size / total_time / 1024);
                        rate = Math.round(rate * 100);
                        rate = rate / 100;

                        double iops = (num_threads / total_time);
                        iops = Math.round(iops * 100);
                        iops = iops / 100;
                        
                    
                        
                        NewJFrame.jTextArea1.append("\nOperation: " + z + ". Time: " + total_time + " seconds." + " Average speed with " + num_threads + " thread(s) is: " + rate + " MB/s. OPS/s: " + iops);
                        performance_logger(total_time, rate);
                        if (counter == 100) {
                            counter = 0;
                            x = new double[op_count];
                            y = new double[op_count];
                            x_latency = new double[op_count];
                            y_latency = new double[op_count];
                        }
                        y[counter] = (Double) rate;
                        x[counter] = counter;
                        y_latency[counter] = total_time;
                        x_latency[counter] = counter;

                        if (graphdata) {
                            graph();
                        }
                        counter++;
                        calibrate();
                    }

                } catch (Exception ex) {
                }

            } else {
                NewJFrame.jTextArea1.append("\n Please specifiy more than 0 threads.");
                calibrate();
            }

            NewJFrame.jTextArea1.append("\nResults saved in CSV format to: " + output_log);
            calibrate();
            NewJFrame.perf = false;

        } else {
            NewJFrame.jTextArea1.append("\nError: Thread and Count values must be greater than 0. Object Size value must be 1024 or greater.");
            calibrate();
        }
    }

    void graph() {

        try {

            //Configures the latency graph
            Data xdata_latency = new Data(x_latency);
            Data ydata_latency = new Data(y_latency);
            Plot plot_latency = Plots.newXYLine(xdata_latency, ydata_latency);
            plot_latency.setColor(Color.RED);
            XYLineChart xyLineChart_latency = GCharts.newXYLineChart(plot_latency);
            xyLineChart_latency.setSize(375, 300);
            xyLineChart_latency.setTitle("Latency Benchmarks");
            xyLineChart_latency.addXAxisLabels(AxisLabelsFactory.newAxisLabels(Arrays.asList("0", "Operations")));
            xyLineChart_latency.addYAxisLabels(AxisLabelsFactory.newAxisLabels(Arrays.asList("0", "Seconds")));
            JLabel label_latency = new JLabel(new ImageIcon(ImageIO.read(new URL(xyLineChart_latency.toURLString()))));

            //Configures the operations graph
            Data xdata = new Data(x);
            Data ydata = new Data(y);
            Plot plot = Plots.newXYLine(xdata, ydata);
            plot.setColor(Color.BLUE);
            XYLineChart xyLineChart = GCharts.newXYLineChart(plot);
            xyLineChart.setSize(375, 300);
            xyLineChart.setTitle("Performance Benchmarks");
            xyLineChart.addXAxisLabels(AxisLabelsFactory.newAxisLabels(Arrays.asList("0", "Operations")));
            xyLineChart.addYAxisLabels(AxisLabelsFactory.newAxisLabels(Arrays.asList("0", "MB/s")));
            label = new JLabel(new ImageIcon(ImageIO.read(new URL(xyLineChart.toURLString()))));

            //Configures the panel
            NewJFrame.jPanel11.removeAll();
            GridLayout layout = new GridLayout(0, 3);
            NewJFrame.jPanel11.setLayout(layout);
            NewJFrame.jPanel11.add(label);
            NewJFrame.jPanel11.add(label_latency);
            NewJFrame.jPanel11.revalidate();
            NewJFrame.jPanel11.repaint();
        } catch (Exception graph) {
        }
    }

    void stop() {
        performancethread.stop();
        NewJFrame.jTextArea1.append("\nTest aborted.");
        calibrate();
    }

    void startc(int Athreadcount, String AgetValue, String AgetOperationCount, String Aaccess_key, String Asecret_key, String Abucket, String Aendpoint, Boolean Aoperation, Boolean Agraphdata) {
        performancethread = new Thread(new PerformanceThread(Athreadcount, AgetValue, AgetOperationCount, Aaccess_key, Asecret_key, Abucket, Aendpoint, Aoperation, Agraphdata));
        performancethread.start();

    }

}
