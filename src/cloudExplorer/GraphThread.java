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

import com.googlecode.charts4j.AxisLabelsFactory;
import com.googlecode.charts4j.Data;
import com.googlecode.charts4j.DataUtil;
import com.googlecode.charts4j.GCharts;
import com.googlecode.charts4j.Plot;
import com.googlecode.charts4j.Plots;
import com.googlecode.charts4j.ScatterPlot;
import com.googlecode.charts4j.XYLineChart;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class GraphThread implements Runnable {

    Thread getThread;
    NewJFrame mainFrame;
    String Home = System.getProperty("user.home");
    String[] object = new String[1];
    String temp_file = (Home + File.separator + "object.tmp");
    String what = null;
    Put put;
    double[] x;
    double[] y;
    String x_whattograph_field;
    String y_whattograph_field;
    String graph_name_field;
    String x_name_field;
    String y_name_field;
    String x_graphsize_field;
    String y_graphsize_field;
    File check_temp = new File(temp_file);
    Thread gt;
    Boolean first_pass = true;
    Boolean proceed = true;
    boolean line = true;
    ArrayList<Double> x_sort;
    ArrayList<Double> y_sort;
    ImageIcon throughput_icon;
    JLabel label_throughput;
    int inter;
    int stop_graphing = 475;

    public GraphThread(NewJFrame Frame, String Awhat, String Agraph_name_field, String xx_whattograph_field, String yy_whattograph_field, String xx_name_field, String yy_name_field, String xx_graphsize_field, String yy_graphsize_field, Boolean ALine, int Ainter) {
        mainFrame = Frame;
        what = Awhat;
        line = ALine;
        graph_name_field = Agraph_name_field;
        x_whattograph_field = xx_whattograph_field;
        y_whattograph_field = yy_whattograph_field;
        x_name_field = xx_name_field;
        y_name_field = yy_name_field;
        x_graphsize_field = xx_graphsize_field;
        y_graphsize_field = yy_graphsize_field;
        inter = Ainter;
    }

    void calibrateTextArea() {
        NewJFrame.jTextArea1.append("\n");
        try {
            NewJFrame.jTextArea1.setCaretPosition(NewJFrame.jTextArea1.getLineStartOffset(NewJFrame.jTextArea1.getLineCount() - 1));
        } catch (Exception e) {

        }
    }

    public void get_csv() {

        mainFrame.jTextArea1.append("\nDownloading data......");
        calibrateTextArea();
        File tempFile = new File(temp_file);
        Get get = new Get(what, mainFrame.cred.access_key, mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), temp_file, null);
        get.run();
    }

    void postsort() {
        Collections.sort(x_sort);
        Collections.sort(y_sort);
    }

    void process_data() {
        mainFrame.jTextArea1.append("\nProcessing every " + inter + " line(s) from the data file......");
        calibrateTextArea();
        int delimiter_conter = 1;

        try {
            FileReader frr = new FileReader(temp_file);
            BufferedReader bfrr = new BufferedReader(frr);
            String read = null;
            int i = 0;
            while ((read = bfrr.readLine()) != null) {
                String[] parse = read.split(",");
                if (parse[0].contains(":")) {
                    String[] cut = parse[0].split(":");
                    parse[0] = cut[0];
                }

                if (parse[1].contains(":")) {
                    String[] cut = parse[1].split(":");
                    parse[1] = cut[1];

                }
                if (delimiter_conter == inter) {
                    System.out.print("\nGraphinh");
                    x_sort.add(Double.parseDouble(parse[0]));
                    y_sort.add(Double.parseDouble(parse[1]));
                    delimiter_conter = 0;
                    postsort();
                    graph();
                }
                delimiter_conter++;
                i++;
            }
            bfrr.close();
        } catch (Exception tempFile) {
            proceed = false;
            //mainFrame.jTextArea1.append("\nError importing data. Please ensure the fields are correct.");
            calibrateTextArea();
        }

    }

    public void graph() {
        // mainFrame.jTextArea1.append("\nGraphing......");
        // calibrateTextArea();
        try {
            if (x_sort.get(0) >= x_sort.get(x_sort.size() - 1) || y_sort.get(0) >= y_sort.get(y_sort.size() - 1) || x_sort.size() > stop_graphing) {
            } else {
                // System.out.print("\nDebug: " + x_sort.get(0) + " " + x_sort.get(x_sort.size() - 1));
                // System.out.print("\nDebug: " + y_sort.get(0) + " " + y_sort.get(y_sort.size() - 1));
                Data xdata = DataUtil.scaleWithinRange(x_sort.get(0), x_sort.get(x_sort.size() - 1), x_sort);
                Data ydata = DataUtil.scaleWithinRange(y_sort.get(0), y_sort.get(y_sort.size() - 1), y_sort);
                Plot plot = Plots.newXYLine(xdata, ydata);
                plot.setColor(com.googlecode.charts4j.Color.BLUE);

                if (line) {
                    XYLineChart xyLineChart = GCharts.newXYLineChart(plot);
                    xyLineChart.setSize(Integer.parseInt(x_graphsize_field), Integer.parseInt(y_graphsize_field));
                    xyLineChart.setTitle(graph_name_field);
                    xyLineChart.addXAxisLabels(AxisLabelsFactory.newAxisLabels(Arrays.asList("", x_name_field)));
                    xyLineChart.addYAxisLabels(AxisLabelsFactory.newAxisLabels(Arrays.asList("", y_name_field)));
                    xyLineChart.addXAxisLabels(AxisLabelsFactory.newNumericRangeAxisLabels(x_sort.get(0), x_sort.get(x_sort.size() - 1)));
                    xyLineChart.addYAxisLabels(AxisLabelsFactory.newNumericRangeAxisLabels(y_sort.get(0), y_sort.get(y_sort.size() - 1)));
                    throughput_icon = (new ImageIcon(ImageIO.read(new URL(xyLineChart.toURLString()))));
                } else {
                    ScatterPlot Scatteredplot = GCharts.newScatterPlot(plot);
                    Scatteredplot.setSize(Integer.parseInt(x_graphsize_field), Integer.parseInt(y_graphsize_field));
                    Scatteredplot.setTitle(graph_name_field);
                    Scatteredplot.addXAxisLabels(AxisLabelsFactory.newAxisLabels(Arrays.asList("", x_name_field)));
                    Scatteredplot.addYAxisLabels(AxisLabelsFactory.newAxisLabels(Arrays.asList("", y_name_field)));
                    Scatteredplot.addXAxisLabels(AxisLabelsFactory.newNumericRangeAxisLabels(x_sort.get(0), x_sort.get(x_sort.size() - 1)));
                    Scatteredplot.addYAxisLabels(AxisLabelsFactory.newNumericRangeAxisLabels(y_sort.get(0), y_sort.get(y_sort.size() - 1)));
                    throughput_icon = (new ImageIcon(ImageIO.read(new URL(Scatteredplot.toURLString()))));
                }

                label_throughput = new JLabel(throughput_icon);

                //Configures the panel
                NewJFrame.jPanel11.removeAll();
                GridLayout layout = new GridLayout(0, 3);
                NewJFrame.jPanel11.setLayout(layout);

                NewJFrame.jPanel11.add(label_throughput);

                NewJFrame.jPanel11.revalidate();
                NewJFrame.jPanel11.repaint();
                System.gc();
            }

        } catch (Exception graph) {
            mainFrame.jTextArea1.append("\nError: " + graph.getMessage());
            calibrateTextArea();
            proceed = false;
        }
    }

    void write_graph() {
        try {
            Image image_throughput = throughput_icon.getImage();
            BufferedImage buffered_throughput_icon = (BufferedImage) image_throughput;
            File outputfile = new File(Home + File.separator + "GRAPH-" + graph_name_field + ".png");
            ImageIO.write(buffered_throughput_icon, "png", outputfile);
            if (outputfile.exists()) {
                mainFrame.jTextArea1.append("\nSaved graph to: " + Home + File.separator + "GRAPH-" + graph_name_field + ".png");
                calibrateTextArea();
            }
        } catch (Exception ex) {

        }
    }

    public void run() {
        File check_what = new File(what);

        x_sort = new ArrayList<Double>();
        y_sort = new ArrayList<Double>();

        if (check_temp.exists()) {
            check_temp.delete();
        }

        get_csv();

        if (check_temp.exists()) {
            process_data();
            write_graph();
        }
    }

    void startc(NewJFrame Frame, String Awhat, String Agraph_name_field, String xx_whattograph_field, String yy_whattograph_field, String xx_name_field, String yy_name_field, String xx_graphsize_field, String yy_graphsize_field, Boolean ALine, int Ainter) {
        {
            (new Thread(new GraphThread(Frame, Awhat, Agraph_name_field, xx_whattograph_field, yy_whattograph_field, xx_name_field, yy_name_field, xx_graphsize_field, yy_graphsize_field, ALine, Ainter))).start();
        }
    }
}
