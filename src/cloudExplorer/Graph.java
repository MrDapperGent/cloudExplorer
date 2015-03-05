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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Graph implements Runnable {

    Thread getThread;
    NewJFrame mainFrame;
    String Home = System.getProperty("user.home");
    String[] object = new String[1];
    String temp_file = (Home + File.separator + "object.tmp");
    String what = null;
    public static final JButton save = new JButton("Graph");
    final JButton close = new JButton("Close");
    final JLabel intro_label_main = new JLabel("Upon clicking the graph button on the bottom left, the selected CSV file will be");
    final JLabel intro_label_main_two = new JLabel("processed and saved to your bucket and computer. There is a limitation of 475 plots.");
    final JLabel intro_label = new JLabel("Graphing controls:");
    final JLabel graph_name = new JLabel("Name:");
    final JLabel x_name = new JLabel("X-axis name:");
    final JLabel x_whattograph = new JLabel("X column # in CSV:");
    final JLabel y_whattograph = new JLabel("Y column # in CSV:");
    final JLabel graph_size_x = new JLabel("Horizontal Graph Size: ");
    final JLabel graph_size_y = new JLabel("Vertical Graph Size:");
    final JLabel interations = new JLabel("Plot every # of lines:");
    final JLabel blank = new JLabel(" ");
    final JLabel blank2 = new JLabel(" ");
    final JLabel blank3 = new JLabel(" ");
    final JLabel blank4 = new JLabel(" ");
    final JLabel blank5 = new JLabel(" ");
    final JLabel blank6 = new JLabel(" ");
    final JLabel blank7 = new JLabel(" ");
    final JLabel blank8 = new JLabel(" ");
    final JLabel blank9 = new JLabel(" ");
    final JLabel blank10 = new JLabel(" ");
    final JLabel blank11 = new JLabel(" ");
    final JLabel blank12 = new JLabel(" ");
    final JLabel blank13 = new JLabel(" ");
    final JLabel blank14 = new JLabel(" ");
    final JLabel blank15 = new JLabel(" ");
    final JLabel blank16 = new JLabel(" ");
    final JLabel blank17 = new JLabel(" ");
    final JLabel blank18 = new JLabel(" ");
    final JLabel blank19 = new JLabel(" ");
    final JLabel blank20 = new JLabel(" ");
    final JLabel y_name = new JLabel("Y-axis name:");
    final JCheckBox line_checkbox = new JCheckBox("Line Graph");
    final JCheckBox debug_checkbox = new JCheckBox("Show debug info in console");
    final JTextField x_whattograph_field = new JTextField("0");
    final JTextField y_whattograph_field = new JTextField("1");
    final JTextField graph_name_field = new JTextField("New Graph");
    final JTextField x_name_field = new JTextField("X");
    final JTextField y_name_field = new JTextField("Y");
    final JTextField x_graphsize_field = new JTextField("600");
    final JTextField interations_field = new JTextField("1");
    final JTextField y_graphsize_field = new JTextField("300");
    final JTextField delimiter_field = new JTextField(",");
    final JLabel delimiter_label = new JLabel("Delimiter:");

    File check_temp = new File(temp_file);
    boolean first_pass = true;
    boolean proceed = true;
    boolean line = true;
    Boolean debug = false;
    Thread gt;
    int inter;
    ExecutorService executor = Executors.newFixedThreadPool((int) 1);

    public Graph(NewJFrame Frame, String Awhat) {
        mainFrame = Frame;
        what = Awhat;
    }

    void calibrateTextArea() {
        NewJFrame.jTextArea1.append("\n");
        try {
            NewJFrame.jTextArea1.setCaretPosition(NewJFrame.jTextArea1.getLineStartOffset(NewJFrame.jTextArea1.getLineCount() - 1));
        } catch (Exception e) {

        }
    }

    public void configure_display() {
        x_whattograph_field.setMaximumSize(new Dimension(250, 20));
        y_whattograph_field.setMaximumSize(new Dimension(250, 20));

        interations_field.setMaximumSize(new Dimension(250, 20));
        interations_field.setMaximumSize(new Dimension(250, 20));

        y_graphsize_field.setMaximumSize(new Dimension(250, 20));
        x_graphsize_field.setMaximumSize(new Dimension(250, 20));

        delimiter_field.setMaximumSize(new Dimension(250, 20));
        delimiter_field.setMaximumSize(new Dimension(250, 20));
        delimiter_field.setMaximumSize(new Dimension(250, 20));

        graph_name_field.setMaximumSize(new Dimension(250, 20));
        x_name_field.setMaximumSize(new Dimension(250, 20));
        y_name_field.setMaximumSize(new Dimension(250, 20));

        y_whattograph.setBackground(Color.white);
        y_whattograph.setForeground(Color.blue);
        y_whattograph.setBorder(null);

        intro_label_main.setBackground(Color.white);
        intro_label_main.setForeground(Color.blue);
        intro_label_main.setBorder(null);

        intro_label_main_two.setBackground(Color.white);
        intro_label_main_two.setForeground(Color.blue);
        intro_label_main_two.setBorder(null);

        line_checkbox.setBackground(Color.white);
        line_checkbox.setForeground(Color.blue);
        line_checkbox.setBorder(null);

        debug_checkbox.setBackground(Color.white);
        debug_checkbox.setForeground(Color.blue);
        debug_checkbox.setBorder(null);

        interations.setBackground(Color.white);
        interations.setForeground(Color.blue);
        interations.setBorder(null);

        x_whattograph.setBackground(Color.white);
        x_whattograph.setForeground(Color.blue);
        x_whattograph.setBorder(null);

        graph_name.setBackground(Color.white);
        graph_name.setForeground(Color.blue);
        graph_name.setBorder(null);

        delimiter_label.setBackground(Color.white);
        delimiter_label.setForeground(Color.blue);
        delimiter_label.setBorder(null);

        graph_size_x.setBackground(Color.white);
        graph_size_x.setForeground(Color.blue);
        graph_size_x.setBorder(null);

        intro_label.setBackground(Color.white);
        intro_label.setForeground(Color.blue);
        intro_label.setBorder(null);

        graph_size_y.setBackground(Color.white);
        graph_size_y.setForeground(Color.blue);
        graph_size_y.setBorder(null);

        y_name.setBackground(Color.white);
        y_name.setForeground(Color.blue);
        y_name.setBorder(null);

        x_name.setBackground(Color.white);
        x_name.setForeground(Color.blue);
        x_name.setBorder(null);

        save.setBackground(Color.white);
        save.setForeground(Color.blue);
        save.setBorder(null);

        close.setBackground(Color.white);
        close.setBorder(null);
        close.setForeground(Color.blue);

        close.setMaximumSize(new Dimension(150, 15));
        save.setMaximumSize(new Dimension(150, 15));

        mainFrame.jPanel11.removeAll();
        mainFrame.jPanel14.removeAll();
        mainFrame.jPanel11.setLayout(new BoxLayout(mainFrame.jPanel11, BoxLayout.Y_AXIS));

        mainFrame.jPanel11.add(intro_label_main);
        mainFrame.jPanel11.add(intro_label_main_two);
        mainFrame.jPanel11.add(blank15);
        mainFrame.jPanel11.add(blank11);
        mainFrame.jPanel11.add(blank18);
        mainFrame.jPanel11.add(graph_name);
        mainFrame.jPanel11.add(graph_name_field);
        mainFrame.jPanel11.add(blank9);
        mainFrame.jPanel11.add(line_checkbox);
        mainFrame.jPanel11.add(blank6);
        mainFrame.jPanel11.add(blank12);
        mainFrame.jPanel11.add(graph_size_x);
        mainFrame.jPanel11.add(x_graphsize_field);
        mainFrame.jPanel11.add(graph_size_y);
        mainFrame.jPanel11.add(y_graphsize_field);
        mainFrame.jPanel11.add(blank);
        mainFrame.jPanel11.add(blank13);
        mainFrame.jPanel11.add(y_name);
        mainFrame.jPanel11.add(y_name_field);
        mainFrame.jPanel11.add(blank2);
        mainFrame.jPanel11.add(x_name);
        mainFrame.jPanel11.add(x_name_field);
        mainFrame.jPanel11.add(blank4);
        mainFrame.jPanel11.add(blank5);
        mainFrame.jPanel11.add(x_whattograph);
        mainFrame.jPanel11.add(x_whattograph_field);
        mainFrame.jPanel11.add(y_whattograph);
        mainFrame.jPanel11.add(y_whattograph_field);
        mainFrame.jPanel11.add(blank15);
        mainFrame.jPanel11.add(blank16);
        //mainFrame.jPanel11.add(delimiter_label);
        //mainFrame.jPanel11.add(delimiter_field);
        //mainFrame.jPanel11.add(blank14);
        //mainFrame.jPanel11.add(blank10);
        mainFrame.jPanel11.add(interations);
        mainFrame.jPanel11.add(interations_field);
        mainFrame.jPanel14.add(blank8);
        mainFrame.jPanel11.add(blank3);
        mainFrame.jPanel11.add(debug_checkbox);
        mainFrame.jPanel14.add(blank19);
        mainFrame.jPanel14.add(blank20);
        mainFrame.jPanel14.add(intro_label);
        mainFrame.jPanel14.add(blank7);
        mainFrame.jPanel14.add(save);
        mainFrame.jPanel14.add(blank11);
        mainFrame.jPanel14.add(close);
        save.setVisible(true);
        mainFrame.jPanel11.repaint();
        mainFrame.jPanel11.revalidate();
        mainFrame.jPanel11.validate();

        mainFrame.jPanel11.add(blank2);

        save.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                if ((x_whattograph_field.getText() == null || y_whattograph_field.getText() == null || graph_name_field.getText() == null) || x_name_field.getText() == null || y_name_field.getText() == null || x_graphsize_field.getText() == null || y_graphsize_field.getText() == null) {
                    mainFrame.jTextArea1.append("\nError, please fill out all of the fields.");
                    calibrateTextArea();
                } else {
                    if (debug_checkbox.isSelected()) {
                        debug = true;
                    } else {
                        debug = false;
                    }
                    if (line_checkbox.isSelected()) {
                        line = true;
                    } else {
                        line = false;
                    }

                    inter = Integer.parseInt(interations_field.getText());

                    if (!debug) {
                        save.setVisible(false);
                    }
                    GraphThread.stop = false;
                    Runnable shit = new GraphThread(mainFrame, what, graph_name_field.getText(), x_whattograph_field.getText(), y_whattograph_field.getText(), x_name_field.getText(), y_name_field.getText(), x_graphsize_field.getText(), y_graphsize_field.getText(), line, inter, delimiter_field.getText(), debug);
                    executor.execute(shit);

                }
            }

        });

        close.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    GraphThread.stop = true;
                    executor.shutdownNow();

                } catch (Exception stopping) {
                    mainFrame.jTextArea1.append("\nError " + stopping.getMessage());
                    calibrateTextArea();
                }

                mainFrame.jPanel11.removeAll();
                mainFrame.jPanel11.repaint();
                mainFrame.jPanel11.revalidate();
                mainFrame.jPanel11.validate();
                mainFrame.reloadBuckets();
            }
        });

    }

    public void run() {
        first_pass = true;

        File check_what = new File(what);

        if (check_temp.exists()) {
            check_temp.delete();
        }
        configure_display();
    }

    void startc(String Awhat
    ) {
        (new Thread(new Graph(mainFrame, Awhat))).start();
    }
}
