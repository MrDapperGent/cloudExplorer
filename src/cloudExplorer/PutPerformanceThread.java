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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;

public class PutPerformanceThread implements Runnable {

    String Home = System.getProperty("user.home");
    NewJFrame mainFrame;
    String output_log = Home + File.separator + "performance_results.csv";
    int threadcount;
    String getValue;
    String getOperationCount;
    Put put;
    Thread performancethreadPUT;
    String temp_file = (Home + File.separator + "object.tmp");
    String what = null;
    String access_key = null;
    String secret_key = null;
    String bucket = null;
    String endpoint = null;

    public void performance_logger(float time, float rate) {
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

    PutPerformanceThread(int Athreadcount, String AgetValue, String AgetOperationCount, String Aaccess_key, String Asecret_key, String Abucket, String Aendpoint) {
        threadcount = Athreadcount;
        getValue = AgetValue;
        getOperationCount = AgetOperationCount;
        access_key = Aaccess_key;
        secret_key = Asecret_key;
        bucket = Abucket;
        endpoint = Aendpoint;
    }

    public void run() {

        File file = new File(temp_file);
        File check_results = new File(Home + File.separator + "perf.txt");
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
        float num_threads = threadcount;

        if (num_threads > 0) {

            try {
                NewJFrame.jTextArea1.append("\nCreating creating temp file....");
                calibrate();

                file_size = Integer.parseInt(getValue);
                FileOutputStream s = new FileOutputStream(temp_file);
                byte[] buf = new byte[file_size * 1024];
                s.write(buf);
                s.flush();
                s.close();
            } catch (Exception add) {
            }

            File tempFile = new File(temp_file);

            if (tempFile.exists()) {

                String upload = file.getAbsolutePath();
                calibrate();

                int op_count = Integer.parseInt(getOperationCount);
                for (int z = 0; z != op_count; z++) {

                    for (int i = 0; i != num_threads; i++) {
                        put = new Put(upload, access_key, secret_key, bucket, endpoint, "test_" + i, false, false);
                        put.startc(upload, access_key, secret_key, bucket, endpoint, "test_" + i, false, false);
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

                    float float_file_size = file_size;
                    float rate = (num_threads * float_file_size / total_time / 1024);
                    NewJFrame.jTextArea1.append("\nOperation: " + z + ". Time:" + total_time + " seconds." + " Average speed with " + num_threads + " threads is: " + rate + " MB/s");
                    performance_logger(total_time, rate);
                    calibrate();
                }
            }
        } else {
            NewJFrame.jTextArea1.append("\n Please specifiy more than 0 threads.");
            calibrate();
        }

        if (check_results.exists()) {
            check_results.delete();
        }

        NewJFrame.jTextArea1.append("\nResults saved in CSV format to: " + output_log);
        calibrate();
        NewJFrame.perf = false;

    }

    void startc(int Athreadcount, String AgetValue, String AgetOperationCount, String Aaccess_key, String Asecret_key, String Abucket, String Aendpoint) {
        performancethreadPUT = new Thread(new PutPerformanceThread(Athreadcount, AgetValue, AgetOperationCount, Aaccess_key, Asecret_key, Abucket, Aendpoint));
        performancethreadPUT.start();

    }

}
