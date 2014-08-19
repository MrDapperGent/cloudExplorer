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

import java.io.FileOutputStream;
import java.io.InputStream;
import static cloudExplorer.NewJFrame.jTextArea1;
import java.io.File;
import java.io.FileInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zip implements Runnable {

    NewJFrame mainFrame;
    String what = null;
    Thread zip;
    String operation = null;
    String Home = System.getProperty("user.home");

    public void calibrate() {
        try {
            jTextArea1.setCaretPosition(jTextArea1.getLineStartOffset(jTextArea1.getLineCount() - 1));
        } catch (Exception e) {
        }
    }

    public void compress() {
        try {
            jTextArea1.append("\nAttempting to compress: " + what);
            calibrate();
            File file = new File(what);
            FileOutputStream fos = new FileOutputStream(Home + File.separator + "compress.tmp");
            ZipOutputStream zos = new ZipOutputStream(fos);
            ZipEntry ze = new ZipEntry(file.getName());
            zos.putNextEntry(ze);
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
            zos.closeEntry();
            zos.close();
            fis.close();
            fos.close();
        } catch (Exception compress) {
            jTextArea1.append("\n" + compress.getMessage());
            calibrate();
        }
    }

    public void uncompress() {

    }

    Zip(String Awhat, String Aoperation) {
        what = Awhat;
        operation = Aoperation;

    }

    void writeFile(InputStream is, String destination) {
        try {
            FileOutputStream fo = new FileOutputStream(destination);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = is.read(bytes)) != -1) {
                fo.write(bytes, 0, read);
            }
            fo.close();
        } catch (Exception add) {
        }

    }

    public void run() {
        String message = null;

        try {
            if (operation.contains("compress")) {
                compress();
            } else {
                uncompress();
            }
        } catch (Exception get) {
            //mainFrame.jTextArea1.append("\n\nAn error has occurred in GET.");
            //mainFrame.jTextArea1.append("\n\nError Message: " + get.getMessage());
            //message = message + "\n" + get.getMessage();
        }

        calibrate();
    }

    void startc(String Awhat, String Aoperation) {
        zip = new Thread(new Zip(Awhat, Aoperation));
        zip.start();
    }

    void stop() {
        zip.stop();
        mainFrame.jTextArea1.setText("\nDownload completed or aborted.\n");
    }

}
