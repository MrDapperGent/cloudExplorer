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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ConfigureIRC implements Runnable {

    NewJFrame mainFrame;
    String Home = System.getProperty("user.home");
    File config = new File(Home + File.separator + ".cloudExplorerIRC");

    public ConfigureIRC(NewJFrame Frame) {
        mainFrame = Frame;

    }

    void calibrateTextArea() {
        NewJFrame.jTextArea1.append("\n");
        try {
            NewJFrame.jTextArea1.setCaretPosition(NewJFrame.jTextArea1.getLineStartOffset(NewJFrame.jTextArea1.getLineCount() - 1));
        } catch (Exception e) {

        }
    }

    public void run() {

        final JButton save = new JButton("Save");
        final JLabel server_label = new JLabel("Server:");
        final JLabel info = new JLabel("Please enter your IRC account information here for the bot.");
        final JLabel blank = new JLabel(" ");
        final JLabel blank2 = new JLabel(" ");
        final JLabel port_label = new JLabel("Port:");
        final JLabel nick_label = new JLabel("Nick:");
        final JLabel room_label = new JLabel("Channel:");
        final JTextField server_field = new JTextField(null);
        final JTextField port_field = new JTextField(null);
        final JTextField nick_field = new JTextField(null);
        final JTextField room_field = new JTextField(null);

        final JButton close = new JButton("Close");

        server_label.setBackground(Color.white);
        server_label.setForeground(Color.blue);
        server_label.setBorder(null);

        port_label.setBackground(Color.white);
        port_label.setForeground(Color.blue);
        port_label.setBorder(null);

        info.setBackground(Color.white);
        info.setForeground(Color.blue);
        info.setBorder(null);

        nick_label.setBackground(Color.white);
        nick_label.setForeground(Color.blue);
        nick_label.setBorder(null);

        room_label.setBackground(Color.white);
        room_label.setForeground(Color.blue);
        room_label.setBorder(null);

        save.setBackground(Color.white);
        save.setForeground(Color.blue);
        save.setBorder(null);

        close.setBackground(Color.white);
        close.setBorder(null);
        close.setForeground(Color.blue);

        close.setMaximumSize(new Dimension(150, 15));
        save.setMaximumSize(new Dimension(150, 15));

        server_field.setMaximumSize(new Dimension(200, 20));
        port_field.setMaximumSize(new Dimension(100, 20));
        room_field.setMaximumSize(new Dimension(200, 20));
        nick_field.setMaximumSize(new Dimension(200, 20));

        mainFrame.jPanel11.removeAll();
        mainFrame.jPanel14.removeAll();

        mainFrame.jPanel11.setLayout(new BoxLayout(mainFrame.jPanel11, BoxLayout.Y_AXIS));
        mainFrame.jPanel11.add(info);
        mainFrame.jPanel11.add(blank2);
        mainFrame.jPanel11.add(server_label);
        mainFrame.jPanel11.add(server_field);
        mainFrame.jPanel11.add(nick_label);
        mainFrame.jPanel11.add(nick_field);
        mainFrame.jPanel11.add(room_label);
        mainFrame.jPanel11.add(room_field);
        mainFrame.jPanel11.add(port_label);
        mainFrame.jPanel11.add(port_field);
        mainFrame.jPanel11.add(blank);
        mainFrame.jPanel11.add(close);
        mainFrame.jPanel11.add(save);

        mainFrame.jPanel11.repaint();
        mainFrame.jPanel11.revalidate();
        mainFrame.jPanel11.validate();

        if (config.exists()) {
            System.out.print("\ndebug");

            try {
                FileReader frr = new FileReader(config);
                BufferedReader bfrr = new BufferedReader(frr);
                String read = null;
                while ((read = bfrr.readLine()) != null) {
                    String cut[] = read.split("=");
                    if (cut[0].contains("server")) {
                        server_field.setText(cut[1]);
                    }
                    if (cut[0].contains("channel")) {
                        room_field.setText(cut[1]);
                    }

                    if (cut[0].contains("port")) {
                        port_field.setText(cut[1]);
                    }
                    if (cut[0].contains("nick")) {
                        nick_field.setText(cut[1]);
                    }
                }
                bfrr.close();
                mainFrame.jPanel11.repaint();
                mainFrame.jPanel11.revalidate();
                mainFrame.jPanel11.validate();
            } catch (Exception read) {

            }
        }

        save.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                Bot bot = new Bot(null, null, null, null);
                if (server_field.getText().length() > 0 && nick_field.getText().length() > 0 && room_field.getText().length() > 0 && port_field.getText().length() > 0) {
                    bot.writer("\nserver=" + server_field.getText()
                            + "\nnick=" + nick_field.getText()
                            + "\nport=" + port_field.getText()
                            + "\nchannel=" + room_field.getText(), mainFrame.Home + File.separator + ".cloudExplorerIRC");
                    if (config.exists()) {
                        NewJFrame.jTextArea1.append("\nConfiguration saved.");
                    } else {
                        NewJFrame.jTextArea1.append("\nError, configuration not saved.");
                    }
                } else {
                    NewJFrame.jTextArea1.append("\nError, please fill out all the fields.");
                }
            }
        });

        close.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                mainFrame.jPanel11.removeAll();
                mainFrame.jPanel11.repaint();
                mainFrame.jPanel11.revalidate();
                mainFrame.jPanel11.validate();

            }
        });

    }

    void startc() {
        (new Thread(new ConfigureIRC(mainFrame))).start();
    }
}
