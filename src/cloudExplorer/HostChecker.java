package cloudExplorer;

import java.net.InetAddress;

public class HostChecker implements Runnable {

    public static String host;
    NewJFrame mainFrame;

    boolean ping(String address) {
        Boolean host_alive = false;
        try {
            InetAddress s3address = InetAddress.getByName(address);
            if (s3address.isReachable(3000)) {
                host_alive = true;
            } else {
                host_alive = false;
            }
        } catch (Exception ping) {
        }

        return host_alive;
    }

    public HostChecker(String Host, NewJFrame Frame) {
        host = Host;
        mainFrame = Frame;

    }

    public void run() {
   
        mainFrame.reloadBuckets();

    }

    void startc() {
        mainFrame.jTextArea1.append("\nLoading configuration: " + mainFrame.jTextField3.getText());
        mainFrame.calibrateTextArea();
        (new Thread(new HostChecker(host, mainFrame))).start();
    }
}
