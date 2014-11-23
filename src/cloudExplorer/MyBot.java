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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jibble.pircbot.*;

public class MyBot extends PircBot {

    public static String servername;
    public static String nick;
    public static String date;
    public static String permlogs;
    public static String buffer;
    public static String password;
    public static String channel;
    public static String room;
    public static String node;
    public static StringBuffer log;
    public static int random;
    public static String masternic;
    public static String userHome = System.getProperty("user.home");
    public static boolean emailmeeting = true;
    public static boolean record = false;

    public static String getPermlogs() {
        return permlogs;
    }

    public static void setPermlogs(String string) {
        permlogs = string;
    }

    public static String getMasternic() {
        return masternic;
    }

    public static void setMasternic(String string) {
        masternic = string;
    }

    public static String getPageTitle(URL url) throws Exception {

        BufferedReader in = new BufferedReader(
                new InputStreamReader(url.openStream(), "UTF-8"));

        Pattern pHead = Pattern.compile("(?i)</HEAD>");
        Matcher mHead;
        Pattern pTitle = Pattern.compile("(?i)</TITLE>");
        Matcher mTitle;

        String inputLine;
        boolean found = false;
        boolean notFound = false;
        String html = "";
        String title = new String();
        try {
            while (!(((inputLine = in.readLine()) == null) || found || notFound)) {
                html = html + inputLine;
                mHead = pHead.matcher(inputLine);
                if (mHead.find()) {
                    notFound = true;
                } else {
                    mTitle = pTitle.matcher(inputLine);
                    if (mTitle.find()) {
                        found = true;
                    }
                }
            }
            in.close();

            html = html.replaceAll("\\s+", " ");
            if (found) {
                Pattern p = Pattern.compile("(?i)<TITLE.*?>(.*?)</TITLE>");
                Matcher m = p.matcher(html);
                while (m.find() == true) {
                    title = m.group(1);
                }
            }
        } catch (Exception e) {
        }
        return title;
    }

    public static String getServername() {
        return servername;
    }

    public static void setServername(String name) {
        servername = name;
    }

    public String getLog() {
        return log.toString();
    }

    public void addLog(String name) {
        log.append(name);

    }

    public static String getRoom() {
        return room;
    }

    public static void setRoom(String name) {
        room = name;
    }

    public static String getNickname() {
        return nick;
    }

    public static void setNickname(String name) {
        nick = name;
    }

    public MyBot(String name) {
        this.setName(name);
    }

    void calibrateTextArea() {
        Bot.ircarea.append("\n");
        try {
            Bot.ircarea.setCaretPosition(Bot.ircarea.getLineStartOffset(Bot.ircarea.getLineCount() - 1));
        } catch (Exception e) {

        }
    }

    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        Bot.ircarea.append("\n" + sender + ": " + message);
        if (message.contains("http://") || message.contains("https://")) {
            URL url = null;
            try {
                String[] disectURL = message.split(" ");
                for (String foo : disectURL) {
                    if (foo.contains("http://") || foo.contains("https://")) {
                        url = new URL(foo);
                    }
                }
                this.sendMessage(channel, "^ " + getPageTitle(url));
                Bot.ircarea.append("\n" + sender + ": ^ " + getPageTitle(url));
            } catch (Exception ex) {
            }
        }
        calibrateTextArea();
    }

    public void onPrivateMessage(String sender, String login, String hostname, String message) {
        Bot.ircarea.append("\n<Private Message from: " + sender + " > " + message);
        calibrateTextArea();
    }
}
