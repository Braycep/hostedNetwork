package priv.braycep;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;

public class MainFrame extends JFrame {
    //���serialVersionUID��Ϊ��ȥ����ɫ�ľ��棬eclipse�Զ����ɵ�
    private static final long serialVersionUID = 1L;
    //��Щ�����ڵ������д��������Ϊ�����ڹ��캯����߶�ʹ��
    //��ȡ��Ļ�ĳ���
    public static final int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

    private static JFrame mainFrame;
    private JPanel contentPane;
    private JTextField wifiNameTxf;
    private JTextField wifiKeyTxf;
    private JButton openBtn;
    private JButton closeBtn;
    private JButton localBtn;

    //process ��������Runtime.getRuntime().exec(cmd);ִ��cmd����Ľ��
    private Process process;
    //br ������ȡprocess�еĽ�����������˲���
    private BufferedReader br;
    //�±߶�����������һ���ִ���������Ĵ������Զ����ɵģ��õ�Eclipse�������WindowBiulder,ʡ�¶�
    public static void main(String[] args) {
        System.out.println("Welcome to use hostednetwork open tool!");
        //�Բ���ϵͳ�����ж�
        if (System.getProperties().getProperty("os.name").toLowerCase().contains("windows")) {
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    try {
                        new MainFrame().setVisible(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            JOptionPane.showMessageDialog(null, "ֻ��������Windows�У�", "��ʾ��", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Create the frame.
     * �Զ����ɵġ�����
     */
    private MainFrame() {
        mainFrame = this;
        setTitle("\u5F00\u542F\u65E0\u7EBF\u70ED\u70B9  by  Braycep");
        setBounds(WIDTH / 2 - 225, HEIGHT / 2 - 150, 450, 300);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel label = new JLabel("\u65E0\u7EBF\u540D\u79F0\uFF1A");
        label.setFont(new Font("΢���ź�", Font.PLAIN, 14));
        label.setBounds(34, 44, 91, 24);
        contentPane.add(label);

        JLabel label_1 = new JLabel("\u8FDE\u63A5\u5BC6\u7801\uFF1A");
        label_1.setFont(new Font("΢���ź�", Font.PLAIN, 14));
        label_1.setBounds(34, 101, 91, 24);
        contentPane.add(label_1);

        wifiNameTxf = new JTextField();
        wifiNameTxf.setBounds(126, 47, 231, 21);
        contentPane.add(wifiNameTxf);
        wifiNameTxf.setColumns(10);

        wifiKeyTxf = new JTextField();
        wifiKeyTxf.setBounds(126, 104, 231, 21);
        contentPane.add(wifiKeyTxf);
        wifiKeyTxf.setColumns(10);

        openBtn = new JButton("\u5F00\u542F\u70ED\u70B9");
        openBtn.setBackground(Color.GREEN);
        openBtn.setBounds(264, 174, 93, 23);
        contentPane.add(openBtn);

        closeBtn = new JButton("\u5173\u95ED\u70ED\u70B9");
        closeBtn.setBackground(Color.RED);
        closeBtn.setBounds(126, 174, 93, 23);
        contentPane.add(closeBtn);

        localBtn = new JButton("\u672C\u5730WIFI");
        localBtn.setBackground(Color.ORANGE);
        localBtn.setBounds(264, 220, 93, 23);
        contentPane.add(localBtn);

        //�Լ�д��
        events();
    }

    //�¼�����������һ�������ڹرյ��¼���������ť����������wifi�����İ�������
    private void events() {
        mainFrame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        //WiFi�����İ�������
        wifiKeyTxf.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                //�ж��Ƿ���Enter��
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    //����start()����
                    start();
                }
            }
        });

        //����wifi������������д��갴���ͷŷ���
        openBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                start();
            }
        });

        //�ر�WiFi����갴������
        closeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                try{
                    //ִ�йر��ȵ��CMD����
                    process = Runtime.getRuntime().exec("netsh wlan stop hostednetwork");
                    br = new BufferedReader(new InputStreamReader(process.getInputStream(),"GBK"));
                    String line;
                    //���м��ÿ�����Ƿ��������ͣ������ַ���
                    while ((line = br.readLine()) != null){
                        if (line.contains("��ͣ")) {
                            //�����Ѿ��ر�WiFi����ʾ
                            JOptionPane.showMessageDialog(mainFrame, "�ȵ��ѹرգ�", "���棺", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        //�鿴����wifi������İ���
        localBtn.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                showLocalWIFIInfo();
            }
        });
    }

    private void showLocalWIFIInfo() {
        //wifiList ��ű����Ѿ��洢��wifi����
        ArrayList<String> wifiList;
        //keyList ��Ŷ�Ӧwifi������
        ArrayList<String> keyList;
        try {
            //ִ��cmd���鿴����Щwifi�����ļ�
            process = Runtime.getRuntime().exec("netsh wlan show profile");
            br = new BufferedReader(new InputStreamReader(process.getInputStream(),"GBK"));
            wifiList = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("�����ļ� :")) {
                    wifiList.add(line.split(":")[1].trim());	//��ȡ�����Ѵ洢��wifi����
                }
            }
            //���������wifi�����ļ�
            if(wifiList.size() > 0) {
                //����wifi��Ϣ��ʾ����
                LocalWifi localWifi = LocalWifi.getInstance();
                keyList = new ArrayList<>();
                for (String string : wifiList) {
                    //�ڿ���̨����ʾwifi����
                    System.out.println("wifi ���ƣ�"+string);
                    //��WiFi��Ϣ��ʾ���������wifi����
                    LocalWifi.addWifiSSID(string);
                    //����WiFi���ƻ�ȡWiFi����
                    Process p = Runtime.getRuntime().exec("netsh wlan show profile name=\""+string+"\" key=clear");
                    BufferedReader brKey = new BufferedReader(new InputStreamReader(p.getInputStream(),"GBK"));
                    String keyLine;
                    while ((keyLine = brKey.readLine()) != null){
                        //ƥ���ַ�������ȡ����
                        if (keyLine.contains("�ؼ�����            :")) {
                            String key = keyLine.split(":")[1].trim();
                            keyList.add(key);
                            System.out.println("wifi ���룺"+key+"\n");
                            LocalWifi.addWifiKey(key);
                            break;
                        }
                    }
                }
                //���ñ���WiFi��Ϣ�鿴����Ϊ��ʾ״̬
                localWifi.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(mainFrame, "��ĵ��Ա���û��WiFi�����ļ����߱������", "��ʾ��", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ִ��CMD������WiFi�ȵ�
     * @param ssid �û�������ȵ�����
     * @param key �û�������ȵ�����
     */
    private void openHostedNetwork(String ssid,String key) {
        //
        try {
            process = Runtime.getRuntime().exec("netsh wlan set hostednetwork mode=allow ssid="+ssid+" key="+key);
            br = new BufferedReader(new InputStreamReader(process.getInputStream(),"GBK"));
            String line;
            while ((line = br.readLine()) != null){
                System.out.println(line);
            }
            process = Runtime.getRuntime().exec("netsh wlan start hostednetwork");
            br = new BufferedReader(new InputStreamReader(process.getInputStream(),"GBK"));
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                if (line.contains("����")) {
                    JOptionPane.showMessageDialog(mainFrame, "�����ɹ���\nSSID��"+ssid+"\nKey��"+key, "��ʾ��", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
            }
            if (line == null) {
                System.err.println("����ʧ�ܣ�");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ����wifi�ȵ�ǰ�ļ��
     */
    private void start(){
        String key = wifiKeyTxf.getText();
        if (key.length() < 8) {
            System.err.println(key+"������Ч�����룬\n����λ����������8��ASSIC�ַ���");
            JOptionPane.showMessageDialog(mainFrame, "����λ����������8��ASSIC�ַ���", "��ʾ��",JOptionPane.INFORMATION_MESSAGE);
            wifiKeyTxf.setText("");
        } else {
            if (key.getBytes().length != key.length()) {
                System.err.println("�������÷�ASSIC�ַ����룡");
                JOptionPane.showMessageDialog(mainFrame, "�������÷�ASSIC�ַ����룡", "��ʾ��",JOptionPane.INFORMATION_MESSAGE);
                wifiKeyTxf.setText("");
            } else {
                openHostedNetwork(wifiNameTxf.getText(), key);
            }
        }
    }
}
