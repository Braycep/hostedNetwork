package priv.braycep;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LocalWifi extends JFrame {

	private static final long serialVersionUID = 1L;
	private JFrame localFrame;
	private JPanel contentPane;
	private static LocalWifi localWifi = null;
	private static JTextArea wifiInfo;

	/**
	 * Create the frame...By WindowBiulder
	 */
	private LocalWifi() {
		localFrame = this;
		setTitle("本地wifi信息查看");
		setBounds(MainFrame.WIDTH / 2 - 225, MainFrame.HEIGHT / 2 - 150, 256, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		//给显示WiFi信息的窗口添加滚动条
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		wifiInfo= new JTextArea();
		wifiInfo.setEditable(false);
		wifiInfo.setFont(new Font("Courier New", Font.PLAIN, 13));
		scrollPane.setViewportView(wifiInfo);
		
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				localFrame.dispose();
			}
		});
	}
	/**
	 * 
	 * @param ssid 要显示的wifi名称
	 * @param key	要显示的密码
	 */
	protected static void addWifiInfo(String ssid, String key) {
		wifiInfo.append("\n********************");
		wifiInfo.append("Wifi:\t\n"+ssid+"\nkey:\n"+key+"\n");
	}

	public static LocalWifi getInstance(){
		if (localWifi == null) {
			localWifi = new LocalWifi();
		}
		return localWifi;
	}

	/**
	 * 添加名称
	 * @param ssid 添加wifi名称
	 */
	protected static void addWifiSSID(String ssid) {
		wifiInfo.append("\n********************\n");
		wifiInfo.append("Wifi:\t\n"+ssid);
	}
	
	/**
	 * 添加密码
	 * @param key 添加wifi密码
	 */
	protected static void addWifiKey(String key) {
		wifiInfo.append("\nkey:\n"+key+"\n");
	}
}
