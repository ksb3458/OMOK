
// JavaObjClientView.java ObjecStram 기반 Client
//실질적인 채팅 창
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.Color;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.JToggleButton;
import javax.swing.JList;
import java.awt.Canvas;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.JLayeredPane;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JRadioButton;
import java.awt.SystemColor;

public class JavaGameClientRoom extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private String UserName;
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	private Socket socket; // 연결소켓
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;

	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	private JButton roomBtn;

	private Frame frame;
	private FileDialog fd;
	private Graphics gc;
	private Graphics gc2 = null;
	private JPanel panel;
	private JLabel roomLabel;
	private JButton cancelBtn;
	private JLabel nameLabel;
	private JTextField textField;
	private JLabel secretLabel;
	private JPanel secretPanel;
	private JRadioButton secretNoBtn;
	private JLabel lookLabel;
	private JPanel lookPanel;
	private JRadioButton lookNoBtn;
	private JRadioButton lookYesBtn;
	private JButton createRoom;
	
	
	/**
	 * Create the frame.
	 * @throws BadLocationException 
	 */
	public JavaGameClientRoom(String username)  {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 375, 426);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(12, 10, 336, 363);
		panel.setLayout(null);
		contentPane.add(panel);
		
		roomLabel = new JLabel("   방 만들기");
		roomLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		roomLabel.setBackground(Color.PINK);
		roomLabel.setOpaque(true);
		roomLabel.setBounds(0, 0, 336, 44);
		panel.add(roomLabel);
		
		cancelBtn = new JButton("취소");
		cancelBtn.setBackground(Color.LIGHT_GRAY);
		cancelBtn.setBounds(257, 4, 67, 37);
		panel.add(cancelBtn);
		
		nameLabel = new JLabel("방 제 목");
		nameLabel.setFont(new Font("굴림", Font.PLAIN, 15));
		nameLabel.setBounds(30, 86, 67, 21);
		panel.add(nameLabel);
		
		textField = new JTextField();
		textField.setBounds(111, 77, 174, 30);
		panel.add(textField);
		textField.setColumns(10);
		
		secretLabel = new JLabel("비 밀 방");
		secretLabel.setFont(new Font("굴림", Font.PLAIN, 15));
		secretLabel.setBounds(30, 161, 67, 21);
		panel.add(secretLabel);
		
		secretPanel = new JPanel();
		secretPanel.setBounds(111, 139, 213, 49);
		panel.add(secretPanel);
		
		JRadioButton secretYesBtn = new JRadioButton("예");
		
		secretNoBtn = new JRadioButton("아니오");
		GroupLayout gl_secretPanel = new GroupLayout(secretPanel);
		gl_secretPanel.setHorizontalGroup(
			gl_secretPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_secretPanel.createSequentialGroup()
					.addComponent(secretYesBtn)
					.addGap(52)
					.addComponent(secretNoBtn, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(100, Short.MAX_VALUE))
		);
		gl_secretPanel.setVerticalGroup(
			gl_secretPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_secretPanel.createSequentialGroup()
					.addContainerGap(20, Short.MAX_VALUE)
					.addGroup(gl_secretPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(secretYesBtn)
						.addComponent(secretNoBtn, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		secretPanel.setLayout(gl_secretPanel);
		
		lookLabel = new JLabel("관전가능");
		lookLabel.setFont(new Font("굴림", Font.PLAIN, 15));
		lookLabel.setBounds(30, 236, 67, 21);
		panel.add(lookLabel);
		
		lookPanel = new JPanel();
		lookPanel.setBounds(111, 213, 215, 49);
		panel.add(lookPanel);
		
		lookNoBtn = new JRadioButton("아니오");
		
		lookYesBtn = new JRadioButton("예");
		GroupLayout gl_lookPanel = new GroupLayout(lookPanel);
		gl_lookPanel.setHorizontalGroup(
			gl_lookPanel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 282, Short.MAX_VALUE)
				.addGroup(gl_lookPanel.createSequentialGroup()
					.addComponent(lookYesBtn)
					.addGap(52)
					.addComponent(lookNoBtn, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(100, Short.MAX_VALUE))
		);
		gl_lookPanel.setVerticalGroup(
			gl_lookPanel.createParallelGroup(Alignment.TRAILING)
				.addGap(0, 49, Short.MAX_VALUE)
				.addGroup(gl_lookPanel.createSequentialGroup()
					.addContainerGap(20, Short.MAX_VALUE)
					.addGroup(gl_lookPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lookYesBtn)
						.addComponent(lookNoBtn, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		lookPanel.setLayout(gl_lookPanel);
		
		createRoom = new JButton("만  들  기");
		createRoom.setFont(new Font("굴림", Font.PLAIN, 17));
		createRoom.setBounds(19, 300, 305, 37);
		panel.add(createRoom);
		setVisible(true);

		UserName = username;

		/*try {
			socket = new Socket(ip_addr, Integer.parseInt(port_no));
//			is = socket.getInputStream();
//			dis = new DataInputStream(is);
//			os = socket.getOutputStream();
//			dos = new DataOutputStream(os);

			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());

			// SendMessage("/login " + UserName);
			ChatMsg obcm = new ChatMsg(UserName, "100", "Hello");
			SendObject(obcm);

			ListenNetwork net = new ListenNetwork();
			net.start();

		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

	}
	
	// Server Message를 수신해서 화면에 표시
	class ListenNetwork extends Thread {
		public void run() {
			while (true) {
				try {

					Object obcm = null;
					String msg = null;
					ChatMsg cm;
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break;
					}
					if (obcm == null)
						break;
					if (obcm instanceof ChatMsg) {
						cm = (ChatMsg) obcm;
						msg = String.format("[%s]\n%s", cm.UserName, cm.data);
						//msg = String.format("\n%s", cm.data);
					} else
						continue;
					switch (cm.code) {
					case "200": // chat message
						break;
						
					case "300": // chat message
						break;
					}
				} catch (IOException e) {
					try {
//						dos.close();
//						dis.close();
						ois.close();
						oos.close();
						socket.close();

						break;
					} catch (Exception ee) {
						break;
					} // catch문 끝
				} // 바깥 catch문끝

			}
		}
	}

	// Windows 처럼 message 제외한 나머지 부분은 NULL 로 만들기 위한 함수
	public byte[] MakePacket(String msg) {
		byte[] packet = new byte[BUF_LEN];
		byte[] bb = null;
		int i;
		for (i = 0; i < BUF_LEN; i++)
			packet[i] = 0;
		try {
			bb = msg.getBytes("euc-kr");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		for (i = 0; i < bb.length; i++)
			packet[i] = bb[i];
		return packet;
	}

	// Server에게 network으로 전송
	public void SendMessage(String msg) {
		try {
			// dos.writeUTF(msg);
//			byte[] bb;
//			bb = MakePacket(msg);
//			dos.write(bb, 0, bb.length);
			ChatMsg obcm = new ChatMsg(UserName, "200", msg);
			oos.writeObject(obcm);
		} catch (IOException e) {
			// AppendText("dos.write() error");
			try {
//				dos.close();
//				dis.close();
				ois.close();
				oos.close();
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.exit(0);
			}
		}
	}

	public void SendObject(Object ob) { // 서버로 메세지를 보내는 메소드
		try {
			oos.writeObject(ob);
		} catch (IOException e) {
			// textArea.append("메세지 송신 에러!!\n");
		}
	}
}