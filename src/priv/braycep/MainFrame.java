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
    //这个serialVersionUID是为了去掉黄色的警告，eclipse自动生成的
    private static final long serialVersionUID = 1L;
    //这些个窗口的组件儿写在这里是为了能在构造函数外边儿使用
    //获取屏幕的长宽
    public static final int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

    private static JFrame mainFrame;
    private JPanel contentPane;
    private JTextField wifiNameTxf;
    private JTextField wifiKeyTxf;
    private JButton openBtn;
    private JButton closeBtn;
    private JButton localBtn;

    //process 用来接收Runtime.getRuntime().exec(cmd);执行cmd命令的结果
    private Process process;
    //br 用来读取process中的结果，并做过滤操作
    private BufferedReader br;
    //下边儿的主函数和一部分窗口组件儿的代码是自动生成的，用的Eclipse插件儿，WindowBiulder,省事儿
    public static void main(String[] args) {
        System.out.println("Welcome to use hostednetwork open tool!");
        //对操作系统进行判断
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
            JOptionPane.showMessageDialog(null, "只能运行于Windows中！", "提示：", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Create the frame.
     * 自动生成的。。。
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
        label.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        label.setBounds(34, 44, 91, 24);
        contentPane.add(label);

        JLabel label_1 = new JLabel("\u8FDE\u63A5\u5BC6\u7801\uFF1A");
        label_1.setFont(new Font("微软雅黑", Font.PLAIN, 14));
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

        //自己写的
        events();
    }

    //事件函数，包含一个主窗口关闭的事件和三个按钮的鼠标监听，wifi密码框的按键监听
    private void events() {
        mainFrame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        //WiFi密码框的按键监听
        wifiKeyTxf.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                //判断是否按下Enter键
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    //调用start()方法
                    start();
                }
            }
        });

        //开启wifi的鼠标监听，重写鼠标按键释放方法
        openBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                start();
            }
        });

        //关闭WiFi的鼠标按键监听
        closeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                try{
                    //执行关闭热点的CMD命令
                    process = Runtime.getRuntime().exec("netsh wlan stop hostednetwork");
                    br = new BufferedReader(new InputStreamReader(process.getInputStream(),"GBK"));
                    String line;
                    //逐行检测每行中是否包含“已停”这个字符串
                    while ((line = br.readLine()) != null){
                        if (line.contains("已停")) {
                            //给出已经关闭WiFi的提示
                            JOptionPane.showMessageDialog(mainFrame, "热点已关闭！", "警告：", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        //查看本地wifi及密码的按键
        localBtn.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                showLocalWIFIInfo();
            }
        });
    }

    private void showLocalWIFIInfo() {
        //wifiList 存放本地已经存储的wifi名称
        ArrayList<String> wifiList;
        //keyList 存放对应wifi的密码
        ArrayList<String> keyList;
        try {
            //执行cmd，查看有哪些wifi配置文件
            process = Runtime.getRuntime().exec("netsh wlan show profile");
            br = new BufferedReader(new InputStreamReader(process.getInputStream(),"GBK"));
            wifiList = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("配置文件 :")) {
                    wifiList.add(line.split(":")[1].trim());	//获取本地已存储的wifi名称
                }
            }
            //如果本地有wifi配置文件
            if(wifiList.size() > 0) {
                //创建wifi信息显示窗口
                LocalWifi localWifi = LocalWifi.getInstance();
                keyList = new ArrayList<>();
                for (String string : wifiList) {
                    //在控制台中显示wifi名称
                    System.out.println("wifi 名称："+string);
                    //在WiFi信息显示窗口中添加wifi名称
                    LocalWifi.addWifiSSID(string);
                    //按照WiFi名称获取WiFi密码
                    Process p = Runtime.getRuntime().exec("netsh wlan show profile name=\""+string+"\" key=clear");
                    BufferedReader brKey = new BufferedReader(new InputStreamReader(p.getInputStream(),"GBK"));
                    String keyLine;
                    while ((keyLine = brKey.readLine()) != null){
                        //匹配字符串，获取密码
                        if (keyLine.contains("关键内容            :")) {
                            String key = keyLine.split(":")[1].trim();
                            keyList.add(key);
                            System.out.println("wifi 密码："+key+"\n");
                            LocalWifi.addWifiKey(key);
                            break;
                        }
                    }
                }
                //设置本地WiFi信息查看窗口为显示状态
                localWifi.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(mainFrame, "你的电脑本地没有WiFi配置文件或者编码错误", "提示：", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行CMD，启动WiFi热点
     * @param ssid 用户输入的热点名称
     * @param key 用户输入的热点密码
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
                if (line.contains("已启")) {
                    JOptionPane.showMessageDialog(mainFrame, "开启成功：\nSSID："+ssid+"\nKey："+key, "提示：", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
            }
            if (line == null) {
                System.err.println("开启失败！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动wifi热点前的检测
     */
    private void start(){
        String key = wifiKeyTxf.getText();
        if (key.length() < 8) {
            System.err.println(key+"不是有效的密码，\n密码位数不能少于8个ASSIC字符！");
            JOptionPane.showMessageDialog(mainFrame, "密码位数不能少于8个ASSIC字符！", "提示：",JOptionPane.INFORMATION_MESSAGE);
            wifiKeyTxf.setText("");
        } else {
            if (key.getBytes().length != key.length()) {
                System.err.println("不能设置非ASSIC字符密码！");
                JOptionPane.showMessageDialog(mainFrame, "不能设置非ASSIC字符密码！", "提示：",JOptionPane.INFORMATION_MESSAGE);
                wifiKeyTxf.setText("");
            } else {
                openHostedNetwork(wifiNameTxf.getText(), key);
            }
        }
    }
}
