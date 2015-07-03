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
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Pattern;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

public class NewJFrame extends javax.swing.JFrame implements ItemListener {

    String major = "6";
    String minor = "0";
    String release_version = major + "." + minor;
    String version = "Cloud Explorer " + release_version;
    String[] folders = new String[1];
    String[] folders2 = folders;
    String[] folders3 = folders;
    ArrayList<String> list = new ArrayList<String>();
    public boolean consoleToggle = false;
    public boolean selectToggle = false;
    public static JRadioButton deleting = new JRadioButton("foo");
    ImageIcon genericEngine = new ImageIcon(
            this.getClass().getResource("engine.png"));
    Credentials cred = new Credentials();
    BucketClass bucket = new BucketClass();
    Acl objectacl = new Acl();
    String Home = System.getProperty("user.home");
    String OS = System.getProperty("os.name");
    String[] bucketarray = null;
    public String[] objectarray = null;
    String[] syncarray = null;
    String[] account_array = new String[1];
    String[] simple_account_array = null;
    int active_account = 0;
    int total_accounts = 0;
    JRadioButton bucket_item[] = null;
    public JRadioButton object_item[];
    JRadioButton account_item[] = null;
    int active_bucket = 0;
    String object_acl_change = null;
    String temp_file = (Home + File.separator + "object.tmp");
    String config_file = (Home + File.separator + "s3.config");
    JFrame dialog = new JFrame();
    JLabel dialog_label = new JLabel("Please wait for operation to complete. This will close upon completion.");
    JPanel dialog_panel = new JPanel();
    int account_counter = 0;
    int content_counter = 0;
    int previous_objectarray_length = 0;
    Put put;
    Get get;
    Delete del;
    SyncFromS3 syncFromS3;
    SyncToS3 syncToS3;
    boolean isSyncingToS3 = true;
    public static boolean object_thread_status;
    ReloadBuckets buckets = null;
    boolean host_alive = false;
    public ArrayList<String> versioning_id;
    public ArrayList<String> versioning_date;
    public ArrayList<String> versioning_name;
    public static Boolean perf = false;
    public Boolean versionDownload = false;
    ShowVersions showVersions;
    ImageViewer imageviewer;
    Thread getThread;
    public static Boolean gui = false;
    String before = null;
    boolean folder_view = true;
    public static String active_folder = null;
    public static String snap_folder = null;

    public NewJFrame() {
        try {
            NewJFrame.gui = true;
            deleting.setEnabled(true);
            this.setTitle(version + " -  No bucket selected.");
            initComponents();
            setLocationRelativeTo(null);
            UIManager.setLookAndFeel(
                    UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.put("TabbedPane.selected", Color.white);
            UIManager.put("TabbedPane.background", Color.white);
            UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
            UIManager.put("TabbedPane.borderHightlightColor", java.awt.Color.white);
            UIManager.put("TabbedPane.borderHightlightColor", java.awt.Color.white);
            UIManager.put("TabbedPane.darkShadow", java.awt.Color.white);
            UIManager.put("TabbedPane.light", java.awt.Color.white);
            UIManager.put("TabbedPane.selectHighlight", java.awt.Color.white);
            UIManager.put("TabbedPane.darkShadow", java.awt.Color.white);
            UIManager.put("TabbedPane.focus", java.awt.Color.white);
            UIManager.put("ScrollBar.background", java.awt.Color.white);
            UIManager.put("ScrollBar.thumbHighlight", new ColorUIResource(Color.white));
            UIManager.put("ScrollBar.thumbDarkShadow", new ColorUIResource(Color.white));
            UIManager.put("ScrollBar.highlight", new ColorUIResource(Color.white));
            UIManager.put("ScrollBar.trackHighlight", new ColorUIResource(Color.white));
            UIManager.getLookAndFeelDefaults().put("Panel.background", Color.white);
            UIManager.getLookAndFeelDefaults().put("Panel.foreground", Color.white);
            this.setIconImage(new ImageIcon(getClass()
                    .getResource("cloud.jpg")).getImage());
            this.jTextField3.setText("https://s3.amazonaws.com");
            this.jTextField4.setText("443");

            this.jTabbedPane1.setToolTipTextAt(1, "Explore");
            this.jTabbedPane1.setToolTipTextAt(2, "Upload a file");
            this.jTabbedPane1.setToolTipTextAt(3, "Sync to and from S3");
            this.jTabbedPane1.setToolTipTextAt(4, "Text Editor");

            this.jScrollPane1.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
            this.jCheckBox1.setSelected(true);
            this.jPanel9.setVisible(false);
            JPopupMenu.setDefaultLightWeightPopupEnabled(false);

            Thread UpdateThread = new Thread(new Update(this, true));
            UpdateThread.start();
            UpdateThread.join();

            File config = new File(config_file);
            if (config.exists()) {
                this.jButton9.doClick();
            }
        } catch (Exception ex) {
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem10 = new javax.swing.JMenuItem();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        panel1 = new java.awt.Panel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jButton9 = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane27 = new javax.swing.JScrollPane();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane28 = new javax.swing.JScrollPane();
        jPanel12 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        scrollPane1 = new java.awt.ScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel13 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jPanel14 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jTextField10 = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jFileChooser1 = new javax.swing.JFileChooser();
        jPanel10 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jCheckBox5 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jPanel4 = new javax.swing.JPanel();
        jFileChooser2 = new javax.swing.JFileChooser();
        jButton16 = new javax.swing.JButton();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox6 = new javax.swing.JCheckBox();
        jButton14 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jButton11 = new javax.swing.JButton();
        jTextField6 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jButton22 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jList4 = new javax.swing.JList();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem23 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem22 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem27 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenu12 = new javax.swing.JMenu();
        jMenuItem32 = new javax.swing.JMenuItem();
        jMenuItem33 = new javax.swing.JMenuItem();
        jMenuItem30 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenu10 = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem29 = new javax.swing.JMenuItem();
        jMenu9 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenuItem26 = new javax.swing.JMenuItem();
        jMenuItem24 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem28 = new javax.swing.JMenuItem();
        jMenu11 = new javax.swing.JMenu();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem25 = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenuItem31 = new javax.swing.JMenuItem();
        jMenuItem19 = new javax.swing.JMenuItem();

        jMenuItem5.setText("jMenuItem5");

        jMenu4.setText("jMenu4");

        jMenuItem10.setText("jMenuItem10");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(240, 99));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(280, 280, 280)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 663, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panel1.setBackground(java.awt.Color.white);

        jTabbedPane1.setBackground(java.awt.SystemColor.text);
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jTabbedPane1.setToolTipText("");
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setToolTipText("Accounts");

        jPanel16.setBackground(java.awt.SystemColor.text);

        jButton9.setBackground(java.awt.SystemColor.text);
        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cloudExplorer/user-info-icon.png"))); // NOI18N
        jButton9.setText("Load");
        jButton9.setBorder(null);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(82, 82, Short.MAX_VALUE)
                .addComponent(jButton9)
                .addGap(19, 19, 19))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jButton9)
                .addGap(0, 0, 0))
        );

        jLabel3.setText("Access Key");

        jTextField1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel4.setText("Secret Key");

        jTextField2.setFont(new java.awt.Font("OpenSymbol", 0, 15)); // NOI18N
        jTextField2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel5.setText("Host URL");

        jTextField3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jLabel6.setText("Port");

        jLabel7.setText("Region");

        jLabel9.setText("Name");

        jTextField4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jTextField5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jTextField7.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(45, 45, 45)
                        .addComponent(jTextField5))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(52, 52, 52)
                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(63, 63, 63)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 27, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jButton10.setBackground(java.awt.SystemColor.text);
        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cloudExplorer/Actions-edit-clear-icon.png"))); // NOI18N
        jButton10.setText("Clear");
        jButton10.setBorder(null);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton8.setBackground(java.awt.SystemColor.text);
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cloudExplorer/Save-icon.png"))); // NOI18N
        jButton8.setText("Save");
        jButton8.setBorder(null);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jLabel15.setText("Saved Accounts");

        jScrollPane27.setBorder(null);
        jScrollPane27.setPreferredSize(new java.awt.Dimension(494, 331));

        jPanel21.setBackground(java.awt.SystemColor.text);
        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel21.setAlignmentX(0.0F);
        jPanel21.setAlignmentY(0.0F);
        jPanel21.setAutoscrolls(true);

        jScrollPane28.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jPanel12.setBackground(java.awt.SystemColor.text);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 362, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 240, Short.MAX_VALUE)
        );

        jScrollPane28.setViewportView(jPanel12);

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane28, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane28, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(105, Short.MAX_VALUE))
        );

        jScrollPane27.setViewportView(jPanel21);

        jButton2.setBackground(java.awt.SystemColor.text);
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cloudExplorer/Misc-Delete-Database-icon.png"))); // NOI18N
        jButton2.setText("Delete");
        jButton2.setBorder(null);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel14.setText("Account Information");

        jCheckBox1.setBackground(java.awt.SystemColor.text);
        jCheckBox1.setForeground(java.awt.Color.blue);
        jCheckBox1.setText("Auto-load buckets");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(157, 157, 157)
                        .addComponent(jLabel14))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton10)
                        .addGap(36, 36, 36)
                        .addComponent(jButton8)
                        .addGap(32, 32, 32)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane27, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(jButton2))))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(129, 129, 129))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton8)
                            .addComponent(jButton10)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jScrollPane27, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addComponent(jCheckBox1)
                .addContainerGap())
        );

        jTabbedPane1.addTab("", new javax.swing.ImageIcon(getClass().getResource("/cloudExplorer/Actions-document-decrypt-icon.png")), jPanel3, "Accounts"); // NOI18N

        scrollPane1.setBackground(java.awt.SystemColor.text);

        jPanel1.setBackground(java.awt.SystemColor.text);

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jPanel5.setBackground(java.awt.SystemColor.text);
        jPanel5.setAlignmentX(0.0F);
        jPanel5.setAlignmentY(0.0F);
        jPanel5.setAutoscrolls(true);

        jScrollPane3.setBorder(null);

        jPanel13.setBackground(java.awt.SystemColor.text);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 148, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 289, Short.MAX_VALUE)
        );

        jScrollPane3.setViewportView(jPanel13);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(56, Short.MAX_VALUE))
        );

        jScrollPane2.setViewportView(jPanel5);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cloudExplorer/Empty-Bucket-icon.png"))); // NOI18N
        jLabel1.setToolTipText("Create a bucket");
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        jScrollPane7.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jScrollPane7.setPreferredSize(new java.awt.Dimension(650, 314));

        jPanel11.setBackground(javax.swing.UIManager.getDefaults().getColor("text"));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGap(797, 797, 797)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
        );

        jScrollPane7.setViewportView(jPanel11);

        jPanel14.setBackground(java.awt.SystemColor.text);
        jPanel14.setOpaque(false);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 166, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 96, Short.MAX_VALUE)
        );

        jButton3.setBackground(java.awt.SystemColor.text);
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cloudExplorer/Actions-svn-update-icon.png"))); // NOI18N
        jButton3.setToolTipText("Download");
        jButton3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(java.awt.SystemColor.text);
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cloudExplorer/Actions-trash-empty-icon.png"))); // NOI18N
        jButton4.setToolTipText("Delete");
        jButton4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton7.setBackground(java.awt.SystemColor.text);
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cloudExplorer/Actions-system-search-icon.png"))); // NOI18N
        jButton7.setToolTipText("Properties");
        jButton7.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton12.setBackground(java.awt.SystemColor.text);
        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cloudExplorer/Apps-scribus-icon.png"))); // NOI18N
        jButton12.setToolTipText("Edit");
        jButton12.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setBackground(java.awt.SystemColor.text);
        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cloudExplorer/Status-media-playlist-repeat-icon.png"))); // NOI18N
        jButton13.setToolTipText("Select All");
        jButton13.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton1.setBackground(java.awt.SystemColor.text);
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cloudExplorer/Actions-svn-commit-icon.png"))); // NOI18N
        jButton1.setToolTipText("Upload");
        jButton1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton17.setBackground(java.awt.SystemColor.text);
        jButton17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cloudExplorer/Actions-media-playback-start-icon.png"))); // NOI18N
        jButton17.setToolTipText("Play");
        jButton17.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jButton18.setBackground(java.awt.SystemColor.text);
        jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cloudExplorer/Apps-preferences-desktop-filetype-association-icon.png"))); // NOI18N
        jButton18.setToolTipText("Versions");
        jButton18.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jButton19.setBackground(java.awt.SystemColor.text);
        jButton19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cloudExplorer/Apps-digikam-icon.png"))); // NOI18N
        jButton19.setToolTipText("View Image");
        jButton19.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jPanel15.setBackground(java.awt.SystemColor.text);
        jPanel15.setOpaque(false);

        jTextField10.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextField10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField10ActionPerformed(evt);
            }
        });

        jButton6.setBackground(java.awt.SystemColor.text);
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cloudExplorer/Actions-page-zoom-icon.png"))); // NOI18N
        jButton6.setToolTipText("Search");
        jButton6.setBorder(null);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton6)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addComponent(jLabel1))
                    .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 664, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addGap(10, 10, 10)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7)
                        .addGap(10, 10, 10)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton19)
                        .addGap(93, 93, 93))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton13)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jButton4)
                        .addComponent(jButton12)
                        .addComponent(jButton7)
                        .addComponent(jButton17)
                        .addComponent(jButton18)
                        .addComponent(jButton19)
                        .addComponent(jButton3))
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        scrollPane1.add(jPanel1);

        jTabbedPane1.addTab("", new javax.swing.ImageIcon(getClass().getResource("/cloudExplorer/Search-icon.png")), scrollPane1); // NOI18N

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jFileChooser1.setBackground(new java.awt.Color(255, 255, 255));
        jFileChooser1.setControlButtonsAreShown(false);
        jFileChooser1.setFileSelectionMode(javax.swing.JFileChooser.FILES_AND_DIRECTORIES);
        jFileChooser1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jFileChooser1.setDragEnabled(true);
        jFileChooser1.setMinimumSize(new java.awt.Dimension(404, 250));
        jFileChooser1.setMultiSelectionEnabled(true);
        jFileChooser1.setOpaque(true);
        jFileChooser1.setPreferredSize(new java.awt.Dimension(989, 393));
        jFileChooser1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFileChooser1ActionPerformed(evt);
            }
        });

        jPanel10.setBackground(java.awt.SystemColor.text);

        jButton5.setBackground(java.awt.SystemColor.text);
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cloudExplorer/Actions-svn-commit-icon.png"))); // NOI18N
        jButton5.setText("Upload");
        jButton5.setBorder(null);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton15.setBackground(java.awt.SystemColor.text);
        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cloudExplorer/Actions-process-stop-icon.png"))); // NOI18N
        jButton15.setText("Abort");
        jButton15.setBorder(null);
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jCheckBox5.setBackground(java.awt.SystemColor.text);
        jCheckBox5.setText("Encrypt\n");

        jCheckBox4.setBackground(java.awt.SystemColor.text);
        jCheckBox4.setText("Compress");
        jCheckBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox4ActionPerformed(evt);
            }
        });

        jCheckBox2.setBackground(java.awt.SystemColor.text);
        jCheckBox2.setText("RRS");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jCheckBox4)
                        .addComponent(jCheckBox5)
                        .addComponent(jCheckBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton5)
                            .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(0, 0, 0))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton15)
                .addGap(18, 18, 18)
                .addComponent(jCheckBox2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox4)
                .addGap(12, 12, 12)
                .addComponent(jCheckBox5)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jLabel11.setText("Upload To Folder:");

        jScrollPane8.setViewportView(jList2);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(32, 32, 32)
                .addComponent(jFileChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 669, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jFileChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addGap(46, 46, 46))
        );

        jTabbedPane1.addTab("", new javax.swing.ImageIcon(getClass().getResource("/cloudExplorer/Actions-go-up-icon.png")), jPanel2); // NOI18N

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jFileChooser2.setBackground(new java.awt.Color(255, 255, 255));
        jFileChooser2.setControlButtonsAreShown(false);
        jFileChooser2.setFileSelectionMode(javax.swing.JFileChooser.FILES_AND_DIRECTORIES);
        jFileChooser2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jFileChooser2.setDragEnabled(true);
        jFileChooser2.setPreferredSize(new java.awt.Dimension(1000, 393));
        jFileChooser2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFileChooser2ActionPerformed(evt);
            }
        });

        jButton16.setBackground(java.awt.SystemColor.text);
        jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cloudExplorer/abort.png"))); // NOI18N
        jButton16.setText("    Abort");
        jButton16.setToolTipText("");
        jButton16.setActionCommand("Abort");
        jButton16.setBorder(null);
        jButton16.setFocusPainted(false);
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jCheckBox3.setBackground(java.awt.SystemColor.text);
        jCheckBox3.setText("RRS");

        jCheckBox6.setBackground(java.awt.SystemColor.text);
        jCheckBox6.setText("Encrypt\n");
        jCheckBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox6ActionPerformed(evt);
            }
        });

        jButton14.setBackground(java.awt.SystemColor.text);
        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cloudExplorer/tab-sync2.png"))); // NOI18N
        jButton14.setText("To S3");
        jButton14.setBorder(null);
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton21.setBackground(java.awt.SystemColor.text);
        jButton21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cloudExplorer/tab-sync2.png"))); // NOI18N
        jButton21.setText("From S3");
        jButton21.setBorder(null);
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        jLabel12.setText("Folders:");

        jScrollPane9.setViewportView(jList3);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jButton16))
                    .addComponent(jButton14)
                    .addComponent(jButton21)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox3)
                            .addComponent(jCheckBox6))))
                .addGap(26, 26, 26)
                .addComponent(jFileChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 669, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton14)
                        .addGap(0, 0, 0)
                        .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBox3)
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBox6)
                        .addGap(29, 29, 29)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE))
                    .addComponent(jFileChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("", new javax.swing.ImageIcon(getClass().getResource("/cloudExplorer/sign-sync-icon.png")), jPanel4); // NOI18N

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jTextArea2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jScrollPane4.setViewportView(jTextArea2);

        jButton11.setBackground(java.awt.SystemColor.text);
        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cloudExplorer/Actions-svn-commit-icon.png"))); // NOI18N
        jButton11.setText("Save");
        jButton11.setBorder(null);
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jTextField6.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jLabel8.setText("File Name:");

        jLabel2.setText("Replace text:");

        jLabel10.setText("With:");

        jTextField8.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jTextField9.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jButton22.setBackground(java.awt.SystemColor.text);
        jButton22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cloudExplorer/engine.png"))); // NOI18N
        jButton22.setText("Replace");
        jButton22.setBorder(null);
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        jButton23.setBackground(java.awt.SystemColor.text);
        jButton23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cloudExplorer/engine.png"))); // NOI18N
        jButton23.setText("Undo");
        jButton23.setBorder(null);
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        jPanel8.setBackground(java.awt.SystemColor.text);

        jLabel13.setText("Folders:");

        jScrollPane10.setViewportView(jList4);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel13)
                .addGap(0, 103, Short.MAX_VALUE))
            .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel8)
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton11, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(jLabel10)
                    .addComponent(jLabel2)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton23)
                            .addComponent(jButton22))
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTextField9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                            .addComponent(jTextField8, javax.swing.GroupLayout.Alignment.LEADING)))
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 671, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton11)
                        .addGap(6, 6, 6)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0))
        );

        jTabbedPane1.addTab("", new javax.swing.ImageIcon(getClass().getResource("/cloudExplorer/Actions-document-edit-icon.png")), jPanel6); // NOI18N

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jMenuBar1.setBorder(null);

        jMenu1.setText("File");

        jMenuItem23.setText("New Window");
        jMenuItem23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem23ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem23);

        jMenuItem4.setText("Exit");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuBar1.add(jMenu1);

        jMenu3.setText("Bucket");

        jMenuItem1.setText("Create Bucket");
        jMenuItem1.setToolTipText("");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem1);

        jMenuItem13.setText("Bucket Life Cycle");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem13);

        jMenuItem22.setText("Add External Bucket");
        jMenuItem22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem22ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem22);

        jMenuBar1.add(jMenu3);

        jMenu2.setText("Objects");

        jMenuItem6.setText("Modify Object ACL");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem6);

        jMenuItem16.setText("Restore Object from Glacier");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem16);

        jMenuItem27.setText("Create Folder");
        jMenuItem27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem27ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem27);

        jMenuBar1.add(jMenu2);

        jMenu5.setText("Background Syncing");

        jMenuItem15.setText("Run");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem15);

        jMenuItem9.setText("Configure");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem9);

        jMenuBar1.add(jMenu5);

        jMenu12.setText("Snapshots and Migration");

        jMenuItem32.setText("Set Migration/Snapshot Account and Bucket");
        jMenuItem32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem32ActionPerformed(evt);
            }
        });
        jMenu12.add(jMenuItem32);

        jMenuItem33.setText("Set Current Folder to Restore Snapshot From");
        jMenuItem33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem33ActionPerformed(evt);
            }
        });
        jMenu12.add(jMenuItem33);

        jMenuItem30.setText("Create or Restore a Bucket Snapshot");
        jMenuItem30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem30ActionPerformed(evt);
            }
        });
        jMenu12.add(jMenuItem30);

        jMenuItem17.setText("Migrate Bucket to Another S3 Account.");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu12.add(jMenuItem17);

        jMenuBar1.add(jMenu12);

        jMenu6.setText("Tools");

        jMenu10.setText("S3");

        jMenuItem8.setText("Delete every object and version");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem8);

        jMenuItem14.setText("Screen shot to S3");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem14);

        jMenu6.add(jMenu10);

        jMenuItem29.setText("Record Audio");
        jMenuItem29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem29ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem29);

        jMenu9.setText("IRC");

        jMenuItem7.setText("Start IRC");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem7);

        jMenuItem20.setText("Configure");
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem20);

        jMenu6.add(jMenu9);

        jMenuItem26.setText("Graph");
        jMenuItem26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem26ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem26);

        jMenuItem24.setText("Delete Temp Files");
        jMenuItem24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem24ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem24);

        jMenuBar1.add(jMenu6);

        jMenu7.setText("Performance");

        jMenuItem3.setText("PUT");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem3);

        jMenuItem11.setText("GET");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem11);

        jMenuItem28.setText("Mixed - 50/50");
        jMenuItem28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem28ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem28);

        jMenuBar1.add(jMenu7);

        jMenu11.setText("Console");

        jMenuItem21.setText("On");
        jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem21ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem21);

        jMenuItem2.setText("Off");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem2);

        jMenuBar1.add(jMenu11);

        jMenu8.setText("Help");

        jMenuItem12.setText("About");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem12);

        jMenuItem25.setText("Check for updates");
        jMenuItem25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem25ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem25);

        jMenuItem18.setText("Bucket Migraton");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem18);

        jMenuItem31.setText("Snapshots");
        jMenuItem31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem31ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem31);

        jMenuItem19.setText("Background Sync");
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem19);

        jMenuBar1.add(jMenu8);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    String convertObject(String what, String operation) {

        if (what.contains("/")) {
            what = what.replace("/", File.separator);
        }

        if (what.contains("\\")) {
            what = what.replace("\\", File.separator);
        }

        int count = 0;
        int slash_counter = 0;
        String out_file = null;
        int another_counter = 0;

        for (int y = 0; y != what.length(); y++) {
            if (what.substring(y, y + 1).contains(File.separator)) {
                slash_counter++;
                another_counter = y;
            }
        }

        for (int y = 0; y != what.length(); y++) {
            if (y == another_counter) {
                if (operation.contains("download")) {
                    if (what.contains(File.separator)) {
                        out_file = (what.substring(y, what.length()));
                    } else {
                        out_file = (what);
                    }
                } else {
                    out_file = (what.substring(y + 1, what.length()));
                }
            }
        }
        return out_file;
    }

    void search_folder_view() {
        if (active_bucket > 0) {
            NewJFrame.perf = false;
            if (consoleToggle) {
                jPanel9.setVisible(true);
            } else {
                jPanel9.setVisible(false);
            }
            showPanel();
            jPanel15.setVisible(true);
            jButton3.setEnabled(true);
            jButton1.setEnabled(true);
            jButton4.setEnabled(true);
            jButton7.setEnabled(true);
            jButton12.setEnabled(true);
            jButton13.setEnabled(true);
            jPanel14.removeAll();
            jPanel14.repaint();
            jPanel14.revalidate();
            jPanel14.validate();
            jButton1.setEnabled(true);
            jButton17.setEnabled(true);
            jButton18.setEnabled(true);
            jButton19.setEnabled(true);

            reloadObjects(true);
            versionDownload = false;
            BucketACL bucketACL = new BucketACL(this);
            bucketACL.startc();
            jButton13.setText("Select All");

            try {
                int found = 0;

                jTabbedPane1.setSelectedIndex(1);

                int display_counter = objectarray.length;

                for (int i = 1; i != display_counter; i++) {
                    if (object_item[i] != null) {
                        if (object_item[i].getText().toLowerCase().contains(jTextField10.getText().toLowerCase())) {
                            jPanel11.add(object_item[i]);
                            object_item[i].setVisible(true);
                            found++;
                        } else {
                            object_item[i].setVisible(false);
                        }
                    }
                }

                if (found == 0) {
                    jTextArea1.append("\n\nNo objects found for search.");
                } else {
                    int display = objectarray.length - 1;
                    jTextArea1.append("\n\nLoaded objects. Total number of objects in this bucket: " + display);
                }
                jTextField10.setText("");
            } catch (Exception searchBar) {

            }
        } else {
            jTextArea1.append("\nError: No bucket has been selected\n");
        }

        calibrateTextArea();
    }
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        if (active_bucket > 0) {
            NewJFrame.perf = false;
            if (consoleToggle) {
                jPanel9.setVisible(true);
            } else {
                jPanel9.setVisible(false);
            }
            showPanel();
            jPanel15.setVisible(true);
            jButton3.setEnabled(true);
            jButton1.setEnabled(true);
            jButton4.setEnabled(true);
            jButton7.setEnabled(true);
            jButton12.setEnabled(true);
            jButton13.setEnabled(true);
            jPanel14.removeAll();
            jPanel14.repaint();
            jPanel14.revalidate();
            jPanel14.validate();
            jButton1.setEnabled(true);
            jButton17.setEnabled(true);
            jButton18.setEnabled(true);
            jButton19.setEnabled(true);
            reloadObjects(false);
            folder_view = false;
            versionDownload = false;
            BucketACL bucketACL = new BucketACL(this);
            bucketACL.startc();
            jButton13.setText("Select All");

            try {
                int found = 0;

                jTabbedPane1.setSelectedIndex(1);

                int display_counter = objectarray.length;

                for (int i = 1; i != display_counter; i++) {
                    if (object_item[i] != null) {
                        if (object_item[i].getText().toLowerCase().contains(jTextField10.getText().toLowerCase())) {
                            jPanel11.add(object_item[i]);
                            object_item[i].setVisible(true);
                            found++;
                        } else {
                            object_item[i].setVisible(false);
                        }
                    }
                }

                if (found == 0) {
                    jTextArea1.append("\n\nNo objects found for search.");
                } else {
                    int display = objectarray.length - 1;
                    jTextArea1.append("\n\nLoaded objects. Total number of objects in this bucket: " + display);
                }
                jTextField10.setText("");
            } catch (Exception searchBar) {

            }
        } else {
            jTextArea1.append("\nError: No bucket has been selected\n");
        }

        calibrateTextArea();
    }

    void clear_old_radio_buttons() {

        try {
            for (int c = 1; c != bucketarray.length; c++) {
                if (c == active_bucket) {
                } else {
                    bucket_item[c].setSelected(false);
                }
            }
        } catch (Exception clear_old_radio) {
        }

    }

    public void itemStateChanged(ItemEvent event) {
        if (deleting.isSelected()) {
            deleting.removeItemListener(this);

            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    search_folder_view();
                }
            });

            deleting.setSelected(false);
        }

        try {
            if (bucket_item != null) {
                for (int h = 1; h != bucketarray.length; h++) {
                    if (bucketarray.length > 1) {
                        if (bucket_item[h] != null) {
                            if (bucket_item[h].isSelected()) {
                                if (h != active_bucket) {
                                    active_bucket = h;
                                    this.setTitle(version + " -  " + bucket_item[h].getText());
                                    cred.setBucket(bucket_item[h].getText());
                                    clear_old_radio_buttons();
                                    h = bucketarray.length;
                                    objectarray = null;
                                    jTextArea1.append("\nPlease wait, loading objects.");
                                    calibrateTextArea();
                                    java.awt.EventQueue.invokeLater(new Runnable() {
                                        public void run() {
                                            search_folder_view();
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ItemStateChanged) {
        }

        try {
            for (int h = 0; h != account_array.length; h++) {
                if (account_item[h] != null) {
                    if (account_item[h].isSelected()) {
                        if (h != active_account) {
                            active_account = h;
                            this.setTitle(version + " -  No bucket selected.");
                            changeAccountRadioButtons();
                            h = account_array.length;
                            objectarray = null;
                            active_bucket = 0;
                            jButton9.doClick();
                        }
                    }
                }
            }
        } catch (Exception ItemStateChanged) {
        }

    }

    public void changeAccountRadioButtons() {
        try {
            for (int c = 0; c != account_array.length; c++) {
                if (account_array[c] != null) {
                    if (c == active_account) {
                    } else {
                        account_item[c].setSelected(false);
                    }
                }
            }
        } catch (Exception clear_old_radio) {
        }
    }

    void loadConfig() {
        String data = null;
        account_array = new String[20];
        account_item = new JRadioButton[account_array.length];
        config_file = (Home + File.separator + "s3.config");

        try {
            for (int k = 0; k != account_array.length; k++) {
                account_array[k] = null;
                account_item[k].setText("");
            }
        } catch (Exception nads) {
        }

        try {
            FileReader fr = new FileReader(config_file);
            BufferedReader bfr = new BufferedReader(fr);
            String read = null;
            int h = 0;
            while ((read = bfr.readLine()) != null) {
                if (read != null) {
                    if (read.length() > 1) {
                       if (read.contains("@")) {
                            account_array[h] = read;
                        }
                        content_counter++;
                    }
                }
                h++;
            }
        } catch (Exception loadConfig) {
        }

        try {
            for (int h = 0; h != account_array.length; h++) {
                if (account_array[h] != null) {
                    String[] analyze_array = account_array[h].split("@");
                    jPanel21.setLayout(new BoxLayout(jPanel21, BoxLayout.Y_AXIS));
                    account_item[h] = new JRadioButton();

                    if (analyze_array.length == 6) {
                        account_item[h].setText(analyze_array[5]);
                    } else {
                        account_item[h].setText(analyze_array[2]);
                    }

                    account_item[h].addItemListener(this);
                    account_item[h].setBackground(Color.white);
                    account_item[h].setForeground(Color.blue);
                    jPanel21.add(account_item[h]);
                    jPanel21.revalidate();
                    //validate();
                }
            }

            jPanel21.setLayout(new BoxLayout(jPanel21, BoxLayout.Y_AXIS));
            jPanel21.repaint();
            jPanel21.revalidate();
            jPanel21.validate();

            if (content_counter == 0) {
                jTextArea1.append("\nError: No saved configurations found.\n");
                account_counter = 0;
            }
            int z = 0;
            calibrateTextArea();
        } catch (Exception draw) {
        }
    }

    void showPanel() {
        jScrollPane1.setVisible(true);
        jScrollPane2.setVisible(true);
        jPanel13.setVisible(true);
        jLabel1.setVisible(true);
        jButton1.setVisible(true);
        jButton3.setVisible(true);
        jButton4.setVisible(true);
        jButton7.setVisible(true);
        jButton12.setVisible(true);
        jButton13.setVisible(true);
        jButton17.setVisible(true);
        jButton18.setVisible(true);
        jButton19.setVisible(true);
        jPanel14.removeAll();
        jPanel14.repaint();
    }

    void hidePanel() {

        jScrollPane2.setVisible(false);
        jPanel13.setVisible(false);
        jLabel1.setVisible(false);
        jButton1.setVisible(false);
        jButton3.setVisible(false);
        jButton4.setVisible(false);
        jButton7.setVisible(false);
        jButton12.setVisible(false);
        jButton13.setVisible(false);
        jButton17.setVisible(false);
        jButton18.setVisible(false);
        jButton19.setVisible(false);
    }

    void drawBuckets() {
        jPanel5.removeAll();
        jPanel5.revalidate();
        jPanel5.repaint();
        showPanel();
        jPanel5.setLayout(new BoxLayout(jPanel5, BoxLayout.PAGE_AXIS));

        bucket_item = new JRadioButton[bucketarray.length];

        if (bucketarray != null) {
            for (int h = 1; h != bucketarray.length; h++) {
                jPanel5.setLayout(new BoxLayout(jPanel5, BoxLayout.Y_AXIS));
                bucket_item[h] = new JRadioButton();
                bucket_item[h].setText(bucketarray[h]);
                bucket_item[h].addItemListener(this);
                bucket_item[h].setBackground(Color.white);
                if (bucket.VersioningStatus(cred.getAccess_key(), cred.getSecret_key(), bucketarray[h], cred.getEndpoint(), cred.getRegion(), false)) {
                    bucket_item[h].setForeground(Color.green);
                } else {
                    bucket_item[h].setForeground(Color.blue);
                }
                jPanel5.add(bucket_item[h]);
                jPanel5.revalidate();
                validate();
            }

            objectarray = null;
            jPanel11.removeAll();
            JLabel helpmessage = new JLabel("1. Buckets are displayed on the left. Click on a bucket to see your files.");
            JLabel blank = new JLabel("");
            JLabel blank2 = new JLabel("");
            JLabel helpmessage2 = new JLabel("2. You can search buckets by typing text in the search box.");
            JLabel helpmessage3 = new JLabel("3. Buckets with versioning enabled are displayed in green.");
            helpmessage.setForeground(Color.blue);
            helpmessage2.setForeground(Color.blue);
            helpmessage3.setForeground(Color.blue);
            jPanel11.setLayout(new BoxLayout(jPanel11, BoxLayout.Y_AXIS));
            jPanel11.add(helpmessage);
            jPanel11.add(blank);
            jPanel11.add(helpmessage2);
            jPanel11.add(blank2);
            jPanel11.add(helpmessage3);
            jPanel11.repaint();
        }

    }

    void reloadBuckets() {
        if ((jTextField1.getText().length() > 1 || jTextField2.getText().length() > 1)) {
            var();
            bucketarray = null;
            ReloadBuckets buckets = new ReloadBuckets(cred.getAccess_key(), cred.getSecret_key(), cred.getEndpoint(), this);
            buckets.run();
            folder_view = true;
            active_bucket = 0;
        } else {
            jTextArea1.append("\nError: Configuration not loaded\n");
        }
        calibrateTextArea();
    }

    void redrawObjects() {
        jPanel11.setLayout(new BoxLayout(jPanel11, BoxLayout.PAGE_AXIS));
        jPanel14.removeAll();
        object_item = new JRadioButton[objectarray.length];
        for (int h = 1; h != objectarray.length; h++) {
            jPanel11.setLayout(new BoxLayout(jPanel11, BoxLayout.Y_AXIS));
            object_item[h] = new JRadioButton();
            object_item[h].setText(objectarray[h]);
            object_item[h].setBackground(Color.white);
            object_item[h].setForeground(Color.blue);
        }
        jPanel11.setLayout(new BoxLayout(jPanel11, BoxLayout.PAGE_AXIS));
    }

    void drawFolders() {
        folders = new String[objectarray.length];
        folders2 = new String[objectarray.length];
        list = null;
        boolean draw_folder = true;
        String add_folder = null;
        String[] cut = new String[1000];
        jPanel11.setLayout(new BoxLayout(jPanel11, BoxLayout.PAGE_AXIS));
        jPanel14.removeAll();
        object_item = new JRadioButton[objectarray.length];
        String unix = "/";
        String win = "\\";
        String separator = null;
        boolean begin = false;

        try {
            for (int h = 1; h != objectarray.length; h++) {
                draw_folder = true;

                if (objectarray[h].contains(unix) || objectarray[h].contains(win)) {
                    if (objectarray[h].contains(win)) {
                        separator = win;
                    } else {
                        separator = unix;
                    }

                    if (objectarray[h].substring(0, 1).contains(win) || objectarray[h].substring(0, 1).contains(unix)) {
                        cut = objectarray[h].split(Pattern.quote(separator));
                        cut[0] = cut[1];
                        cut[0] = separator + cut[0] + separator;
                        begin = true;

                    } else {

                        cut = objectarray[h].split(Pattern.quote(separator));
                        cut[0] = cut[0] + separator;
                        begin = false;
                    }

                    int bn = 0;
                    for (int i = 0; i != folders.length; i++) {
                        if (folders[i] != null && objectarray[h] != null) {
                            String[] she = objectarray[h].split(Pattern.quote(separator));
                            if (begin) {
                                she[0] = separator + she[1] + separator;
                            } else {
                                she[0] = she[0] + separator;
                            }

                            if (she[0].contains(folders[i])) {
                                draw_folder = false;
                                bn++;
                            }
                        }
                    }

                    for (int i = 0; i != folders.length; i++) {
                        if (folders[i] != null && objectarray[h] != null) {
                            if (objectarray[h].contains(folders[i])) {
                                bn++;
                            }
                        }
                    }

                    if (draw_folder) {
                        jPanel11.setLayout(new BoxLayout(jPanel11, BoxLayout.Y_AXIS));
                        object_item[h] = new JRadioButton();
                        object_item[h].setText(cut[0]);
                        object_item[h].setBackground(Color.white);
                        object_item[h].setForeground(Color.gray);
                        object_item[h].addActionListener(new ActionListener() {

                            public void actionPerformed(ActionEvent e) {

                                calibrateTextArea();
                                try {
                                    for (int i = 0; i != objectarray.length; i++) {
                                        if (object_item[i] != null) {
                                            if (object_item[i].isSelected()) {
                                                active_folder = object_item[i].getText();
                                                jTextField10.setText(object_item[i].getText());
                                                object_item[i].setSelected(false);
                                                folder_view = false;
                                                jButton6.doClick();

                                            }

                                        }
                                    }
                                } catch (Exception writeConfig) {
                                }
                            }
                        });
                    }

                    folders[h] = cut[0];

                    if (bn > 1) {
                        folders[h] = null;
                    }

                    try {
                        list = new ArrayList<String>();
                        for (int p = 0; p != folders.length; p++) {
                            if (folders[p] != null) {
                                list.add(folders[p]);
                            }
                        }

                        folders2 = list.toArray(new String[list.size()]);
                    } catch (Exception list) {
                    }

                    jList2.setModel(new javax.swing.AbstractListModel() {

                        public int getSize() {
                            return folders2.length;
                        }

                        public Object getElementAt(int i) {
                            return folders2[i];
                        }
                    });

                    jList3.setModel(new javax.swing.AbstractListModel() {

                        public int getSize() {
                            return folders2.length;
                        }

                        public Object getElementAt(int i) {
                            return folders2[i];
                        }
                    });

                    jList4.setModel(new javax.swing.AbstractListModel() {

                        public int getSize() {
                            return folders2.length;
                        }

                        public Object getElementAt(int i) {
                            return folders2[i];
                        }
                    });

                } else {
                    jPanel11.setLayout(new BoxLayout(jPanel11, BoxLayout.Y_AXIS));
                    object_item[h] = new JRadioButton();
                    object_item[h].setText(objectarray[h]);
                    object_item[h].setBackground(Color.white);
                    object_item[h].setForeground(Color.blue);
                }
            }
            jPanel11.setLayout(new BoxLayout(jPanel11, BoxLayout.PAGE_AXIS));

        } catch (Exception draw) {
        }
    }

    void reloadObjects(boolean drawFolders) {

        if ((jTextField1.getText().length() > 1 || jTextField2.getText().length() > 1)) {
            var();
            jPanel11.removeAll();
            jPanel11.revalidate();
            jPanel11.repaint();
            jPanel11.setLayout(new BoxLayout(jPanel11, BoxLayout.PAGE_AXIS));

            try {
                for (int h = 1; h != bucketarray.length; h++) {
                    if (bucket_item[h] != null) {
                        if (bucket_item[h].isSelected()) {
                            ReloadObjects object = new ReloadObjects(cred.getAccess_key(), cred.getSecret_key(), bucket_item[h].getText(), cred.getEndpoint(), this);
                            object.run();
                        }
                    }
                }

                while (object_thread_status) {
                }

                if (drawFolders) {
                    drawFolders();
                } else {
                    redrawObjects();
                }

            } catch (Exception listing) {
            }

        } else {
            jTextArea1.append("\nError: Configuration not loaded\n");
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    void editorSync(String file
    ) {

        temp_file = (Home + File.separator + "object.tmp");

        File tmp = new File(temp_file);

        try {
            FileWriter fr = new FileWriter(temp_file);
            BufferedWriter bfr = new BufferedWriter(fr);
            bfr.write(jTextArea2.getText());
            bfr.close();
        } catch (Exception writeConfig) {
            jTextArea1.append("\n" + writeConfig.getMessage() + "\n");
        }
    }
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        if ((jTextField1.getText().length() > 1 || jTextField2.getText().length() > 1)) {
            var();
            MakeBucket makebucket = new MakeBucket(this);
            makebucket.startc();
        } else {
            jTextArea1.append("\nError: Configuration not loaded\n");
        }
        calibrateTextArea();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        System.exit(-1);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed

        if (active_bucket > 0) {
            ObjectACL acl = new ObjectACL(this);
            acl.startc();
        } else {
            jTextArea1.append("\nError: No bucket has been selected");
            calibrateTextArea();
        }

    }//GEN-LAST:event_jMenuItem6ActionPerformed
    void deleteFle(String what) {
        try {
            File file = new File(what);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception deleteFIle) {
            jTextArea1.append("\n" + deleteFIle.getMessage());
        }
    }
    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed

    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        if (active_bucket > 0) {
            jPanel9.setVisible(true);
            temp_file = (Home + File.separator + "object.tmp");
            editorSync(jTextField6.getText());
            int index = jList4.getSelectedIndex(); //get selected index

            if (index != -1) {
                put = new Put(temp_file, cred.getAccess_key(), cred.getSecret_key(), cred.getBucket(), cred.getEndpoint(), jList4.getSelectedValue().toString() + jTextField6.getText(), false, false);
                put.startc(temp_file, cred.getAccess_key(), cred.getSecret_key(), cred.getBucket(), cred.getEndpoint(), jList4.getSelectedValue().toString() + jTextField6.getText(), false, false);
            } else {
                put = new Put(temp_file, cred.getAccess_key(), cred.getSecret_key(), cred.getBucket(), cred.getEndpoint(), jTextField6.getText(), false, false);
                put.startc(temp_file, cred.getAccess_key(), cred.getSecret_key(), cred.getBucket(), cred.getEndpoint(), jTextField6.getText(), false, false);
            }
            jTextArea1.append("\nSaved Object\n");
            objectarray = null;
            bucket_item[active_bucket].setSelected(true);
            reloadBuckets();
        } else {
            jTextArea1.append("\nError: no bucket selected.");
        }
        calibrateTextArea();
    }//GEN-LAST:event_jButton11ActionPerformed

    void syncToS3() {
        if (active_bucket > 0) {
            //syncing_to_S3 = true;
            jPanel4.repaint();
            SyncToS3.running = true;
            jPanel9.setVisible(true);
            jTextArea1.setText("\n\nStarted Sync.");
            calibrateTextArea();
            reloadObjects(false);

            if (bucket_item[active_bucket].isSelected()) {
                if (jFileChooser2.getSelectedFile() == null) {
                    jTextArea1.append("\nError: please select a destination directory.");
                } else {
                    Boolean selected = false;

                    Boolean rrs = false;
                    Boolean encrypt = false;

                    if (jCheckBox3.isSelected()) {
                        rrs = true;
                    }
                    if (jCheckBox6.isSelected()) {
                        encrypt = true;
                    }

                    syncToS3 = new SyncToS3(this, jFileChooser2.getSelectedFile(), cred.getAccess_key(), cred.getSecret_key(), cred.getBucket(), cred.getEndpoint(), objectarray, rrs, encrypt);
                    syncToS3.startc(this, jFileChooser2.getSelectedFile(), cred.getAccess_key(), cred.getSecret_key(), cred.getBucket(), cred.getEndpoint(), objectarray, rrs, encrypt);
                    objectarray = null;
                }
            } else {
                jTextArea1.append("\nError, no bucket has been selected.");
                calibrateTextArea();
            }

        } else {
            jTextArea1.append("\nError: No bucket selected.");
        }
    }

    private void jFileChooser2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFileChooser2ActionPerformed

    }//GEN-LAST:event_jFileChooser2ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        try {
            jTextField1.setText("");
            jTextField2.setText("");
            jTextField3.setText("");
            jTextField4.setText("");
            jTextField5.setText("");
            jTextField7.setText("");
        } catch (Exception clear) {
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    void calibrateTextArea() {
        jTextArea1.append("\n");
        try {
            jTextArea1.setCaretPosition(jTextArea1.getLineStartOffset(jTextArea1.getLineCount() - 1));
        } catch (Exception e) {

        }
    }

    void reloadAccounts() {
        String[] account = new String[account_array.length];
        String account_value = null;

        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField5.setText("");
        jTextField7.setText("");

        try {

            for (int i = 0; i != account_array.length; i++) {
                if (account_array[i] != null) {
                    if (account_item[i].isSelected()) {
                        active_account = i;
                        account = account_array[i].split("@");
                        jTextField1.setText(account[0]);
                        jTextField2.setText(account[1]);
                        jTextField3.setText(account[2]);
                        jTextField4.setText(account[3]);
                        jTextField5.setText(account[4]);
                        if (account.length == 6) {
                            jTextField7.setText(account[5]);
                        }
                    }
                }
            }

        } catch (Exception loadconfig) {
        }
        calibrateTextArea();
    }
    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed

        if (account_counter == 0) {
            try {
                jPanel21.removeAll();
                jPanel21.revalidate();
                jPanel21.validate();
                jPanel21.repaint();
                loadConfig();
            } catch (Exception load) {
            }

            if (content_counter > 0) {
                account_counter = 1;
                total_accounts = 1;
                objectarray = null;
            }

        } else {
            reloadAccounts();
            if (active_account > 0) {
                if (NewJFrame.jCheckBox1.isSelected()) {
                    HostChecker hostchecker = new HostChecker(jTextField3.getText(), this);
                    hostchecker.startc();
                }

            } else {
                jTextArea1.append("\nError: No account has been selected.");
            }
        }

    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        String save = cred.writeConfig(jTextField1.getText(), jTextField2.getText(), jTextField3.getText(), jTextField4.getText(), jTextField5.getText(), jTextField7.getText());
        jTextArea1.append(save);
        account_counter = 0;
        jButton9.doClick();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed

    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed

    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        if (active_bucket > 0) {
            jTextArea1.setText("\n\nPlease wait for the upload operation to complete.");
            File file = jFileChooser1.getSelectedFile();
            String upload = (file.getAbsolutePath());
            String new_object_name = convertObject(file.getAbsolutePath().toString(), "upload");
            //jTextField7.setText(jTextField7.getText().replace("null", ""));
            if (jCheckBox4.isSelected()) {
                Zip zip = new Zip(upload, "compress");
                zip.run();
                upload = Home + File.separator + "compress.tmp";
                new_object_name = new_object_name + ".zip";
                File checkZip = new File(upload);
                if (checkZip.exists()) {
                } else {
                    jTextArea1.setText("\nError: Zip file does not exist.");
                    calibrateTextArea();
                }
            }
            Boolean rrs = false;
            Boolean encrypt = false;

            if (jCheckBox2.isSelected()) {
                rrs = true;
            }
            if (jCheckBox5.isSelected()) {
                encrypt = true;
            }

            int index = jList2.getSelectedIndex(); //get selected index

            if (index != -1) {
                new_object_name = jList2.getSelectedValue().toString() + new_object_name;
            }

            put = new Put(upload, cred.getAccess_key(), cred.getSecret_key(), cred.getBucket(), cred.getEndpoint(), new_object_name, rrs, encrypt);
            put.startc(upload, cred.getAccess_key(), cred.getSecret_key(), cred.getBucket(), cred.getEndpoint(), new_object_name, rrs, encrypt);
            jPanel9.setVisible(true);
            reloadBuckets();
        } else {
            jTextArea1.append("\nError: No bucket selected.");
        }
        objectarray = null;
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jFileChooser1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFileChooser1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFileChooser1ActionPerformed
    void syncFromS3() {
        if (active_bucket > 0) {
            objectarray = null;
            reloadObjects(false);
            SyncFromS3.running = true;
            if (objectarray.length > 1) {
                jPanel9.setVisible(true);
                jTextArea1.setText("\n\nStarted Sync.");
                calibrateTextArea();

                if (bucket_item[active_bucket].isSelected()) {

                    if (jFileChooser2.getSelectedFile() == null) {
                        jTextArea1.append("\nError: please select a destination directroy.");

                    } else {

                        String Destination = jFileChooser2.getSelectedFile().toString();
                        String[] ObjectsConverted = new String[objectarray.length];

                        for (int i = 1; i != objectarray.length; i++) {
                            if (objectarray[i] != null) {
                                ObjectsConverted[i] = convertObject(objectarray[i], "download");
                            }
                        }

                        syncFromS3 = new SyncFromS3(this, objectarray, ObjectsConverted, cred.getAccess_key(), cred.getSecret_key(), cred.getBucket(), cred.getEndpoint(), Destination);
                        syncFromS3.startc(this, objectarray, ObjectsConverted, cred.getAccess_key(), cred.getSecret_key(), cred.getBucket(), cred.getEndpoint(), Destination);
                    }

                } else {
                    jTextArea1.append("\nError: No bucket selected.");
                }

            } else {
                jTextArea1.append("\nError: Bucket has no objects to sync");
                calibrateTextArea();
            }
            calibrateTextArea();
        }
    }
    private void jTextField10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField10ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        Daemon daemon = new Daemon();
        daemon.gui = true;
        jPanel9.setVisible(true);
        daemon.start();
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        try {

            if (active_bucket > 0) {
                final JFrame bg_frame = new JFrame("Directory to Sync:");
                final JFileChooser bg_choose = new JFileChooser();
                bg_choose.setControlButtonsAreShown(false);
                bg_choose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                final JButton bg_button = new JButton("Save");

                bg_button.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {

                        File choice = new File(bg_choose.getSelectedFile().toString());
                        try {
                            FileWriter fr = new FileWriter(Home + File.separator + "s3config.sync");
                            BufferedWriter bfr = new BufferedWriter(fr);
                            bfr.write(bg_choose.getSelectedFile().toString() + " " + bucket_item[active_bucket].getText());
                            bfr.close();
                        } catch (Exception writeConfig) {
                            jTextArea1.append("\n" + writeConfig.getMessage());
                        }
                        jPanel9.setVisible(true);
                        jTextArea1.append("\nWritten config: " + Home + File.separator + "s3config.sync");
                        calibrateTextArea();
                        bg_frame.setVisible(false);
                    }
                });

                JPanel bg_panel = new JPanel();
                bg_frame.setResizable(false);
                bg_panel.setLayout(new BoxLayout(bg_panel, BoxLayout.PAGE_AXIS));
                bg_panel.add(bg_choose);
                bg_frame.add(bg_panel);
                bg_panel.add(bg_button);
                bg_frame.setLocation(500, 500);
                bg_frame.pack();
                bg_frame.setVisible(true);
            } else {
                jTextArea1.append("\nError: No bucket has been selected");
            }
        } catch (Exception Download) {
            jTextArea1.append("\n" + Download.getMessage());
        }
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        if (total_accounts != 0) {
            for (int i = 0; i != account_array.length; i++) {
                if (account_array[i] != null) {
                    if (account_item[i].isSelected()) {
                        jTextArea1.append("\nDeleting Account: " + account_item[i].getText() + "\n");
                        account_array[i] = null;
                    } else {
                        account_array[i] = account_array[i];
                    }
                }
            }

            try {
                FileWriter fr = new FileWriter(config_file);
                BufferedWriter bfr = new BufferedWriter(fr);
                String read = null;
                for (int i = 0; i != account_array.length; i++) {
                    if (account_array[i] != null) {
                        bfr.write(("\n" + account_array[i]));
                    }
                }
                bfr.close();
                account_counter = 0;
                content_counter = 0;
                loadConfig();
                jButton9.doClick();
            } catch (Exception loadConfig) {
            }

            jTextField1.setText("");
            jTextField2.setText("");
            jTextField3.setText("");
            jTextField4.setText("");
            jTextField5.setText("");
        } else {
            jTextArea1.append("\nError: No accounts have been loaded.\n");
        }
        calibrateTextArea();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (active_bucket > 0) {

            final JFrame download = new JFrame("Please choose destination directory.");
            final JPanel downloadPanel = new JPanel();
            final JFileChooser downloadChooser = new JFileChooser();
            downloadChooser.setControlButtonsAreShown(false);
            downloadChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            final JButton downloadButton = new JButton("OK");

            downloadButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    jTextArea1.append("\nPlease wait for the download operation to complete.");
                    calibrateTextArea();
                    if (downloadChooser.getSelectedFile().getAbsolutePath() != null) {

                        File File_Destination = new File(downloadChooser.getSelectedFile().getAbsolutePath());
                        String[] getArray = new String[previous_objectarray_length];
                        jPanel9.setVisible(true);
                        if (versionDownload) {

                            for (int i = 0; i != versioning_name.size() + 1; i++) {
                                if (object_item[i].isSelected()) {
                                    download.setVisible(false);
                                    String new_object_name = convertObject(versioning_name.get(i), "download");
                                    get = new Get(versioning_name.get(i), cred.access_key, cred.getSecret_key(), cred.getBucket(), cred.getEndpoint(), File_Destination.toString() + File.separator + new_object_name, versioning_id.get(i));
                                    get.startc(versioning_name.get(i), cred.access_key, cred.getSecret_key(), cred.getBucket(), cred.getEndpoint(), File_Destination.toString() + File.separator + new_object_name, versioning_id.get(i));
                                    object_item[i].setSelected(false);
                                    break;
                                }
                            }
                        } else {
                            int p = 0;
                            for (int i = 1; i != previous_objectarray_length; i++) {
                                if (object_item[i] != null) {

                                    if (object_item[i].isSelected()) {
                                        download.setVisible(false);
                                        getArray[i] = object_item[i].getText();
                                        object_item[i].setSelected(false);
                                        p = i;
                                    }
                                }
                            }
                            getThread = new Thread(new GetThread(getArray, cred.access_key, cred.getSecret_key(), cred.getBucket(), cred.getEndpoint(), null, File_Destination));
                            getThread.start();
                        }

                    } else {

                        jTextArea1.append("\nError: destination not specified.");
                    }

                    calibrateTextArea();
                }
            });

            downloadPanel.setLayout(new BoxLayout(downloadPanel, BoxLayout.PAGE_AXIS));
            downloadPanel.add(downloadChooser);
            downloadPanel.add(downloadButton);
            download.add(downloadPanel);
            download.setLocation(500, 500);
            download.pack();

            if (!versionDownload) {
                try {
                    for (int i = 1; i != objectarray.length; i++) {
                        if (object_item[i] != null) {
                            if (object_item[i].isSelected()) {
                                download.setVisible(true);
                            }
                        }
                    }
                } catch (Exception GetThreadRUN) {
                }
            }

            if (versionDownload) {
                try {
                    int i = 0;
                    for (String what : versioning_name) {
                        if (object_item[i] != null) {
                            if (object_item[i].isSelected()) {
                                download.setVisible(true);
                            }
                        }
                        i++;
                    }
                } catch (Exception GetThreadRUN) {
                }
            }
        } else {
            jTextArea1.append("\nError: No bucket has been selected");
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        int delcounter = 0;
        try {

            if (active_bucket > 0) {
                jPanel9.setVisible(true);
                calibrateTextArea();
                jTextArea1.append("\nPlease wait, deleting selected file(s)");
                calibrateTextArea();
                if (versionDownload) {
                    String[] delArray = new String[versioning_name.size()];
                    String[] verArray = new String[versioning_name.size()];
                    for (int i = 0; i != versioning_name.size(); i++) {
                        if (object_item[i].isSelected()) {
                            delArray[i] = versioning_name.get(i);
                            verArray[i] = versioning_id.get(i);
                        }
                    }

                    Thread delThread = new Thread(new DeleteThread(this, delArray, verArray, cred.getAccess_key(), cred.getSecret_key(), cred.getBucket(), cred.getEndpoint(), null));
                    deleting.addItemListener(this);
                    delThread.start();

                } else {
                    String[] delArray = new String[previous_objectarray_length];

                    for (int i = 1; i != previous_objectarray_length; i++) {
                        if (object_item[i] != null) {
                            if (object_item[i].isSelected()) {
                                delArray[i] = object_item[i].getText();
                            }
                            delcounter++;
                        }
                    }
                    Thread delThread = new Thread(new DeleteThread(this, delArray, null, cred.getAccess_key(), cred.getSecret_key(), cred.getBucket(), cred.getEndpoint(), null));
                    deleting.addItemListener(this);
                    delThread.start();

                }

            } else {
                jTextArea1.append("\nError: No bucked selected.");
            }

            versionDownload = false;
            jTextField10.setText("");
            objectarray = null;
            //jButton6.doClick();
        } catch (Exception checkbox) {
        }

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed

        if (active_bucket > 0) {
            ObjectProperties properties = new ObjectProperties(this);
            properties.startc();
        } else {
            jTextArea1.append("\nError: No bucket has been selected\n");
        }

    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        try {
            temp_file = (Home + File.separator + "object.tmp");
            String objectToedit = null;
            if (versionDownload) {
                for (int i = 0; i != versioning_name.size() + 1; i++) {
                    if (object_item[i].isSelected()) {
                        String new_object_name = convertObject(versioning_name.get(i), "download");
                        get = new Get(versioning_name.get(i), cred.access_key, cred.getSecret_key(), cred.getBucket(), cred.getEndpoint(), temp_file, versioning_id.get(i));
                        get.run();
                        objectToedit = versioning_name.get(i);
                        object_item[i].setSelected(false);
                        break;
                    }
                }
            } else {
                for (int i = 1; i != previous_objectarray_length; i++) {
                    if (object_item[i] != null) {
                        if (object_item[i].isSelected()) {
                            String new_object_name = convertObject(object_item[i].getText(), "download");
                            get = new Get(object_item[i].getText(), cred.access_key, cred.getSecret_key(), cred.getBucket(), cred.getEndpoint(), temp_file, null);
                            get.run();
                            objectToedit = object_item[i].getText();
                            object_item[i].setSelected(false);
                            break;
                        }
                    }
                }
            }
            try {
                FileReader frr = new FileReader(temp_file);
                BufferedReader bfrr = new BufferedReader(frr);
                String read = null;
                jTextArea2.setText("");
                while ((read = bfrr.readLine()) != null) {
                    jTextArea2.append("\n" + read);
                }
                bfrr.close();
            } catch (Exception tempFile) {
                jTextArea1.append("\n" + tempFile.getMessage());
            }
            jTabbedPane1.setSelectedIndex(4);
            jTextField6.setText(objectToedit);
            jTextArea2.setCaretPosition(0);

        } catch (Exception Download) {
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        try {
            if (versionDownload) {
                for (int i = 0; i != versioning_id.size(); i++) {
                    if (object_item[i].isVisible()) {
                        if (object_item[i].isSelected()) {
                            object_item[i].setSelected(false);
                            jButton13.setText("Select");
                        } else {
                            object_item[i].setSelected(true);
                            jButton13.setText("Deselect");
                        }
                    }
                }
            } else {

                for (int i = 1; i != previous_objectarray_length; i++) {
                    if (object_item[i] != null) {
                        if (object_item[i].isVisible()) {
                            if (folder_view) {
                                if (object_item[i].getText().contains("/") || object_item[i].getText().contains("\\")) {
                                } else {
                                    if (object_item[i].isSelected()) {
                                        object_item[i].setSelected(false);
                                        jButton13.setText("Select");
                                    } else {
                                        object_item[i].setSelected(true);
                                        jButton13.setText("Deselect");
                                    }
                                }
                            } else {

                                if (object_item[i].isSelected()) {
                                    object_item[i].setSelected(false);
                                    jButton13.setText("Select");
                                } else {
                                    object_item[i].setSelected(true);
                                    jButton13.setText("Deselect");

                                }
                            }
                        }

                    }
                }
            }
        } catch (Exception SelectALL) {
        }
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        put.stop();
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed

        try {
            if (SyncToS3.running) {
                Abort abortToS3 = new Abort("To");
                abortToS3.run();
            }

            if (SyncFromS3.running) {
                Abort abortFromS3 = new Abort("From");
                abortFromS3.run();
            }
        } catch (Exception abo) {

        }
        reloadBuckets();
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        if (active_bucket > 0) {
            jTextArea1.append("\nPlease wait, loading MP3 player.");
            calibrateTextArea();
            MusicPlayer musicplayer = new MusicPlayer(this);
            musicplayer.startc();
            jTextArea1.append("\nMusic player has been started. Please observe for any errors.");
            calibrateTextArea();
        } else {
            jTextArea1.append("\nError: No bucket has been selected");
        }
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        if (active_bucket > 0) {
            boolean countSelected = false;
            jButton1.setEnabled(false);
            jButton7.setEnabled(false);
            jButton17.setEnabled(false);
            jButton18.setEnabled(false);
            jButton19.setEnabled(false);
            jPanel9.setVisible(true);

            for (int i = 1; i != objectarray.length; i++) {
                if (object_item[i].isSelected()) {
                    countSelected = true;
                    showVersions = new ShowVersions(object_item[i].getText(), cred.getAccess_key(), cred.getSecret_key(), cred.getBucket(), cred.getEndpoint(), this);
                    showVersions.startc(object_item[i].getText(), cred.getAccess_key(), cred.getSecret_key(), cred.getBucket(), cred.getEndpoint(), this);;
                    break;
                }
            }

            if (!countSelected) {
                showVersions = new ShowVersions(null, cred.getAccess_key(), cred.getSecret_key(), cred.getBucket(), cred.getEndpoint(), this);
                showVersions.startc(null, cred.getAccess_key(), cred.getSecret_key(), cred.getBucket(), cred.getEndpoint(), this);
            }

        } else {
            jTextArea1.append("\nError: No bucket has been selected");
        }
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        if (active_bucket > 0) {
            imageviewer = new ImageViewer(this);
            jButton1.setEnabled(false);
            jButton3.setEnabled(false);
            jButton4.setEnabled(false);
            jButton7.setEnabled(false);
            jButton12.setEnabled(false);
            jButton13.setEnabled(false);
            jButton17.setEnabled(false);
            jButton18.setEnabled(false);
            jButton19.setEnabled(false);
            imageviewer.startc();
            jPanel9.setVisible(true);
        } else {
            jTextArea1.append("\nError: No bucket has been selected");
        }
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        if (active_bucket > 0) {
            DeleteEverything delEverything = new DeleteEverything(this);
            Versioning.delete = true;
            jPanel14.removeAll();
            jPanel14.repaint();
            jPanel14.validate();
            delEverything.startc(this);
            jPanel9.setVisible(true);
        } else {
            jTextArea1.append("\nError: No bucket has been selected");
        }
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        if (active_bucket > 0) {
            ConfigureObjectTransition trans = new ConfigureObjectTransition(this);
            trans.startc();
        } else {
            jTextArea1.append("\nError: No bucket has been selected");
        }

    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        if (active_bucket > 0) {
            jTextArea1.append("\nScreen shot will start in 5 seconds.");
            calibrateTextArea();
            ScreenShot foo = new ScreenShot(null, cred.getAccess_key(), cred.getSecret_key(), cred.getBucket(), cred.getEndpoint(), null, this);
            foo.startc(null, cred.getAccess_key(), cred.getSecret_key(), cred.getBucket(), cred.getEndpoint(), null, this);
            jPanel9.setVisible(true);
        } else {
            jTextArea1.append("\nError: No bucket has been selected");
            calibrateTextArea();
        }
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        if (active_bucket > 0) {
            for (int i = 1; i != previous_objectarray_length; i++) {
                if (object_item[i] != null) {
                    if (object_item[i].isSelected()) {
                        RestoreObject restoreOBJ = new RestoreObject(object_item[i].getText(), cred.getAccess_key(), cred.getSecret_key(), cred.getBucket(), cred.getEndpoint());
                        restoreOBJ.startc(object_item[i].getText(), cred.access_key, cred.getSecret_key(), cred.getBucket(), cred.getEndpoint());
                    }
                }
            }
            jPanel9.setVisible(true);
        } else {
            jTextArea1.append("\nError: No bucket has been selected");
        }
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
        if (active_bucket > 0) {
            jTabbedPane1.setSelectedIndex(1);
            MakeDestinationBucket makeDestbucket = new MakeDestinationBucket(this);
            makeDestbucket.startc();
            jPanel9.setVisible(true);
        } else {
            jTextArea1.append("\nError: No bucket has been selected");
        }
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox4ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        if (active_bucket > 0) {
            jTabbedPane1.setSelectedIndex(1);
            jTextArea1.append("\nPlease wait, loading Performance test module.");
            calibrateTextArea();
            NewJFrame.perf = true;
            hidePanel();
            Performance performanceTest = new Performance(this, true, false);
            performanceTest.startc(this, true, false);
            jTextArea1.append("\nPerformance test module has started. Please observe for any errors.");
            calibrateTextArea();
        } else {
            jTextArea1.append("\nError: No bucket has been selected");
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        if (active_bucket > 0) {
            jTabbedPane1.setSelectedIndex(1);
            jTextArea1.append("\nPlease wait, loading Performance test module.");
            calibrateTextArea();
            NewJFrame.perf = true;
            hidePanel();
            Performance performanceTest = new Performance(this, false, false);
            performanceTest.startc(this, false, false);
            jTextArea1.append("\nPerformance test module has started. Please observe for any errors.");
            calibrateTextArea();
        } else {
            jTextArea1.append("\nError: No bucket has been selected");
        }
    }//GEN-LAST:event_jMenuItem11ActionPerformed
    void helpMenu(String what) {

        try {
            jTextArea2.setText("");
            jTabbedPane1.setSelectedIndex(4);
            InputStream is = getClass().getResourceAsStream(what);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bfrr = new BufferedReader(isr);
            String read = null;
            while ((read = bfrr.readLine()) != null) {
                jTextArea2.append("\n" + read);
            }
            bfrr.close();
            jTextArea2.setCaretPosition(0);
        } catch (Exception releasenotes) {
        }

    }
    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        helpMenu("Release_Notes.txt");
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
        helpMenu("migrate.txt");
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed
        helpMenu("backsync.txt");
    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        if (active_bucket > 0) {
            jButton1.setEnabled(false);
            jButton3.setEnabled(false);
            jButton4.setEnabled(false);
            jButton7.setEnabled(false);
            jButton12.setEnabled(false);
            jButton13.setEnabled(false);
            jButton17.setEnabled(false);
            jButton18.setEnabled(false);
            jButton19.setEnabled(false);
            jPanel15.setVisible(false);
            jPanel14.removeAll();
            Bot bot = new Bot(cred.getAccess_key(), cred.getSecret_key(), cred.getBucket(), cred.getEndpoint());
            bot.startc(cred.getAccess_key(), cred.getSecret_key(), cred.getBucket(), cred.getEndpoint());
        } else {
            jTextArea1.append("\nError: No bucket has been selected\n");
        }

    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        if (active_bucket > 0) {
            ConfigureIRC irc = new ConfigureIRC(this);
            jTabbedPane1.setSelectedIndex(1);
            irc.startc();
        } else {
            jTextArea1.append("\nError: No bucket has been selected\n");
        }

    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void jCheckBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox6ActionPerformed

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged

    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        jPanel9.setVisible(false);
        consoleToggle = false;
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed
        jPanel9.setVisible(true);
        consoleToggle = true;
    }//GEN-LAST:event_jMenuItem21ActionPerformed

    private void jMenuItem22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem22ActionPerformed
        AddExternalBucket addBucket = new AddExternalBucket(this);
        addBucket.startc();
    }//GEN-LAST:event_jMenuItem22ActionPerformed

    private void jMenuItem23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem23ActionPerformed
        try {
            jPanel9.setVisible(true);
            NewJFrame.jTextArea1.append("\nTrying to start another Cloud Explorer.");
            NewJFrame.jTextArea1.append("\nPlease ensure that your Java path is set correctly on the OS.");
            String path = NewJFrame.class
                    .getProtectionDomain().getCodeSource().getLocation().getPath();

            Process pb = null;
            if (path.contains("/C:")) {
                path = path.replace("/C:", "C:");
                path = path.replace("/", File.separator);
                pb = Runtime.getRuntime().exec("java -jar -Xms100m -Xmx500m " + path);
            } else {
                pb = Runtime.getRuntime().exec("java -jar -Xms100m -Xmx500m " + path);
            }

            InputStream in = pb.getInputStream();
            InputStream err = pb.getErrorStream();
        } catch (Exception pb) {
            NewJFrame.jTextArea1.append("\nError:" + pb.getMessage());
            calibrateTextArea();
        }
    }//GEN-LAST:event_jMenuItem23ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        syncToS3();
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        syncFromS3();
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jMenuItem24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem24ActionPerformed
        cleanup();
    }//GEN-LAST:event_jMenuItem24ActionPerformed

    private void jMenuItem25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem25ActionPerformed
        Update update = new Update(this, false);
        update.startc(false);
    }//GEN-LAST:event_jMenuItem25ActionPerformed

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        if ((jTextField1.getText().length() > 1 || jTextField2.getText().length() > 1)) {
            var();
            MakeBucket makebucket = new MakeBucket(this);
            makebucket.startc();
        } else {
            jTextArea1.append("\nError: Configuration not loaded\n");
        }
        calibrateTextArea();
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jMenuItem26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem26ActionPerformed

        if (active_bucket > 0) {
            for (int h = 1; h != objectarray.length; h++) {
                if (object_item[h] != null) {
                    if (object_item[h].isSelected()) {
                        jPanel9.setVisible(true);
                        jPanel15.setVisible(false);
                        jButton1.setEnabled(false);
                        jButton3.setEnabled(false);
                        jButton4.setEnabled(false);
                        jButton7.setEnabled(false);
                        jButton12.setEnabled(false);
                        jButton13.setEnabled(false);
                        jButton17.setEnabled(false);
                        jButton18.setEnabled(false);
                        jButton19.setEnabled(false);
                        Thread graph = new Thread(new Graph(this, object_item[h].getText()));
                        graph.start();
                        break;
                    }
                }
            }

        } else {
            jTextArea1.append("\nError: No bucket has been selected");
            calibrateTextArea();
        }

    }//GEN-LAST:event_jMenuItem26ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        try {
            if (active_bucket > 0 && jTextArea2.getText() != null && jTextField6.getText() != null && jTextField9.getText() != null && jTextField8.getText().length() >= 1) {
                before = jTextArea2.getText();
                String after = before.replace(jTextField8.getText(), jTextField9.getText());
                jTextArea2.setText(after);
                jTextArea2.setCaretPosition(0);
            } else {
                jTextArea1.append("\nError: No bucket has been selected or a Text field is empty.");
                calibrateTextArea();
            }
        } catch (Exception replace_text) {

        }
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        try {
            if (before != null) {
                jTextArea2.setText(before);
                jTextArea2.setCaretPosition(0);
                before = null;
            }
        } catch (Exception undo_replace) {
        }
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jMenuItem27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem27ActionPerformed
        if (active_bucket > 0) {
            CreateFolder fol = new CreateFolder(this);
            fol.startc();
            jPanel9.setVisible(true);
        } else {
            jTextArea1.append("\nError: No bucket has been selected");
        }

    }//GEN-LAST:event_jMenuItem27ActionPerformed

    private void jMenuItem28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem28ActionPerformed
        if (active_bucket > 0) {
            jTabbedPane1.setSelectedIndex(1);
            jTextArea1.append("\nPlease wait, loading Performance test module.");
            calibrateTextArea();
            NewJFrame.perf = true;
            hidePanel();
            Performance performanceTest = new Performance(this, true, true);
            performanceTest.startc(this, true, true);
            jTextArea1.append("\nPerformance test module has started. Please observe for any errors.");
            calibrateTextArea();
        } else {
            jTextArea1.append("\nError: No bucket has been selected");
        }
    }//GEN-LAST:event_jMenuItem28ActionPerformed

    private void jMenuItem29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem29ActionPerformed

        if (active_bucket > 0) {
            jPanel9.setVisible(true);
            SoundRecorder recorder = new SoundRecorder(this);
            recorder.startc();
        } else {
            jTextArea1.append("\nError: No bucket has been selected\n");
        }
        calibrateTextArea();
    }//GEN-LAST:event_jMenuItem29ActionPerformed

    private void jMenuItem30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem30ActionPerformed
        if (active_bucket > 0) {
            jTabbedPane1.setSelectedIndex(1);
            Snapshot snapshot = new Snapshot(this, active_folder);
            snapshot.startc(active_folder);
            jPanel9.setVisible(true);
        } else {
            jTextArea1.append("\nError: No bucket has been selected");
        }
    }//GEN-LAST:event_jMenuItem30ActionPerformed

    private void jMenuItem32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem32ActionPerformed
        if (active_account > 0) {
            if (active_bucket > 0) {
                jPanel9.setVisible(true);
                String save = cred.writeMigrateConfig(jTextField1.getText(), jTextField2.getText(), jTextField3.getText(), jTextField4.getText(), jTextField5.getText(), bucketarray[active_bucket]);
                jTextArea1.append(save);
                calibrateTextArea();
            } else {
                jTextArea1.append("\nError: No bucket has been selected");
            }
        } else {
            jTextArea1.append("\nError: No account has been selected.");
        }
    }//GEN-LAST:event_jMenuItem32ActionPerformed

    private void jMenuItem33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem33ActionPerformed
        if (active_account > 0) {
            if (active_bucket > 0) {
                if (active_folder != null) {
                    jPanel9.setVisible(true);
                    snap_folder = active_folder;
                    jTextArea1.append("\nSet " + snap_folder + " as the snapshot origin folder.");
                    calibrateTextArea();
                } else {
                    jTextArea1.append("\nError: No folder has been selected");
                }
            } else {
                jTextArea1.append("\nError: No bucket has been selected");
            }
        } else {
            jTextArea1.append("\nError: No account has been selected.");
        }
    }//GEN-LAST:event_jMenuItem33ActionPerformed

    private void jMenuItem31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem31ActionPerformed
        helpMenu("snapshots.txt");
    }//GEN-LAST:event_jMenuItem31ActionPerformed

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed
    void cleanup() {
        try {
            jPanel9.setVisible(true);
            NewJFrame.jTextArea1.append("\n\nScanning for stale tmp files and will delete if found.....");
            calibrateTextArea();
            File location = new File(Home);
            File dir = new File(Home);
            File[] list = dir.listFiles();
            for (File file_found : list) {
                if (file_found.getName().toString().contains("object.tmp") || file_found.getName().toString().contains("screenshot.s3") || file_found.getName().toString().contains("ops_results.csv") || file_found.getName().toString().contains("throughput_results.csv") || file_found.getName().toString().contains("latency_results.csv") || file_found.getName().toString().contains("GRAPH-") || file_found.getName().toString().contains("compress.tmp")) {
                    File del = new File(file_found.getAbsolutePath());
                    del.delete();
                    NewJFrame.jTextArea1.append("\nDeleting temp file: " + file_found.getName());
                }
            }
        } catch (Exception cleanup) {
        }
        NewJFrame.jTextArea1.append("\nScan complete");
        calibrateTextArea();
    }

    void var() {
        try {
            cred.setAccess_key(jTextField1.getText());
            cred.setSecret_key(jTextField2.getText());
            String endpoint = (jTextField3.getText() + ":" + jTextField4.getText());
            cred.setEndpoint(endpoint);
            cred.setRegion(jTextField5.getText());
        } catch (Exception var) {

        }
    }

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewJFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    public static javax.swing.JButton jButton11;
    public static javax.swing.JButton jButton12;
    public static javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    public static javax.swing.JButton jButton17;
    public static javax.swing.JButton jButton18;
    public static javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    public static javax.swing.JButton jButton3;
    public static javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    public static javax.swing.JButton jButton6;
    public static javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    public static javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    public javax.swing.JFileChooser jFileChooser1;
    public static javax.swing.JFileChooser jFileChooser2;
    public static javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    public static javax.swing.JList jList2;
    public static javax.swing.JList jList3;
    public static javax.swing.JList jList4;
    public static javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu10;
    private javax.swing.JMenu jMenu11;
    private javax.swing.JMenu jMenu12;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem22;
    public static javax.swing.JMenuItem jMenuItem23;
    private javax.swing.JMenuItem jMenuItem24;
    private javax.swing.JMenuItem jMenuItem25;
    private javax.swing.JMenuItem jMenuItem26;
    private javax.swing.JMenuItem jMenuItem27;
    private javax.swing.JMenuItem jMenuItem28;
    private javax.swing.JMenuItem jMenuItem29;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem30;
    private javax.swing.JMenuItem jMenuItem31;
    private javax.swing.JMenuItem jMenuItem32;
    private javax.swing.JMenuItem jMenuItem33;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    public static javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    public static javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    public static javax.swing.JPanel jPanel13;
    public static javax.swing.JPanel jPanel14;
    public static javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    public javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    public static javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    public static javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    public static javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane27;
    public static javax.swing.JScrollPane jScrollPane28;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    public static javax.swing.JTabbedPane jTabbedPane1;
    public static javax.swing.JTextArea jTextArea1;
    public static javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField1;
    public static javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField2;
    public static javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    public static javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private java.awt.Panel panel1;
    private java.awt.ScrollPane scrollPane1;
    // End of variables declaration//GEN-END:variables

}
