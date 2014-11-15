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
        NewJFrame.jTextArea1.append("\n");
        try {
            Bot.ircarea.setCaretPosition(Bot.ircarea.getLineStartOffset(Bot.ircarea.getLineCount() - 1));
        } catch (Exception e) {

        }
    }

    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        Bot.ircarea.append("\n" + sender + ": " + message);
        calibrateTextArea();
    }

    public void onPrivateMessage(String sender, String login, String hostname, String message) {

    }
}
