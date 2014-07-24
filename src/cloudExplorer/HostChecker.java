package cloudExplorer;

public class HostChecker implements Runnable {

    public static String host;
    NewJFrame mainFrame;

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
