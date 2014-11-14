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

public class Bot implements Runnable {

    String Home = System.getProperty("user.home");
    NewJFrame mainFrame;
    Thread bot;
    String room = null;
    String server = null;
    String bucket = null;
    String nick = null;
    int port;

    public Bot(String Anick, String Aserver, int Aport, String Aroom, String Abucket) {
        nick = Anick;
        server = Aserver;
        room = Aroom;
        bucket = Abucket;
        port = Aport;
    }

    void calibrateTextArea() {
        mainFrame.jTextArea1.append("\n");
        try {
            mainFrame.jTextArea1.setCaretPosition(jTextArea1.getLineStartOffset(jTextArea1.getLineCount() - 1));
        } catch (Exception e) {

        }
    }

    public void run() {
   
        NewJFrame.jTextArea1.append("\nStarting IRC bot......");
        calibrateTextArea();

        MyBot bot = new MyBot(nick);
        bot.setVerbose(true);
        bot.setServername(server);
        bot.setRoom(room);

        try {
            bot.connect(server, port);
            bot.joinChannel(room);
        } catch (Exception connect) {
            NewJFrame.jTextArea1.append("\n" + connect.getMessage());
            NewJFrame.jTextArea1.append("\n" + port + "\n" + server + "\n" + room + "\n" + nick);
            calibrateTextArea();
        }

    }

    void startc(String Anick, String Aserver, int Aport, String Aroom, String Abucket) {
        bot = new Thread(new Bot(Anick, Aserver, Aport, Aroom, Abucket));
        bot.start();
    }

    void stop() {
        bot.stop();
        mainFrame.jTextArea1.setText("\nDownload completed or aborted.\n");

    }
}
