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
import java.io.*;
import java.io.IOException;
import java.util.Random;
import org.jibble.pircbot.*;

/**
 *
 * @author ptribble
 */
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
    public static String[] nodes = new String[10];
    public static int random;
    public static String masternic;
    public static String gui;
    public static String userHome = System.getProperty("user.home");
    public static boolean emailmeeting = true;
    public static boolean record = false;
    public static boolean masternodecheck = false;
    public static int todoarray[] = new int[20];
    public static String todoarray2[] = new String[20];

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

    public static String getGUI() {
        return gui;
    }

    public static void setGUI(String string) {
        gui = string;
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

    public static void setrandomnick() {
        Random randomnick = new Random();
        int maximum = 10000;
        int minimum = 1000;
        int range = maximum - minimum + 1;
        random = randomnick.nextInt(range) + minimum;
    }

    public static String getrandomnick() {
        return getNickname() + random;
    }

    public void writernoappend(String message, String Location) {
        try {
            String seek = null;
            FileWriter meeting = new FileWriter(Location, true);
            BufferedWriter bmeeting = new BufferedWriter(meeting);
            bmeeting.write("\n" + message);
            bmeeting.close();
        } catch (Exception recordmeeting) {
        }
    }

    public void writer(String message, String Location) {
        try {
            String seek = null;
            FileWriter meeting = new FileWriter(Location, true);
            BufferedWriter bmeeting = new BufferedWriter(meeting);
            bmeeting.write("\n" + message);
            bmeeting.close();
        } catch (Exception recordmeeting) {
        }
    }

    void calibrateTextArea() {
        NewJFrame.jTextArea1.append("\n");
        try {
            NewJFrame.jTextArea1.setCaretPosition(jTextArea1.getLineStartOffset(jTextArea1.getLineCount() - 1));
        } catch (Exception e) {

        }
    }

    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        NewJFrame.ircarea.append("\n" + sender + ": " + message);
      
                
        calibrateTextArea();
        /**
         * if (message.equals("!help")) { this.sendMessage(channel, "Use !
         * before command. Commands: ls,ver,viewtodo,todo add-task,todo
         * rm-task,cookie,startmeeting nosend,startmeeting,stopmeeting"); }
         *
         * if (message.contains("!startmeeting")) { if (MyBot.record) {
         * this.sendMessage(channel, "Error: There is already a meeting being
         * recorded!"); } else if (!MyBot.record) { if (message.length() > 13) {
         * String meeting[] = message.split(" "); if
         * (meeting[1].contains("nosend")) { MyBot.emailmeeting = false;
         * this.sendMessage(channel, this.getNick() + " will not email this
         * meeting when it is complete."); } }
         *
         * File meetinglog = new File(meetingfile); if (meetinglog.exists()) {
         * meetinglog.delete(); } MyBot.record = true; this.sendMessage(channel,
         * this.getNick() + " is now recording the meeting!"); }
         *
         * if (MyBot.record) {
         *
         * }
         *
         * }
         *
         * if (message.contains("!ver")) { this.sendMessage(channel,
         * MyBot.getVer()); } if (message.contains("!stopmeeting")) { if
         * (!MyBot.record) { this.sendMessage(channel, "Error: There is no
         * meeting being recorded!"); } else { MyBot.record = false;
         * this.sendMessage(channel, this.getNick() + " has now stopped
         * recording the meeting!"); File meetinglog = new
         * File(this.meetingfile); File emailscriptfile = new
         * File(this.emailscript); if (emailscriptfile.exists()) {
         * emailscriptfile.delete(); } File move = new File(userHome + "/move");
         * if (move.exists()) { move.delete(); }
         * writernoappend("\n!/bin/bash\ncp -f " + this.meetingfile + " " +
         * userHome + "/Meeting-`date +%m-%d-%Y-%H:%M:%S`.txt", userHome +
         * "/move"); simpleshell("bash " + userHome + "/move");
         *
         * if (MyBot.emailmeeting) { if (meetinglog.exists()) { if
         * (getEmailogs() != null) { writernoappend("!/bin/bash\necho " +
         * this.getNick() + " has just finished recording a meeting in IRC
         * Meeting-`date +%m-%d-%Y-%H:%M:%S`| mail -s Meeting-`date
         * +%m-%d-%Y-%H:%M:%S` -a " + this.meetingfile + " " +
         * this.getEmailogs(), this.emailscript); simpleshell("bash " +
         * this.emailscript); } } else { this.sendMessage(channel, "Error:
         * Unable to find meeting logs to email."); } }
         *
         * MyBot.emailmeeting = true; }
         *
         * }
         *
         * if (MyBot.record) { recordmeeting(sender + ":", message); }
         *
         */
    }

    public void reader(String infile, String channel, String nick) {
        int j = 0;
        String seek = null;
        try {
            FileReader seekfile = new FileReader(infile);
            BufferedReader seekbr = new BufferedReader(seekfile);
            while ((seek = seekbr.readLine()) != null) {
                if (seek.contains(nick)) {
                    MyBot.todoarray[j] = j;
                    MyBot.todoarray2[j] = seek;
                    String[] filter = seek.split(nick + ":");
                    this.sendMessage(channel, MyBot.todoarray[j] + ". " + filter[1]);
                    j++;
                }
            }
        } catch (IOException readconfigfile) {
        }
    }

    public void writer(String string) {
        try {
            FileWriter fw = new FileWriter(userHome + File.separator + "nodes", true);
            BufferedWriter bfw = new BufferedWriter(fw);
            bfw.write(string + "\n");
            bfw.close();
        } catch (Exception ads) {
        }
    }

    public void onPrivateMessage(String sender, String login, String hostname, String message) {

        if (message.equals("!help")) {
            this.sendMessage(sender, "Use ! before command. Commands: ver,quit, term , showreg");
        }

        if (message.equalsIgnoreCase("!quit")) {
            this.disconnect();
            System.exit(-1);
        }
    }
}
