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

import javax.sound.sampled.*;
import java.io.*;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

public class SoundRecorderThread implements Runnable {

    NewJFrame mainFrame;
    public static String response = null;
    public static String access_key = null;
    public static String secret_key = null;
    public static String endpoint = null;
    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
    TargetDataLine line;
    public String bucket = null;
    public static String Home = System.getProperty("user.home");
    public static String temp_file = (Home + File.separator + "object.tmp");

    public SoundRecorderThread(String Aaccess_key, String Asecret_key, String Abucket, String Aendpoint, NewJFrame AmainFrame) {
        access_key = Aaccess_key;
        secret_key = Asecret_key;
        bucket = Abucket;
        endpoint = Aendpoint;
        mainFrame = AmainFrame;

    }

    AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                channels, signed, bigEndian);
        return format;
    }

    public void run() {
        try {
            AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            if (!AudioSystem.isLineSupported(info)) {
                NewJFrame.jTextArea1.append("\nError: Line not supported");
                calibrateTextArea();
            } else {
                line = (TargetDataLine) AudioSystem.getLine(info);
                line.open(format);
                line.start();
                NewJFrame.jTextArea1.append("\nRecording has started.");
                calibrateTextArea();
                AudioInputStream ais = new AudioInputStream(line);
                File wavFile = new File(temp_file);
                AudioSystem.write(ais, fileType, wavFile);
            }
        } catch (Exception recording) {
            NewJFrame.jTextArea1.append("\n" + recording.getMessage());
        }
    }

    void calibrateTextArea() {
        mainFrame.jTextArea1.append("\n");
        try {
            mainFrame.jTextArea1.setCaretPosition(mainFrame.jTextArea1.getLineStartOffset(mainFrame.jTextArea1.getLineCount() - 1));
        } catch (Exception e) {

        }
    }

    void startc(String Aaccess_key, String Asecret_key, String Abucket, String Aendpoint, NewJFrame AmainFrame) {
        (new Thread(new SoundRecorderThread(Aaccess_key, Asecret_key, Abucket, Aendpoint, AmainFrame))).start();
    }
}
